package com.mobirizer.smartinvitationpro;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends Activity  {

	public static MainActivity mainInstance;
	public static Context mainContext;
	private ImageView opt_btn,tvg,tvf,tvt;
	private Button wedding,bday,anniversary,party,engagement,festival;
	private String fontPath = "fonts/SCRIPTBL.TTF";
	private TextView tv_title,tv1,tv2,tv4;
	Typeface tf ;
	Point p;

   @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.menu_screen);
       
        registerWidget();
		setClickListener();
		
		 tf = Typeface.createFromAsset(getAssets(), fontPath);
		 wedding.setTypeface(tf);
		 bday.setTypeface(tf);
		 festival.setTypeface(tf);
		 party.setTypeface(tf);
		 engagement.setTypeface(tf);
		 anniversary.setTypeface(tf);
		  tv_title.setTypeface(tf);
		  tv_title.setTextSize(30.0f);
		  
		  opt_btn.setOnClickListener(new OnClickListener() {
		@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (p != null)
			        showPopup(MainActivity.this, p);
			}
	    });
	}

	private void registerWidget() {
		wedding = (Button) findViewById(R.id.img_wed);
		bday = (Button) findViewById(R.id.img_bday);
		festival = (Button) findViewById(R.id.img_fest);
		party = (Button) findViewById(R.id.img_party);
		engagement = (Button) findViewById(R.id.img_eng);
		anniversary = (Button) findViewById(R.id.img_ann);
		opt_btn = (ImageView) findViewById(R.id.option_btn); 
		tv_title =(TextView)findViewById(R.id.tv_title);
		

		
	}
	
	
	private void setClickListener(){
		wedding.setOnClickListener(mClick);
		bday.setOnClickListener(mClick);
		festival.setOnClickListener(mClick);
		party.setOnClickListener(mClick);
		engagement.setOnClickListener(mClick);
		anniversary.setOnClickListener(mClick);
	}

	OnClickListener mClick = new OnClickListener() {
		Intent mIntent;
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.img_wed:	
					mIntent = new Intent(MainActivity.this,CardGridActivity.class);
					mIntent.putExtra("cat_id", "1");
					startActivity(mIntent);
					MainActivity.this.finish();
				break;
			case R.id.img_bday:
						mIntent = new Intent(MainActivity.this,CardGridActivity.class);
						mIntent.putExtra("cat_id", "2");
						startActivity(mIntent);
						MainActivity.this.finish();
					break;
			case R.id.img_ann:
						mIntent = new Intent(MainActivity.this,CardGridActivity.class);
						mIntent.putExtra("cat_id", "3");
						startActivity(mIntent);
						MainActivity.this.finish();
				break;
			case R.id.img_eng:
						mIntent = new Intent(MainActivity.this,CardGridActivity.class);
						mIntent.putExtra("cat_id", "4");
						startActivity(mIntent);
						MainActivity.this.finish();
				break;
			case R.id.img_fest:		
						mIntent = new Intent(MainActivity.this,CardGridActivity.class);
						mIntent.putExtra("cat_id", "5");
						startActivity(mIntent);
						MainActivity.this.finish();
				break;
			case R.id.img_party:
						mIntent = new Intent(MainActivity.this,CardGridActivity.class);
						mIntent.putExtra("cat_id", "6");
						startActivity(mIntent);
						MainActivity.this.finish();
				break;
			default:	
				break;
			}
		}
	};	
	
	
		public void onWindowFocusChanged(boolean hasFocus) {
		 
		   int[] location = new int[2];
		   ImageView button = (ImageView) findViewById(R.id.option_btn);
		 
		   // Get the x, y location and store it in the location[] array
		   // location[0] = x, location[1] = y.
		   button.getLocationOnScreen(location);
		 
		   //Initialize the Point with x, and y positions
		   p = new Point();
		   p.x = location[0];
		   p.y = location[1];
		}
		 
		// The method that displays the popup.
		private void showPopup(final Activity context, Point p) {
		   int popupWidth = 330;
		   int popupHeight = 380;
		 
		   // Inflate the popup_layout.xml
		   LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
		   LayoutInflater layoutInflater = (LayoutInflater) context
		     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   View layout = layoutInflater.inflate(R.layout.popup_menu, viewGroup);
		   
		   //cancel = (Button) v.findViewById(R.id.cancel);  
		    tv1 = (TextView) layout.findViewById(R.id.tv_aboutcomp);
		    tv1.setTypeface(tf);
		    tv2 = (TextView) layout.findViewById(R.id.tv_aboutprod);
			tv2.setTypeface(tf);
		    tv4 = (TextView) layout.findViewById(R.id.tv_more);
			tv4.setTypeface(tf);
			
			tvg=(ImageView) layout.findViewById(R.id.gplus);
			tvf=(ImageView) layout.findViewById(R.id.facebook);
			tvt=(ImageView) layout.findViewById(R.id.twitter);
				
		   // Creating the PopupWindow
		   final PopupWindow popup = new PopupWindow(context);
		   popup.setContentView(layout);
		   popup.setWidth(popupWidth);
		   popup.setHeight(popupHeight);
		   popup.setFocusable(true);
		   
		    tv1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent(MainActivity.this, AboutCompanyActivity.class);
					startActivity(mIntent);
					popup.dismiss();
				}
				   
			   });
		  
		   tv2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent(MainActivity.this, AboutProductActivity.class);
					startActivity(mIntent);
					popup.dismiss();
				}
				   
			   });
		 
		   tvg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/108790281049035345752/about"));
					startActivity(mIntent);
					popup.dismiss();
				}
		   });
		   tvf.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mobiRizer"));
					startActivity(mIntent);
					popup.dismiss();
				}
		   });
		   tvt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Mobirizer"));
					startActivity(mIntent);
					popup.dismiss();
				}
		   });

		   tv4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Mobirizer"));
					startActivity(mIntent);
					popup.dismiss();
				}
		   });
		   // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
		   int OFFSET_X = 87;
		   int OFFSET_Y = 87;
		 
		   // Clear the default translucent background
		   popup.setBackgroundDrawable(new BitmapDrawable());
		 
		   // Displaying the popup at the specified location, + offsets.
		   popup.showAtLocation(layout, Gravity.TOP, p.x + OFFSET_X, p.y + OFFSET_Y);
		  }	
		
		
		  @Override
		  public void onResume() {
		    super.onResume();
		  }

		  @Override
		  public void onPause() {
		    super.onPause();
		  }
		  
		  /** Called before the activity is destroyed. */
		  @Override
		  public void onDestroy() {
		    super.onDestroy();
		  }
		  
	}



