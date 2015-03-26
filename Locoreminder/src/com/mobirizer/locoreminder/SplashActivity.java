package com.mobirizer.locoreminder;

import gps.GPSTracker;

import java.util.Timer;
import java.util.TimerTask;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splashactivity);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
			int sec = 2;
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {				
					Intent intent = new Intent(SplashActivity.this,FirstActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}
			}, sec * 1000);	
		
	}

}
