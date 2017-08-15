package com.smb.smbutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.smb.smbutils.enums.NetState;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;

/**
 * 获取手机相关信息
 * Created by mtf on 2017/6/7.
 * update by heliquan 将MobileInfoUtil更名为SystemUtil 此类包含获取手机系统相关统一管理工具类 包含：获取IMEI，IMSI，MAC，验证设备当前网络情况等
 */
public class SystemUtil {

    /**
     * 获取版本信息
     * add by heliquan at 2017年7月18日
     */
    public static String getVersionName(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            return versionName;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取版本code
     * add by heliquan at 2017年7月18日
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        try {
            String pkName = context.getPackageName();
            String versionCode = String.valueOf(context.getPackageManager().getPackageInfo(
                    pkName, 0).versionCode);

            return versionCode;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 根据用户当前网络状态返回相应Code码
     * add by heliquan at 2017年6月12日
     *
     * @param context
     * @return
     */
    public static int getNetWorkState(Context context) {
        String netWorkState = SystemUtil.getNetworkCategory(context);
        if ("WIFI网络".equals(netWorkState)) {
            return 1;
        }
        if ("2g网络".equals(netWorkState) || "3g网络".equals(netWorkState) || "4g网络".equals(netWorkState)) {
            return 2;
        }
        if ("没有网络连接".equals(netWorkState) || "未知网络".equals(netWorkState) || "异常情况".equals(netWorkState)) {
            return -1;
        }
        return 0;
    }

    /**
     * 根据不同网络状态提示不同消息
     * add by heliquan at 2017年6月12日
     *
     * @param context
     * @return
     */
    public static String getNetWorkMessage(Context context) {
        String netWorkState = SystemUtil.getNetworkCategory(context);
        if ("WIFI网络".equals(netWorkState)) {
            return "\n\n当前处于Wifi环境下";
        }
        if ("2g网络".equals(netWorkState) || "3g网络".equals(netWorkState) || "4g网络".equals(netWorkState)) {
            return "\n\n当前处于移动网络环境下";
        }
        if ("没有网络连接".equals(netWorkState) || "未知网络".equals(netWorkState) || "异常情况".equals(netWorkState)) {
            return "\n\n当前处于无网络的二次元环境下";
        }
        return null;
    }

    /**
     * 获取网络类型
     * add by heliquan at 2017年6月12日
     *
     * @return
     */
    private static String getNetworkCategory(Context context) {
        String category = null;
        NetState state = isConnected(context);
        switch (state) {
            case NET_NO:
                category = "没有网络连接";
                break;
            case NET_2G:
                category = "2g网络";
                break;
            case NET_3G:
                category = "3g网络";
                break;
            case NET_4G:
                category = "4g网络";
                break;
            case NET_WIFI:
                category = "WIFI网络";
                break;
            case NET_UNKNOWN:
                category = "未知网络";
                break;
            default:
                category = "异常情况";
        }
        return category;
    }

    /**
     * 判断当前是否网络连接,以及连接的网络的类型
     * add by heliquan at 2017年6月12日
     *
     * @param context
     * @return 状态码
     */
    private static NetState isConnected(Context context) {
        NetState stateCode = NetState.NET_NO;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            switch (ni.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    stateCode = NetState.NET_WIFI;
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (ni.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            stateCode = NetState.NET_2G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            stateCode = NetState.NET_3G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            stateCode = NetState.NET_4G;
                            break;
                        default:
                            stateCode = NetState.NET_UNKNOWN;
                    }
                    break;
                default:
                    stateCode = NetState.NET_UNKNOWN;
            }
        }
        return stateCode;
    }

    /**
     * 获取手机IMEI
     * 移动设备唯一标识码
     *
     * @param context
     * @return
     */
    public static String getMobileIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机IMSI
     * 手机卡唯一标识码
     *
     * @param context
     * @return
     */
    public static String getMobileIMSI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = telephonyManager.getSubscriberId();
            if (imsi == null) {
                imsi = "";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 兼容Android 6.0以上设备获取MAC地址
     * 物理地址、硬件地址，用来定义网络设备的位置
     * 兼容原因：从android 6.0之后，android 移除了通过 WiFi 和蓝牙 API 来在应用程序中可编程的访问本地硬件标示符。现在 WifiInfo.getMacAddress() 和 BluetoothAdapter.getAddress() 方法都将返回 02:00:00:00:00:00
     * add by heliquan at 2017年6月12日
     *
     * @return
     */
    public static String getMobileMAC(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 如果当前设备系统大于等于6.0 使用下面的方法
            return getHeightMac();
        } else {
            return getLowerMac(context);
        }
    }

    /**
     * 兼容Android 6.0以下设备获取MAC地址 Android API < 23
     *
     * @return
     */
    private static String getLowerMac(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            // 获取MAC地址
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String mac = wifiInfo.getMacAddress();
            if (null == mac) {
                // 未获取到
                mac = "";
            }
            return mac;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取高版本手机的MAC地址 Android API >= 23
     *
     * @return
     */
    private static String getHeightMac() {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return macSerial;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    /**
     * 判断存储卡是否存在
     *
     * @return
     */
    public static boolean existSDcard() {
        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
            return true;
        } else
            return false;
    }
}
