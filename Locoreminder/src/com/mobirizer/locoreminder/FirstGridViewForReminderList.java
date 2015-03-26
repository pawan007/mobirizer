package com.mobirizer.locoreminder;

import java.util.ArrayList;

import android.R.color;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FirstGridViewForReminderList extends BaseAdapter {
	private ArrayList<String> listReminderName;
	private ArrayList<String> placemessage;
	private ArrayList<String> Alarmtypearray;
	private ArrayList<Integer> listImage;
	private Activity activity;
	ViewHolder view;

	public FirstGridViewForReminderList(Activity activity,
			ArrayList<String> listReminderName, ArrayList<String> placemessage,
			ArrayList<String> alarmlist, ArrayList<Integer> listImage) {
		super();
		this.listReminderName = listReminderName;
		this.placemessage = placemessage;
		this.Alarmtypearray = alarmlist;
		this.listImage = listImage;
		this.activity = activity;
	}

	@Override
	public int getCount() {

		return listReminderName.size();

	}

	@Override
	public Object getItem(int position) {

		return listReminderName.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	public static class ViewHolder {
		public ImageView imgViewBackground;
		public TextView txtViewReninderTilale, txtViewReminderMessage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflator = activity.getLayoutInflater();
		String fontPath = "fonts/Gabriola_0.ttf";
		Typeface tf = Typeface.createFromAsset(this.activity.getAssets(),
				fontPath);

		if (convertView == null) {
			try {
				view = new ViewHolder();
				convertView = inflator.inflate(R.layout.loco_grid, null);
				view.txtViewReninderTilale = (TextView) convertView
						.findViewById(R.id.tv_loco);
				view.txtViewReminderMessage = (TextView) convertView
						.findViewById(R.id.tv_secondloco);
				view.txtViewReninderTilale.setTextSize(25);
				view.txtViewReninderTilale.setTypeface(tf);
				view.txtViewReminderMessage.setTextSize(16);
				view.txtViewReminderMessage.setTypeface(tf);

				view.imgViewBackground = (ImageView) convertView
						.findViewById(R.id.iv_note);

				convertView.setTag(view);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("exception at view>>>>" + e);
			}
		} else {
			view = (ViewHolder) convertView.getTag();
		}

		view.txtViewReninderTilale.setText(listReminderName.get(position));
		view.txtViewReminderMessage.setText(placemessage.get(position));
		view.imgViewBackground.setImageResource(listImage.get(position));
		view.imgViewBackground.setOnClickListener(new OnItemClickListener(
				position));

		return convertView;
	}

	private class OnItemClickListener implements OnClickListener {
		private int mPosition;
		ImageView img;
		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View v) {
			
			
			
			FirstActivity sct = (FirstActivity) activity;
			sct.onItemClick(mPosition);

		}
	}

}
