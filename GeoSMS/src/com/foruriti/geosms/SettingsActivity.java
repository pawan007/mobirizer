package com.foruriti.geosms;

import java.util.TooManyListenersException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.foruriti.locosms.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class SettingsActivity extends FragmentActivity{
	public static SettingsActivity settingsInstance;
	public static Context settingsContext;
//	private MainActivity mainInstance;
	private SavePreferences savePreferences;
	private SharedPreferences sharedPreferences;
	private RadioGroup radioGroupaccuracy,radioGroupsound,radioGroupvibration;
	private RadioButton accuracylow,accuracymedium,accuracyhigh,soundon,soundoff,vibrationon,vibrationoff,opt1,opt2,opt3;
	private int accuracylevel,soundlevel,vibrationlevel;
	public static int soundonoff,vibrateonoff,accuracylowmediumhigh;
	private TextView showArriveFence, showMpgRange;
	private SeekBar setArriveFence, setMpgRange;

	private boolean geoFenceChecked = true, timeFenceChecked = false;
	private boolean unitMetricChecked = false, unitUSChecked = true;
	private Button confirmBtn;
	private int setArriveRangeProgress, setMpgRangeProgress;
	private String geoFenceText = "Radius: $ Meters", timeFenceText = "Time Fence: $ Minutes";
	private String mpgText = "MPG: $";
//	private String APP_NAME = "RydeLog";
	public static int aArriveProgress;
	ToggleButton toggleButton_sound,toggleButton_vibration;
	//initialise add variable
	private static final String AD_UNIT_ID = "ca-app-pub-1765898360821109/8391395873";
	/** The view to show the ad. */
	private AdView adView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		InitOnContentChanged();
        
		//call add
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
    }
	private void InitOnContentChanged() {
		settingsInstance = SettingsActivity.this;
		settingsContext = SettingsActivity.this;
		savePreferences = new SavePreferences();

		radioGroupaccuracy=(RadioGroup) findViewById(R.id.accuracy);
//		radioGroupsound=(RadioGroup) findViewById(R.id.sound);
//		radioGroupvibration=(RadioGroup) findViewById(R.id.vibration);
		accuracylow=(RadioButton) findViewById(R.id.radioButton1);
		accuracyhigh=(RadioButton) findViewById(R.id.radioButton2);
		accuracymedium=(RadioButton) findViewById(R.id.radioButton3);
//		soundon=(RadioButton) findViewById(R.id.radioButton4);
//		soundoff=(RadioButton) findViewById(R.id.radioButton5);
//		vibrationoff=(RadioButton) findViewById(R.id.radioButton7);
//		vibrationon=(RadioButton) findViewById(R.id.radioButton6);
		toggleButton_sound = (ToggleButton) findViewById(R.id.toggle);
		toggleButton_vibration = (ToggleButton) findViewById(R.id.toggle1);
		System.out.println("togglebutton>>>>>"+toggleButton_sound);
		checkRadioButtonStatusforaccuracy();
		checkRadioButtonStatusforsound();
		checkRadioButtonStatusforvibrate();
		
//		confirmBtn = (Button)this.findViewById(R.id.confirm_btn);
//		confirmBtn.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				int selectedIdforaccuracy = radioGroupaccuracy.getCheckedRadioButtonId();
//				int selectedidforsound=radioGroupsound.getCheckedRadioButtonId();
//				int selectedidforvibrate=radioGroupvibration.getCheckedRadioButtonId();
//				opt1 = (RadioButton) findViewById(selectedIdforaccuracy);
//				opt2=(RadioButton) findViewById(selectedidforsound);
//				opt3=(RadioButton) findViewById(selectedidforvibrate);
//				String bullonChooseaccuracy = opt1.getText().toString();
//				String bullonchoosesound=opt2.getText().toString();
//				String bullonchoosevibrate=opt3.getText().toString();
//				if (bullonChooseaccuracy.equalsIgnoreCase("Low")) {
//					accuracylevel = 1;
//					SetDataValueForReminder.accuracylevel=300000;
//					
//				}
//				else if (bullonChooseaccuracy.equalsIgnoreCase("Medium")) {
//					accuracylevel = 2;
//					SetDataValueForReminder.accuracylevel=150000;
//				}
//				else if (bullonChooseaccuracy.equalsIgnoreCase("High")) {
//					accuracylevel = 3;
//					SetDataValueForReminder.accuracylevel=50000;
//				}
//				
//				savePreferences.SavePreferences(SettingsActivity.this, "accuracy_level", accuracylevel);
//				if (bullonchoosesound.equalsIgnoreCase("on")) {
//					soundlevel = 1;
//					SetDataValueForReminder.soundstatus=true;
//				}
//				else if (bullonchoosesound.equalsIgnoreCase("off")) {
//					soundlevel = 2;
//					SetDataValueForReminder.soundstatus=false;
//				}
//				
//				savePreferences.SavePreferences(SettingsActivity.this, "sound_level", soundlevel);
//				if (bullonchoosevibrate.equalsIgnoreCase("on")) {
//					SetDataValueForReminder.vibratestatus=true;
//					vibrationlevel = 1;
//				}
//				else if (bullonchoosevibrate.equalsIgnoreCase("off")) {
//					SetDataValueForReminder.vibratestatus=false;
//					vibrationlevel = 2;
//				}
//				savePreferences.SavePreferences(SettingsActivity.this, "vibrate_level", vibrationlevel);
//				
//				Intent i = new Intent(SettingsActivity.this,FirstActivity.class);
//				startActivity(i);
//				SettingsActivity.this.finish();
//			}
//		});
		toggleButton_sound.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				if(isChecked){
//					stateOnOff.setText("On");
					soundonoff=1;
					
				}else{
//					stateOnOff.setText("Off");
					soundonoff=2;
				}

			}
		});
		toggleButton_vibration.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				if(isChecked){
//					stateOnOff.setText("On");
					vibrateonoff=1;
				}else{
//					stateOnOff.setText("Off");
					vibrateonoff=2;
				}

			}
		});
	}
	private void checkRadioButtonStatusforaccuracy(){
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
		accuracylowmediumhigh=sharedPreferences.getInt("accuracy_level", 0);
//		soundonoff=sharedPreferences.getInt("sound_level", 0);
//		System.out.println("accuracylowmediumhigh>>>>>>>"+accuracylowmediumhigh+sharedPreferences);
		if(accuracylowmediumhigh==1){
			accuracylow.setChecked(true);
		}
		else if (accuracylowmediumhigh==2) {
			accuracymedium.setChecked(true);
		}
		else if (accuracylowmediumhigh==3) {
			accuracyhigh.setChecked(true);
		}
		else{
			accuracyhigh.setChecked(true);	
		}
		
	}
	private void checkRadioButtonStatusforsound(){
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
		soundonoff=sharedPreferences.getInt("sound_level", 0);
		System.out.println("accuracylowmediumhigh>>>>>>>"+soundonoff);
		System.out.println("tooglebutton>>>>>>>>>>>."+toggleButton_sound.isChecked());
		if(soundonoff==1){
//			soundon.setChecked(true);
			toggleButton_sound.setChecked(true);
		}
		else if (soundonoff==2) {
//			soundoff.setChecked(true);
			toggleButton_sound.setChecked(false);
		}
		
		else{
//			soundon.setChecked(true);
			toggleButton_sound.setChecked(true);
//			toggleButton_sound.isChecked();
		}
		
	}
	private void checkRadioButtonStatusforvibrate(){
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
		vibrateonoff=sharedPreferences.getInt("vibrate_level", 0);
//		System.out.println("accuracylowmediumhigh>>>>>>>"+vibrateonoff);
		if(vibrateonoff==1){
//			vibrationon.setChecked(true);
			toggleButton_vibration.setChecked(true);
		}
		else if (vibrateonoff==2) {
//			vibrationoff.setChecked(true);
			toggleButton_vibration.setChecked(false);
		}
		
		else{
//			vibrationon.setChecked(true);	
			toggleButton_vibration.setChecked(true);
		}
		
	}
	public void onResume(){
		super.onResume();
//		StartOnResumeFragments();
		
	}
	public void onResumeFragments(){
		super.onResumeFragments();
//		StartOnResumeFragments();
	}
	public void onPause(){
		super.onPause();
		
	}
	
	public void onStop(){
		super.onStop();
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
			saveInput();
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
	private void saveInput() {
		// TODO Auto-generated method stub
		int selectedIdforaccuracy = radioGroupaccuracy.getCheckedRadioButtonId();
//		int selectedidforsound=radioGroupsound.getCheckedRadioButtonId();
//		int selectedidforvibrate=radioGroupvibration.getCheckedRadioButtonId();
		opt1 = (RadioButton) findViewById(selectedIdforaccuracy);
//		opt2=(RadioButton) findViewById(selectedidforsound);
//		opt3=(RadioButton) findViewById(selectedidforvibrate);
		String bullonChooseaccuracy = opt1.getText().toString();
//		String bullonchoosesound=opt2.getText().toString();
//		String bullonchoosevibrate=opt3.getText().toString();
		if (bullonChooseaccuracy.equalsIgnoreCase("Low")) {
			accuracylevel = 1;
			SetDataValueForReminder.accuracylevel=300000;
			
		}
		else if (bullonChooseaccuracy.equalsIgnoreCase("Medium")) {
			accuracylevel = 2;
			SetDataValueForReminder.accuracylevel=150000;
		}
		else if (bullonChooseaccuracy.equalsIgnoreCase("High")) {
			accuracylevel = 3;
			SetDataValueForReminder.accuracylevel=50000;
		}
		
		savePreferences.SavePreferences(SettingsActivity.this, "accuracy_level", accuracylevel);
		if (toggleButton_sound.isChecked()) {
			soundlevel = 1;
			SetDataValueForReminder.soundstatus=true;
		}
		else if (!toggleButton_sound.isChecked()) {
//			toggleButton_sound.isChecked()
			soundlevel = 2;
			SetDataValueForReminder.soundstatus=false;
		}
		
		savePreferences.SavePreferences(SettingsActivity.this, "sound_level", soundlevel);
		if (toggleButton_vibration.isChecked()) {
			SetDataValueForReminder.vibratestatus=true;
			vibrationlevel = 1;
		}
		else if (!toggleButton_vibration.isChecked()) {
			SetDataValueForReminder.vibratestatus=false;
			vibrationlevel = 2;
		}
		savePreferences.SavePreferences(SettingsActivity.this, "vibrate_level", vibrationlevel);
	}
}
