package io.buzznerd.varys.whisper.job;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.Locale;

import io.buzznerd.varys.whisper.component.JobRunnable;
import io.buzznerd.varys.whisper.component.WhisperConfig;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHelper;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHit;
import io.buzznerd.varys.whisper.model.realmobject.WhisperSession;
import io.buzznerd.varys.whisper.utils.DeviceUtils;
import io.realm.Realm;

/**
 * Created on 16/10/3
 *
 * @author Xingye
 * @since 1.0.0
 */

public class StartSessionJob extends JobRunnable {

    private String mUserId;
    private Context mContext;

    public StartSessionJob(Context context, String userId) {
        this.mContext = context;
        this.mUserId = userId;
    }

    @Override
    public void run() {
        Realm realm = Realm.getInstance(mRealmConfiguration);

        realm.beginTransaction();
        WhisperHelper idHelper = realm.where(WhisperHelper.class).findFirst();
        idHelper.setSessionId(idHelper.getNextSessionId());

        WhisperSession session = realm.createObject(WhisperSession.class);
        session.setId(idHelper.getSessionId());
        session.setUserId(mUserId);
        session.setStartTime(System.currentTimeMillis());

        session.setNetStatus(DeviceUtils.getNetStatus(mContext));
        session.setLanguage(Locale.getDefault().toString());

        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getNetworkCountryIso();
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = tm.getSimCountryIso();
        }
        session.setCountryCode(countryCode);

        session.setApplicationId(
                realm.where(WhisperHelper.class).findFirst().getApplicationId());

        WhisperHit hit = realm.createObject(WhisperHit.class);
        hit.setApplicationId(realm.where(WhisperHelper.class).findFirst().getApplicationId());
        hit.setSessionId(realm.where(WhisperHelper.class).findFirst().getSessionId());
        hit.setTime(System.currentTimeMillis());
        hit.setTarget(null);
        hit.setAction("start session");
        hit.setActionType(WhisperConfig.ACTION_START_SESSION);
        hit.setExtra(null);
        realm.commitTransaction();

        mLog.log("create session: " + session.toString(), mLogLevel);
        mLog.log(hit.toString(), mLogLevel);

        realm.close();
    }

}
