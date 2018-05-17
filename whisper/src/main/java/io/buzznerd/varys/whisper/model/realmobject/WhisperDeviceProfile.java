package io.buzznerd.varys.whisper.model.realmobject;

import io.realm.RealmObject;

/**
 * Created on 16/10/3
 *
 * @author Xingye
 * @since 1.0.0
 */

public class WhisperDeviceProfile extends RealmObject {

    public static final String FIELD_CREATE_TIME = "createTime";

    private String osVersion;
    // The user-visible security patch level
    private String securityPatch;
    private long createTime;

    private String reserveField1;
    private String reserveField2;
    private String reserveField3;

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

    public String getReserveField1() {
        return reserveField1;
    }

    public void setReserveField1(String reserveField1) {
        this.reserveField1 = reserveField1;
    }

    public String getReserveField2() {
        return reserveField2;
    }

    public void setReserveField2(String reserveField2) {
        this.reserveField2 = reserveField2;
    }

    public String getReserveField3() {
        return reserveField3;
    }

    public void setReserveField3(String reserveField3) {
        this.reserveField3 = reserveField3;
    }

    @Override
    public String toString() {
        return "WhisperDeviceProfile{" +
                "createTime=" + createTime +
                ", osVersion='" + osVersion + '\'' +
                ", securityPatch='" + securityPatch + '\'' +
                ", reserveField1='" + reserveField1 + '\'' +
                ", reserveField2='" + reserveField2 + '\'' +
                ", reserveField3='" + reserveField3 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof WhisperDeviceProfile)) return false;

        WhisperDeviceProfile that = (WhisperDeviceProfile) o;

        if (osVersion != null ? !osVersion.equals(that.osVersion) : that.osVersion != null)
            return false;
        if (securityPatch != null ? !securityPatch.equals(that.securityPatch) : that.securityPatch != null)
            return false;
        if (reserveField1 != null ? !reserveField1.equals(that.reserveField1) : that.reserveField1 != null)
            return false;
        if (reserveField2 != null ? !reserveField2.equals(that.reserveField2) : that.reserveField2 != null)
            return false;
        return reserveField3 != null ? reserveField3.equals(that.reserveField3) : that.reserveField3 == null;

    }

    @Override
    public int hashCode() {
        int result = osVersion != null ? osVersion.hashCode() : 0;
        result = 31 * result + (securityPatch != null ? securityPatch.hashCode() : 0);
        result = 31 * result + (reserveField1 != null ? reserveField1.hashCode() : 0);
        result = 31 * result + (reserveField2 != null ? reserveField2.hashCode() : 0);
        result = 31 * result + (reserveField3 != null ? reserveField3.hashCode() : 0);
        return result;
    }
}
