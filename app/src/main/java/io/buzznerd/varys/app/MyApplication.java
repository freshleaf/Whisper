package io.buzznerd.varys.app;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import io.buzznerd.varys.whisper.Whisper;

/**
 * Created on 2016/11/2
 *
 * @author Xingye
 * @since 1.0.0
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        // logger
        String tag = "WhisperApp";
        Logger.init(tag).logLevel(LogLevel.FULL);

        // umeng analyze
        if (BuildConfig.DEBUG) {
            MobclickAgent.setDebugMode(true);
        }
        MobclickAgent.enableEncrypt(true); // encrypt umeng log

        // whisper
        Whisper.Builder whisperBuilder = new Whisper.Builder();
        whisperBuilder.setLogLevel(Whisper.LogLevel.FULL);
        Whisper whisper = whisperBuilder.build();
        whisper.init(this);
    }
}
