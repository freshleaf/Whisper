package io.buzznerd.varys.whisper.model.realmobject;

import io.buzznerd.varys.whisper.component.WhisperConfig;
import io.realm.RealmObject;

/**
 * Created on 16/9/30
 *
 * @author Xingye
 * @since 1.0.0
 */

public class WhisperHit extends RealmObject {

    private long applicationId;
    private long sessionId;
    private long time;
    private String target;
    private int actionType = WhisperConfig.ACTION_TYPE_NORMAL;
    private String action;
    private String extra;
    private int syncStatus = WhisperConfig.SYNC_STATUS_NOT_UPLOAD;
    private String okchemHitCode;

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getOkchemHitCode() {
        return okchemHitCode;
    }

    public void setOkchemHitCode(String okchemHitCode) {
        this.okchemHitCode = okchemHitCode;
    }

    @Override
    public String toString() {
        return "WhisperHit{" +
                "action='" + action + '\'' +
                ", applicationId=" + applicationId +
                ", sessionId=" + sessionId +
                ", time=" + time +
                ", target='" + target + '\'' +
                ", actionType=" + actionType +
                ", extra='" + extra + '\'' +
                ", syncStatus=" + syncStatus +
                ", okchemHitCode='" + okchemHitCode + '\'' +
                '}';
    }
}
