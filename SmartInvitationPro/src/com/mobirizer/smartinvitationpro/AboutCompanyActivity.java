package com.mobirizer.smartinvitationpro;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutCompanyActivity extends Activity{
	TextView about_company,smart_invi,company_desc,version,develop,rights;
	ImageButton option_btn;
	Typeface tf ;
	private String fontPath = "fonts/SCRIPTBL.TTF";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onCreate(savedInstanceState);
	   setContentView(R.layout.about_company);
	   
	   about_company=(TextView) findViewById(R.id.abt_comp);
	   smart_invi=(TextView) findViewById(R.id.smart_invi);
	   company_desc=(TextView) findViewById(R.id.company_desc);
	   version=(TextView) findViewById(R.id.version);
	   develop=(TextView) findViewById(R.id.develop);
	   rights=(TextView) findViewById(R.id.rights);
	   
	   tf = Typeface.createFromAsset(getAssets(), fontPath);
	   about_company.setTypeface(tf);
	   smart_invi.setTypeface(tf);
	   company_desc.setTypeface(tf);
	   version.setTypeface(tf);
	   develop.setTypeface(tf);
	   rights.setTypeface(tf);
	   
	}

}
