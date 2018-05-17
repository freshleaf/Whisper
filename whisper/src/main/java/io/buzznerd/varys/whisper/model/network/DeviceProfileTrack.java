package io.buzznerd.varys.whisper.model.network;

import java.io.Serializable;

import io.buzznerd.varys.whisper.model.realmobject.WhisperDeviceProfile;

/**
 * Created on 16/10/5
 *
 * @author Xingye
 * @since 1.0.0
 */

public class DeviceProfileTrack implements Serializable {

    private String osVersion;
    private String securityPatch;
    private long createTime;

    public static DeviceProfileTrack fromRealmObject(WhisperDeviceProfile whisperDeviceProfile) {
        if (whisperDeviceProfile == null) {
            return null;
        }

        DeviceProfileTrack deviceProfileTrack = new DeviceProfileTrack();
        deviceProfileTrack.createTime = whisperDeviceProfile.getCreateTime();
        deviceProfileTrack.securityPatch = whisperDeviceProfile.getSecurityPatch();
        deviceProfileTrack.osVersion = whisperDeviceProfile.getOsVersion();

        return deviceProfileTrack;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getSecurityPatch() {
        return securityPatch;
    }

    public void setSecurityPatch(String securityPatch) {
        this.securityPatch = securityPatch;
    }
}
