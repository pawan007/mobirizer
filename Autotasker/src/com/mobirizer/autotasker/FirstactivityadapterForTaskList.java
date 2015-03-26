package com.mobirizer.autotasker;

import java.util.ArrayList;

import com.mobirizer.utility.Customfont;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

@SuppressLint("NewApi")
public class FirstactivityadapterForTaskList extends BaseAdapter {
	private ArrayList<String> listReminderName;
	private ArrayList<String> placemessage;
	private ArrayList<String> Alarmtypearray;
	@SuppressWarnings("unused")
	private ArrayList<String> taskname;
	private ArrayList<Integer> tasktypearray;
	@SuppressWarnings("unused")
	private ArrayList<Integer>mapradius;
	private final byte LEFTMARGIN=25,layout_thumb_height=80,layout_thumb_width=80;
	private int seekbarweidth=450;
	private byte LAYOUT_WIDTH=50,LAYOUT_HEIGHT=50;
	float[] outR = new float[] {6,6,6,6,6,6,6,6};  
	private static Bitmap bitmap;
	Resources res;
	int colorRed,colorGreen;
	private Activity activity;
	LayoutInflater inflater;
	public FirstactivityadapterForTaskList(Activity activity,
			ArrayList<String> listReminderName, ArrayList<String> placemessage,ArrayList<String> alarmlist,
			ArrayList<String> tasknamelist,ArrayList<Integer> radius, ArrayList<Integer> tasktype) {
		super();
		this.listReminderName = listReminderName;
		this.placemessage = placemessage;
		this.taskname = tasknamelist;
		this.Alarmtypearray = alarmlist;
		this.mapradius=radius;
		this.tasktypearray = tasktype;
		this.activity = activity;
		res = activity.getResources();
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		colorRed = res.getColor(R.color.light_red);
		colorGreen = res.getColor(R.color.light_green);
		inflater = activity.getLayoutInflater();
		//For large screen devices set icon size
		if(height>1280)
		{
			LAYOUT_WIDTH = 70;
			LAYOUT_HEIGHT = 70;
		}
		//micromax-480
		if(width<600){
			seekbarweidth=300;
		}
		System.out.println("1111111111width>>>>>>>"+width+"..."+Alarmtypearray+listReminderName);
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
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
			
				convertView = inflater.inflate(R.layout.loco_grid, null);
				TextView txtViewReninderTilale = (TextView) convertView.findViewById(R.id.tv_secondloco);
				txtViewReninderTilale.setTypeface(Customfont.getCustomFont(activity));
				ImageView imagethumb=(ImageView) convertView.findViewById(R.id.img_thumb);
				ImageView editbulb=(ImageView) convertView.findViewById(R.id.img_edit);
				TextView txtViewReminderMessage = (TextView) convertView.findViewById(R.id.tv_loco);
				HorizontalScrollView hzscrollview=(HorizontalScrollView) convertView.findViewById(R.id.horizontalScrollView1);
				
				if(Alarmtypearray.get(position).equalsIgnoreCase("0")){
					editbulb.setImageDrawable(activity.getResources().getDrawable(R.drawable.bulb_un));
				}
				if(Alarmtypearray.get(position).equalsIgnoreCase("1")){
					editbulb.setImageDrawable(activity.getResources().getDrawable(R.drawable.bulb_s));
				}
				try {
				if(tasktypearray.get(position)==0){
					txtViewReminderMessage = (TextView) convertView.findViewById(R.id.tv_loco);
					imagethumb.setImageDrawable(activity.getResources().getDrawable(R.drawable.brightnessforlist));
					float seekvalue=Float.parseFloat(placemessage.get(position));
					txtViewReminderMessage.setVisibility(View.GONE);
					LinearLayout mainLayout = (LinearLayout) convertView.findViewById(R.id.iconlayoutforadapter);
					hzscrollview.setVisibility(View.VISIBLE);
					SeekBar seekBar = new SeekBar(activity);
					ShapeDrawable thumb = new ShapeDrawable(new RoundRectShape(outR, null, null));      
			        thumb.setIntrinsicHeight(50);
			        thumb.setIntrinsicWidth(0);
			        seekBar.setThumb(thumb);
			        seekBar.setMax(250);
			        seekBar.setProgress((int) seekvalue);
			        seekBar.setEnabled(false);
			        seekBar.setVisibility(View.VISIBLE);
			        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(seekbarweidth,40);
			        parms.setMargins(45,10,0, 0);
			        seekBar.setLayoutParams(parms);
					mainLayout.addView(seekBar);
					mainLayout.setOnLongClickListener(new OnItemClickListener(position));
					
				}
				else if(tasktypearray.get(position)==1){
					hzscrollview.setVisibility(View.GONE);
					txtViewReminderMessage = (TextView) convertView.findViewById(R.id.tv_loco);
					imagethumb.setImageDrawable(activity.getResources().getDrawable(R.drawable.bluetoothforlist));
					txtViewReminderMessage.setVisibility(View.VISIBLE);
					txtViewReminderMessage.setText("Bluetooth : "+placemessage.get(position));
					
				}
				else if(tasktypearray.get(position)==2){
					txtViewReminderMessage = (TextView) convertView.findViewById(R.id.tv_loco);
					imagethumb.setImageDrawable(activity.getResources().getDrawable(R.drawable.soundforlist));
					float seekvalue=Float.parseFloat(placemessage.get(position));
					System.out.println("seekvalue>>>>>"+seekvalue);
					txtViewReminderMessage.setVisibility(View.GONE);
					LinearLayout mainLayout = (LinearLayout) convertView.findViewById(R.id.iconlayoutforadapter);
					hzscrollview.setVisibility(View.VISIBLE);
					SeekBar seekBar = new SeekBar(activity);
					ShapeDrawable thumb = new ShapeDrawable(new RoundRectShape(outR, null, null));      
			        thumb.setIntrinsicHeight(50);
			        thumb.setIntrinsicWidth(0);
			        seekBar.setThumb(thumb);
			        seekBar.setMax(15);
			        seekBar.setProgress((int) seekvalue);
			        seekBar.setEnabled(false);
			        seekBar.setVisibility(View.VISIBLE);
			        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(seekbarweidth,40);
			        parms.setMargins(45,10,0, 0);
			        seekBar.setLayoutParams(parms);
					mainLayout.addView(seekBar);
					mainLayout.setOnLongClickListener(new OnItemClickListener(position));
			
				}
				else if(tasktypearray.get(position)==3){
					hzscrollview.setVisibility(View.GONE);
					txtViewReminderMessage = (TextView) convertView.findViewById(R.id.tv_loco);
					imagethumb.setImageDrawable(activity.getResources().getDrawable(R.drawable.messageiconforlist));
					txtViewReminderMessage.setVisibility(View.VISIBLE);
					txtViewReminderMessage.setText("Message to "+placemessage.get(position).substring(placemessage.get(position).indexOf("*")+1,(placemessage.get(position).indexOf("$"))));
				
				}
				else if(tasktypearray.get(position)==4){
					hzscrollview.setVisibility(View.GONE);
					txtViewReminderMessage = (TextView) convertView.findViewById(R.id.tv_loco);
					imagethumb.setImageDrawable(activity.getResources().getDrawable(R.drawable.wififorlist));
					txtViewReminderMessage.setVisibility(View.VISIBLE);
					txtViewReminderMessage.setText("Wi-Fi : "+placemessage.get(position));

				}
				else if(tasktypearray.get(position)==5){
					imagethumb.setImageDrawable(activity.getResources().getDrawable(R.drawable.lockiconforlist));
					txtViewReminderMessage.setVisibility(View.GONE);
					String str=placemessage.get(position);
					str=str.substring(str.indexOf("#")+1, str.length());
					String[] splitstr=str.split("#");
					LinearLayout mainLayout = (LinearLayout) convertView.findViewById(R.id.iconlayoutforadapter);
					hzscrollview.setVisibility(View.VISIBLE);
					for(int i=0;i<splitstr.length;i++){
						final ImageView imageView = new ImageView (activity);
						imageView.setId(i);
						try {
//							System.out.println("at split>>>"+splitstr.length);
							Drawable icon1 = activity.getPackageManager().getApplicationIcon(splitstr[i]);
							LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LAYOUT_WIDTH,LAYOUT_HEIGHT);
							parms.setMargins(LEFTMARGIN, 0, 0, 0);
							imageView.setLayoutParams(parms);
							imageView.setBackground(icon1);
							mainLayout.addView(imageView);
							mainLayout.setOnLongClickListener(new OnItemClickListener(position));
						} catch (NameNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
				}}
				else if(tasktypearray.get(position)==6){
					hzscrollview.setVisibility(View.GONE);
					txtViewReminderMessage = (TextView) convertView.findViewById(R.id.tv_loco);
					imagethumb.setImageDrawable(activity.getResources().getDrawable(R.drawable.remindericonforlist));
					txtViewReminderMessage.setVisibility(View.VISIBLE);
					txtViewReminderMessage.setText("Reminder : "+placemessage.get(position));

				}
				else if (tasktypearray.get(position)==7) {
					LinearLayout mainLayout = (LinearLayout) convertView.findViewById(R.id.iconlayoutforadapter);
					hzscrollview.setVisibility(View.VISIBLE);
					txtViewReminderMessage.setVisibility(View.GONE);
					imagethumb.setImageDrawable(activity.getResources().getDrawable(R.drawable.wallpaperforlist));
					try {
						bitmap=null;
						final BitmapFactory.Options options = new BitmapFactory.Options();
						options.inTempStorage = new byte[16*1024];
						options.inSampleSize = 8;
						bitmap = BitmapFactory.decodeFile(placemessage.get(position),options);
						System.gc();
						if(bitmap!=null){
							final ImageView imageView = new ImageView (activity);
								LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(layout_thumb_width,layout_thumb_height);
								parms.setMargins(LEFTMARGIN, 0, 0, 0);
								imageView.setLayoutParams(parms);
								Bitmap btmp=Bitmap.createScaledBitmap(bitmap,layout_thumb_width,layout_thumb_height, false);  
								imageView.setBackgroundResource(0);
								imageView.setImageBitmap(btmp);
								mainLayout.addView(imageView);
								mainLayout.setOnLongClickListener(new OnItemClickListener(position));
						}
					} catch (OutOfMemoryError e) {
						// TODO Auto-generated catch block
//						System.out.println("inside catch>>>>>");
						System.gc();
						e.printStackTrace();
					}
					catch (Exception e1) {
						// TODO Auto-generated catch block
//						System.out.println("inside catch>>>>>");
						e1.printStackTrace();
					}

					
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("exception at view>>>>" + e);
			}
//		} 
		if(position%2==1)
		{
			convertView.setBackgroundColor(colorGreen);
		}
		else{
			convertView.setBackgroundColor(colorRed);
		}
		txtViewReninderTilale.setText(listReminderName.get(position));
		editbulb.setOnClickListener(new OnItemClickListenerbulb(position)); 
		convertView.setOnLongClickListener(new OnItemClickListener(position));
		convertView.setOnClickListener(new OnItemClickListener(position));

		return convertView;
	}

	private class OnItemClickListener implements OnClickListener, OnLongClickListener {
		private int mPosition;
		OnItemClickListener(int position) {
			mPosition = position;
		}
		@Override
		public void onClick(View v) {
			FirstActivity sct = (FirstActivity) activity;
			sct.onItemclick(mPosition);
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
		OnItemClickListenerbulb(int position) {
			mPosition = position;
		}
		@Override
		public void onClick(View v) {
			FirstActivity sct = (FirstActivity) activity;
			sct.onItembulbclick(mPosition);
		}
		
		
	}
	
}
