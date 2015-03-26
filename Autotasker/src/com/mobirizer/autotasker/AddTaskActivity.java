package com.mobirizer.autotasker;
import gps.ConnectionDetector;
import gps.GPSTracker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;
import com.mobirizer.utility.UtilityHandler;
//called from first activity... provide all detail related entry task to database
@SuppressLint("NewApi")
public class AddTaskActivity extends Activity{
	private static final int IMAGE_PICKER_RESULT = 1,CONTACT_PICKER_RESULT = 2,LOCATION_PICKER_RESULT=3,PACKAGE_PICKER_RESULT=4;
	private final int LAYOUT_WIDTH=100,LAYOUT_HEIGHT=100,LEFTMARGIN=10;
	private String messagetextvalue="",phoneno="",phoneno_name="",filePath,apppackagename="",tasknametext="",tempstate="";
	public static Bitmap bmp;
	private ImageView tasknamebtn,action_chooser_ok,locationpin,actionmenu,defaultwallicon,taskbtn_resolution,taskbtn_bluetooth,taskbtn_sound,taskbtn_message,taskbtn_wifi,taskbtn_lock,taskbtn_reminder,taskbtn_wallpaper;
	private TextView locationvalue,action_name,newtask,taskname;
	private String valueLocation="", LOgiLatiValue="",repeatvalue="";
	private int valueradius,updateid,seekbarwidth=450;
	private double latitude,longitude;
	private HorizontalScrollView hzscrollview;
	private byte tasktype=-1;
	private ToggleButton wifitoggle;
	final Context context = this;
	private float[] outR = new float[] {6,6,6,6,6,6,6,6};
	private SeekBar seekBarlaout;
	//for map
//	private Typeface customtf;
	private GPSTracker gps;
	private ConnectionDetector connectionDetector;
	private SharedPreferences sharedPreferences;
	private SavePreferences savePreferences;
	private boolean addcheck,taskcheck;
	private ProgressDialog pDialog; 
	/** The view to show the ad. */
	private AdView adView;
	private LinearLayout mainLayout;
	private RelativeLayout relativeLayoutsettingaction,relativeLayoutsetwall;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_addtask);
//		gps = new GPSTracker(AddTaskActivity.this);
		connectionDetector=new ConnectionDetector(getApplicationContext());
		savePreferences = new SavePreferences();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AddTaskActivity.this);
		DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		if(width<600){
			seekbarwidth=300;
		}
		initialiseView();
		initialiseValue();
		setbuttonClicklistner();
		//call map screen from click map icon 
		tasknamebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setActionmenuInvisible();
				setTaskName(v);
			}
		});
		locationpin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setActionmenuInvisible();
				callMappage();
			}
		});
		actionmenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				setSeekbarInvisibile();
				setActionMenuVisibility();
//				defaultwallicon.setImageBitmap(null);
			}
		});
		action_chooser_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ((checkdistanceForEntry(latitude, longitude)) != false && (ChecktasktypeToDataBase(tasktype)!=false) && LocationActivity.dbinsertupdate==false) {
					ToastMessage("Same Task Already exits for near location");
				}
				else {
				setActionmenuInvisible();
//				setSeekbarInvisibile();
				savevalueinDatabase();
			}}
		});
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(getResources().getString(R.string.Banner_ad_id));
		LinearLayout layout = (LinearLayout) findViewById(R.id.add_layout);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}
	
	OnClickListener mclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.imageViewcontrast:
				tasktype=0;
				setActionmenuInvisible();
				setWalliconInvisible();
				setSeekbarInvisibile();
				setBrightness();
				break;
			case R.id.imageViewbluetooth:
				tasktype=1;
				setActionmenuInvisible();
				setWalliconInvisible();
				setSeekbarInvisibile();
				setBluetoothstate();
				break;
			case R.id.imageViewsound:
				tasktype=2;
				setActionmenuInvisible();
				setWalliconInvisible();
				setSeekbarInvisibile();
				setSoundLevel();
				break;
			case R.id.imageViewmessage:
				tasktype=3;
				setActionmenuInvisible();
				setWalliconInvisible();
				setSeekbarInvisibile();
				setMessageBox(v);
				break;
			case R.id.imageViewwifi:
				tasktype=4;
				setActionmenuInvisible();
				setWalliconInvisible();
				setSeekbarInvisibile();
				setWifistate();
				break;
			case R.id.imageViewlock:
				tasktype=5;
				setActionmenuInvisible();
				setWalliconInvisible();
				setSeekbarInvisibile();
				Intent intent =new Intent(AddTaskActivity.this,ApplicationListforlockActivity.class);
//				startActivity(intent);
				intent.putExtra("Message", messagetextvalue);
				startActivityForResult(intent, PACKAGE_PICKER_RESULT);
				break;
			case R.id.imageViewreminder:
				tasktype=6;
				setActionmenuInvisible();
				setWalliconInvisible();
				setSeekbarInvisibile();
				setReminderMessage(v);
				break;
			case R.id.imageViewwallpaper:
				tasktype=7;
				setSeekbarInvisibile();
				setActionmenuInvisible();
				setWalliconInvisible();
				chooseImage(v);
				break;
			default:
				break;
			}
		}
	};
	protected void savevalueinDatabase() {
		// TODO Auto-generated method stub
//		System.out.println("555555555555task type at save is"+tasktype);
//		messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0
		if(!(messagetextvalue.length()>0) && !(LOgiLatiValue.length()>0) && !(valueLocation.length()>0) && !(tasknametext.length()>0)){
			ToastMessage("Please insert all feilds");	
		}
		else if((!(messagetextvalue.length()>0) && !(valueLocation.length()>0)) || (!(valueLocation.length()>0) && !(tasknametext.length()>0))||(!(messagetextvalue.length()>0) && !(tasknametext.length()>0))){
			ToastMessage("Please insert all feilds");	
		}
		else if(!(tasknametext.length()>0)){
			ToastMessage("Please insert Task Name");
		}
		else if (!(valueLocation.length()>0)) {
			ToastMessage("Please select any location");
		}
		else if(tasktype==-1 ){
			ToastMessage("Please select any task");	
		}
		else{
		if(tasktype==0){
			saveValueindatabaseTasktypezero();
		}
		else if (tasktype==1) {
			saveValueindatabaseTasktypeone();
		}
		else if (tasktype==2) {
			saveValueindatabaseTasktypetwo();
		}
		else if (tasktype==3) {
			saveValueindatabaseTasktypethree();
		}
		else if (tasktype==4) {
			saveValueindatabaseTasktypefour();
		}
		else if (tasktype==5) {
			saveValueindatabaseTasktypefive();
		}
		else if (tasktype==6) {
			saveValueindatabaseTasktypesix();
		}
		else if(tasktype==7){
			saveValueindatabaseTasktypeseven();
		}
		}

	}
//	private boolean CheckStringNameToDataBase(String string) {
//		try {
//			DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
//			List<DbLocationObject> value = db.getAllFreqLocationDataObject();
//			for (DbLocationObject valueName : value) {
//				String placename = valueName.getLocatonType().toString().trim();
//				if (placename.equalsIgnoreCase(string)) {
//					addcheck = true;
//					return addcheck;
//				} 
//				else {
//					addcheck = false;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			e.getLocalizedMessage();
//		}
//		return addcheck;
//	}
	private boolean checkdistanceForEntry(double latitude2, double longitude2) {

		try {
			DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
			List<DbLocationObject> value = db.getAllFreqLocationDataObject();
			for (DbLocationObject valueName : value) {
				double templat=Double.parseDouble(valueName.getLocationLat().toString());
				double templng=Double.parseDouble(valueName.getLocationLong().toString());
				double distance = UtilityHandler.distFrom(templat, templng,
						latitude2, longitude2);
				if (distance > 0 && distance < 200) {
					addcheck = true;
					return addcheck;
				} else {
					addcheck = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getLocalizedMessage();
		}
		return addcheck;
	}
	private boolean ChecktasktypeToDataBase(int tasktype) {
		try {
			DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
			List<DbLocationObject> value = db.getAllFreqLocationDataObject();
			for (DbLocationObject valueName : value) {
				int taskno = valueName.getTasktype();
				if (taskno==tasktype) {
					taskcheck = true;
					return taskcheck;
				} 
				else {
					taskcheck = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getLocalizedMessage();
		}
		return taskcheck;
	}
	private void fetchcontact(View view) {
		final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
		Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
	    startActivityForResult(intentPickContact, CONTACT_PICKER_RESULT);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if(resultCode == RESULT_OK){
			  seekbarLayoutfalse();
		   if(requestCode == CONTACT_PICKER_RESULT){
		    Uri returnUri = data.getData();
		    Cursor cursor = getContentResolver().query(returnUri, null, null, null, null);
		    
		    if(cursor.moveToNext()){
		     int columnIndex_ID = cursor.getColumnIndex(ContactsContract.Contacts._ID);
		     String contactID = cursor.getString(columnIndex_ID);
		     
		     int columnIndex_HASPHONENUMBER = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		     String stringHasPhoneNumber = cursor.getString(columnIndex_HASPHONENUMBER);
		     
		     if(stringHasPhoneNumber.equalsIgnoreCase("1")){
		      Cursor cursorNum = getContentResolver().query(
		        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
		        null, 
		        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactID, 
		        null, 
		        null);
		      //Get the first phone number
		      if(cursorNum.moveToNext()){
		       int columnIndex_number = cursorNum.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
		       String columnIndex_name= cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		       phoneno = cursorNum.getString(columnIndex_number);
		       phoneno_name=columnIndex_name;
		       phoneno=phoneno_name+"$"+phoneno;
		       if(phoneno!=null && phoneno.length()>0){
		    	   action_name.setText("Message to "+phoneno_name);
		   		}
		      }
		     }
		    }else{
		     Toast.makeText(getApplicationContext(), "NO data!", Toast.LENGTH_SHORT).show();
		    }
		   }
		   else if (requestCode == IMAGE_PICKER_RESULT) {
			   if(data!=null){
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				filePath = cursor.getString(columnIndex);
				System.out.println("1111filepath>>>>>columnindex"+filePath);
				messagetextvalue=filePath;
				cursor.close();
				if(filePath.startsWith("https:")){
//				ToastMessage("Please select image from Gallery.");
				new LoadImage().execute(filePath);
				}
				try {
					savePreferences.SavePreferences(AddTaskActivity.this, "filepath", filePath);
					bmp=get_Picture_bitmap(filePath);
					System.gc();
				}catch (OutOfMemoryError e1) {
					// TODO Auto-generated catch block
					System.gc();
					e1.printStackTrace();
				} 
				catch (NullPointerException ne) {
					// TODO Auto-generated catch block
					ne.printStackTrace();
				}
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
				int screenWidthInPix = displayMetrics.widthPixels/2;
				int screenHeightInPix = displayMetrics.heightPixels/3;
				LayoutParams params = (LayoutParams) relativeLayoutsetwall.getLayoutParams();
				params.height = screenHeightInPix;
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(defaultwallicon.getLayoutParams());
				lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				try {
					Bitmap btmp=null;
					btmp=scaleBitmap(bmp, screenWidthInPix, screenHeightInPix);
					defaultwallicon.setBackgroundResource(0);
					defaultwallicon.setImageBitmap(btmp);
					defaultwallicon.setLayoutParams(lp);
					action_name.setText("Wallpaper");
					System.gc();
					if (bmp != null) {
						bmp.recycle();
						bmp = null;
						
					}
				} catch (OutOfMemoryError e) {
					// TODO Auto-generated catch block
					System.gc();
					e.printStackTrace();
				}
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		   }
		}
		   else if (requestCode == LOCATION_PICKER_RESULT) {
			   if(data!=null){
			   valueLocation=data.getExtras().getString("location");
//				messagetextvalue=data.getExtras().getString("Message");
			   latitude=data.getExtras().getDouble("lats");
			   longitude=data.getExtras().getDouble("longs");
				LOgiLatiValue=data.getExtras().getString("arrayname");
				valueradius=data.getExtras().getInt("radius");
				locationvalue.setText(valueLocation);

		   }	
		  }
		   else if (requestCode == PACKAGE_PICKER_RESULT) {
		   if(data!=null){
			   messagetextvalue=data.getExtras().getString("Package"); 
			   if(messagetextvalue!=null && messagetextvalue.length()>2){
					String temp_pkg=messagetextvalue;
					temp_pkg=temp_pkg.substring(temp_pkg.indexOf("#")+1, temp_pkg.length());
					if(temp_pkg.startsWith("#")){
						temp_pkg=temp_pkg.substring(temp_pkg.indexOf("#")+1, temp_pkg.length());	
					}
					String[] splitstr=temp_pkg.split("#");
					hzscrollview.setVisibility(View.VISIBLE);
					//removing child view before adding
					if(mainLayout!=null && mainLayout.getChildCount()>0){
						mainLayout.removeAllViews();
					}
					for(int i=0;i<splitstr.length;i++){
						final ImageView imageView = new ImageView (this);
						imageView.setId(i);
						try {
						Drawable iconi = getApplicationContext().getPackageManager().getApplicationIcon(splitstr[i]);
						LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LAYOUT_WIDTH,LAYOUT_HEIGHT);
						parms.setMargins(LEFTMARGIN, 0, 0, 0);
						imageView.setLayoutParams(parms);
						imageView.setBackground(iconi);
				        mainLayout.addView(imageView);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		action_name.setVisibility(View.GONE); 
		   }
		  }
		   }
	}
		  else{
			  action_name.setText("Action");
			  System.out.println("1111on activity result?>>>>>");
			  tasktype=-1;
		  }  
		  }
	//value to set after provide name at getedittextactivity
	private void initialiseValue() {
		if(tasknametext!=null && tasknametext!=""){
			taskname.setText(tasknametext);
		}
		if(LocationActivity.dbinsertupdate==true){
			newtask.setText("Edit Task");
		Intent getdata=getIntent();
		if(getdata!=null){
			valueLocation=getdata.getExtras().getString("location");
			messagetextvalue=getdata.getExtras().getString("Message");
			LOgiLatiValue=getdata.getExtras().getString("arrayname");
			valueradius=getdata.getExtras().getInt("radius");
			updateid=getdata.getExtras().getInt("updateid");
			tasktype=(byte) getdata.getExtras().getInt("taskid");
			tasknametext=getdata.getExtras().getString("taskname");
//			System.out.println("1111at edit>>>>>>"+updateid+valueLocation);
			taskname.setText(tasknametext);
			locationvalue.setText(valueLocation);	
		}}
		if(tasktype==5){
			if(LocationActivity.dbinsertupdate==false){
//				String str=sharedPreferences.getString("completeaddress","" );
				apppackagename=sharedPreferences.getString("getapplist", apppackagename);
				if(apppackagename!=null && apppackagename.length()>2){
					String temp_pkg=apppackagename;
					temp_pkg=temp_pkg.substring(temp_pkg.indexOf("#")+1, temp_pkg.length());
//					System.out.println("temp_pkg is>>>>>>>"+temp_pkg);
					String[] splitstr=temp_pkg.split("#");
//					LinearLayout mainLayout = (LinearLayout) findViewById(R.id.icon_layout);
					hzscrollview.setVisibility(View.VISIBLE);
					for(int i=0;i<splitstr.length;i++){
						final ImageView imageView = new ImageView (this);
						imageView.setId(i);
						try {
						Drawable iconi = getApplicationContext().getPackageManager().getApplicationIcon(splitstr[i]);
						LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LAYOUT_WIDTH,LAYOUT_HEIGHT);
						parms.setMargins(LEFTMARGIN, 0, 0, 0);
						imageView.setLayoutParams(parms);
						imageView.setBackground(iconi);
				        mainLayout.addView(imageView);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		action_name.setVisibility(View.GONE); 
				
				}
				}
				if(LocationActivity.dbinsertupdate==true){
//				if(messagetextvalue!=null && messagetextvalue.length()>2){
					String temp_pkg=messagetextvalue;
					temp_pkg=temp_pkg.substring(temp_pkg.indexOf("#")+1, temp_pkg.length());
//					System.out.println("temp_pkg is>>>>>>>"+temp_pkg);
					String[] splitstr=temp_pkg.split("#");
//					LinearLayout mainLayout = (LinearLayout) findViewById(R.id.icon_layout);
					hzscrollview.setVisibility(View.VISIBLE);
					for(int i=0;i<splitstr.length;i++){
						final ImageView imageView = new ImageView (this);
						imageView.setId(i);
						try {
						Drawable iconi = getApplicationContext().getPackageManager().getApplicationIcon(splitstr[i]);
						LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LAYOUT_WIDTH,LAYOUT_HEIGHT);
						parms.setMargins(LEFTMARGIN, 0, 0, 0);
						imageView.setLayoutParams(parms);
						imageView.setBackground(iconi);
				        mainLayout.addView(imageView);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		action_name.setVisibility(View.GONE); 
		   }	
				}
		else if (tasktype==7) {

			try {
				filePath=messagetextvalue;
				bmp=get_Picture_bitmap(filePath);
				System.gc();
			}catch (OutOfMemoryError e1) {
				// TODO Auto-generated catch block
				System.gc();
				e1.printStackTrace();
			} 
			catch (NullPointerException ne) {
				// TODO Auto-generated catch block
				ne.printStackTrace();
				ToastMessage("Please select image from Gallery.");
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			initialiseValue();
			DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
//			int screenWidthInPix = displayMetrics.widthPixels/2+displayMetrics.widthPixels/4;
//			int screenHeightInPix = displayMetrics.heightPixels/2+displayMetrics.heightPixels/25-40;
			int screenWidthInPix = displayMetrics.widthPixels/2;
			int screenHeightInPix = displayMetrics.heightPixels/3;
			LayoutParams params = (LayoutParams) relativeLayoutsetwall.getLayoutParams();
			params.height = screenHeightInPix;
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(defaultwallicon.getLayoutParams());
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			try {
				Bitmap btmp=null;
				btmp=scaleBitmap(bmp, screenWidthInPix, screenHeightInPix);
				defaultwallicon.setBackgroundResource(0);
				defaultwallicon.setImageBitmap(btmp);
				defaultwallicon.setLayoutParams(lp);
				action_name.setText("Wallpaper");
				System.gc();
				if (bmp != null) {
					bmp.recycle();
					bmp = null;
					
				}
			} catch (OutOfMemoryError e) {
				// TODO Auto-generated catch block
				System.gc();
				e.printStackTrace();
			}
			catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(tasktype==3){
		if(LocationActivity.dbinsertupdate==true){
//			if(phoneno!=null && phoneno.length()>0){
//			newtask.setText("Edit Task");
			Intent location = getIntent();
			if (location != null) {
				valueLocation = location.getExtras().getString("location");
				LOgiLatiValue = location.getExtras().getString("arrayname");
				messagetextvalue = location.getExtras().getString("Message");
				valueradius=location.getExtras().getInt("radius");
				repeatvalue=location.getExtras().getString("repeat");
//				if (valueLocation != null) {
//					locationvalue.setText(valueLocation);
//				}
				if (messagetextvalue .length()>0) {
//					edtMessage.setText(ValueMessage);
					action_name.setText("Message send to "+messagetextvalue.substring(messagetextvalue.indexOf('*')+1, messagetextvalue.indexOf('$')));
//					messagevalue.setText(ValueMessage.substring(0, ValueMessage.indexOf('*')));
					String ValueMessage=messagetextvalue;
					messagetextvalue=ValueMessage.substring(0, ValueMessage.indexOf('*'));
					phoneno=ValueMessage.substring(ValueMessage.indexOf('*')+1,ValueMessage.length());
				}
			}

		}else{
			if(phoneno!=null && phoneno.length()>0){
				action_name.setText("Message send to "+phoneno_name);
				}
		}
		
		}
		else if (tasktype==0||tasktype==2) {
			action_name.setVisibility(View.GONE);
			hzscrollview.setVisibility(View.VISIBLE);
			seekBarlaout.setVisibility(View.VISIBLE);
			if(tasktype==0){
			 seekBarlaout.setMax(250);
			 }
			else if (tasktype==2) {
				seekBarlaout.setMax(15);	
			}
			 seekBarlaout.setMinimumWidth(2);
	        float seekvalue=Float.parseFloat(messagetextvalue);
	        ShapeDrawable thumb = new ShapeDrawable(new RoundRectShape(outR, null, null));      
	        thumb.setIntrinsicHeight(50);
	        thumb.setIntrinsicWidth(0);
	        seekBarlaout.setThumb(thumb);
	        seekBarlaout.setProgress((int) seekvalue);
	        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(420,40);
	        parms.setMargins(80, 0,0, 0);
	        seekBarlaout.setEnabled(false);
	        seekBarlaout.setLayoutParams(parms);
			mainLayout.addView(seekBarlaout);	
		}
		else{
			gettaskName(tasktype);
			if (messagetextvalue != null) {
//				edtMessage.setText(ValueMessage);
				action_name.setText(taskItemName+messagetextvalue);
//				messagevalue.setText(ValueMessage.substring(0, ValueMessage.indexOf('*')));
//				messagetextvalue=ValueMessage.substring(0, ValueMessage.indexOf('*'));
//				phoneno=ValueMessage.substring(ValueMessage.indexOf('*')+1,ValueMessage.length());
			}
		}
	}
//inittialise all value from layout
	private void initialiseView() {
		// TODO Auto-generated method stub
//		messagevalue= (EditText) findViewById(R.id.message_detail);
		locationvalue=(TextView) findViewById(R.id.picklocation);
		action_name=(TextView) findViewById(R.id.actionname);
//		locationvalue.setTypeface(Customfont.getCustomFont(this));
//		action_name.setTypeface(Customfont.getCustomFont(this));
		tasknamebtn = (ImageView) findViewById(R.id.editmessage_task);
		locationpin=(ImageView)findViewById(R.id.locationpin);
		actionmenu = (ImageView) findViewById(R.id.actioniconfortask);
		defaultwallicon=(ImageView) findViewById(R.id.walliconfortask);
		newtask=(TextView) findViewById(R.id.newtask);
		taskname=(TextView) findViewById(R.id.task_name);
//		newtask.setTypeface(Customfont.getCustomFont(this));
//		taskname.setTypeface(Customfont.getCustomFont(this));
		hzscrollview=(HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		action_chooser_ok = (ImageView) findViewById(R.id.action_chooser_ok);
		relativeLayoutsettingaction=(RelativeLayout) findViewById(R.id.relativeLayoutsettingaction);
		relativeLayoutsetwall=(RelativeLayout) findViewById(R.id.relativeLayoutsetwall);
		taskbtn_resolution=(ImageView) findViewById(R.id.imageViewcontrast);
		taskbtn_bluetooth=(ImageView) findViewById(R.id.imageViewbluetooth);
		taskbtn_sound=(ImageView) findViewById(R.id.imageViewsound);
		taskbtn_message=(ImageView) findViewById(R.id.imageViewmessage);
		taskbtn_wifi=(ImageView) findViewById(R.id.imageViewwifi);
		taskbtn_lock=(ImageView) findViewById(R.id.imageViewlock);
		taskbtn_reminder=(ImageView) findViewById(R.id.imageViewreminder);
		taskbtn_wallpaper=(ImageView) findViewById(R.id.imageViewwallpaper);
		seekBarlaout= new SeekBar(context);
		mainLayout = (LinearLayout) findViewById(R.id.icon_layout);
	}
	private void setbuttonClicklistner(){
	taskbtn_resolution.setOnClickListener(mclick);
	taskbtn_bluetooth.setOnClickListener(mclick);
	taskbtn_sound.setOnClickListener(mclick);
	taskbtn_message.setOnClickListener(mclick);
	taskbtn_wifi.setOnClickListener(mclick);
	taskbtn_lock.setOnClickListener(mclick);
	taskbtn_reminder.setOnClickListener(mclick);
	taskbtn_wallpaper.setOnClickListener(mclick);
	}
	private void setActionMenuVisibility()
	{
		if(!relativeLayoutsettingaction.isShown())
			relativeLayoutsettingaction.setVisibility(View.VISIBLE);
		else
			relativeLayoutsettingaction.setVisibility(View.GONE);
	}
	private void setActionmenuInvisible(){
		if(relativeLayoutsettingaction.isShown()){
		relativeLayoutsettingaction.setVisibility(View.GONE);	
		}
	}
	private void setWalliconInvisible(){
		if(defaultwallicon.getDrawable()!=null){
			defaultwallicon.setImageBitmap(null);
			System.out.println("111111set wall invisible");
			action_name.setText("Action");
		}
	}
	protected void callMappage() {
		// TODO Auto-generated method stub
		if(connectionDetector.isConnectingToInternet()==true){
		// check if GPS enabled
			gps=null;
			gps = new GPSTracker(AddTaskActivity.this);
		if (gps.canGetLocation()) {
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			if (latitude == 0 || longitude == 0) {
				ToastMessage("No GPS Found");
			} else {
				Intent i = new Intent(AddTaskActivity.this,
						LocationActivity.class);
				if(LocationActivity.dbinsertupdate==true){
				i.putExtra("activity",1);
				i.putExtra("location", valueLocation);
//				i.putExtra("Message", messagetextvalue);
				i.putExtra("arrayname", LOgiLatiValue);
				i.putExtra("radius", valueradius);
				i.putExtra("repeat", repeatvalue);
				}
				else{
					i.putExtra("activity", 2);
					LOgiLatiValue=latitude+","+longitude;
					i.putExtra("arrayname", LOgiLatiValue);
				}
				startActivityForResult(i,LOCATION_PICKER_RESULT);
			}
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert(this);
		}
		}else{
			ToastMessage("Please check Internet connection");
		}	
	}
	private void ToastMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	protected void chooseImage(View v) {
		// TODO Auto-generated method stub
//		 setSeekbarInvisibile();
		Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, IMAGE_PICKER_RESULT);
	}
	public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
	    float originalWidth = bitmap.getWidth();
	    float originalHeight = bitmap.getHeight();
	    Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    Matrix m = new Matrix();
	    float scalex = wantedWidth/originalWidth;
	    float scaley = wantedHeight/originalHeight;
	    float xTranslation = 0.0f, yTranslation = (wantedHeight - originalHeight * scaley)/2.0f;
	    m.postTranslate(xTranslation, yTranslation);
	    m.preScale(scalex, scaley);
	    Paint paint = new Paint();
	    paint.setFilterBitmap(true);
	    canvas.drawBitmap(bitmap, m, paint);
	    return output;
	}
	public Bitmap get_Picture_bitmap(String imagePath) {
	    long size_file = getFileSize(new File(imagePath));
	    size_file = (size_file) / 1000;// in Kb now
	    int ample_size = 0;

	    if (size_file <= 250) {
	        ample_size = 0;

	    } else if (size_file > 251 && size_file < 1500) {
	        ample_size = 4;

	    } else if (size_file >= 1500 && size_file < 3000) {
	        ample_size = 8;

	    } else if (size_file >= 3000 && size_file <= 4500) {
	        ample_size = 12;

	    } else if (size_file >= 4500) {
	        ample_size = 16;

	    }
	    Bitmap bitmap = null;
	    BitmapFactory.Options bitoption = new BitmapFactory.Options();
	    bitoption.inSampleSize = ample_size;
	    Bitmap bitmapPhoto = BitmapFactory.decodeFile(imagePath, bitoption);
	    ExifInterface exif = null;
	    try {
	        exif = new ExifInterface(imagePath);
	    } catch (IOException e) {
	        // Auto-generated catch block
	        e.printStackTrace();
	    }
	    int orientation = exif
	            .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
	    Matrix matrix = new Matrix();

	    if ((orientation == 3)) {
	        matrix.postRotate(180);
	        bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
	                bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
	                true);

	    } else if (orientation == 6) {
	        matrix.postRotate(90);
	        bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
	                bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
	                true);

	    } else if (orientation == 8) {
	        matrix.postRotate(270);
	        bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
	                bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,
	                true);

	    } else {
	        matrix.postRotate(0);
	        bitmap = Bitmap.createBitmap(bitmapPhoto, 0, 0,
	                bitmapPhoto.getWidth(), bitmapPhoto.getHeight(), matrix,true);
	    }
	    return bitmap;
}
	public long getFileSize(final File file) {
	    if (file == null || !file.exists())
	        return 0;
	    if (!file.isDirectory())
	        return file.length();
	    final List<File> dirs = new LinkedList<File>();
	    dirs.add(file);
	    long result = 0;
	    while (!dirs.isEmpty()) {
	        final File dir = dirs.remove(0);
	        if (!dir.exists())
	            continue;
	        final File[] listFiles = dir.listFiles();
	        if (listFiles == null || listFiles.length == 0)
	            continue;
	        for (final File child : listFiles) {
	            result += child.length();
	            if (child.isDirectory())
	                dirs.add(child);
	        }
	    }
return result;
	}
	//methods to give task name for tasker
	protected void setTaskName(final View v) {
	// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.taskname_customdialog);
		final EditText input = (EditText) dialog.findViewById(R.id.edit_taskname);
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				tasknametext= input.getText().toString();
				System.out.println("text>>>>>11111>>>"+tasknametext);
				if(tasknametext.length()>0){
					taskname.setText(tasknametext);
					}
			}
		});
if(!dialog.isShowing()){
		dialog.show();
}
}
	//methods to provide smstext to use task sms functionality
	protected void setMessageBox(final View v) {
	// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.message_customdialog);
		final EditText input = (EditText) dialog.findViewById(R.id.edit_taskname);
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            	tasktype=-1;
            	action_name.setText("Action");
            }
        });
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				setSeekbarInvisibile();
				messagetextvalue= input.getText().toString();
				if(messagetextvalue.length()>0){
				fetchcontact(v);
				}
			}
		});

		if(!dialog.isShowing()){
			dialog.show();
	}
}
	private void setWifistate(){
		final Dialog dialog = new Dialog(context);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.wifi_customdialog);
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE); 
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
		wifitoggle=(ToggleButton) dialog.findViewById(R.id.togglewifi);
		if(!wifiManager.isWifiEnabled()){
			wifitoggle.setChecked(true);
			tempstate="Off";
		}
		else{
			wifitoggle.setChecked(false);	
			tempstate="On";
		}
		wifitoggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tempstate="Off";
				} else {
					tempstate="On";
				}
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            	tasktype=-1;
            	action_name.setText("Action");
            }
        });

//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                // TODO Auto-generated method stub
//            	System.out.println("111111111Back button pressed?");
//            	tasktype=-1;
//            }
//        });
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				setSeekbarInvisibile();
				if(tempstate.length()>0){
					messagetextvalue=tempstate;
					dialog.dismiss();
//				tasktype=4;
				action_name.setText("Wi-Fi : "+messagetextvalue);
				}
			}
		});

		if(!dialog.isShowing()){
			dialog.show();
	}
	}
	private void setBluetoothstate(){
		
		final Dialog dialog = new Dialog(context);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.bluetooth_customdialog);
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
		wifitoggle=(ToggleButton) dialog.findViewById(R.id.togglewifi);
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); 
		if(!mBluetoothAdapter.isEnabled()){
			wifitoggle.setChecked(true);
			tempstate="Off";
		}
		else {
			wifitoggle.setChecked(false);
			tempstate="On";
		}
		wifitoggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tempstate="Off";
				} else {
					tempstate="On";
				}

			}
		});
		
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            	tasktype=-1;
            	action_name.setText("Action");
            }
        });

//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                // TODO Auto-generated method stub
//            	tasktype=-1;
//            }
//        });
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				setSeekbarInvisibile();
				if(tempstate.length()>0){
					messagetextvalue=tempstate;
//					tasktype=1;
					dialog.dismiss();
				
				action_name.setText("Bluetooth : "+messagetextvalue);
				}
			}
		});
		if(!dialog.isShowing()){
			dialog.show();
	}
	}
	private void setBrightness(){
//		messagetextvalue="50 ";
		 float curBrightnessValue = 0;
		final Dialog dialog = new Dialog(context);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.resolution_customdialog);
		try {
		    curBrightnessValue=android.provider.Settings.System.getInt(
		    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
		    messagetextvalue=String.valueOf(curBrightnessValue);
		    System.out.println("11111brightness value>>>>>>>>>>>>>>"+messagetextvalue+curBrightnessValue);
		
		 SeekBar bar = (SeekBar)dialog.findViewById(R.id.seekbar);
		 bar.setProgress((int) curBrightnessValue);
	     bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				messagetextvalue=""+progress;
				
			}
		});
	     dialog.setCanceledOnTouchOutside(false);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

	            @Override
	            public void onCancel(DialogInterface dialog) {
	            	tasktype=-1;
	            	action_name.setText("Action");
	            }
	        });
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				setSeekbarInvisibile();
				action_name.setVisibility(View.GONE);
				hzscrollview.setVisibility(View.VISIBLE);
				seekBarlaout.setVisibility(View.VISIBLE);
				seekBarlaout.setMax((int)(Float.parseFloat(messagetextvalue)));
				seekBarlaout.setMinimumWidth(2);
		        float seekvalue=Float.parseFloat(messagetextvalue);
		        System.out.println("111111after selecting value?>>>>>>>>>"+messagetextvalue);
		        if(mainLayout!=null && mainLayout.getChildCount()>0){
					mainLayout.removeAllViews();
				}
		        ShapeDrawable thumb = new ShapeDrawable(new RoundRectShape(outR, null, null));      
		        thumb.setIntrinsicHeight(50);
		        thumb.setIntrinsicWidth(0);
		        seekBarlaout.setThumb(thumb);
		        seekBarlaout.setProgress((int) seekvalue);
//		        seekBarlaout.getProgressDrawable().setColorFilter(Color.GRAY, Mode.SRC_IN);
		        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(seekbarwidth,40);
		        parms.setMargins(40, 0,0, 0);
		        seekBarlaout.setEnabled(false);
		        seekBarlaout.setLayoutParams(parms);
				mainLayout.addView(seekBarlaout);
			}
		});
		} catch (SettingNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		catch (Exception ee) {
			// TODO: handle exception
		}
		if(!dialog.isShowing()){
			dialog.show();
	}
	}
	private void setSeekbarInvisibile(){
		System.out.println("55555inside seekinvisible");
		if(!action_name.isShown()){
		action_name.setVisibility(View.VISIBLE);
		}
		if(seekBarlaout.isShown()){
			System.out.println("6666inside seekinvisible");
			mainLayout.removeView(seekBarlaout);
			seekBarlaout.setVisibility(View.GONE);
		}
		if(hzscrollview.isShown()){
		hzscrollview.setVisibility(View.GONE);
		}
	}
	private void seekbarLayoutfalse(){
		if(seekBarlaout.isShown()){
			System.out.println("6666inside seekinvisible");
			mainLayout.removeView(seekBarlaout);
			seekBarlaout.setVisibility(View.GONE);
		}
		if(hzscrollview.isShown()){
		hzscrollview.setVisibility(View.GONE);
		}
	}
	private void setSoundLevel(){
		int currentsound=0;
		final Dialog dialog = new Dialog(context);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.sound_customdialog);
			currentsound=(int) ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).getStreamVolume(AudioManager.STREAM_RING);
			int maxVolume = (int) ((AudioManager) getSystemService(Context.AUDIO_SERVICE)).getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		    messagetextvalue=String.valueOf(currentsound);
		 SeekBar bar = (SeekBar)dialog.findViewById(R.id.seekbar);
		 bar.setProgress((int) currentsound);
		 bar.setMax(maxVolume);
	     bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
//				System.out.println("progress at seek>>>>>"+progress);
				messagetextvalue=""+progress;
				
			}
		});
	     dialog.setCanceledOnTouchOutside(false);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

	            @Override
	            public void onCancel(DialogInterface dialog) {
	            	tasktype=-1;
	            	action_name.setText("Action");
	            }
	        });
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				setSeekbarInvisibile();
				action_name.setVisibility(View.GONE);
				hzscrollview.setVisibility(View.VISIBLE);
				seekBarlaout.setVisibility(View.VISIBLE);
				seekBarlaout.setMinimumWidth(2);
		        float seekvalue=Float.parseFloat(messagetextvalue);
		        ShapeDrawable thumb = new ShapeDrawable(new RoundRectShape(outR, null, null));      
		        thumb.setIntrinsicHeight(50);
		        thumb.setIntrinsicWidth(0);
		        seekBarlaout.setThumb(thumb);
		        seekBarlaout.setMax(15);
		        seekBarlaout.setProgress((int) seekvalue);
//		        seekBarlaout.getProgressDrawable().setColorFilter(Color.GRAY, Mode.SRC_IN);
		        if(mainLayout!=null && mainLayout.getChildCount()>0){
					mainLayout.removeAllViews();
				}
		        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(seekbarwidth,40);
		        parms.setMargins(40, 0,0, 0);
				seekBarlaout.setLayoutParams(parms);
				mainLayout.addView(seekBarlaout);
			}
		});

		if(!dialog.isShowing()){
			dialog.show();
	}
	}
	protected void setReminderMessage(final View v) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.remminder_customdialog);
		final EditText input = (EditText) dialog.findViewById(R.id.edit_taskname);
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButton);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				setSeekbarInvisibile();
//				action_name.setText("Wi-Fi : "+messagetextvalue);
				messagetextvalue= input.getText().toString();
				if(messagetextvalue.length()>0){
				action_name.setText("Reminder :"+messagetextvalue);
				}
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            	tasktype=-1;
            	action_name.setText("Action");
            }
        });
		if(!dialog.isShowing()){
			dialog.show();
	}
	}
	private String taskItemName="Action";
	private String gettaskName(byte tasktype){
//		if(tasktype==0){
//			taskItemName="Resolution : ";
//		}
		if(tasktype==1){
			taskItemName="Bluetooth : ";
		}
//		else if(tasktype==2){
//			taskItemName="Sound : ";
//		}
		else if(tasktype==4){
			taskItemName="Wi-Fi : ";
		}
		
		else if(tasktype==6){
			taskItemName="Reminder : ";
		}
		
		
		return taskItemName;
		
	}
	public void hideKeyboard(View view) {
	    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	private void saveValueindatabaseTasktypezero(){

		if(LocationActivity.dbinsertupdate==false){
			if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0){
			// adding to database
			String arraypass = LOgiLatiValue;
			String[] Location = arraypass.split(",");
			DbLocationObject locationObj = new DbLocationObject();
			locationObj.setLocationLat(Location[0]);
			locationObj.setLocationLong(Location[1]);
			locationObj.setLocationType(valueLocation);
			locationObj.setLocationProvider("GPS");
			locationObj.setRepeate("1");
			locationObj.setMessage(messagetextvalue);
			locationObj.setMapradius(valueradius);
			locationObj.setTasktype(tasktype);
			locationObj.setTaskname(tasknametext);
			LocationActivity.dbinsertupdate=false;
			DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
			db.insertFreqLocationDataObject(locationObj);
			finish();
			}
			else{
				ToastMessage("Please insert all feild");
			}}
			if(LocationActivity.dbinsertupdate==true){
				if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0){
					String arraypass = LOgiLatiValue;
					String[] Location = arraypass.split(",");
				DbLocationObject locationObj = new DbLocationObject();
				locationObj.setLocationLat(Location[0]);
				locationObj.setLocationLong(Location[1]);
				locationObj.setLocationType(valueLocation);
				locationObj.setLocationProvider("GPS");
				locationObj.setRepeate("1");
				locationObj.setId(updateid);
				locationObj.getId();
				locationObj.setMessage(messagetextvalue);
				locationObj.setMapradius(valueradius);
				locationObj.setTasktype(tasktype);
				locationObj.setTaskname(tasknametext);
				// insert data to data base
				DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
				db.updateFreqLocationDataObject(locationObj);
				LocationActivity.dbinsertupdate=false;
				finish();
				}
				else{
					ToastMessage("Please insert all feild");
				}}		
		
	
	}
	private void saveValueindatabaseTasktypeone(){

		if(LocationActivity.dbinsertupdate==false){
			if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0){
			// adding to database
				String arraypass = LOgiLatiValue;
				String[] Location = arraypass.split(",");
			DbLocationObject locationObj = new DbLocationObject();
			locationObj.setLocationLat(Location[0]);
			locationObj.setLocationLong(Location[1]);
			locationObj.setLocationType(valueLocation);
			locationObj.setLocationProvider("GPS");
			locationObj.setRepeate("1");
			locationObj.setMessage(messagetextvalue);
			locationObj.setMapradius(valueradius);
			locationObj.setTasktype(tasktype);
			locationObj.setTaskname(tasknametext);
			DataSourceHandler db = DataSourceHandler
					.getInstance(getApplicationContext());
			db.insertFreqLocationDataObject(locationObj);
			LocationActivity.dbinsertupdate=false;
			finish();
			}
			else{
				ToastMessage("Please insert all feild");
			}}
			if(LocationActivity.dbinsertupdate==true){
				if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0){
					String arraypass = LOgiLatiValue;
					String[] Location = arraypass.split(",");
				DbLocationObject locationObj = new DbLocationObject();
				locationObj.setLocationLat(Location[0]);
				locationObj.setLocationLong(Location[1]);
				locationObj.setLocationType(valueLocation);
				locationObj.setLocationProvider("GPS");
				locationObj.setRepeate("1");
				locationObj.setId(updateid);
				locationObj.getId();
				String locomsgno=messagetextvalue;
				locationObj.setMessage(locomsgno);
				locationObj.setMapradius(valueradius);
				locationObj.setTasktype(tasktype);
				locationObj.setTaskname(tasknametext);
				DataSourceHandler db = DataSourceHandler
						.getInstance(getApplicationContext());
				db.updateFreqLocationDataObject(locationObj);
				LocationActivity.dbinsertupdate=false;
				finish();
				}
				else{
					ToastMessage("Please insert all feild");
				}}		
		
	}
	private void saveValueindatabaseTasktypetwo(){

		if(LocationActivity.dbinsertupdate==false){
			if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0){
			// adding to database
				String arraypass = LOgiLatiValue;
				String[] Location = arraypass.split(",");
			DbLocationObject locationObj = new DbLocationObject();
			locationObj.setLocationLat(Location[0]);
			locationObj.setLocationLong(Location[1]);
			locationObj.setLocationType(valueLocation);
			locationObj.setLocationProvider("GPS");
			locationObj.setRepeate("1");
			locationObj.setMessage(messagetextvalue);
			locationObj.setMapradius(valueradius);
			locationObj.setTasktype(tasktype);
			locationObj.setTaskname(tasknametext);
			DataSourceHandler db = DataSourceHandler
					.getInstance(getApplicationContext());
			db.insertFreqLocationDataObject(locationObj);
			LocationActivity.dbinsertupdate=false;
			finish();
			}
			else{
				ToastMessage("Please insert all feild");
			}}
			if(LocationActivity.dbinsertupdate==true){
				if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0){
					String arraypass = LOgiLatiValue;
					String[] Location = arraypass.split(",");
				DbLocationObject locationObj = new DbLocationObject();
				locationObj.setLocationLat(Location[0]);
				locationObj.setLocationLong(Location[1]);
				locationObj.setLocationType(valueLocation);
				locationObj.setLocationProvider("GPS");
				locationObj.setRepeate("1");
				locationObj.setId(updateid);
				locationObj.getId();
				String locomsgno=messagetextvalue;
				locationObj.setMessage(locomsgno);
				locationObj.setMapradius(valueradius);
				locationObj.setTasktype(tasktype);
				locationObj.setTaskname(tasknametext);
				DataSourceHandler db = DataSourceHandler
						.getInstance(getApplicationContext());
				db.updateFreqLocationDataObject(locationObj);
				LocationActivity.dbinsertupdate=false;
				finish();
				}
				else{
					ToastMessage("Please insert all feild");
				}}		
		
		
	
	}
	private void saveValueindatabaseTasktypethree(){
		if(LocationActivity.dbinsertupdate==false){
		if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0){
			String arraypass = LOgiLatiValue;
			String[] Location = arraypass.split(",");
		DbLocationObject locationObj = new DbLocationObject();
		locationObj.setLocationLat(Location[0]);
		locationObj.setLocationLong(Location[1]);
		locationObj.setLocationType(valueLocation);
		locationObj.setLocationProvider("GPS");
		locationObj.setRepeate("1");
		String locomsgno=messagetextvalue;
		if(phoneno!=null||phoneno.length()!=0){
			locomsgno=locomsgno+"*"+phoneno;
		}
		if(locomsgno.contains("*") && locomsgno.contains("$")){
		locationObj.setMessage(locomsgno);
		locationObj.setMapradius(valueradius);
		locationObj.setTasktype(tasktype);
		locationObj.setTaskname(tasknametext);
		LocationActivity.dbinsertupdate=false;
		DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
		db.insertFreqLocationDataObject(locationObj);
		finish();
		}
		else{
			ToastMessage("Please insert correct contact");
		}}
		else{
			ToastMessage("Please insert all feild");
		}}
		if(LocationActivity.dbinsertupdate==true){
			if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0){
				String arraypass = LOgiLatiValue;
				String[] Location = arraypass.split(",");
			DbLocationObject locationObj = new DbLocationObject();
			locationObj.setLocationLat(Location[0]);
			locationObj.setLocationLong(Location[1]);
			locationObj.setLocationType(valueLocation);
			locationObj.setLocationProvider("GPS");
			locationObj.setRepeate("1");
			locationObj.setId(updateid);
			locationObj.getId();
			String locomsgno=messagetextvalue;
			if(phoneno!=null||phoneno.length()!=0){
				locomsgno=locomsgno+"*"+phoneno;
			}
			if(locomsgno.contains("*") && locomsgno.contains("$")){
			locationObj.setMessage(locomsgno);
			locationObj.setMapradius(valueradius);
			locationObj.setTasktype(tasktype);
			locationObj.setTaskname(tasknametext);
			DataSourceHandler db = DataSourceHandler
					.getInstance(getApplicationContext());
			db.updateFreqLocationDataObject(locationObj);
			LocationActivity.dbinsertupdate=false;

			finish();
			}
			else{
				ToastMessage("Please insert correct contact");
			}}
			else{
				ToastMessage("Please insert all feild");
			}}	
		
	}
	private void saveValueindatabaseTasktypefour(){

		if(LocationActivity.dbinsertupdate==false){
			if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0){
			// adding to database
				String arraypass = LOgiLatiValue;
				String[] Location = arraypass.split(",");
			DbLocationObject locationObj = new DbLocationObject();
			locationObj.setLocationLat(Location[0]);
			locationObj.setLocationLong(Location[1]);
			locationObj.setLocationType(valueLocation);
			locationObj.setLocationProvider("GPS");
			locationObj.setRepeate("1");
			locationObj.setMessage(messagetextvalue);
			locationObj.setMapradius(valueradius);
			locationObj.setTasktype(tasktype);
			locationObj.setTaskname(tasknametext);
			// insert data to data base
			DataSourceHandler db = DataSourceHandler
					.getInstance(getApplicationContext());
			db.insertFreqLocationDataObject(locationObj);
			LocationActivity.dbinsertupdate=false;
			finish();
			}
			else{
				ToastMessage("Please insert all feild");
			}}
			if(LocationActivity.dbinsertupdate==true){
				if(!(messagetextvalue.length()<=0)  && LOgiLatiValue!=null && valueLocation!=null ){
					String arraypass = LOgiLatiValue;
					String[] Location = arraypass.split(",");
				DbLocationObject locationObj = new DbLocationObject();
				locationObj.setLocationLat(Location[0]);
				locationObj.setLocationLong(Location[1]);
				locationObj.setLocationType(valueLocation);
				locationObj.setLocationProvider("GPS");
				locationObj.setRepeate("1");
				locationObj.setId(updateid);
				locationObj.getId();
				locationObj.setMessage(messagetextvalue);
				locationObj.setMapradius(valueradius);
				locationObj.setTasktype(tasktype);
				locationObj.setTaskname(tasknametext);
				DataSourceHandler db = DataSourceHandler
						.getInstance(getApplicationContext());
				db.updateFreqLocationDataObject(locationObj);
				LocationActivity.dbinsertupdate=false;
				finish();
				}
				else{
					ToastMessage("Please insert all feild");
				}}		
	
	}
	private void saveValueindatabaseTasktypefive(){

		if(LocationActivity.dbinsertupdate==false){
			if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0 ){
				String arraypass = LOgiLatiValue;
				String[] Location = arraypass.split(",");
			DbLocationObject locationObj = new DbLocationObject();
			locationObj.setLocationLat(Location[0]);
			locationObj.setLocationLong(Location[1]);
			locationObj.setLocationType(valueLocation);
			locationObj.setLocationProvider("GPS");
			locationObj.setRepeate("1");
			locationObj.setMessage(messagetextvalue);
			locationObj.setMapradius(valueradius);
			locationObj.setTasktype(tasktype);
			locationObj.setTaskname(tasknametext);
			DataSourceHandler db = DataSourceHandler
					.getInstance(getApplicationContext());
			db.insertFreqLocationDataObject(locationObj);
			finish();
			}
			else{
				ToastMessage("Please insert all feild");
			}}
			if(LocationActivity.dbinsertupdate==true){
				if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0 ){
					String arraypass = LOgiLatiValue;
					String[] Location = arraypass.split(",");
				DbLocationObject locationObj = new DbLocationObject();
				locationObj.setLocationLat(Location[0]);
				locationObj.setLocationLong(Location[1]);
				locationObj.setLocationType(valueLocation);
				locationObj.setLocationProvider("GPS");
				locationObj.setRepeate("1");
				locationObj.setId(updateid);
				locationObj.getId();
				locationObj.setMessage(messagetextvalue);
				locationObj.setMapradius(valueradius);
				locationObj.setTasktype(tasktype);
				locationObj.setTaskname(tasknametext);
				LocationActivity.dbinsertupdate=false;
				DataSourceHandler db = DataSourceHandler
						.getInstance(getApplicationContext());
				db.updateFreqLocationDataObject(locationObj);
				finish();
			}}
	
	}
	private void saveValueindatabaseTasktypesix(){

		if(LocationActivity.dbinsertupdate==false){
		if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0 ){
			String arraypass = LOgiLatiValue;
			String[] Location = arraypass.split(",");
		DbLocationObject locationObj = new DbLocationObject();
		locationObj.setLocationLat(Location[0]);
		locationObj.setLocationLong(Location[1]);
		locationObj.setLocationType(valueLocation);
		locationObj.setLocationProvider("GPS");
		locationObj.setRepeate("1");
		locationObj.setMessage(messagetextvalue);
		locationObj.setMapradius(valueradius);
		locationObj.setTasktype(tasktype);
		locationObj.setTaskname(tasknametext);
		DataSourceHandler db = DataSourceHandler
				.getInstance(getApplicationContext());
		db.insertFreqLocationDataObject(locationObj);
		LocationActivity.dbinsertupdate=false;
		finish();
		}
		else{
			ToastMessage("Please insert all feild");
		}}
		if(LocationActivity.dbinsertupdate==true){
			if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0 ){
				String arraypass = LOgiLatiValue;
				String[] Location = arraypass.split(",");
			DbLocationObject locationObj = new DbLocationObject();
			locationObj.setLocationLat(Location[0]);
			locationObj.setLocationLong(Location[1]);
			locationObj.setLocationType(valueLocation);
			locationObj.setLocationProvider("GPS");
			locationObj.setRepeate("1");
			locationObj.setId(updateid);
			locationObj.getId();
			locationObj.setMessage(messagetextvalue);
			locationObj.setMapradius(valueradius);
			locationObj.setTasktype(tasktype);
			locationObj.setTaskname(tasknametext);
			LocationActivity.dbinsertupdate=false;
			DataSourceHandler db = DataSourceHandler
					.getInstance(getApplicationContext());
			db.updateFreqLocationDataObject(locationObj);
			finish();
			}
			else{
				ToastMessage("Please insert correct contact");
			}}
			}	
	
	
	private void saveValueindatabaseTasktypeseven(){

//		System.out.println("at save tasktype is >>>"+tasktype+LocationActiviry.mapradius);
		if(LocationActivity.dbinsertupdate==false){
			if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0 ){
			String arraypass = LOgiLatiValue;
			String[] Location = arraypass.split(",");
			DbLocationObject locationObj = new DbLocationObject();
			locationObj.setLocationLat(Location[0]);
			locationObj.setLocationLong(Location[1]);
			locationObj.setLocationType(valueLocation);
			locationObj.setMapradius(valueradius);
			locationObj.setLocationProvider("GPS");
			locationObj.setRepeate("1");
			if(messagetextvalue!=null && messagetextvalue.length()>2){
				locationObj.setMessage(messagetextvalue);
				locationObj.setTasktype(tasktype);
				locationObj.setTaskname(tasknametext);
			DataSourceHandler db = DataSourceHandler
					.getInstance(getApplicationContext());
			db.insertFreqLocationDataObject(locationObj);
			LocationActivity.dbinsertupdate=false;
			finish();
			}
			else{
				ToastMessage("Please choose wallpaper");
			}}
			else{
				ToastMessage("Please insert all feild");
			}}
			if(LocationActivity.dbinsertupdate==true){
				if(messagetextvalue.length()>0  && LOgiLatiValue.length()>0 && valueLocation.length()>0 && tasknametext.length()>0 ){
					String arraypass = LOgiLatiValue;
					String[] Location = arraypass.split(",");
				DbLocationObject locationObj = new DbLocationObject();
				locationObj.setLocationLat(Location[0]);
				locationObj.setLocationLong(Location[1]);
				locationObj.setLocationType(valueLocation);
				locationObj.setLocationProvider("GPS");
				locationObj.setMapradius(valueradius);
				locationObj.setRepeate("1");
				locationObj.setId(updateid);
				locationObj.getId();
				if(filePath!=null){
					locationObj.setMessage(messagetextvalue);
					locationObj.setTasktype(tasktype);
					locationObj.setTaskname(tasknametext);
				LocationActivity.dbinsertupdate=false;
				DataSourceHandler db = DataSourceHandler
						.getInstance(getApplicationContext());
				db.updateFreqLocationDataObject(locationObj);
				finish();
				}
				else{
					ToastMessage("Please insert correct contact");
				}}
				else{
					ToastMessage("Please insert all feild");
				}}
	
	}
	Bitmap bitmap;
	private class LoadImage extends AsyncTask<String, String, Bitmap> {
	    @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(AddTaskActivity.this);
	            pDialog.setMessage("Loading Image ...");
	            pDialog.show();
	    }
	       protected Bitmap doInBackground(String... args) {
	         try {
	        	 bitmap=null;
	               bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
	               System.out.println("111at backgrounf>>>>>>>>"+bitmap+"..."+new URL(args[0]).getContent().toString());
	        } catch (Exception e) {
	              e.printStackTrace();
	        }
	      return bitmap;
	       }
	       protected void onPostExecute(Bitmap image) {
	         if(image != null){
	           defaultwallicon.setImageBitmap(image);
	        saveImagetoSDcard(image);
	           pDialog.dismiss();
	         }else{
	           pDialog.dismiss();
	           Toast.makeText(AddTaskActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
	         }
	       }
	   }
	private void saveImagetoSDcard(Bitmap finalBitmap) {

	    String root = Environment.getExternalStorageDirectory().toString();
	    File myDir = new File(root + "/Cloud_images");    
	    myDir.mkdirs();
	    Random generator = new Random();
	    int n = 10000;
	    n = generator.nextInt(n);
	    String fname = "Image-"+ n +".jpg";
	    File file = new File (myDir, fname);
	    if (file.exists ()) file.delete (); 
	    try {
	    	System.out.println("1111111111saving>>>>>>>>>>>>>>"+file);
	           FileOutputStream out = new FileOutputStream(file);
	           finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
	           out.flush();
	           out.close();

	    } catch (Exception e) {
	           e.printStackTrace();
	    }
//	    System.out.println("1111111111saving>>>>>>>>>>>>>>"+file.getAbsolutePath());
//	    System.out.println("1111111111saving>>>>>>>>>>>>>>"+file.getPath());
	    messagetextvalue=file.getAbsolutePath();
//	    DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
//		int screenWidthInPix = displayMetrics.widthPixels/2;
//		int screenHeightInPix = displayMetrics.heightPixels/3;
//		LayoutParams params = (LayoutParams) relativeLayoutsetwall.getLayoutParams();
//		params.height = screenHeightInPix;
//		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(defaultwallicon.getLayoutParams());
//		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//		try {
//			Bitmap btmp=null;
//			btmp=scaleBitmap(bmp, screenWidthInPix, screenHeightInPix);
//			defaultwallicon.setBackgroundResource(0);
//			defaultwallicon.setImageBitmap(btmp);
//			defaultwallicon.setLayoutParams(lp);
			action_name.setText("Wallpaper");
			System.gc();
//			if (bmp != null) {
//				bmp.recycle();
//				bmp = null;
//				
//			}
//		}catch(Exception ee){
//			ee.printStackTrace();
//		}
//	    System.out.println("1111111111saving>>>>>>>>>>>>>>"+myDir+"..."+fname);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		LocationActivity.dbinsertupdate=false;
		
		if(relativeLayoutsettingaction.isShown()){
			relativeLayoutsettingaction.setVisibility(View.GONE);	
			}
		else{
		setSeekbarInvisibile();
		super.onBackPressed();
		}


	}

}
