package com.mobirizer.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobirizer.autotasker.R;

public class TutorialFirst extends Fragment {
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
