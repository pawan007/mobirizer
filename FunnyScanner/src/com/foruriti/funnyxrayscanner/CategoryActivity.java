package com.foruriti.funnyxrayscanner;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class CategoryActivity extends Activity implements OnItemClickListener {
	private static final String IMAGEID="imageid";
	private static final String ID="id";
	
	/** Your ad unit id*/
	  private static final String AD_UNIT_ID = "ca-app-pub-4272695453752252/9692671529";
	  /** The view to show the ad. */
	  private AdView adView;
	  
	SharedPreferences sharep;
	Editor edt;

	public static final String[] titles=new String[] {"Couple",
		"Danger","Dog","Girl","Happy","Michel","Play","Walk"};
	public static final Integer[] imgThumb ={
			R.drawable.couple,
			R.drawable.danger,
			R.drawable.dog,
			R.drawable.girl,
			R.drawable.happy,
			R.drawable.michel,
			R.drawable.play,
			R.drawable.walk,
	};
	ListView listView;
	List<RowItemModel> rowItems;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	setContentView(R.layout.list);
	
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
    RelativeLayout layoutAd = (RelativeLayout) findViewById(R.id.layoutAd);
    layoutAd.addView(adView);

 // Create an ad request. Check logcat output for the hashed device ID to	    // get test ads on a physical device.

    AdRequest adRequest = new AdRequest.Builder().build();

    // Start loading the ad in the background.
    adView.loadAd(adRequest);
    
	sharep = getApplicationContext().getSharedPreferences(IMAGEID, MODE_PRIVATE);
	 edt=sharep.edit();
	 rowItems=new ArrayList<RowItemModel>();
	 for(int i=0;i<titles.length;i++) {
		 RowItemModel item = new RowItemModel(imgThumb[i],titles[i]);
		 rowItems.add(item);
	 }
	 
	 listView=(ListView) findViewById(R.id.listView1);
	 CustomBaseAdapter adapter = new CustomBaseAdapter(this, rowItems);
	 listView.setAdapter(adapter);
	 listView.setOnItemClickListener(this);
	} 		
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		edt.putInt(ID, position); 
		edt.commit();
		Intent i = new Intent(getApplicationContext(),CameraFemActivity.class);
		startActivity(i);
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

