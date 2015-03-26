package com.location.finder;

import com.kl.kitlocate.interfaces.KLLocation;
import com.kl.kitlocate.interfaces.KitLocate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LocationMatcherService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		System.out.println("aman check start service ");

		// Init Kitlocate library
		KitLocate.initKitLocate(this.getApplicationContext(),
				"eb587ebb-38b0-40c7-8fab-3b9745a2a7ef",
				KitLocateCallbacks.class);

		// Start Periodic location update
		KLLocation.registerPeriodicLocation(this.getApplicationContext());
		KLLocation.setPeriodicMinimumTimeInterval(this.getApplicationContext(),
				30);

		// Stop Periodic location update
		// KLLocation.unregisterPeriodicLocation(MainActivity.this);

		// Starting geofence events
		// KLLocation.addGeofence(this, 40.758928f, -73.98512f, 200,
		// KLGeofence.Type.OUT, "TIMES SQUARE");
		// KLLocation.registerGeofencing(this);

		// Stop Geofence location update
		// KLLocation.unregisterGeofencing(MainActivity.this);
		// KLLocation.deleteAllGeofences(MainActivity.this);

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
