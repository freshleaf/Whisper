package io.buzznerd.varys.whisper.model.realmobject;

import io.realm.RealmObject;

/**
 * Created on 16/10/3
 *
 * @author Xingye
 * @since 1.0.0
 */

public class WhisperApplication extends RealmObject {

    private long id;
    private long createTime;
    private WhisperDevice device;
    private WhisperDeviceProfile deviceProfile;

    private String language;
    private String netStatus;
    private String countryCode;

    private Integer appVersionCode;
    private String appVersionName;

    public WhisperDevice getDevice() {
        return device;
    }

    public void setDevice(WhisperDevice device) {
        this.device = device;
    }

    public WhisperDeviceProfile getDeviceProfile() {
        return deviceProfile;
    }

    public void setDeviceProfile(WhisperDeviceProfile deviceProfile) {
        this.deviceProfile = deviceProfile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public Integer getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(Integer appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    @Override
    public String toString() {
        return "WhisperApplication{" +
                "appVersionCode=" + appVersionCode +
                ", id=" + id +
                ", createTime=" + createTime +
                ", device=" + device +
                ", deviceProfile=" + deviceProfile +
                ", language='" + language + '\'' +
                ", netStatus='" + netStatus + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", appVersionName='" + appVersionName + '\'' +
                '}';
    }
}
