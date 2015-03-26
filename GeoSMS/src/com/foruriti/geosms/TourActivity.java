package com.foruriti.geosms;

import java.util.List;
import java.util.Vector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

import com.foruriti.locosms.R;
import com.location.demo.Fragment1;
import com.location.demo.Fragment2;
import com.location.demo.Fragment3;
import com.location.demo.Fragment4;
import com.location.demo.Fragment5;
import com.location.demo.PagerAdapter;

public class TourActivity extends FragmentActivity {
	private PagerAdapter mPagerAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tour_layout);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initialisePaging();
	}

	private void initialisePaging() {
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this,Fragment1.class.getName()));
		fragments.add(Fragment.instantiate(this,Fragment2.class.getName()));
		fragments.add(Fragment.instantiate(this,Fragment3.class.getName()));
		fragments.add(Fragment.instantiate(this,Fragment4.class.getName()));
		fragments.add(Fragment.instantiate(this,Fragment5.class.getName()));		
		mPagerAdapter =new PagerAdapter(this.getSupportFragmentManager(), fragments);
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setAdapter(mPagerAdapter);
		pager.setOnTouchListener(new OnTouchListener() {
			float x, y, upx, upy; 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) { 
					x = event.getX(); 
					y = event.getY(); 
				} 
				if (event.getAction() == MotionEvent.ACTION_UP) { 
					upx = event.getX(); 
					upy = event.getY(); 
					System.out.println("(x - upx):" + (x - upx));
					if ( y - upy > 220) {
						SharedPreferences aSharedPreferences = PreferenceManager.getDefaultSharedPreferences(TourActivity.this);
			    		String aDemoChecked = aSharedPreferences.getString("demo_checked", "N");
			    		if(aDemoChecked.equals("N")){
			    			SavePreferences aSavePreferences = new SavePreferences();
							aSavePreferences.SavePreferences(TourActivity.this, "demo_checked", "Y");
							Intent aMainActivityIntent;
							aMainActivityIntent = new Intent(TourActivity.this, FirstActivity.class);
							TourActivity.this.startActivity(aMainActivityIntent);
							TourActivity.this.finish();
			    		}else {
			    			Intent aMainActivityIntent;
							aMainActivityIntent = new Intent(TourActivity.this, FirstActivity.class);
							TourActivity.this.startActivity(aMainActivityIntent);
			    			TourActivity.this.finish();
			    		}
					}
				}
				return false;
			} 
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			mainInstance.GetAlertDialog(SettingsActivity.this, "onKeyDown", APP_NAME, "Okay To Close?", "Yes", null, "No");
			Intent intent = new Intent(TourActivity.this,FirstActivity.class);
			startActivity(intent);
			TourActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
