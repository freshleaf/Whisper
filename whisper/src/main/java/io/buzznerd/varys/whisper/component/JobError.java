package io.buzznerd.varys.whisper.component;

/**
 * Created on 16/9/30
 *
 * @author Xingye
 * @since 1.0.0
 */

public class JobError extends RuntimeException {

    public JobError() {
        super();
    }

    public JobError(String message) {
        super(message);
    }

    public JobError(Throwable cause) {
        super(cause);
    }
}
