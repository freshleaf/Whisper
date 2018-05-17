package io.buzznerd.varys.whisper;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.buzznerd.varys.whisper.component.ApiService;
import io.buzznerd.varys.whisper.component.Callbackable;
import io.buzznerd.varys.whisper.component.CrashHandler;
import io.buzznerd.varys.whisper.component.JobCallbackRunnable;
import io.buzznerd.varys.whisper.component.JobError;
import io.buzznerd.varys.whisper.component.JobRunnable;
import io.buzznerd.varys.whisper.component.RestClient;
import io.buzznerd.varys.whisper.component.WhisperConfig;
import io.buzznerd.varys.whisper.component.WhisperRealmMigration;
import io.buzznerd.varys.whisper.component.WhisperRealmModule;
import io.buzznerd.varys.whisper.job.ClearUploadedJob;
import io.buzznerd.varys.whisper.job.EndSessionJob;
import io.buzznerd.varys.whisper.job.InitJob;
import io.buzznerd.varys.whisper.job.LogApplicationEventJob;
import io.buzznerd.varys.whisper.job.LogSessionEventJob;
import io.buzznerd.varys.whisper.job.StartSessionJob;
import io.buzznerd.varys.whisper.job.UploadJob;
import io.buzznerd.varys.whisper.sync.Uploader;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created on 16/9/30
 *
 * @author Xingye
 * @since 1.0.0
 */
public final class Whisper {

    // TODO get server configuration to change upload policy
    // TODO enable Https

    private static final String SDK_THREAD_NAME = "Whisper-SDK";
    private static final String SDK_LOG_TAG = "Whisper";

    private static Whisper mWhisper;

    private Executor mJobExecutor;
    private Executor mCallbackExecutor;
    private LogLevel mLogLevel;
    private Log mLog;

    private boolean isInit = false;

    private RealmConfiguration mRealmConfiguration;
    private ApiService mApiService;
    private String mMobileAppId;

    public static Whisper getDefaultInstance() {
        if (mWhisper == null || mWhisper.mRealmConfiguration == null) {
            throw new RuntimeException("Whisper must build first");
        }
        return mWhisper;
    }

    private Whisper(Executor jobExecutor, Executor callbackExecutor, Log log,
                    LogLevel logLevel) {
        this.mJobExecutor = jobExecutor;
        this.mCallbackExecutor = callbackExecutor;
        this.mLog = log;
        this.mLogLevel = logLevel;
    }

    private void doJobWithCallback(JobCallbackRunnable job) {
        if (!isInit) {
            throw new RuntimeException("init Whisper first");
        }

        job.setLog(mLog);
        job.setLogLevel(mLogLevel);
        job.setRealmConfiguration(mRealmConfiguration);
        job.setCallbackExecutor(mCallbackExecutor);
        mJobExecutor.execute(job);
    }

    private void doJob(JobRunnable job) {
        if (!isInit) {
            throw new RuntimeException("init Whisper first");
        }

        job.setLog(mLog);
        job.setLogLevel(mLogLevel);
        job.setRealmConfiguration(mRealmConfiguration);
        mJobExecutor.execute(job);
    }

    public void init(Context context) {
        if (isInit) {
            mWhisper.mLog.log("already init", mLogLevel);
            return;
        }

        isInit = true;
        Realm.init(context);

        // realm the database
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder();

        // TODO to use migration when stable
        builder.deleteRealmIfMigrationNeeded();

        builder.name("library.whisper.realm")
                .schemaVersion(WhisperRealmMigration.CURRENT_REALM_SCHEMA_VERSION)
                .migration(new WhisperRealmMigration())
                .modules(new WhisperRealmModule());

        mWhisper.mRealmConfiguration = builder.build();

        CrashHandler.getInstance().init(context, mLog, mLogLevel);

        mApiService = RestClient.getApiService(context, mLogLevel);
        mMobileAppId = context.getApplicationContext().getPackageName();

        doJobWithCallback(new InitJob(context, new Callbackable<Void>() {

            @Override
            public void success(Void aVoid) {
                mLog.log("init Whisper success", mLogLevel);

                Uploader.startUpload(mRealmConfiguration);
            }

            @Override
            public void failure(JobError cause) {
                mLog.log("init Whisper failed", cause, mLogLevel);

                Uploader.startUpload(mRealmConfiguration);
            }
        }));
    }

    public void doUpload(Callbackable<Void> callback) {
        doJobWithCallback(new UploadJob(mApiService, mMobileAppId, callback));
    }

    public void doClearUploaded() {
        doJob(new ClearUploadedJob());
    }

    public void startSession(Context context, String userId) {
        doJob(new StartSessionJob(context, userId));
    }

    public void endSession() {
        doJob(new EndSessionJob());
    }

    private void logSessionEvent(String target, String action, int actionType, String extra, String okchemBizHitCode) {
        doJob(new LogSessionEventJob(target, action, actionType, extra, okchemBizHitCode));
    }

    public void logSessionEvent(String target, String action, int actionType, String extra) {
        logSessionEvent(target, action, actionType, extra, null);
    }

    public void logOkchemBizInSession(String okchemBizHitCode) {
        logSessionEvent(null, null, WhisperConfig.ACTION_OKCHEM_HIT, null, okchemBizHitCode);
    }

    private void logApplicationEvent(String target, String action, int actionType, String extra, String okchemBizHitCode) {
        doJob(new LogApplicationEventJob(target, action, actionType, extra, okchemBizHitCode));
    }

    public void logApplicationEvent(String target, String action, int actionType, String extra) {
        logApplicationEvent(target, action, actionType, extra, null);
    }

    public void logOkchemBizInApplication(String okchemBizHitCode) {
        logApplicationEvent(null, null, WhisperConfig.ACTION_OKCHEM_HIT, null, okchemBizHitCode);
    }

    public void logAppStart() {
        logApplicationEvent(null, "app start", WhisperConfig.ACTION_TYPE_START_APP, null);
    }

    public void logUserLogin(String userId) {
        logApplicationEvent(userId, "login", WhisperConfig.ACTION_USER_LOGIN, null);
    }

    public void logUserLogout(String userId) {
        logApplicationEvent(userId, "logout", WhisperConfig.ACTION_USER_LOGOUT, null);
    }

    public void logOnResume(Context context) {
        if (context == null) {
            return;
        }

        String target = context.getClass().getName();
        String action = "resume";
        int actionType = WhisperConfig.ACTION_PAGE_RESUME;
        logApplicationEvent(target, action, actionType, null, null);
    }

    public void logOnPause(Context context) {
        if (context == null) {
            return;
        }

        String target = context.getClass().getName();
        String action = "pause";
        int actionType = WhisperConfig.ACTION_PAGE_PAUSE;
        logApplicationEvent(target, action, actionType, null, null);
    }

    /**
     * Controls the level of logging.
     */
    public enum LogLevel {
        /**
         * No logging.
         */
        NONE,
        // BASIC,
        FULL;

        public boolean isLog() {
            return this != NONE;
        }
    }

    /**
     * Simple logging abstraction for debug messages.
     */
    public interface Log {

        /**
         * Log a debug message to the appropriate console.
         */
        void log(String message, LogLevel logLevel);

        void log(String message);

        void log(String message, Throwable t, LogLevel logLevel);

        void log(String message, Throwable t);

    }

    public static class SdkLog implements Log {

        private static final int LOG_CHUNK_SIZE = 4000;

        private final String mTag;

        public SdkLog(String tag) {
            this.mTag = tag;
        }

        public final void log(String message, LogLevel logLevel) {
            if (logLevel == null || logLevel.isLog()) {
                log(message);
            }
        }

        public final void log(String message) {
            for (int i = 0, len = message.length(); i < len; i += LOG_CHUNK_SIZE) {
                int end = Math.min(len, i + LOG_CHUNK_SIZE);
                logChunk(message.substring(i, end));
            }
        }

        @Override
        public void log(String message, Throwable t) {
            android.util.Log.d(getTag(), message, t);
        }

        @Override
        public void log(String message, Throwable t, LogLevel logLevel) {
            if (logLevel == null || logLevel.isLog()) {
                log(message, t);
            }
        }

        /**
         * Called one or more times for each call to {@link #log(String)}. The length of {@code chunk}
         * will be no more than 4000 characters to support Android's {@link android.util.Log} class.
         */
        public void logChunk(String chunk) {
            android.util.Log.d(getTag(), chunk);
        }

        public String getTag() {
            return mTag;
        }
    }

    public static class Builder {

        private static Executor sJobExecutor;
        private static Executor sCallbackExecutor;

        private LogLevel mLogLevel = LogLevel.FULL;
        private Log mLog;

        public Whisper build() {
            if (mLog == null) {
                mLog = getDefaultLog();
            }

            if (mWhisper != null) {
                mWhisper.mJobExecutor = sJobExecutor;
                mWhisper.mCallbackExecutor = sCallbackExecutor;
                mWhisper.mLog = mLog;
                mWhisper.mLogLevel = mLogLevel;
            } else {
                mWhisper = new Whisper(getJobExecutor(), getCallbackExecutor(), mLog, mLogLevel);
            }

            return mWhisper;
        }

        public Builder setLogLevel(LogLevel logLevel) {
            if (logLevel == null) {
                throw new NullPointerException("LogLevel can not be null");
            }
            this.mLogLevel = logLevel;
            return this;
        }

        public Builder setLog(Log log) {
            if (log == null) {
                throw new NullPointerException("Log can not be null");
            }
            this.mLog = log;
            return this;
        }

        private static Executor getCallbackExecutor() {
            if (sCallbackExecutor == null) {
                sCallbackExecutor = new Executor() {
                    private final Handler handler = new Handler(Looper.getMainLooper());

                    @Override
                    public void execute(Runnable r) {
                        handler.post(r);
                    }
                };
            }

            return sCallbackExecutor;
        }

        private static Executor getJobExecutor() {
            if (sJobExecutor == null) {
                ThreadFactory threadFactory = new ThreadFactory() {
                    @Override
                    public Thread newThread(final Runnable r) {
                        return new Thread(new Runnable() {
                            @Override
                            public void run() {
                                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                                r.run();
                            }
                        }, SDK_THREAD_NAME);
                    }
                };
                sJobExecutor = Executors.newSingleThreadExecutor(threadFactory);
            }
            return sJobExecutor;
        }

        private Log getDefaultLog() {
            return new SdkLog(SDK_LOG_TAG);
        }

    }

}
