package com.mobirizer.hollywoodemoji;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class AboutAll extends Activity {
	static int isWhat = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		if (isWhat == 0) {
			setContentView(R.layout.ab_product);

		} else {
			setContentView(R.layout.ab_company);
		}

	}

	public void openFB(View v) {
		Intent FBI = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("http://www.facebook.com/pages/MobiRizer/292070540980264"));
		startActivity(FBI);
	}

	public void openGPlus(View v) {
		Intent GPI = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("https://plus.google.com/108790281049035345752/about"));
		startActivity(GPI);

	}

	public void openTwit(View v) {
		Intent TWI = new Intent(Intent.ACTION_VIEW,
				Uri.parse("https://twitter.com/Mobirizer"));
		startActivity(TWI);
	}
}
