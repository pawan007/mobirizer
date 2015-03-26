package com.mobirizer.hollywoodemoji;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class SplashActivity extends Activity {
	
	  private static final String AD_UNIT_ID = "ca-app-pub-1765898360821109/7104525475";

	  private InterstitialAd interstitial;
	  private Timer waitTimer;
	  private boolean interstitialCanceled = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_splash);
		System.out.println("INSIDE ONCREATE SPLASH ACTIVITY");
		interstitial = new InterstitialAd(this);
	    interstitial.setAdUnitId(AD_UNIT_ID);
	    
	 // Create ad request.
	    AdRequest adRequest = new AdRequest.Builder().build();
	    // Begin loading your interstitial.
	    interstitial.loadAd(adRequest);
	    interstitial.setAdListener(new AdListener() {
	      @Override
	      public void onAdLoaded() {
	        if (!interstitialCanceled) {
	          waitTimer.cancel();
	          interstitial.show();
	        }
	      }
	      
	      @Override
	      public void onAdFailedToLoad(int errorCode) {
	        startMainActivity();
	      }
	    });
	    interstitial.loadAd(new AdRequest.Builder().build());
	      
	int sec=2;
	 waitTimer = new Timer();
	 waitTimer.schedule(new TimerTask() {
		 @Override
	      public void run() {
		 interstitialCanceled = true;
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
	    waitTimer.cancel();
	    interstitialCanceled = true;
	    super.onPause();
	  }

	  @Override
	  public void onResume() {
	    super.onResume();
	    if (interstitial.isLoaded()) {
	      interstitial.show();
	    } else if (interstitialCanceled) {
	      startMainActivity();
	    }
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
