package com.location.finder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.foruriti.geosms.SetDataValueForReminder;
import com.kl.kitlocate.interfaces.KLLocation;
import com.kl.kitlocate.interfaces.KitLocate;

public class LocationMatcherService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		System.out.println("aman check start service ");
		// Init Kitlocate library
		KitLocate.initKitLocate(this, "34bfbbab-00ae-4adb-84f2-62d7cb4f22e4",
				KitLocateCallbacks.class);
		System.out.println("value at service "
				+ SetDataValueForReminder.accuracylevel + ".."
				+ SetDataValueForReminder.soundstatus + ".."
				+ SetDataValueForReminder.vibratestatus);
		// Start Periodic location update
		KLLocation.registerPeriodicLocation(this.getApplicationContext());
		KLLocation.setPeriodicMinimumTimeInterval(this.getApplicationContext(),
				30);

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
