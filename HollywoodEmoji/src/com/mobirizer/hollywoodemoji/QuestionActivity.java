package com.mobirizer.hollywoodemoji;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.mobirizer.hollywoodemoji.db.HollywoodDb;
import com.mobirizer.hollywoodemoji.db.Question;

public class QuestionActivity extends Activity {

	private Fragment fragment1;
	private FragmentTransaction ft;
	static Context c;
	final Handler myHandler = new Handler();
	int totalTime = 0;
	TextView timer;
	private MediaPlayer mp;
	public static String luck = "Well Done!";// can be used
	int i = 0, _a, _b, _c, _d;
	static int id = 0;
	static int currentFrag = 0;
	static int timeNow = 0;
	int getAnsId = 0;
	ImageView ansA, ansB, ansC, ansD;
	public static int correctAnswers = 0;
	
	/** Your ad unit id*/
	  private static final String AD_UNIT_ID = "ca-app-pub-1765898360821109/5627792276";
	  /** The view to show the ad. */
	  private AdView adView;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		this.finish();
		MainActivity.currentTime = i;
		MainActivity.active = currentFrag;
		MainActivity.correctAns = correctAnswers;
		timeNow = 0;
		correctAnswers = 0;
		Intent main = new Intent(this, MainActivity.class);
		startActivity(main);
	}

	public void initialView() {
		ansA = (ImageView) findViewById(R.id.currect1);
		ansB = (ImageView) findViewById(R.id.currect2);
		ansC = (ImageView) findViewById(R.id.currect3);
		ansD = (ImageView) findViewById(R.id.currect4);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.activity_question);
		
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

	 // Create an ad request. Check logcat output for the hashed device ID to
	    // get test ads on a physical device.
	    AdRequest adRequest = new AdRequest.Builder().build();
	     

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);

		

		timer = (TextView) findViewById(R.id.TimerTask);
		i = timeNow;
		// timeLeft();
		c = QuestionActivity.this;
		fragment1 = new Fragment1(currentFrag);
		ft = getFragmentManager().beginTransaction();
		/*
		 * ft.setCustomAnimations(R.animator.slide1, R.animator.slide2,
		 * R.animator.slide2, R.animator.slide1);
		 */
		ft.setCustomAnimations(R.animator.slide1, R.animator.slide2);

		ft.replace(R.id.fragCont, fragment1);
		ft.addToBackStack(null);

		ft.commit();
	}

	// get ids of answer images to compare from data base

	public void getAns(View v) {

		String imgResult = getCorrectAnswer();
		String mytag = "q" + Fragment1.i;
		Fragment1.change.setEnabled(true);
		Fragment1.img1.setEnabled(false);
		Fragment1.img2.setEnabled(false);
		Fragment1.img3.setEnabled(false);
		Fragment1.img4.setEnabled(false);
		switch (v.getId()) {
		case R.id.f1ansView1:
			mytag = mytag + v.getTag();

			id = 1;
			break;
		case R.id.f1ansView2:
			mytag = mytag + v.getTag();

			id = 2;

			break;
		case R.id.f1ansView3:
			mytag = mytag + v.getTag();

			id = 3;

			break;
		case R.id.f1ansView4:
			mytag = mytag + v.getTag();

			id = 4;

			break;
		default:
			break;
		}
		if (imgResult.equals(mytag)) {
			initSound(1);
			compare(id);
			correctAnswers = correctAnswers + 1;
			luck = "Well Done!";
		} else {
			initSound(0);
			luck = "Bad Luck!";
			// ToastMesssage("Incorrect answer" + id);
			String lastInd = getCorrectAnswer();
			lastInd = lastInd.substring(lastInd.length() - 1, lastInd.length());
			int finalAns = Integer.parseInt(lastInd);
			compare(finalAns);
			System.out.println("lastind>>>>>>>>" + lastInd);

		}
	}

	private void initSound(int soundCheck) {
		// TODO Auto-generated method stub
		int resId = 0;
		if (soundCheck == 1) {
			resId = R.raw.right;
		} else {
			resId = R.raw.wrong;
		}
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

	private void setAns(int a_id, int b_id, int c_id, int d_id) {
		initialView();

		ansA.setVisibility(1);
		ansB.setVisibility(1);
		ansC.setVisibility(1);
		ansD.setVisibility(1);

		ansA.setBackgroundResource(a_id);
		ansB.setBackgroundResource(b_id);
		ansC.setBackgroundResource(c_id);
		ansD.setBackgroundResource(d_id);

	}

	private int getDrawable(String ansId) {
		int drawableId = getApplicationContext().getResources().getIdentifier(
				ansId, "drawable", getApplicationContext().getPackageName());
		return drawableId;
	}

	private void compare(int id) {
		getAnsId = id;
		// ToastMesssage("Answer " + getAnsId + " isCorrect");
		f1(getAnsId);
	}

	private void f1(int finalId) {
		int drawableIdyes = getDrawable("yes");
		int drawableIdNo = getDrawable("no");
		if (finalId == 1) {

			setAns(drawableIdyes, drawableIdNo, drawableIdNo, drawableIdNo);
		}
		if (finalId == 2) {

			setAns(drawableIdNo, drawableIdyes, drawableIdNo, drawableIdNo);
		}
		if (finalId == 3) {

			setAns(drawableIdNo, drawableIdNo, drawableIdyes, drawableIdNo);
		}
		if (finalId == 4) {

			setAns(drawableIdNo, drawableIdNo, drawableIdNo, drawableIdyes);
		}

	}

	private String getCorrectAnswer() {
		HollywoodDb db = new HollywoodDb(c);
		int questIndex = Fragment1.i;// current question index
		List<Question> list = db.getAnswers(questIndex);

		Question q1 = list.get(0);
		String ansId = q1.getANSWER();
		return ansId;

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
