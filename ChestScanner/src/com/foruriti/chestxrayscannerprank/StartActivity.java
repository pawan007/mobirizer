package com.foruriti.chestxrayscannerprank;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

 public class StartActivity extends Activity {
	private ImageView start_btn;
	/** Your ad unit id*/
	  private static final String AD_UNIT_ID = "ca-app-pub-4272695453752252/9832272325";
	  /** The view to show the ad. */
	  private AdView adView;
	  private InterstitialAd interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);	
		
		setContentView(R.layout.start);
		
		 // Create the interstitial.
	    interstitial = new InterstitialAd(this);
	    interstitial.setAdUnitId("ca-app-pub-4272695453752252/2309005529");
	    // Create ad request.
	    AdRequest adRequest = new AdRequest.Builder().build();

	    // Begin loading your interstitial.
	    interstitial.loadAd(adRequest);


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
	    RelativeLayout layoutAd = (RelativeLayout) findViewById(R.id.layoutAd1);
	    layoutAd.addView(adView);

	 // Create an ad request. Check logcat output for the hashed device ID to	    // get test ads on a physical device.

	     

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);

        start_btn=(ImageView) findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(StartActivity.this,CategoryActivity.class);
				startActivity(intent);		
			}
		});
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
  	  
  	@Override
    public void onBackPressed() {
		showExitDialog();
	}
	  
	 private void showExitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Do you want to exit ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
            	interstitial.show();
            	StartActivity.this.finish();
            }
        });
        
        builder.setNeutralButton("Rate us",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
               // dialog.cancel();
            	Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.foruriti.chestxrayscannerprank"));
				startActivity(mIntent);
				StartActivity.this.finish();
            	 
            }
        });
        
        builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
        TextView textView = (TextView) alert.findViewById(android.R.id.message);
        textView.setTextSize(20);
        Button btn1 = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        Button btn2 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button btn3 = alert.getButton(DialogInterface.BUTTON_NEUTRAL);
        btn1.setTextSize(16);
        btn2.setTextSize(16);
        btn3.setTextSize(16);
	}
} 

