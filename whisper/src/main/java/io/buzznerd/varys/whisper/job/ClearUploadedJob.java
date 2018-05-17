package io.buzznerd.varys.whisper.job;

import io.buzznerd.varys.whisper.component.JobRunnable;
import io.buzznerd.varys.whisper.component.WhisperConfig;
import io.buzznerd.varys.whisper.model.realmobject.WhisperApplication;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHit;
import io.buzznerd.varys.whisper.model.realmobject.WhisperSession;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created on 16/10/7
 *
 * @author Xingye
 * @since 1.0.0
 */

public class ClearUploadedJob extends JobRunnable {

    @Override
    public void run() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.beginTransaction();

        RealmResults<WhisperHit> whisperHitRealmResults = realm.where(WhisperHit.class)
                .equalTo("syncStatus", WhisperConfig.SYNC_STATUS_UPLOADED)
                .findAll();
        whisperHitRealmResults.deleteAllFromRealm();

        RealmResults<WhisperApplication> applications = realm.where(WhisperApplication.class)
                .findAllSorted("id");
        int range = applications.size() - 1; // keep last application, it's currently in use
        for (int i = 0; i < range; i++) {
            WhisperApplication application = applications.get(i);
            long applicationId = application.getId();

            long applicationHitsCount = realm.where(WhisperHit.class)
                    .equalTo("applicationId", applicationId)
                    .count();
            if (applicationHitsCount == 0) {
                application.deleteFromRealm();

                realm.where(WhisperSession.class)
                        .equalTo("applicationId", applicationId)
                        .findAll()
                        .deleteAllFromRealm();

                mLog.log("delete all records for application id = " + applicationId, mLogLevel);
            }

        }

        realm.commitTransaction();
        realm.close();
    }
}
