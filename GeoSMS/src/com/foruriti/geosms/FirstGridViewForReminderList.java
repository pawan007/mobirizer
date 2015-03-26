package com.foruriti.geosms;

import java.util.ArrayList;

import com.foruriti.locosms.R;

import bin.ReminderInfo;

import android.R.color;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FirstGridViewForReminderList extends BaseAdapter {
	private ArrayList<String> listReminderName;
	private ArrayList<String> placemessage;
	private ArrayList<String> Alarmtypearray;
	private ArrayList<Integer> listImage;
	private ArrayList<Integer>mapradius;
	
	private Activity activity;
	ViewHolder view;

	public FirstGridViewForReminderList(Activity activity,
			ArrayList<String> listReminderName, ArrayList<String> placemessage,
			ArrayList<String> alarmlist,ArrayList<Integer> radius, ArrayList<Integer> listImage) {
		super();
		this.listReminderName = listReminderName;
		this.placemessage = placemessage;
		this.Alarmtypearray = alarmlist;
		this.mapradius=radius;
		this.listImage = listImage;
		this.activity = activity;
		System.out.println("alarm type>>>>>"+Alarmtypearray);
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
		public ImageView imgViewBackground,bulb;
		public TextView txtViewReninderTilale, txtViewReminderMessage,txtviewphoneno;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflator = activity.getLayoutInflater();
//		String fontPath = "fonts/Gabriola_0.ttf";
//		Typeface tf = Typeface.createFromAsset(this.activity.getAssets(),
//				fontPath);

		if (convertView == null) {
			try {
				view = new ViewHolder();
				convertView = inflator.inflate(R.layout.loco_grid, null);
				if(position%2==1)
				{
				convertView.setBackgroundColor(Color.parseColor("#7bcd4d"));
				}
				else{
					convertView.setBackgroundColor(Color.parseColor("#f26665"));
				}
				view.txtViewReninderTilale = (TextView) convertView
						.findViewById(R.id.tv_secondloco);
				view.txtViewReminderMessage = (TextView) convertView
						.findViewById(R.id.tv_loco);
				
				view.txtViewReninderTilale.setTextSize(15);
//				view.txtViewReninderTilale.setTypeface(tf);
				view.txtViewReminderMessage.setTextSize(20);
				view.txtviewphoneno=(TextView) convertView.findViewById(R.id.phoneno);
				view.txtViewReminderMessage.setTextSize(15);
				if(Alarmtypearray.get(position).equalsIgnoreCase("0")){
//					System.out.println("at condition zero0000000");
					view.bulb=(ImageView) convertView.findViewById(R.id.img_bulbun);
					view.bulb.setImageDrawable(activity.getResources().getDrawable(R.drawable.bulb_un));
				}
				if(Alarmtypearray.get(position).equalsIgnoreCase("1")){
//					System.out.println("at condition one1111111111");
					view.bulb=(ImageView) convertView.findViewById(R.id.img_bulb);	
					view.bulb.setImageDrawable(activity.getResources().getDrawable(R.drawable.bulb_s));
				}
				
//				view.txtViewReminderMessage.setTypeface(tf);
				/*view.imgViewBackground = (ImageView) convertView
						.findViewById(R.id.iv_note);*/
//				view.pinedit=(ImageView) convertView.findViewById(R.id.img_edit);
//				view.pindelete=(ImageView)convertView.findViewById(R.id.img_delete);
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
//		int indexofstric=placemessage.get(position).indexOf("*");
//		int indexofdolar=placemessage.get(position).indexOf("$");
//		String str=placemessage.get(position).substring(0, (placemessage.get(position).indexOf("*")));
//		System.out.println("seuihytrgikeusrmhkyugtsehxsudeeyrgtu>>>>>>>"+placemessage.get(position));
			view.txtViewReminderMessage.setText("Message to "+placemessage.get(position).substring(placemessage.get(position).indexOf("*")+1,(placemessage.get(position).indexOf("$"))));
//		view.txtviewphoneno.setText((placemessage.get(position).substring((placemessage.get(position).indexOf("$")+1))));
//		view.txtviewphoneno.setText(placemessage.get(position).substring(0, (placemessage.get(position).indexOf("*"))));
			view.txtViewReninderTilale.setOnLongClickListener(new OnItemClickListener(position));
			view.txtViewReminderMessage.setOnLongClickListener(new OnItemClickListener(position));
			view.bulb.setOnClickListener(new OnItemClickListenerbulb(position));
//		view.pindelete.setOnClickListener(new OnItemClickListenerdelete(position));
		return convertView;
	}

	private class OnItemClickListener implements OnClickListener, OnLongClickListener {
		private int mPosition;
		ImageView img;

		OnItemClickListener(int position) {
			mPosition = position;
		}
//		
		@Override
		public void onClick(View v) {
			FirstActivity sct = (FirstActivity) activity;
			sct.onItemlongclick(mPosition);

		}

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			FirstActivity sct = (FirstActivity) activity;
			sct.onItemlongclick(mPosition);
			return false;
			
		}

		
	}
	private class OnItemClickListenerbulb implements OnClickListener {
		private int mPosition;
//		ImageView img;

		OnItemClickListenerbulb(int position) {
			mPosition = position;
		}
//		
		@Override
		public void onClick(View v) {
			FirstActivity sct = (FirstActivity) activity;
			sct.onItembulbclick(mPosition);

		}

		
		
	}
	
	
}
