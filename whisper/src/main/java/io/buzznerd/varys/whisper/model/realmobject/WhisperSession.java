package io.buzznerd.varys.whisper.model.realmobject;

import io.realm.RealmObject;

/**
 * Created on 16/9/30
 *
 * @author Xingye
 * @since 1.0.0
 */

public class WhisperSession extends RealmObject {

    public static final String FIELD_ID = "id";

    private long id;

    // context
    private long applicationId;

    private long startTime;
    private long endTime;

    private String mUserId;

    private String language;
    private String netStatus;
    private String countryCode;

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(String netStatus) {
        this.netStatus = netStatus;
    }

    @Override
    public String toString() {
        return "WhisperSession{" +
                "applicationId=" + applicationId +
                ", id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", mUserId='" + mUserId + '\'' +
                ", language='" + language + '\'' +
                ", netStatus='" + netStatus + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}
