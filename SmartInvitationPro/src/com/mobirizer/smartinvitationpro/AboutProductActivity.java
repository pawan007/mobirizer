package com.mobirizer.smartinvitationpro;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutProductActivity extends Activity {
	TextView about_product,smart_invi,product_dect,version,develop,rights;
	ImageButton option_btn;
	Typeface tf ;
	private String fontPath = "fonts/SCRIPTBL.TTF";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onCreate(savedInstanceState);
	  setContentView(R.layout.about_product);
	  
	  about_product=(TextView) findViewById(R.id.abt_prod);
	   smart_invi=(TextView) findViewById(R.id.smart_invi);
	   product_dect=(TextView) findViewById(R.id.prod_desc);
	   version=(TextView) findViewById(R.id.version);
	   develop=(TextView) findViewById(R.id.develop);
	   rights=(TextView) findViewById(R.id.rights);
	   
	   tf = Typeface.createFromAsset(getAssets(), fontPath);
	   about_product.setTypeface(tf);
	   smart_invi.setTypeface(tf);
	   product_dect.setTypeface(tf);
	   version.setTypeface(tf);
	   develop.setTypeface(tf);
	   rights.setTypeface(tf);
	}
}
