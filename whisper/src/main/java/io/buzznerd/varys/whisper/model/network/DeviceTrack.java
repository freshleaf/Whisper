package io.buzznerd.varys.whisper.model.network;

import java.io.Serializable;

import io.buzznerd.varys.whisper.model.realmobject.WhisperDevice;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public class DeviceTrack implements Serializable {

    private String deviceId;
    private String deviceMode;
    private String deviceManufacturer;
    private String screenResolution;
    private String os;

    public static DeviceTrack fromRealmObject(WhisperDevice whisperDevice) {
        if (whisperDevice == null) {
            return null;
        }

        DeviceTrack deviceTrack = new DeviceTrack();
        deviceTrack.deviceId = whisperDevice.getDeviceId();
        deviceTrack.deviceManufacturer = whisperDevice.getDeviceManufacturer();
        deviceTrack.deviceMode = whisperDevice.getDeviceMode();
        deviceTrack.screenResolution = whisperDevice.getScreenResolution();
        deviceTrack.os = whisperDevice.getOs();

        return deviceTrack;
    }

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

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getScreenResolution() {
        return screenResolution;
    }

    public void setScreenResolution(String screenResolution) {
        this.screenResolution = screenResolution;
    }
}
