package io.buzznerd.varys.whisper.model.network;

import java.io.Serializable;

/**
 * Created on 2016/10/11
 *
 * @author Xingye
 * @since 1.0.0
 */

public class WhisperResponse implements Serializable {

    protected String respcode;
    protected String message;
    protected String respUUID;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRespcode() {
        return respcode;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }

    public String getRespUUID() {
        return respUUID;
    }

    public void setRespUUID(String respUUID) {
        this.respUUID = respUUID;
    }
}
