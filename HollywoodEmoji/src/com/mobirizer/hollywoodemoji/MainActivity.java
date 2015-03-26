package com.mobirizer.hollywoodemoji;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

//Hollywood Emoji Quiz_Banner - ca-app-pub-1765898360821109/5627792276
//Hollywood Emoji Quiz_Int - ca-app-pub-1765898360821109/7104525475
public class MainActivity extends Activity {
	final Context c = this;
	ImageView resume,soundView;
	static int active = 0;
	static int currentTime = 0;
	boolean isSoundOn = false;
	AudioManager audioMan;
	static int correctAns = 0;
	private MediaPlayer mp;
    
	/** Your ad unit id*/
	  private static final String AD_UNIT_ID = "ca-app-pub-1765898360821109/5627792276";
	  /** The view to show the ad. */
	  private AdView adView;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.activity_main);

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

	 // Create an ad request. Check logcat output for the hashed device ID to
	    // get test ads on a physical device.
	    AdRequest adRequest = new AdRequest.Builder().build();
	     

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);

	    
		initView();
		if (active >= 1) {
			resume = (ImageView) findViewById(R.id.resume);

			resume.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					resumeGame();
				}
			});
		} else {
			// button is deactivated by default
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		//this.finish();
		showExitDialog();

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
		if(adView!=null){
			adView.destroy();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		soundView = (ImageView) findViewById(R.id.sound);
    	soundView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isSoundOn == true) {
					setSound(true);
					soundView.setBackgroundDrawable(getResources().getDrawable(R.drawable.soundon_selector));  
				} else {
					setSound(false);
					soundView.setBackgroundDrawable(getResources().getDrawable(R.drawable.soundoffselector)); 
				}
			}
		});
	}

	private void setSound(boolean status) {
		if (status == false) {
			audioMan = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioMan.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
					AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);

			// audioMan.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);

			isSoundOn = true;
		} else if (status == true) {
			audioMan = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

			// audioMan.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);
			audioMan.setStreamVolume(AudioManager.STREAM_MUSIC, 100,
					AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
			isSoundOn = false;
		}

	}

	public void playGame(View v) {
		//v.setSoundEffectsEnabled(false);
		initSound();
		Intent round = new Intent(c, QuestionActivity.class);
	
		startActivity(round);
		
		QuestionActivity.currentFrag = 1;

	}

	public void resumeGame() {
		
		Intent round = new Intent(c, QuestionActivity.class);
		startActivity(round);
		initSound();
		QuestionActivity.currentFrag = Fragment1.i;
		QuestionActivity.timeNow = currentTime;
		QuestionActivity.correctAnswers = correctAns;
		/*
		 * Toast.makeText(getApplicationContext(), "resume", Toast.LENGTH_LONG)
		 * .show();
		 */
	}

	public void soundOnOff(View v) {
		/*
		 * Toast.makeText(getApplicationContext(), "sound", Toast.LENGTH_LONG)
		 * .show();
		 */
	}

	public void about(View v) {
		Intent about = new Intent(this, AboutCActivity.class);
		startActivity(about);
		initSound();
		/*
		 * Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_LONG)
		 * .show();
		 */
	}
	private void showExitDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

		// set title
		alertDialogBuilder.setTitle("Do you really want to exit this game?");
		

		// set dialog message
		alertDialogBuilder
				.setCancelable(true)
				.setPositiveButton("Quit",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finishMe();
							}
						})
				.setNegativeButton("Not Now",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
								
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
		// return nowWhat;

	}
	private void finishMe()
	{
		this.finish();
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
