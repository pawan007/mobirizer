package com.location.finder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Launching location matcher service to keep checking location updates
		// from background
		Intent locationMatcherService = new Intent(context,
				LocationMatcherService.class);
		context.startService(locationMatcherService);
	}
}
