package io.buzznerd.varys.whisper.model.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.buzznerd.varys.whisper.model.realmobject.WhisperApplication;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public class ApplicationTrack implements Serializable {

    private long appId;
    private long createTime;

    private DeviceTrack deviceTrack;
    private DeviceProfileTrack deviceProfileTrack;

    private String language;
    private String netStatus;
    private String countryCode;

    private Integer appVersionCode;
    private String appVersionName;

    private List<SessionTrack> sessions;
    private List<HitTrack> hits;

    public static ApplicationTrack fromRealmObject(WhisperApplication whisperApplication) {
        if (whisperApplication == null) {
            return null;
        }

        ApplicationTrack applicationTrack = new ApplicationTrack();
        applicationTrack.createTime = whisperApplication.getCreateTime();
        applicationTrack.appId = whisperApplication.getId();
        applicationTrack.deviceTrack = DeviceTrack.fromRealmObject(whisperApplication.getDevice());
        applicationTrack.deviceProfileTrack = DeviceProfileTrack
                .fromRealmObject(whisperApplication.getDeviceProfile());
        applicationTrack.language = whisperApplication.getLanguage();
        applicationTrack.netStatus = whisperApplication.getNetStatus();
        applicationTrack.countryCode = whisperApplication.getCountryCode();
        applicationTrack.appVersionCode = whisperApplication.getAppVersionCode();
        applicationTrack.appVersionName = whisperApplication.getAppVersionName();

        return applicationTrack;
    }

    public void addSessionTrack(SessionTrack sessionTrack) {
        if (sessionTrack == null) {
            return;
        }

        if (sessions == null) {
            sessions = new ArrayList<>();
        }

        sessions.add(sessionTrack);
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public DeviceProfileTrack getDeviceProfileTrack() {
        return deviceProfileTrack;
    }

    public void setDeviceProfileTrack(DeviceProfileTrack deviceProfileTrack) {
        this.deviceProfileTrack = deviceProfileTrack;
    }

    public DeviceTrack getDeviceTrack() {
        return deviceTrack;
    }

    public void setDeviceTrack(DeviceTrack deviceTrack) {
        this.deviceTrack = deviceTrack;
    }

    public List<HitTrack> getHits() {
        return hits;
    }

    public void setHits(List<HitTrack> hits) {
        this.hits = hits;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public List<SessionTrack> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionTrack> sessions) {
        this.sessions = sessions;
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

    public Integer getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(Integer appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }
}
