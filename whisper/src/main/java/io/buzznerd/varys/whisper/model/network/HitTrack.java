package io.buzznerd.varys.whisper.model.network;


import java.io.Serializable;

import io.buzznerd.varys.whisper.model.realmobject.WhisperHit;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public class HitTrack implements Serializable {

    private long time;
    private String target;
    private int actionType;
    private String action;
    private String extra;
    private String okchemHitCode;

    public static HitTrack fromRealmObject(WhisperHit whisperHit) {
        if (whisperHit == null) {
            return null;
        }

        HitTrack hitTrack = new HitTrack();
        hitTrack.time = whisperHit.getTime();
        hitTrack.target = whisperHit.getTarget();
        hitTrack.actionType = whisperHit.getActionType();
        hitTrack.action = whisperHit.getAction();
        hitTrack.extra = whisperHit.getExtra();
        hitTrack.okchemHitCode = whisperHit.getOkchemHitCode();

        return hitTrack;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getOkchemHitCode() {
        return okchemHitCode;
    }

    public void setOkchemHitCode(String okchemHitCode) {
        this.okchemHitCode = okchemHitCode;
    }
}
