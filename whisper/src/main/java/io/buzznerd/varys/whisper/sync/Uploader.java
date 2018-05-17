package io.buzznerd.varys.whisper.sync;

import android.os.Handler;
import android.os.Message;

import io.buzznerd.varys.whisper.Whisper;
import io.buzznerd.varys.whisper.component.Callbackable;
import io.buzznerd.varys.whisper.component.JobError;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHelper;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public final class Uploader {

    private static final long SYNC_INTERVAL_NORMAL = 30 * 60 * 1000;

    // minimal interval time to start next sync
    // scenario: app start, lots of work to do, added interval for networking payload
    private static final long SYNC_INTERVAL_MIN = 10000;

    private static final int MSG_DO_UPLOAD_ACTION = 40001;
    private static RealmConfiguration sRealmConfiguration;

    private Uploader() {
    }

    private static Handler sHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_DO_UPLOAD_ACTION) {
                sHandler.removeMessages(MSG_DO_UPLOAD_ACTION);
                doUpload();
            }
        }
    };

    public static void startUpload(RealmConfiguration realmConfiguration) {
        sRealmConfiguration = realmConfiguration;
        if (!sHandler.hasMessages(MSG_DO_UPLOAD_ACTION)) {
            scheduleNextUpload(true);
        }
    }

    public static void doUpload() {
        Whisper.getDefaultInstance().doUpload(new Callbackable<Void>() {
            @Override
            public void success(Void aVoid) {
                scheduleNextUpload(false);
                Whisper.getDefaultInstance().doClearUploaded();
            }

            @Override
            public void failure(JobError cause) {
                scheduleNextUpload(false);
            }
        });
    }

    private static long getNextUploadInterval(int failedTimes) {
        // upload strategy

        if (failedTimes == 0) {
            return SYNC_INTERVAL_NORMAL;
        } else if (failedTimes < 3) { // failed once, twice
            return 60000; // do it in next 1 minute
        } else if (failedTimes < 6) {
            return 600000; // do it in next 10 minutes
        }

        return SYNC_INTERVAL_NORMAL;
    }

    private static void scheduleNextUpload(boolean appStart) {
        if (appStart) {
            sHandler.sendEmptyMessageDelayed(MSG_DO_UPLOAD_ACTION, SYNC_INTERVAL_MIN);
            return;
        }

        Realm realm = Realm.getInstance(sRealmConfiguration);

        WhisperHelper helper = realm.where(WhisperHelper.class).findFirst();
        long lastTime = helper.getLastUploadTime();
        int failedTimes = helper.getUploadFailedTimes();

        long intervalTime = getNextUploadInterval(failedTimes);
        long nextUploadIntervalTime = lastTime + intervalTime - System.currentTimeMillis();
        if (nextUploadIntervalTime > 0) {
            sHandler.sendEmptyMessageDelayed(MSG_DO_UPLOAD_ACTION, nextUploadIntervalTime);
        } else {
            sHandler.sendEmptyMessage(MSG_DO_UPLOAD_ACTION);
        }

        realm.close();
    }

}
