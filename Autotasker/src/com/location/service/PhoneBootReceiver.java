package com.location.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Launching location matcher service to keep checking location updates
		// from background
		System.out.println("at phonebootreceiver");
		Intent locationMatcherService = new Intent(context,
				LocationMatcherService.class);
		context.startService(locationMatcherService);
	}
}
