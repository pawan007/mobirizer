package com.mobirizer.utility;

import android.app.Activity;
import android.graphics.Typeface;

public class Customfont {
	public static Typeface customtf;
	
	public static Typeface getCustomFont(Activity activity){
		if(customtf==null){
		customtf= Typeface.createFromAsset(activity.getAssets(), "fonts/corbel.ttf");
	}
		return customtf;
			
		}
}
