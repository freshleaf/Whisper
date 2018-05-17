package io.buzznerd.varys.whisper.component;

/**
 * Created on 16/9/30
 *
 * @author Xingye
 * @since 1.0.0
 */

public interface Callbackable<T> {

    void success(T t);

    void failure(JobError cause);

}
