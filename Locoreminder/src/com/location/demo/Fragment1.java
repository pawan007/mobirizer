package com.location.demo;



import com.mobirizer.locoreminder.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Fragment1 extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
//		return (LinearLayout) inflater.inflate(R.layout.fragment1_layout,container, false);
		return inflater.inflate(R.layout.fragment1_layout,container,false);
	}
}
