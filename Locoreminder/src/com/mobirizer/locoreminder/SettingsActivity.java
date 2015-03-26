package com.mobirizer.locoreminder;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends FragmentActivity{
	public static SettingsActivity settingsInstance;
	public static Context settingsContext;
	private MainActivity mainInstance;
	private SavePreferences savePreferences;
	private SharedPreferences sharedPreferences;

	private TextView showArriveFence, showMpgRange;
	private SeekBar setArriveFence, setMpgRange;
//	private RadioButton geoFenceButton, timeFenceButton;
//	private RadioButton unitMetric, unitUS;
	private boolean geoFenceChecked = true, timeFenceChecked = false;
	private boolean unitMetricChecked = false, unitUSChecked = true;
	private Button confirmBtn;
	private int setArriveRangeProgress, setMpgRangeProgress;
	private String geoFenceText = "Geo Fence: $ Meters", timeFenceText = "Time Fence: $ Minutes";
	private String mpgText = "MPG: $";
	private String APP_NAME = "RydeLog";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		InitOnContentChanged();
        
      
    }
	private void InitOnContentChanged() {
		settingsInstance = SettingsActivity.this;
		settingsContext = SettingsActivity.this;
		savePreferences = new SavePreferences();
		showArriveFence = (TextView)this.findViewById(R.id.show_arrive_fence);
		setArriveFence = (SeekBar)this.findViewById(R.id.set_arrive_fence);
		setArriveFence.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar));
		setMpgRange = (SeekBar)this.findViewById(R.id.set_mpg_range);
		setMpgRange.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar));
		setArriveFence.setMax(4);
		setArriveFence.setProgress(0);
		setMpgRange.setMax(40);
		setMpgRange.setProgress(15);
		confirmBtn = (Button)this.findViewById(R.id.confirm_btn);
		confirmBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SettingsActivity.this,FirstActivity.class);
				startActivity(i);
				SettingsActivity.this.finish();
			}
		});
		
	}
	
	
	public void onResume(){
		super.onResume();
//		StartOnResumeFragments();
		
	}
	public void onResumeFragments(){
		super.onResumeFragments();
		StartOnResumeFragments();
	}
	public void onPause(){
		super.onPause();
		
	}
	
	public void onStop(){
		super.onStop();
	}
	
	private void StartOnResumeFragments() {
		try {
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
			int aArriveProgress = sharedPreferences.getInt("arrive_progress", 0);
			int aMpgProgress = sharedPreferences.getInt("mpg_progress", 15);
			unitMetricChecked = sharedPreferences.getBoolean("unit_metric", false);
			
			unitUSChecked = sharedPreferences.getBoolean("unit_us", true);
			geoFenceChecked = sharedPreferences.getBoolean("geo_fence", true);
			timeFenceChecked = sharedPreferences.getBoolean("time_fence", false);
			
			setArriveFence.setProgress(aArriveProgress);
			
			/* if(geoFenceChecked){
				showArriveFence.setText(geoFenceText.replace("$", Integer.toString((aArriveProgress+1)*200)));
			}else if(timeFenceChecked){
				showArriveFence.setText(timeFenceText.replace("$", Integer.toString((aArriveProgress+1)*5)));
			} */
			setMpgRange.setProgress(aMpgProgress);
			
			
			showArriveFence.setText(geoFenceText.replace("$", Integer.toString((aArriveProgress+1)*200)));
			OnSeekBarChangeListener aSetArriveRangeListener = new OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					setArriveRangeProgress = progress;
					int aArriveFence = -1;
					if(geoFenceChecked){
						aArriveFence = (progress+1)*200;
						
						if(aArriveFence>0){
							showArriveFence.setText(geoFenceText.replace("$", Integer.toString(aArriveFence)));
						}
					}else if(timeFenceChecked){
						aArriveFence = (progress+1)*5;
						if(aArriveFence>0){
							showArriveFence.setText(timeFenceText.replace("$", Integer.toString(aArriveFence)));
						}
					}
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					savePreferences.SavePreferences(SettingsActivity.this, "arrive_progress", setArriveRangeProgress);
				}
			};
			setArriveFence.setOnSeekBarChangeListener(aSetArriveRangeListener);
			
			OnSeekBarChangeListener aSetMpgRangeListener = new OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					setMpgRangeProgress = progress;
					int aMpgRange = progress+10;
//				showMpgRange.setText(mpgText.replace("$", Integer.toString(aMpgRange)));
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					savePreferences.SavePreferences(SettingsActivity.this, "mpg_progress", setMpgRangeProgress);
				}
			};
			setMpgRange.setOnSeekBarChangeListener(aSetMpgRangeListener);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public static boolean isNumeric(String thePhoneNum){
		for (int i = 0; i<thePhoneNum.length(); i++){   
			if (!Character.isDigit(thePhoneNum.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			mainInstance.GetAlertDialog(SettingsActivity.this, "onKeyDown", APP_NAME, "Okay To Close?", "Yes", null, "No");
			Intent intent = new Intent(SettingsActivity.this,FirstActivity.class);
			startActivity(intent);
			SettingsActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
  //  @Override
//    public void onBackPressed() {
//    	// TODO Auto-generated method stub
//    	super.onBackPressed();
//    	Intent intent = new Intent(SettingsActivity.this,FirstActivity.class);
//		startActivity(intent);
//		SettingsActivity.this.finish();
//    }
}
