package io.buzznerd.varys.whisper.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created on 16/10/1
 *
 * @author Xingye
 * @since 1.0.0
 */

public final class DeviceUtils {

    private DeviceUtils() {

    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        String marshmallowMacAddress = "02:00:00:00:00:00";

        String macAddress = "";
        if (TextUtils.isEmpty(interfaceName)) {
            macAddress = marshmallowMacAddress;
        }

        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                if (interfaceName != null) {
                    if (!networkInterface.getName().equalsIgnoreCase(interfaceName)) {
                        continue;
                    }
                }
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    StringBuilder buf = new StringBuilder();
                    for (int idx = 0; idx < mac.length; idx++) {
                        buf.append(String.format("%02x:", mac[idx]));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    macAddress = buf.toString();
                    return macAddress;
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return macAddress;
    }

    public static String createDeviceMark(String macAddress) {
        int time = (int) (System.currentTimeMillis() / 1000);
        int random = new Random().nextInt();
        int hash = macAddress.hashCode();

        StringBuilder buffer = new StringBuilder();
        buffer.append(byteArrayToString(intToByteArray(time)));
        buffer.append(":");
        buffer.append(byteArrayToString(intToByteArray(random)));
        buffer.append(":");
        buffer.append(byteArrayToString(intToByteArray(hash)));

        return buffer.toString();
    }

    private static byte[] intToByteArray(int s) {
        byte[] targets = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    private static String byteArrayToString(byte[] byteArray) {
        StringBuilder buf = new StringBuilder();
        for (int idx = 0; idx < byteArray.length; idx++) {
            buf.append(String.format("%02X", byteArray[idx]));
        }
        return buf.toString();
    }

    /**
     * get net status<br>
     * see <a href="http://stackoverflow.com/questions/2802472/detect-network-connection-type-on-android">http://stackoverflow.com/questions/2802472/detect-network-connection-type-on-android</a>
     *
     * @param context
     * @return WIFI, 2G, 3G, 4G, disconnected, unknown
     */
    public static String getNetStatus(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();

        if (info == null || !info.isConnected()) {
            return "disconnected";
        }

        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return "WIFI";
        }

        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case 16: // TelephonyManager.NETWORK_TYPE_GSM:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                case 17: // TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                case 18: // TelephonyManager.NETWORK_TYPE_IWLAN:
                    return "4G";
                default:
                    return "unknown";
            }
        }

        return "unknown";
    }
}
