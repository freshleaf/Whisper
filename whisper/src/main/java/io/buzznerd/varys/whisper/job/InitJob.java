package io.buzznerd.varys.whisper.job;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.Locale;

import io.buzznerd.varys.whisper.component.Callbackable;
import io.buzznerd.varys.whisper.component.JobCallbackRunnable;
import io.buzznerd.varys.whisper.component.JobError;
import io.buzznerd.varys.whisper.model.realmobject.WhisperApplication;
import io.buzznerd.varys.whisper.model.realmobject.WhisperDevice;
import io.buzznerd.varys.whisper.model.realmobject.WhisperDeviceProfile;
import io.buzznerd.varys.whisper.model.realmobject.WhisperHelper;
import io.buzznerd.varys.whisper.utils.DeviceUtils;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created on 16/10/1
 *
 * @author Xingye
 * @since 1.0.0
 */
public class InitJob extends JobCallbackRunnable<Void> {

    private Context mContext;

    public InitJob(Context context, Callbackable<Void> callback) {
        super(callback);
        this.mContext = context;
    }

    @Override
    public void run() {
        // 1. do init environment
        // 2. do add a new "application" context

        // if Whisper is init in Application, and there is service, init may be called more than once

        Realm realm = Realm.getInstance(mRealmConfiguration);
        realm.beginTransaction();

        // TODO fix bug of multi calling
        long currentTime = System.currentTimeMillis();
        RealmResults<WhisperApplication> applicationRealmResults = realm.where(WhisperApplication.class)
                .findAll();
        if (applicationRealmResults.size() > 0) {
            long lastAppTime = applicationRealmResults.last().getCreateTime();
            if (currentTime - lastAppTime < 1000) {
                // ignore too closed two init
                realm.cancelTransaction();

                realm.close();
                callbackFailed(new JobError("too soon to start a new application"));
                return;
            }
        }

        // init context
        RealmResults<WhisperHelper> helperRealmResults = realm.where(WhisperHelper.class).findAll();
        if (helperRealmResults.size() == 0) {
            WhisperHelper whisperHelper = realm.createObject(WhisperHelper.class);
            whisperHelper.setApplicationId(0);
            whisperHelper.setSessionId(0);
            whisperHelper.setLastUploadTime(System.currentTimeMillis());
            whisperHelper.setLastUploadSuccessTime(System.currentTimeMillis());
        }

        // check device info
        RealmResults<WhisperDevice> results = realm.where(WhisperDevice.class).findAll();
        boolean isHasDeviceId = results.size() != 0;
        if (isHasDeviceId) {
            // check device profile changed or not
            WhisperDevice device = results.first();

            mLog.log("hit stored device info: " + device.toString(), mLogLevel);

            RealmResults<WhisperDeviceProfile> profileRealmResults = realm.where(WhisperDeviceProfile.class)
                    .findAllSorted(WhisperDeviceProfile.FIELD_CREATE_TIME, Sort.DESCENDING);

            WhisperDeviceProfile deviceProfile = new WhisperDeviceProfile();
            deviceProfile.setCreateTime(System.currentTimeMillis());
            deviceProfile.setOsVersion(Build.VERSION.RELEASE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                deviceProfile.setSecurityPatch(Build.VERSION.SECURITY_PATCH);
            }

            boolean isNewProfile = false;
            if (profileRealmResults.size() != 0) {
                WhisperDeviceProfile storedProfile = profileRealmResults.first();
                if (!deviceProfile.equals(storedProfile)) {
                    isNewProfile = true;
                } else {
                    mLog.log("hit stored device profile: " + storedProfile.toString(), mLogLevel);
                }
            }

            if (isNewProfile) {
                realm.copyToRealm(deviceProfile);

                mLog.log("create device profile: " + deviceProfile.toString(), mLogLevel);
            }
        } else {
            // not exist, create device info
            mLog.log("not found device id, create one", mLogLevel);

            String mac = DeviceUtils.getMACAddress("wlan0");
            if (TextUtils.isEmpty(mac)) {
                mac = DeviceUtils.getMACAddress("eth0");
            }
            if (TextUtils.isEmpty(mac)) {
                mac = DeviceUtils.getMACAddress(null);
            }

            String mark = DeviceUtils.createDeviceMark(mac);

            StringBuilder buffer = new StringBuilder();
            buffer.append("{").append(mac).append(",").append(mark).append("}");
            String deviceId = buffer.toString();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) mContext.getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                wm.getDefaultDisplay().getRealMetrics(displayMetrics);
            } else {
                wm.getDefaultDisplay().getMetrics(displayMetrics);
            }
            int widthPixels = displayMetrics.widthPixels;
            int heightPixels = displayMetrics.heightPixels;
            String resolution = widthPixels + "x" + heightPixels;

            WhisperDevice device = realm.createObject(WhisperDevice.class);
            device.setDeviceId(deviceId);
            device.setDeviceMode(Build.MODEL);
            device.setDeviceManufacturer(Build.MANUFACTURER);
            device.setScreenResolution(resolution);

            WhisperDeviceProfile deviceProfile = realm.createObject(WhisperDeviceProfile.class);
            deviceProfile.setCreateTime(System.currentTimeMillis());
            deviceProfile.setOsVersion(Build.VERSION.RELEASE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                deviceProfile.setSecurityPatch(Build.VERSION.SECURITY_PATCH);
            }

            mLog.log("create device info: " + device.toString(), mLogLevel);
            mLog.log("create device profile: " + deviceProfile.toString(), mLogLevel);
        }

        // create new application context
        WhisperHelper whisperHelper = realm.where(WhisperHelper.class).findFirst();
        whisperHelper.setApplicationId(whisperHelper.getNextApplicationId());

        WhisperApplication application = realm.createObject(WhisperApplication.class);
        application.setCreateTime(System.currentTimeMillis());
        application.setId(whisperHelper.getApplicationId());
        application.setDevice(realm.where(WhisperDevice.class).findFirst());
        application.setDeviceProfile(
                realm.where(WhisperDeviceProfile.class)
                        .findAllSorted(WhisperDeviceProfile.FIELD_CREATE_TIME, Sort.DESCENDING)
                        .first());
        application.setNetStatus(DeviceUtils.getNetStatus(mContext));
        application.setLanguage(Locale.getDefault().toString());

        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (pi != null) {
            application.setAppVersionName(pi.versionName);
            application.setAppVersionCode(pi.versionCode);
        }

        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getNetworkCountryIso();
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = tm.getSimCountryIso();
        }
        application.setCountryCode(countryCode);

        realm.commitTransaction();

        mLog.log(application.toString(), mLogLevel);

        realm.close();

        callbackSuccess(null);
    }
}
