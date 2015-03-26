package com.mobirizer.locoreminder;



import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NetworkAlert {
	public static void ShowNetworkAlert(Context theContext){
		final Dialog dialog = new Dialog(theContext);
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setTitle("Alert!");
		dialog.setContentView(R.layout.internet_alert_dialog);
//		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,R.drawable.title_logo);
		Button okBtn = (Button) dialog.findViewById(R.id.ok_btn);
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}
