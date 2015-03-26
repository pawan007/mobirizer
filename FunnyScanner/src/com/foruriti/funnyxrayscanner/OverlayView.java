package com.foruriti.funnyxrayscanner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * @author Evan
 *
 */
public class OverlayView extends SurfaceView {

	public OverlayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public OverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public OverlayView(Context context) {
		super(context);
	}

}
