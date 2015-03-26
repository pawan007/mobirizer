package com.mobirizer.autotasker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.haibison.android.lockpattern.LockPatternActivity;
import com.haibison.android.lockpattern.util.Settings;

public class ApplicationSettingActivity extends Activity{
	private RelativeLayout changepatterbtn,defaultapplock,help,url;
	private AdView adView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.secondactivity_applicationsetting);
		initView();
		changepatterbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCreatePattern();
			}
		});
		defaultapplock.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						DefaultappLockSettingsActivity.class);
				startActivity(intent);	
			}
		});
		help.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						TourActivity.class);
				startActivity(intent);	
			}
		});
		url.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("https://www.youtube.com/watch?v=NmC_IWSNG-s"));
				startActivity(intent);	
			}
		});
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(getResources().getString(R.string.Banner_ad_id));
		LinearLayout layout = (LinearLayout) findViewById(R.id.add_layout);
		layout.setVisibility(View.VISIBLE);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}
private void initView(){
	changepatterbtn=(RelativeLayout) findViewById(R.id.relativelayout_changepattern);
	defaultapplock=(RelativeLayout) findViewById(R.id.relativelayout_defaultapplock);
	help=(RelativeLayout) findViewById(R.id.relativelayout_help);
	url=(RelativeLayout) findViewById(R.id.relativelayout_url);
}
private void showCreatePattern()
{
	Settings.Security.setAutoSavePattern(ApplicationSettingActivity.this, true);
	Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN,
			null, ApplicationSettingActivity.this, LockPatternActivity.class);
	startActivityForResult(intent, 1);
}
}
