package io.buzznerd.varys.whisper.job;


import io.buzznerd.varys.whisper.component.JobRunnable;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHelper;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHit;
import io.realm.Realm;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public class LogApplicationEventJob extends JobRunnable {

    private long mTime;

    private String mTarget;
    private int mActionType;
    private String mAction;
    private String mExtra;
    private String mOkchemBizHitCode;

    public LogApplicationEventJob(long time, String target, String action, int actionType, String extra, String okchemBizHitCode) {
        this.mTime = time;
        this.mTarget = target;
        this.mAction = action;
        this.mActionType = actionType;
        this.mExtra = extra;
        this.mOkchemBizHitCode = okchemBizHitCode;
    }

    public LogApplicationEventJob(String target, String action, int actionType, String extra, String okchemBizHitCode) {
        this(System.currentTimeMillis(), target, action, actionType, extra, okchemBizHitCode);
    }

    @Override
    public void run() {
        Realm realm = Realm.getInstance(mRealmConfiguration);

        realm.beginTransaction();
        WhisperHit hit = realm.createObject(WhisperHit.class);
        hit.setApplicationId(realm.where(WhisperHelper.class).findFirst().getApplicationId());
        hit.setTime(mTime);
        hit.setTarget(mTarget);
        hit.setAction(mAction);
        hit.setActionType(mActionType);
        hit.setExtra(mExtra);
        hit.setOkchemHitCode(mOkchemBizHitCode);
        realm.commitTransaction();

        mLog.log(hit.toString(), mLogLevel);

        realm.close();
    }
}
