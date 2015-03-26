package com.location.finder;

import android.content.Context;
import android.widget.Toast;

import com.kl.kitlocate.class_interface.KLLocationValue;
import com.kl.kitlocate.notification.KLLocalNotification;
import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;
import com.mobirizer.locoreminder.R;
import com.mobirizer.utility.UtilityHandler;

import java.util.Date;
import java.util.List;

public class KitLocateCallbacks {

	public static void gotPeriodicLocation(Context context,
			KLLocationValue lvLocation) {

		System.out.println("aman check kit locate location callback");

		if (lvLocation.isLocationFound()) {
			// Location found
			// Get Location basic parameters
			float fLatitude = lvLocation.getLatitude();
			float fLongitude = lvLocation.getLongitude();
			float fAccuracy = lvLocation.getAccuracyInMeters(context, true);
			Date dateReceived = lvLocation.getReceivedTime();

			try {
				DataSourceHandler db = DataSourceHandler.getInstance(context);
				List<DbLocationObject> value = db
						.getAllFreqLocationDataObject();

				for (DbLocationObject valueName : value) {
					String latitude = valueName.getLocationLat().toString();
					String logitude = valueName.getLocationLong().toString();
					String placename = valueName.getLocatonType().toString();
					double lat1 = Double.parseDouble(latitude);
					double lng1 = Double.parseDouble(logitude);
					double ddd = UtilityHandler.distFrom(lat1, lng1, fLatitude,
							fLongitude);

					// Check result
					if (ddd > 0 && ddd < 300) {
						Toast.makeText(context, "Distance less Than 200" + ddd,
								Toast.LENGTH_LONG).show();

						new KLLocalNotification(context,
								"Driving state changed: ",
								R.drawable.ic_launcher).setDefaultSound(true)
								.setNotificationID(102)
								.setTickerText("Ticker text")
								.setContentTitle("Driving State Changed")
								.send();

						// displayNotificationOne(placename);
						Toast.makeText(context, "Notification Send",
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(context,
								"distance more than 200 :=" + ddd,
								Toast.LENGTH_LONG).show();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				e.getLocalizedMessage();
			}

		} else {
			// No Location found

		}
	}

	// private void displayNotificationOne(String place) {
	// // Invoking the default notification service
	// NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
	// this);
	//
	// mBuilder.setContentTitle(place);
	// mBuilder.setContentText("You around 1 km");
	// mBuilder.setTicker("Explicit: New Message Received!");
	// mBuilder.setSmallIcon(R.drawable.ic_launcher);
	//
	// // Increase notification number every time a new notification arrives
	// mBuilder.setNumber(++numMessagesOne);
	//
	// // Creates an explicit intent for an Activity in your app
	// Intent resultIntent = new Intent(this, FirstActivity.class);
	// resultIntent.putExtra("notificationId", notificationIdOne);
	//
	// // This ensures that navigating backward from the Activity leads out of
	// // the app to Home page
	// TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	// // Adds the back stack for the Intent
	// stackBuilder.addParentStack(FirstActivity.class);
	//
	// // Adds the Intent that starts the Activity to the top of the stack
	// stackBuilder.addNextIntent(resultIntent);
	// PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
	// PendingIntent.FLAG_ONE_SHOT // can only be used once
	// );
	// // start the activity when the user clicks the notification text
	// mBuilder.setContentIntent(resultPendingIntent);
	//
	// myNotificationManager = (NotificationManager)
	// getSystemService(Context.NOTIFICATION_SERVICE);
	//
	// // pass the Notification object to the system
	// myNotificationManager.notify(notificationIdOne, mBuilder.build());
	//
	// }

	// public static void geofenceIn(Context context,
	// ArrayList<KLGeofence> alGeofences) {
	// // Received geofence In
	// // new KLLocalNotification(context,
	// // getMessageFromArrayOfGeofences(alGeofences), R.drawable.point)
	// // .setDefaultSound(true).setNotificationID(101)
	// // .setContentTitle("GEOFENCE IN").send();
	// }
	//
	// public static void geofenceOut(Context context,
	// ArrayList<KLGeofence> alGeofences) {
	// // Received geofence Out
	// // new KLLocalNotification(context,
	// // getMessageFromArrayOfGeofences(alGeofences), R.drawable.point)
	// // .setDefaultSound(true).setNotificationID(102)
	// // .setContentTitle("GEOFENCE OUT").send();
	// }
	//
	// private static String getMessageFromArrayOfGeofences(
	// ArrayList<KLGeofence> alGeofences) {
	// String strMessage = "";
	// if (alGeofences != null && alGeofences.size() > 0) {
	// strMessage += "{" + String.valueOf(alGeofences.size()) + "}";
	// for (Object obj : alGeofences) {
	// KLGeofence geo = (KLGeofence) obj;
	// strMessage += "(";
	// boolean bSaw = false;
	// if (!geo.getIDServer().equalsIgnoreCase("")) {
	// strMessage += "S[" + geo.getIDServer() + "]";
	// bSaw = true;
	// }
	// if (!geo.getIDUser().equalsIgnoreCase("")) {
	// strMessage += "U[" + geo.getIDUser() + "]";
	// bSaw = true;
	// }
	// if (!bSaw) {
	// strMessage += "I[" + String.valueOf(geo.getIDPrimary())
	// + "]";
	// }
	// strMessage += ")";
	// }
	// } else {
	// strMessage += " Problem";
	// }
	// return strMessage;
	// }
	//
	// public static void onMotionStateChanged(Context context,
	// KLMotionDetection.MotionState state) {
	// // new KLLocalNotification(context, "Driving state changed: "
	// // + state.toString(), R.drawable.point).setDefaultSound(true)
	// // .setNotificationID(102)
	// // .setContentTitle("Driving State Changed").send();
	//
	// switch (state) {
	// case ACTIVE:
	// // DRIVING
	// break;
	// case NOT_ACTIVE:
	// // STATIONARY
	// break;
	// default:
	// break;
	// }
	// }
}
