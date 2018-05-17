package io.buzznerd.varys.whisper.model.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public class UploadRequest implements Serializable {

    private String deviceId;
    private String trackingId;
    private long clientUploadTime;
    private String mobileAppId;
    private String whisperVersion;

    private List<ApplicationTrack> applications;

    public void addApplicationTrack(ApplicationTrack applicationTrack) {
        if (applicationTrack == null) {
            return;
        }

        if (applications == null) {
            applications = new ArrayList<>();
        }
        applications.add(applicationTrack);
    }

    public int getTrackSize() {
        if (applications == null) {
            return 0;
        }

        return applications.size();
    }

    public List<ApplicationTrack> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationTrack> applications) {
        this.applications = applications;
    }

    public long getClientUploadTime() {
        return clientUploadTime;
    }

    public void setClientUploadTime(long clientUploadTime) {
        this.clientUploadTime = clientUploadTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getMobileAppId() {
        return mobileAppId;
    }

    public void setMobileAppId(String mobileAppId) {
        this.mobileAppId = mobileAppId;
    }

    public String getWhisperVersion() {
        return whisperVersion;
    }

    public void setWhisperVersion(String whisperVersion) {
        this.whisperVersion = whisperVersion;
    }
}
