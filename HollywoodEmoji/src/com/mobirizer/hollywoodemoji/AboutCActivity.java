package com.mobirizer.hollywoodemoji;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


public class AboutCActivity extends Activity {
	private MediaPlayer mp;
	/** Your ad unit id*/
	  private static final String AD_UNIT_ID = "";
	  /** The view to show the ad. */
	  private AdView adView;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.activity_about);
		
		// Create an ad.
	    adView = new AdView(this);
	    adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);

	    // Set the AdListener.
	    adView.setAdListener(new AdListener() {
	      /** Called when an ad is clicked and about to return to the application. */
	      @Override
	      public void onAdClosed() {
	       // Log.d(LOG_TAG, "onAdClosed");
	       // Toast.makeText(MainActivity.this, "onAdClosed", Toast.LENGTH_SHORT).show();
	      }

	      /** Called when an ad failed to load. */
	      @Override
	      public void onAdFailedToLoad(int error) {
	        String message = "onAdFailedToLoad: " + getErrorReason(error);
	       // Log.d(LOG_TAG, message);
	       // Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
	      }

	      /**
	       * Called when an ad is clicked and going to start a new Activity that will
	       * leave the application (e.g. breaking out to the Browser or Maps
	       * application).
	       */
	      @Override
	      public void onAdLeftApplication() {
	       // Log.d(LOG_TAG, "onAdLeftApplication");
	       // Toast.makeText(MainActivity.this, "onAdLeftApplication", Toast.LENGTH_SHORT).show();
	      }

	      /**
	       * Called when an Activity is created in front of the app (e.g. an
	       * interstitial is shown, or an ad is clicked and launches a new Activity).
	       */
	      @Override
	      public void onAdOpened() {
	     //   Log.d(LOG_TAG, "onAdOpened");
	        //Toast.makeText(MainActivity.this, "onAdOpened", Toast.LENGTH_SHORT).show();
	      }

	      /** Called when an ad is loaded. */
	      @Override
	      public void onAdLoaded() {
	        //Log.d(LOG_TAG, "onAdLoaded");
	        //Toast.makeText(MainActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
	      }
	    });    
	    // Add the AdView to the view hierarchy.
	    RelativeLayout layoutAd = (RelativeLayout) findViewById(R.id.layoutAd2);
	    layoutAd.addView(adView);

	 // Create an ad request. Check logcat output for the hashed device ID to
	    // get test ads on a physical device.
	    AdRequest adRequest = new AdRequest.Builder().build();
	     

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);

	}

	public void showCompany(View v) {
		AboutAll.isWhat = 0;
		initSound();
		Intent company = new Intent(this, AboutAll.class);
		startActivity(company);
		// Toast.makeText(this, "company", Toast.LENGTH_SHORT).show();
	}

	public void showProduct(View v) {
		AboutAll.isWhat = 1;
		initSound();
		Intent product = new Intent(this, AboutAll.class);
		startActivity(product);
		// Toast.makeText(this, "product", Toast.LENGTH_SHORT).show();
	}

	public void openPlayStore(View v) {
		initSound();
		Intent PSI = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("https://play.google.com/store/apps/developer?id=Mobirizer"));
		startActivity(PSI);

	}

	private void initSound() {
		// TODO Auto-generated method stub

		int resId = com.mobirizer.hollywoodemoji.R.raw.all_buttons;
		// Release any resources from previous MediaPlayer
		if (mp != null) {
			mp.release();
		}
		// Create a new MediaPlayer to play this sound
		mp = MediaPlayer.create(this, resId);
		mp.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (null != mp) {
			mp.release();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent mainInt = new Intent(AboutCActivity.this, MainActivity.class);
		startActivity(mainInt);
	}
	
	 @Override
	  public void onResume() {
	    super.onResume();
	    if (adView != null) {
	      adView.resume();
	    }
	  }

	  @Override
	  public void onPause() {
	    if (adView != null) {
	      adView.pause();
	    }
	    super.onPause();
	  }
	 
	  /** Gets a string error reason from an error code. */
	  private String getErrorReason(int errorCode) {
	    String errorReason = "";
	    switch(errorCode) {
	      case AdRequest.ERROR_CODE_INTERNAL_ERROR:
	        errorReason = "Internal error";
	        break;
	      case AdRequest.ERROR_CODE_INVALID_REQUEST:
	        errorReason = "Invalid request";
	        break;
	      case AdRequest.ERROR_CODE_NETWORK_ERROR:
	        errorReason = "Network Error";
	        break;
	      case AdRequest.ERROR_CODE_NO_FILL:
	        errorReason = "No fill";
	        break;
	    }
	    return errorReason;
 }
}
