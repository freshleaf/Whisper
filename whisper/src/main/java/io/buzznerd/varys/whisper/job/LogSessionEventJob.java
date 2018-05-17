package io.buzznerd.varys.whisper.job;


import io.buzznerd.varys.whisper.component.JobError;
import io.buzznerd.varys.whisper.component.JobRunnable;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHelper;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHit;
import io.buzznerd.varys.whisper.model.realmobject.WhisperSession;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public class LogSessionEventJob extends JobRunnable {

    private long mTime;

    private String mTarget;
    private int mActionType;
    private String mAction;
    private String mExtra;
    private String mOkchemBizHitCode;

    public LogSessionEventJob(long time, String target, String action, int actionType, String extra, String okchemBizHitCode) {
        this.mTime = time;
        this.mTarget = target;
        this.mAction = action;
        this.mActionType = actionType;
        this.mExtra = extra;
        this.mOkchemBizHitCode = okchemBizHitCode;
    }

    public LogSessionEventJob(String target, String action, int actionType, String extra, String okchemBizHitCode) {
        this(System.currentTimeMillis(), target, action, actionType, extra, okchemBizHitCode);
    }

    @Override
    public void run() {
        Realm realm = Realm.getInstance(mRealmConfiguration);

        realm.beginTransaction();
        long sessionId = realm.where(WhisperHelper.class).findFirst().getSessionId();

        RealmResults<WhisperSession> sessions = realm.where(WhisperSession.class)
                .equalTo(WhisperSession.FIELD_ID, sessionId).findAll();
        if (sessions.size() == 0) {
            realm.cancelTransaction();
            realm.close();
            throw new JobError("not found session: session id = " + sessionId);
        }

        WhisperSession session = sessions.first();

        WhisperHit hit = realm.createObject(WhisperHit.class);
        hit.setApplicationId(session.getApplicationId());
        hit.setTime(mTime);
        hit.setTarget(mTarget);
        hit.setAction(mAction);
        hit.setActionType(mActionType);
        hit.setExtra(mExtra);
        hit.setOkchemHitCode(mOkchemBizHitCode);
        if (session.getEndTime() != 0) {
            mLog.log("session is already ended", mLogLevel);
        } else {
            hit.setSessionId(session.getId());
        }
        mLog.log(hit.toString(), mLogLevel);
        realm.commitTransaction();
        realm.close();
    }

}
