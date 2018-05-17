package io.buzznerd.varys.whisper.job;

import io.buzznerd.varys.whisper.component.JobRunnable;
import io.buzznerd.varys.whisper.component.WhisperConfig;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHelper;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHit;
import io.buzznerd.varys.whisper.model.realmobject.WhisperSession;
import io.realm.Realm;
import io.realm.Sort;

/**
 * Created on 16/10/3
 *
 * @author Xingye
 * @since 1.0.0
 */

public class EndSessionJob extends JobRunnable {

    @Override
    public void run() {
        Realm realm = Realm.getInstance(mRealmConfiguration);

        WhisperSession session = realm.where(WhisperSession.class)
                .findAllSorted(WhisperSession.FIELD_ID, Sort.DESCENDING)
                .first();

        realm.beginTransaction();

        WhisperHit hit = realm.createObject(WhisperHit.class);
        hit.setApplicationId(realm.where(WhisperHelper.class).findFirst().getApplicationId());
        hit.setSessionId(realm.where(WhisperHelper.class).findFirst().getSessionId());
        hit.setTime(System.currentTimeMillis());
        hit.setTarget(null);
        hit.setAction("end session");
        hit.setActionType(WhisperConfig.ACTION_END_SESSION);
        hit.setExtra(null);

        session.setEndTime(System.currentTimeMillis());
        realm.commitTransaction();

        mLog.log("end session: " + session.toString(), mLogLevel);
        mLog.log(hit.toString(), mLogLevel);

        realm.close();
    }
}
