package io.buzznerd.varys.whisper.job;

import java.util.Iterator;
import java.util.UUID;

import io.buzznerd.varys.whisper.BuildConfig;
import io.buzznerd.varys.whisper.component.ApiService;
import io.buzznerd.varys.whisper.component.Callbackable;
import io.buzznerd.varys.whisper.component.JobCallbackRunnable;
import io.buzznerd.varys.whisper.component.JobError;
import io.buzznerd.varys.whisper.component.RestClient;
import io.buzznerd.varys.whisper.component.WhisperConfig;
import io.buzznerd.varys.whisper.model.network.ApplicationTrack;
import io.buzznerd.varys.whisper.model.network.HitTrack;
import io.buzznerd.varys.whisper.model.network.SessionTrack;
import io.buzznerd.varys.whisper.model.network.UploadRequest;
import io.buzznerd.varys.whisper.model.network.WhisperResponse;
import io.buzznerd.varys.whisper.model.realmobject.WhisperApplication;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHelper;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHit;
import io.buzznerd.varys.whisper.model.realmobject.WhisperSession;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public class UploadJob extends JobCallbackRunnable<Void> {

    private ApiService mApiService;
    private String mMobileAppId;

    public UploadJob(ApiService apiService, String mobileAppId, Callbackable<Void> callbackable) {
        super(callbackable);
        mApiService = apiService;
        mMobileAppId = mobileAppId;
    }

    @Override
    public void run() {
        mLog.log("do upload job", mLogLevel);

        Realm realm = Realm.getInstance(mRealmConfiguration);

        UploadRequest request = new UploadRequest();
        realm.beginTransaction();
        RealmResults<WhisperApplication> apps = realm.where(WhisperApplication.class)
                .findAllSorted("id", Sort.ASCENDING);

        // assemble upload payload
        for (WhisperApplication whisperApplication : apps) {
            ApplicationTrack applicationTrack = ApplicationTrack.fromRealmObject(whisperApplication);
            RealmResults<WhisperSession> sessions = realm.where(WhisperSession.class)
                    .equalTo("applicationId", whisperApplication.getId())
                    .findAllSorted(WhisperSession.FIELD_ID);
            int itemCount = 0;
            for (WhisperSession session : sessions) {
                RealmResults<WhisperHit> hits = realm.where(WhisperHit.class)
                        .equalTo("sessionId", session.getId())
                        .findAllSorted("time", Sort.ASCENDING);
                if (hits.size() == 0) {
                    continue;
                }
                SessionTrack sessionTrack = SessionTrack.fromRealmObject(session);
                for (WhisperHit hit : hits) {
                    sessionTrack.addHitTrack(HitTrack.fromRealmObject(hit));
                    hit.setSyncStatus(WhisperConfig.SYNC_STATUS_UPLOADING);
                    itemCount++;
                }
                applicationTrack.addSessionTrack(sessionTrack);
            }

            RealmResults<WhisperHit> hits = realm.where(WhisperHit.class)
                    .equalTo("applicationId", whisperApplication.getId())
                    .equalTo("sessionId", 0)
                    .findAllSorted("time", Sort.ASCENDING);
            if (hits.size() == 0 && itemCount == 0) {
                continue;
            }

            for (WhisperHit hit : hits) {
                applicationTrack.addHitTrack(HitTrack.fromRealmObject(hit));
                hit.setSyncStatus(WhisperConfig.SYNC_STATUS_UPLOADING);
            }
            request.addApplicationTrack(applicationTrack);
        }
        realm.commitTransaction();

        // upload
        if (request.getTrackSize() > 0) {

            request.setClientUploadTime(System.currentTimeMillis());
            request.setDeviceId(realm.where(WhisperApplication.class).findFirst().getDevice().getDeviceId());
            request.setTrackingId(UUID.randomUUID().toString());
            request.setMobileAppId(mMobileAppId);
            request.setWhisperVersion(BuildConfig.VERSION_NAME);

            Call<WhisperResponse> call = mApiService.doUpload(request);
            call.enqueue(new Callback<WhisperResponse>() {

                @Override
                public void onResponse(Call<WhisperResponse> call, Response<WhisperResponse> response) {
                    int responseCode = response.code();
                    boolean isSuccess = true;
                    JobError jobError = null;

                    if (responseCode < 200 || responseCode >= 300) { // 2xx success
                        isSuccess = false;
                    }
                    if (isSuccess) {
                        WhisperResponse whisperResponse = response.body();
                        if (whisperResponse == null) {
                            isSuccess = false;
                        } else if (!RestClient.RESPONSE_CODE_OK.equals(whisperResponse.getRespcode())) {
                            isSuccess = false;
                            jobError = new JobError(whisperResponse.getRespUUID() + " "
                                    + whisperResponse.getMessage());
                        }
                    }

                    if (isSuccess) {
                        handleUploadSucceeded();

                        mLog.log("Upload Whisper successfully.", mLogLevel);

                        callbackSuccess(null);
                    } else {
                        handleUploadFailed();

                        if (jobError == null) {
                            jobError = new JobError("failed: response code: " + response.code()
                                    + ", " + response.message());
                        }

                        mLog.log("Upload Whisper failed. ", jobError, mLogLevel);

                        callbackFailed(jobError);

                        return;
                    }
                }

                @Override
                public void onFailure(Call<WhisperResponse> call, Throwable t) {
                    handleUploadFailed();

                    mLog.log("Upload Whisper failed. ", t, mLogLevel);

                    callbackFailed(new JobError(t));
                }
            });
        }

        realm.close();
    }

    private void handleUploadFailed() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.beginTransaction();
        RealmResults<WhisperHit> hits = realm.where(WhisperHit.class)
                .equalTo("syncStatus", WhisperConfig.SYNC_STATUS_UPLOADING)
                .findAll();
        Iterator<WhisperHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            WhisperHit hit = iterator.next();
            hit.setSyncStatus(WhisperConfig.SYNC_STATUS_NOT_UPLOAD);
        }

        WhisperHelper whisperHelper = realm.where(WhisperHelper.class).findFirst();
        whisperHelper.setUploadFailedTimes(whisperHelper.getUploadFailedTimes() + 1);
        whisperHelper.setLastUploadTime(System.currentTimeMillis());
        realm.commitTransaction();
        realm.close();
    }

    private void handleUploadSucceeded() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.beginTransaction();
        RealmResults<WhisperHit> hits = realm.where(WhisperHit.class)
                .equalTo("syncStatus", WhisperConfig.SYNC_STATUS_UPLOADING)
                .findAll();
        Iterator<WhisperHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            WhisperHit hit = iterator.next();
            hit.setSyncStatus(WhisperConfig.SYNC_STATUS_UPLOADED);
        }

        WhisperHelper whisperHelper = realm.where(WhisperHelper.class).findFirst();
        whisperHelper.setLastUploadSuccessTime(System.currentTimeMillis());
        whisperHelper.setLastUploadTime(System.currentTimeMillis());
        whisperHelper.setUploadFailedTimes(0);

        realm.commitTransaction();
        realm.close();
    }

}
