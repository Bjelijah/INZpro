package com.inz.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * @author huozhihao
 * 
 * 用于获取手机屏幕长度和宽度的工具类
 */

public class PhoneConfig {
	private static WindowManager wm;
	
	public static int getPhoneWidth(Context context){
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();//��Ļ���
		return width;
	}
	
	public static int getPhoneHeight(Context context){
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();//��Ļ���
		return height;
	}

	public static String getPhoneDeveceID(Context context){
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		Log.i("123", "deviceid = "+tm.getDeviceId());
		return tm.getDeviceId();
//		return TEST_DEVICE_ID;
	}

	public static long showUserSerialNum(Context context){
		UserManager um = (UserManager) context.getSystemService(Context.USER_SERVICE);
		if (um==null) {
			return -1;
		}
		UserHandle userHandle = Process.myUserHandle();
		long sn=  um.getSerialNumberForUser(userHandle);
		return sn;
	}

	public static String getPhoneUid(Context context){
		final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		UUID uuid = null;
		try {
			if (!"9774d56d682e549c".equals(androidId)) //在主流厂商生产的设备上，有一个很经常的bug，就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
			{
				uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
			}
			else
			{
				final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
				uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (uuid==null) {
			return null;
		}
		String id = uuid.toString();
		id = id.replace("-", "");
		id = id.replace(":", "");
		return id;
	}

	public static String getIMEI(Context context){
		return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	public static String getPhoneModel(){
		Log.i("123", "getPhoneModel model = "+android.os.Build.MODEL);
		return android.os.Build.MODEL;
	}

	public static String getOSVersion(){
		Log.i("123", "getOSVersion version = "+android.os.Build.VERSION.RELEASE);
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getPhoneManufactory(){
		Log.i("123", "getPhoneManufactory factory = "+android.os.Build.MANUFACTURER);
		return android.os.Build.MANUFACTURER;
	}

	public static String getPhoneOperator(Context context){
		TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String name = telMgr.getSimOperatorName();
		Log.i("123", "name="+name);
		return name;
	}

	private static String getAppVersion(Context context) throws PackageManager.NameNotFoundException {

		PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
		return pi.versionName;

	}

	public static boolean isNewVersion(Context context,String version){
		try {
			String thisAPPVersion = getAppVersion(context);
			Log.i("123","thisAppVersion="+thisAPPVersion+"    version="+version);
			String [] str = version.split("\\.");
			Log.e("123","len="+str.length);
			int [] vers = new int[str.length];
			for (int i=0;i<str.length;i++){
				Log.i("123","~~~~~~~~~~~~  str="+str[i]);
				vers[i] = Integer.valueOf(str[i]);
			}
			String [] thisStr = thisAPPVersion.split("\\.");
			int [] thisVers = new int[thisStr.length];
			for (int i=0;i<thisStr.length;i++){
				thisVers[i] = Integer.valueOf(thisStr[i]);
			}

			int len = Math.min(vers.length,thisVers.length);
			for (int i=0;i<len;i++){
				if (vers[i]>thisVers[i]){
					return true;
				}
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return false;
	}


}
