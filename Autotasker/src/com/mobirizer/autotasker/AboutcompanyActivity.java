package com.mobirizer.autotasker;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class AboutcompanyActivity extends Activity{
//	ImageView fb,google,twitter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_aboutcompany);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//		google=(ImageView) findViewById(R.id.img_google);
//		google.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(
//						Intent.ACTION_VIEW,
//						Uri.parse("https://plus.google.com/108790281049035345752/about"));
//				startActivity(intent);	
//			}
//		});
//		fb=(ImageView) findViewById(R.id.img_fb);
//		fb.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(
//						Intent.ACTION_VIEW,
//						Uri.parse("https://www.facebook.com/mobiRizer"));
//				startActivity(intent);	
//			}
//		});
//		twitter=(ImageView) findViewById(R.id.img_twitter);
//		twitter.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(
//						Intent.ACTION_VIEW,
//						Uri.parse("https://twitter.com/Mobirizer"));
//				startActivity(intent);	
//			}
//		});
	}

}
