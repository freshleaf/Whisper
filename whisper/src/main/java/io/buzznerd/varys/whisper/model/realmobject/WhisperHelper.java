package io.buzznerd.varys.whisper.model.realmobject;

import io.realm.RealmObject;

/**
 * Created on 16/10/3
 *
 * @author Xingye
 * @since 1.0.0
 */

public class WhisperHelper extends RealmObject {

    private static final long STEP_APPLICATION_ID = 1;
    private static final long STEP_SESSION_ID = 1;

    private long applicationId;
    private long sessionId;
    private long lastUploadTime;
    private long lastUploadSuccessTime;
    private int uploadFailedTimes;

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getNextApplicationId() {
        return applicationId + STEP_APPLICATION_ID;
    }

    public long getNextSessionId() {
        return sessionId + STEP_SESSION_ID;
    }

    public long getLastUploadTime() {
        return lastUploadTime;
    }

    public void setLastUploadTime(long lastUploadTime) {
        this.lastUploadTime = lastUploadTime;
    }

    public int getUploadFailedTimes() {
        return uploadFailedTimes;
    }

    public void setUploadFailedTimes(int uploadFailedTimes) {
        this.uploadFailedTimes = uploadFailedTimes;
    }

    public long getLastUploadSuccessTime() {
        return lastUploadSuccessTime;
    }

    public void setLastUploadSuccessTime(long lastUploadSuccessTime) {
        this.lastUploadSuccessTime = lastUploadSuccessTime;
    }
}
