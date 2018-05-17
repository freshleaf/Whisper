package io.buzznerd.varys.whisper.component;

import android.content.Context;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import io.buzznerd.varys.whisper.Whisper;

/**
 * Created on 2016/10/18
 *
 * @author Xingye
 * @since 1.0.0
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler sCrashHandler;

    private Context mContext;
    // original app exception handler
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private Whisper.Log mLog;
    private Whisper.LogLevel mLogLevel;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (sCrashHandler == null) {
            sCrashHandler = new CrashHandler();
        }

        return sCrashHandler;
    }

    public void init(Context context, Whisper.Log log, Whisper.LogLevel logLevel) {
        mContext = context;
        mLog = log;
        mLogLevel = logLevel;

        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        mLog.log("exception on thread:" + t.getName() + "(" + t.getId() + ")", mLogLevel);

        handleException(e);

        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        } else {
            System.exit(0);
        }

    }

    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);

        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        String result = info.toString();
        printWriter.close();

        Whisper.getDefaultInstance().logApplicationEvent(null, "error", WhisperConfig.ACTION_EXCEPTION, result);
        Whisper.getDefaultInstance().doUpload(null);

        return true;
    }
}