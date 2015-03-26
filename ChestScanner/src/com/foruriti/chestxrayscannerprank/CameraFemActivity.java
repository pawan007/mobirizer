package com.foruriti.chestxrayscannerprank;
import android.view.animation.Animation.AnimationListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import static com.foruriti.chestxrayscannerprank.CameraHelper.cameraAvailable;
import static com.foruriti.chestxrayscannerprank.CameraHelper.getCameraInstance;
import android.widget.ToggleButton;

public class CameraFemActivity extends Activity { 

	private Camera camera;
	private CameraPreview cameraPreview;

	private ImageView mScanner;
	private Animation mAnimation;
	Camera.Parameters parameters;

	/** Your ad unit id*/
	  private static final String AD_UNIT_ID = "ca-app-pub-4272695453752252/5262471921";
	  /** The view to show the ad. */
	  private AdView adView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.main);
		camera = getCameraInstance();
		initCameraPreview();
		StartLine();
		
	}

	// Show the camera view on the activity
	private void initCameraPreview() {
		cameraPreview = (CameraPreview) findViewById(R.id.overlayViewCameraFem);
		cameraPreview.init(camera);
		
		parameters = camera.getParameters();
		parameters.setColorEffect(Camera.Parameters.EFFECT_NEGATIVE);
		camera.setParameters(parameters);
		camera.setDisplayOrientation(90);
	}


	private void releaseCamera() {
		if(camera != null){
			camera.release();
			camera = null;
		}
	}
	
	
	public void StartLine() {
		try {
			mScanner = (ImageView) findViewById(R.id.Scanner);
			mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE,
					0f, TranslateAnimation.ABSOLUTE, 0f,
					TranslateAnimation.RELATIVE_TO_PARENT, 0f,
					TranslateAnimation.RELATIVE_TO_PARENT, 1.0f);
			
			
			mAnimation.setAnimationListener(new AnimationListener() {

			        public void onAnimationEnd(Animation anim) {

			    		CallActivity();

			        }

			        public void onAnimationRepeat(Animation arg0) {}

			        public void onAnimationStart(Animation arg0) {
			        }

			    });

			
			mAnimation.setDuration(1000);
			mAnimation.setRepeatCount(8);
			mAnimation.setRepeatMode(Animation.REVERSE);
			mAnimation.setInterpolator(new LinearInterpolator());
			mScanner.setAnimation(mAnimation);
			mScanner.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.getLocalizedMessage();
			e.printStackTrace();
		}
	}
//	@Override
//	public void onAnimationEnd(Animation animation) {
//		CallActivity();
//    }
//    public void onAnimationRepeat(Animation animation) {
//        // TODO Auto-generated method stub
//
//    }
//    public void onAnimationStart(Animation animation) {
//        // TODO Auto-generated method stub
//
//    }

	public void CallActivity() {
		Intent intent_ImageShow = new Intent(CameraFemActivity.this,
				ImageShow.class);
		startActivity(intent_ImageShow);
		CameraFemActivity.this.finish();
	}
	
	@Override
	  public void onResume() {
	    super.onResume();
	  }
	  @Override
	  public void onPause() {
	    super.onPause();
	    releaseCamera();
	  }
	  
	  /** Called before the activity is destroyed. */
	  @Override
	  public void onDestroy() {
	    super.onDestroy();
	  }
	 
}






























