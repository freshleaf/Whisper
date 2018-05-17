package io.buzznerd.varys.whisper.model.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.buzznerd.varys.whisper.model.realmobject.WhisperSession;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public class SessionTrack implements Serializable {

    private long sessionId;
    private long applicationId;

    private long startTime;
    private long endTime;

    private String userId;

    private String language;
    private String netStatus;
    private String countryCode;

    private List<HitTrack> hits;

    public static SessionTrack fromRealmObject(WhisperSession whisperSession) {
        if (whisperSession == null) {
            return null;
        }

        SessionTrack sessionTrack = new SessionTrack();
        sessionTrack.sessionId = whisperSession.getId();
        sessionTrack.applicationId = whisperSession.getApplicationId();
        sessionTrack.startTime = whisperSession.getStartTime();
        sessionTrack.endTime = whisperSession.getEndTime();
        sessionTrack.userId = whisperSession.getUserId();
        sessionTrack.language = whisperSession.getLanguage();
        sessionTrack.netStatus = whisperSession.getNetStatus();
        sessionTrack.countryCode = whisperSession.getCountryCode();

        return sessionTrack;
    }

    public void addHitTrack(HitTrack hitTrack) {
        if (hitTrack == null) {
            return;
        }

        if (hits == null) {
            hits = new ArrayList<>();
        }

        hits.add(hitTrack);
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public List<HitTrack> getHits() {
        return hits;
    }

    public void setHits(List<HitTrack> hits) {
        this.hits = hits;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(String netStatus) {
        this.netStatus = netStatus;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
}
