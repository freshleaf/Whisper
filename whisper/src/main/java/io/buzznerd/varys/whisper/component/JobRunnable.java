package io.buzznerd.varys.whisper.component;

import io.buzznerd.varys.whisper.Whisper;
import io.realm.RealmConfiguration;

/**
 * Created on 16/9/30
 *
 * @author Xingye
 * @since 1.0.0
 */

public abstract class JobRunnable implements Runnable {

    protected Whisper.Log mLog;
    protected Whisper.LogLevel mLogLevel;
    protected RealmConfiguration mRealmConfiguration;

    public void setRealmConfiguration(RealmConfiguration realmConfiguration) {
        this.mRealmConfiguration = realmConfiguration;
    }

    public void setLog(Whisper.Log log) {
        this.mLog = log;
    }

    public void setLogLevel(Whisper.LogLevel logLevel) {
        this.mLogLevel = logLevel;
    }

}
