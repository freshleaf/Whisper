package io.buzznerd.varys.whisper.component;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public final class WhisperConfig {

    private static String mServerUrl;

    private WhisperConfig() {

    }

    public static final int SYNC_STATUS_NOT_UPLOAD = 0;
    public static final int SYNC_STATUS_UPLOADING = 1;
    public static final int SYNC_STATUS_UPLOADED = 2;

    /**
     * default hit action type
     */
    public static final int ACTION_TYPE_NORMAL = 0;

    /**
     * hit action: app start
     */
    public static final int ACTION_TYPE_START_APP = 100;

    /**
     * hit action: page resume
     */
    public static final int ACTION_PAGE_RESUME = 101;

    /**
     * hit action: page pause
     */
    public static final int ACTION_PAGE_PAUSE = 102;

    /**
     * hit action: start a new session
     */
    public static final int ACTION_START_SESSION = 103;

    /**
     * hit action: end current session
     */
    public static final int ACTION_END_SESSION = 104;

    /**
     * hit action: user login
     */
    public static final int ACTION_USER_LOGIN = 105;

    /**
     * hit action: user logout
     */
    public static final int ACTION_USER_LOGOUT = 106;

    /**
     * hit action: okchem biz event
     */
    public static final int ACTION_OKCHEM_HIT = 200;

    /**
     * hit action: exception caught
     */
    public static final int ACTION_EXCEPTION = 400;

    /**
     * get Varys server url
     *
     * @param context Context
     * @return varys server url
     */
    public static String getServerUrl(Context context) {
        if (mServerUrl == null) {
            ApplicationInfo appInfo = null;
            try {
                appInfo = context.getPackageManager()
                        .getApplicationInfo(context.getPackageName(),
                                PackageManager.GET_META_DATA);
                mServerUrl = appInfo.metaData.getString("WHISPER_SERVER_ADDRESS");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mServerUrl;
    }

    // TODO define token and get it
    public static String getToken() {
        String token = "96179ce8939c4cdfacba65baab1d5ff8";
        return token;
    }

}
