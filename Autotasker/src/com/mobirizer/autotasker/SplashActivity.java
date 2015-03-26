package com.mobirizer.autotasker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {
	private Handler handler;
	private Runnable runnable = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splashactivity);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		handler = new Handler();
		runnable = new ActivityRunnable();
		
	}
	private class ActivityRunnable implements Runnable
	{
		@Override
		public void run() 
		{
			Intent intent = new Intent(SplashActivity.this,SecurityQuestionAnswerActivity.class);
			startActivity(intent);
			finish();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		handler.removeCallbacks(runnable);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		handler.postDelayed(runnable , 2000);
		
	}
	
}
