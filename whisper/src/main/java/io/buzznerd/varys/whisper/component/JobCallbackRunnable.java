package io.buzznerd.varys.whisper.component;

import java.util.concurrent.Executor;

/**
 * Created on 16/9/30
 *
 * @author Xingye
 * @since 1.0.0
 */

public abstract class JobCallbackRunnable<T> extends JobRunnable {

    protected Callbackable<T> mCallback;
    protected Executor mCallbackExecutor;

    public JobCallbackRunnable(Callbackable<T> callback) {
        this.mCallback = callback;
    }

    public void setCallbackExecutor(Executor callbackExecutor) {
        this.mCallbackExecutor = callbackExecutor;
    }

    protected void callbackSuccess(final T t) {
        if (mCallback == null || mCallbackExecutor == null) {
            return;
        }
        mCallbackExecutor.execute(new Runnable() {

            @Override
            public void run() {
                mCallback.success(t);
            }
        });
    }

    protected void callbackFailed(final JobError cause) {
        if (mCallback == null || mCallbackExecutor == null) {
            return;
        }
        mCallbackExecutor.execute(new Runnable() {

            @Override
            public void run() {
                mCallback.failure(cause);
            }
        });
    }

}
