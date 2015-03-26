package com.foruriti.funnyxrayscanner;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageShow extends Activity {
	
	private static final String IMAGEID="imageid";
	private static final String ID="id";
	
	/** Your ad unit id*/
	  private static final String AD_UNIT_ID = "ca-app-pub-4272695453752252/9692671529";
	  /** The view to show the ad. */
	  private AdView adView;
	SharedPreferences sharep;
	Editor edt;
	private ImageView mScanner;
	private Animation mAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.show_image);
		
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
	    RelativeLayout layoutAd = (RelativeLayout) findViewById(R.id.layoutAd3);
	    layoutAd.addView(adView);

	 // Create an ad request. Check logcat output for the hashed device ID to	    // get test ads on a physical device.

	    AdRequest adRequest = new AdRequest.Builder().build();

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);

		 sharep = getApplicationContext().getSharedPreferences(IMAGEID, MODE_PRIVATE);
		 edt=sharep.edit();
		 
			mScanner = (ImageView) findViewById(R.id.Scanner);
			
			switch(sharep.getInt(ID, 20)) {
			case 0:
				  mScanner.setBackgroundDrawable(getResources().getDrawable(R.drawable.couple_full));
				  StartLine();
				break;
			case 1:
				  mScanner.setBackgroundDrawable(getResources().getDrawable(R.drawable.danger_full));
				  StartLine();
			       
				break;
			case 2:
				  mScanner.setBackgroundDrawable(getResources().getDrawable(R.drawable.dog_full));
				  StartLine();
			        
				break;
			case 3:
				  mScanner.setBackgroundDrawable(getResources().getDrawable(R.drawable.girl_full));
				  StartLine();
			      
				break;
			case 4:
				  mScanner.setBackgroundDrawable(getResources().getDrawable(R.drawable.happy_full));
				  StartLine();
			      
				break;
			
			case 5:
				  mScanner.setBackgroundDrawable(getResources().getDrawable(R.drawable.michel_full));
				  StartLine();
			      
				break;
			
			case 6:
				  mScanner.setBackgroundDrawable(getResources().getDrawable(R.drawable.play_full));
				  StartLine();
			      
				break;
			
			case 7:
				  mScanner.setBackgroundDrawable(getResources().getDrawable(R.drawable.walk_full));
				  StartLine();
			      
				break;	
			}

	}

	public void StartLine() {
		try {
			
			mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
					0f, TranslateAnimation.ABSOLUTE, 0f,
					TranslateAnimation.RELATIVE_TO_PARENT, 0f,
					TranslateAnimation.RELATIVE_TO_PARENT, 0.5f);
			mAnimation.setDuration(10000);
			mAnimation.setRepeatCount(-1);
			mAnimation.setRepeatMode(Animation.REVERSE);
			mAnimation.setInterpolator(new LinearInterpolator());
			mScanner.setAnimation(mAnimation);
			mScanner.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.getLocalizedMessage();
			e.printStackTrace();
		}
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
	  
	  /** Called before the activity is destroyed. */
	  @Override
	  public void onDestroy() {
	    if (adView != null) {
	      // Destroy the AdView.
	      adView.destroy();
	    }
	    super.onDestroy();
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
