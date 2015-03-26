package com.location.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.app.WallpaperManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.telephony.gsm.SmsManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.kl.kitlocate.class_interface.KLLocationValue;
import com.kl.kitlocate.notification.KLLocalNotification;
import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;
import com.mobirizer.autotasker.FirstActivity;
import com.mobirizer.autotasker.R;
import com.mobirizer.autotasker.SavePreferences;
import com.mobirizer.utility.UtilityHandler;

public class KitLocateCallbacks {
	public static double lat1,lng1,ddd;
	public static long  maindbid,currentdbidthree,currentdbidsix,tasktype_three_id,tasktype_six_id;
	public static String placename = "", placemsg = "", tempstr = "",defaultapplist="";
	private static SharedPreferences sharedPreferences;
	private static SavePreferences savePreferences;
	public static Bitmap bitmap;
	private final static String packagefrommydb="packagefrommydb";
	private final static String packagenameforoutsidearea="getapplistforsetting";
	private static List<DbLocationObject> value = new ArrayList<DbLocationObject>();
	public static void gotPeriodicLocation(Context context,KLLocationValue lvLocation){
		savePreferences = new SavePreferences();
		DataSourceHandler db = DataSourceHandler.getInstance(context);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		defaultapplist = sharedPreferences.getString(packagenameforoutsidearea,"");
		currentdbidthree = sharedPreferences.getLong("currentdbidthree", 0l);
		currentdbidsix = sharedPreferences.getLong("currentdbidsix", 0l);
		if (lvLocation.isLocationFound()){
		// Location found
		// Get Location basic parameters
		float fLatitude = lvLocation.getLatitude();
		float fLongitude = lvLocation.getLongitude();
		try {
			tempstr = sharedPreferences.getString("tempadd", "");
//			for lock
			if(value.size()==0){
				if(defaultapplist.length()>2){
					savePreferences.SavePreferences(context, packagefrommydb, defaultapplist);	
				}else{
				savePreferences.SavePreferences(context, packagefrommydb, "");	
				}
				}
			value = db.getAllFreqLocationDataObject();
			for (DbLocationObject valueName : value) {
				try {
					String latitude = valueName.getLocationLat().toString();
					String logitude = valueName.getLocationLong().toString();
					int radiusdata = valueName.getMapradius();
					String repeatvalue = valueName.getMessage();
					int tasktype = valueName.getTasktype();
					placename = valueName.getLocatonType().toString();
					placemsg = valueName.getRepeate();
					maindbid=valueName.getId();
					lat1 = Double.parseDouble(latitude);
					lng1 = Double.parseDouble(logitude);
					ddd = UtilityHandler.distFrom(lat1, lng1, fLatitude,fLongitude);
				// Check result
				if (ddd > 0 && ddd < (radiusdata)) {
						if(tasktype==0){
							if(repeatvalue.equalsIgnoreCase("1")){
						setBrightness(context, placemsg);	
							}
						}
						else if(tasktype==1){
							if(repeatvalue.equalsIgnoreCase("1")){
							doBluetooth(context, placemsg);
							}
						}
						else if(tasktype==2){
							if(repeatvalue.equalsIgnoreCase("1")){
							setSoundlevel(context, placemsg);
							}
						}
						else if(tasktype==3){
							if(repeatvalue.equalsIgnoreCase("1")){
							tasktype_three_id=sharedPreferences.getLong("update_idthree", 0l);
							if(tasktype_three_id!=maindbid && currentdbidthree!=maindbid){
							String phoneno = placemsg;
							placemsg = phoneno.substring(0, phoneno.indexOf("$"));
							phoneno = phoneno.substring(phoneno.indexOf("$") + 1);
							placemsg = placemsg.substring(0, placemsg.indexOf("*"));
							sendSms(context, placemsg, phoneno);
							}
							}
						}
						else if(tasktype==4){
							if(repeatvalue.equalsIgnoreCase("1")){
							doWiFi(context,placemsg);
							}
						}
						else if(tasktype==5){
							System.out.println("5555555555task type 5 at service*****>>>"+repeatvalue+placemsg);
							if(repeatvalue.equalsIgnoreCase("1")){
								System.out.println("5555555555task type 5 at service>>>"+repeatvalue+placemsg);
							savePreferences.SavePreferences(context, packagefrommydb, placemsg);
							}
						}
						else if(tasktype==6){
							if(repeatvalue.equalsIgnoreCase("1")){
							tasktype_six_id=sharedPreferences.getLong("update_idsix",0l);
							if(tasktype_six_id!=maindbid && currentdbidsix!=maindbid){
							new KLLocalNotification(context,"Message :"+placemsg+"\n"+"Place: "+placename,R.drawable.icon).setDefaultSound(false).setVibrate(true).setNotificationID(102).setTickerText("Auto Tasker")
							.setContentTitle("Auto Tasker").setContentIntent(new Intent(context, FirstActivity.class))
							.send();
							savePreferences.SavePreferences(context,"update_idsix", maindbid);
							}
							}
						}
						else if(tasktype==7){
							if(repeatvalue.equalsIgnoreCase("1")){
							String Bmp=placemsg;
							try {
								 final BitmapFactory.Options options = new BitmapFactory.Options();
									options.inTempStorage = new byte[16*1024];
									options.inSampleSize = 8;
								 bitmap = BitmapFactory.decodeFile(Bmp,options);
							   } catch(Exception e) {
							      e.getMessage();
							   }
							setWallapepr(context,bitmap);
						}
						}
				} else {
//					for lock app
					savePreferences.SavePreferences(context, packagefrommydb, defaultapplist);
					savePreferences.SavePreferences(context, "update_idthree", 0l);
					savePreferences.SavePreferences(context, "update_idsix", 0l);
					savePreferences.SavePreferences(context,"currentdbidthree",0l);
					savePreferences.SavePreferences(context,"currentdbidsix",0l);
				}
				} catch (Exception e) {
					e.printStackTrace();
					e.getLocalizedMessage();
					System.out.println("..................e..."+e);
				}
			}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getLocalizedMessage();
		}
		 }
		else{
			//for lock app
//			System.out.println("55555555555555555555555555");
			savePreferences.SavePreferences(context, packagefrommydb, defaultapplist);	
			savePreferences.SavePreferences(context, "update_idthree", 0l);
			savePreferences.SavePreferences(context, "update_idsix", 0l);
			new KLLocalNotification(context,"Please turn on location service to enable Auto Tasker.",R.drawable.notification_icon).setDefaultSound(false).setVibrate(false).setNotificationID(105)
			.setContentTitle("Auto Tasker")
			.send();
		}
	}
	@SuppressWarnings("deprecation")
	private static void sendSms(Context context, String sms, String phoneNO) {
		try {
			SmsManager smsManager = SmsManager.getDefault();
			ArrayList<String> parts = smsManager.divideMessage(sms);
			smsManager.sendMultipartTextMessage(phoneNO, null, parts, null,	null);
			savePreferences.SavePreferences(context,"update_idthree", maindbid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static  void setWallapepr(Context contxt,Bitmap bitmap) {
		DisplayMetrics metrics = new DisplayMetrics(); 
        WindowManager windowManager = (WindowManager) contxt.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels; 
        int width = metrics.widthPixels;
        bitmap = Bitmap.createScaledBitmap(bitmap,width,height, true);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(contxt); 
        wallpaperManager.setWallpaperOffsetSteps(1, 1);
        wallpaperManager.suggestDesiredDimensions(width, height);
        try {
          wallpaperManager.setBitmap(bitmap);
          } catch (IOException e) {
          e.printStackTrace();
        }
	}
	public static void doWiFi(Context context,String wifistate ){
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE); 
		if(wifistate.equalsIgnoreCase("on") && !wifiManager.isWifiEnabled()){
		
		wifiManager.setWifiEnabled(true);
		}
		if(wifistate.equalsIgnoreCase("off") && wifiManager.isWifiEnabled())
		{
			wifiManager.setWifiEnabled(false);
		}
	}
	public static void doBluetooth(Context context,String bluetooth ){
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
		if(bluetooth.equalsIgnoreCase("on") && !mBluetoothAdapter.isEnabled()){
			mBluetoothAdapter.enable();
		}
		if(bluetooth.equalsIgnoreCase("off") && mBluetoothAdapter.isEnabled())
		{
			 mBluetoothAdapter.disable(); 
		}
	}
	public static void setBrightness(Context context,String brightness ){
		float brightnessvalue=Float.parseFloat(brightness);
		android.provider.Settings.System.putInt(context.getContentResolver(),
			      android.provider.Settings.System.SCREEN_BRIGHTNESS,
			      (int) brightnessvalue);
	}
	public static void setSoundlevel(Context context,String sound ){
		int soundlevel=Integer.parseInt(sound);
		AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, soundlevel, 0);
	}
	
}
