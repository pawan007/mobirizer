package com.location.finder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.gsm.SmsManager;

import com.foruriti.geosms.SavePreferences;
import com.foruriti.utility.UtilityHandler;
import com.kl.kitlocate.class_interface.KLLocationValue;
import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;

public class KitLocateCallbacks {
	public static double lat1;
	public static double lng1;
	public static double ddd;
	public static String placename = "", placemsg = "", tempstr = "",
			currentadd_atinsert = "", atinsert_lat = "";
	private static SharedPreferences sharedPreferences;
	private static SavePreferences savePreferences;
	private static int sound, vibrate;
	private static boolean soundon, vibrateon;
	static List<DbLocationObject> value = new ArrayList<DbLocationObject>();

	public static void gotPeriodicLocation(Context context,
			KLLocationValue lvLocation) {
		System.out.println("aman check kit locate location callback");
		savePreferences = new SavePreferences();
		DataSourceHandler db = DataSourceHandler.getInstance(context);
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		// Location found
		// Get Location basic parameters
		float fLatitude = lvLocation.getLatitude();
		float fLongitude = lvLocation.getLongitude();
		float fAccuracy = lvLocation.getAccuracyInMeters(context, true);
		Date dateReceived = lvLocation.getReceivedTime();
		try {
			sound = sharedPreferences.getInt("sound_level", 0);
			vibrate = sharedPreferences.getInt("vibrate_level", 0);
			tempstr = sharedPreferences.getString("tempadd", placename);
			currentadd_atinsert = sharedPreferences.getString(
					"currentadd_atinsert", "");
			atinsert_lat = sharedPreferences.getString(
					"lat_at_currentadd_atinsert", "");
			checksoundStatus(sound);
			checkvibrateStatus(vibrate);
			value = db.getAllFreqLocationDataObject();
			for (DbLocationObject valueName : value) {
				try {
					String latitude = valueName.getLocationLat().toString();
					String logitude = valueName.getLocationLong().toString();
					int radiusdata = valueName.getMapradius();
					String repeatvalue = valueName.getMessage();
					placename = valueName.getLocatonType().toString();
					placemsg = valueName.getRepeate();
					lat1 = Double.parseDouble(latitude);
					lng1 = Double.parseDouble(logitude);
					ddd = UtilityHandler.distFrom(lat1, lng1, fLatitude,
							fLongitude);
				 System.out.println("1>>>>>>>>>>>>>>>>at kitlocate.."+repeatvalue+"...notification counter"+placemsg);
				// Check result
				if (ddd > 0 && ddd < (radiusdata)) {
					if(repeatvalue.equalsIgnoreCase("1")){
					if (!placename.equalsIgnoreCase(tempstr)
							&& !currentadd_atinsert.equalsIgnoreCase(placename)) {
						String phoneno = placemsg;
						placemsg = phoneno.substring(0, phoneno.indexOf("$"));
						phoneno = phoneno.substring(phoneno.indexOf("$") + 1);
						placemsg = placemsg.substring(0, placemsg.indexOf("*"));
						savePreferences.SavePreferences(context, "tempadd",
								placename);
						savePreferences.SavePreferences(context,
								"currentadd_atinsert", placename);
						// System.out.println("phone no is"+phoneno);
						// fireNotification(context);
						// System.out.println("phone no at kitlocate"+phoneno);

						sendSms(context, placemsg, phoneno);

					}}
				} else {
					// new
					// KLLocalNotification(context,placename,R.drawable.ic_launcher).setDefaultSound(true).setNotificationID(102).setTickerText("Ticker text")
					// .setContentTitle("Driving State Changed")
					// .send();
					// Toast.makeText(context,
					// "distance more than 200 :=" + ddd,
					// Toast.LENGTH_LONG).show();
					// savePreferences.SavePreferences(context,
					// "lat_at_currentadd_atinsert","0");
				}
				} catch (Exception e) {
					e.printStackTrace();
					e.getLocalizedMessage();
					System.out.println("..................e..."+e);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			e.getLocalizedMessage();
		}

		// }
	}

	private static void checkvibrateStatus(int vibratevalue) {
		// TODO Auto-generated method stub
		if (vibratevalue == 1) {
			vibrateon = true;
		} else if (vibratevalue == 2) {
			vibrateon = false;
		} else {
			vibrateon = true;
		}
		// System.out.println("at check vibrate value>>"+vibrateon);
	}

	private static void checksoundStatus(int soundvalue) {
		// TODO Auto-generated method stub
		if (soundvalue == 1) {
			soundon = true;
		} else if (soundvalue == 2) {
			soundon = false;
		} else {
			soundon = true;
		}
		// System.out.println("at check sound value>>"+soundon);
	}

	private static void sendSms(Context context, String sms, String phoneNO) {
		System.out.println("aman check sms content : " + sms + " , num : "
				+ phoneNO);

		try {
			@SuppressWarnings("deprecation")
			SmsManager smsManager = SmsManager.getDefault();
			ArrayList<String> parts = smsManager.divideMessage(sms);
			smsManager.sendMultipartTextMessage(phoneNO, null, parts, null,
					null);
			System.out.println("messsgae sent");
			// Toast.makeText(context, "Message Sent!",
			// Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			// Toast.makeText(context,
			// "SMS faild, please try again later!", Toast.LENGTH_LONG)
			// .show();
			e.printStackTrace();
			System.out.println("eeeeeeeeeeeeeeeeeeeeeee" + e);
		}
	}

}
