package io.buzznerd.varys.whisper.model.realmobject;

import io.realm.RealmObject;

/**
 * Created on 16/9/30
 *
 * @author Xingye
 * @since 1.0.0
 */

public class WhisperDevice extends RealmObject {

    private String deviceId;

    private String deviceMode;

    private String deviceManufacturer;

    private String screenResolution;

    private String os = "Android";

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }

    public String getDeviceMode() {
        return deviceMode;
    }

    public void setDeviceMode(String deviceMode) {
        this.deviceMode = deviceMode;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Override
    public String toString() {
        return "WhisperDevice{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceMode='" + deviceMode + '\'' +
                ", deviceManufacturer='" + deviceManufacturer + '\'' +
                ", screenResolution='" + screenResolution + '\'' +
                ", os='" + os + '\'' +
                '}';
    }

}
