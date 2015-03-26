package com.mobirizer.locoreminder;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;



public class ProgressDialog {
	Dialog dialog;
	public void GetDialog(Context theContext, String theMsg){
		dialog = new Dialog(theContext);
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setContentView(R.layout.proress_dialog);
		dialog.setTitle("Loco Reminder");
//		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.title_logo);
		TextView progressText = (TextView) dialog.findViewById(R.id.progress_text);
		progressText.setText(theMsg);
		dialog.show();
	}

	public void DismissDialog(){
		dialog.dismiss();
	}
}