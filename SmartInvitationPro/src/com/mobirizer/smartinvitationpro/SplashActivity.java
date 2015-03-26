package com.mobirizer.smartinvitationpro;



import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

	  private Timer waitTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_main);
		System.out.println("INSIDE ONCREATE SPLASH ACTIVITY");
		
	int sec=2;
	 waitTimer = new Timer();
	 waitTimer.schedule(new TimerTask() {
		 @Override
	      public void run() {
	        SplashActivity.this.runOnUiThread(new Runnable() {
	          @Override
	          public void run() {
	            startMainActivity();
	          }
	        });
	      }
	}, sec*1000);
	}
	
	@Override
	  public void onPause() {
	    super.onPause();
	  }

	  @Override
	  public void onResume() {
	    super.onResume();
	 }

	  /**
	   * Starts the application's {@link MainActivity}.
	   */
	  private void startMainActivity() {
	    Intent intent = new Intent(this, MainActivity.class);
	    startActivity(intent);
	    finish();
	  }
}


