package com.foruriti.geosms;

import globalVariable.GlobalVariable;
import gps.GPSTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bin.ReminderInfo;

import com.foruriti.locosms.R;
import com.foruriti.utility.UtilityHandler;
import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;
import com.location.finder.LocationMatcherService;
import com.navdrawer.SimpleSideDrawer;

import conditionCheck.ConnectionDetector;


public class FirstActivity extends Activity {
	private SimpleSideDrawer slide_me;
	private ConnectionDetector connectionDetector;
	private double latitude,longitude;
	private boolean dis;
	private List<Address> tempaddress;
	private Geocoder geocoder;
//	private final String fontPath="fonts/Gabriola_0.ttf";
	public static FirstActivity mainInstance;
	public static Context pickAddressContext;
//	public static String locationnameformap = "";
	private ImageView searchAddress, menubtn, notepad,addlocation;
	private EditText getAddresstext;
	public ListView pickAddrList;
	public TextView selectedAddress, addreminder;
	public LinearLayout selectedAddressOptionsLayout;
	private Point point;
	private GridView gridview;
	private ImageView imgpin;
	DataSourceHandler db;
	List<DbLocationObject> value;
	public static String[] LOCATIONNAME;
	public static String[] LOCATIONMSG;
	public static ArrayList<String> placename;
	public static ArrayList<String> placemsg;
	public static ArrayList<String> alramtype;
	public static ArrayList<Integer> mapradius;
	public static String[] LOCO_NAME;
	public static int[] LOCO_ICON;
	ArrayAdapter<String> adapter;
	ArrayList<ReminderInfo> reminderList;
	private ArrayList<String> listReminderName;
	private ArrayList<Integer> listImageUseInBackgrond;
	FirstGridViewForReminderList mAdapter;
	public static double aLat,aLng;
	private ProgressDialog pDialog; 
	private String getcompadd="";
	// for notification
//	Typeface tf;
	GPSTracker gps;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pickAddressContext = FirstActivity.this;
		mainInstance = FirstActivity.this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//		TextView txt = (TextView) findViewById(R.id.input_address);
//		txt.setTextSize(25);
//		txt.setTypeface(tf);
		
		initialiseVariable();
		InitialzeArray();
		GetSavePlaceName();
//		CreateStringArray();
//		CheckSizeOFString();
//		drawLightImage();
//		AddnameLogoArray();
		prepareListUse();
		pickAddrList = (ListView) this.findViewById(R.id.pick_address_list);
//		addreminder = (TextView) findViewById(R.id.input_address);
//		addreminder.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent serchLocation = new Intent(FirstActivity.this,
//						SearchLocationActivity.class);
//				startActivity(serchLocation);
//				FirstActivity.this.finish();
//			}
//		});
addlocation=(ImageView) findViewById(R.id.addlocation);
addlocation.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ActionSelectorActivity.messagetextvalue="";
		SetDataValueForReminder.completeADD="";
		ActionSelectorActivity.phoneno="";
		Intent i=new Intent(FirstActivity.this,ActionSelectorActivity.class);
//		i.putExtra("updateid", 0);
		startActivity(i);
		finish();
//		callMappage();
	}
});
	/*	menubtn = (ImageView) findViewById(R.id.img_menu);
		menubtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DisplayMetrics displayMetrics = getApplicationContext().getResources()
	                    .getDisplayMetrics();
				int screenWidthInPix = displayMetrics.widthPixels;
				int screenheightInPix = displayMetrics.heightPixels;
//				System.out.println("height>>>>>>>>"+screenheightInPix);
				final Dialog dialog = new Dialog(FirstActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
				LayoutInflater inflater = (LayoutInflater) getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater
						.inflate(R.layout.popup_layout, null, false);
				dialog.setContentView(view);
				dialog.getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);
				WindowManager.LayoutParams layoutparam = dialog.getWindow()
						.getAttributes();
				if(screenheightInPix<1000){
					layoutparam.y = 110;	
				}
				else{
				layoutparam.y = 140;	
				}
				dialog.getWindow().setAttributes(layoutparam);
				TextView txt1 = (TextView) view.findViewById(R.id.textView1);
				txt1.setOnClickListener(mclick);
				TextView txt2 = (TextView) view.findViewById(R.id.textView2);
				txt2.setOnClickListener(mclick);
				TextView txt3 = (TextView) view.findViewById(R.id.textView3);
				txt3.setOnClickListener(mclick);
				TextView txt4 = (TextView) view.findViewById(R.id.textView4);
				txt4.setOnClickListener(mclick);
//				TextView txt5 = (TextView) view.findViewById(R.id.text_tute);
//				txt5.setOnClickListener(mclick);
				dialog.show();
			}
		});*/


		// Starting lcoation matcher service to gather geofence and periodic
		// location updates
		Intent locationService = new Intent(this, LocationMatcherService.class);
		startService(locationService);
	}

	private void initialiseVariable() {
		// TODO Auto-generated method stub
		geocoder=new Geocoder(FirstActivity.this, Locale.getDefault());
		GPSTracker gps = new GPSTracker(FirstActivity.this);
		connectionDetector=new ConnectionDetector(getApplicationContext());
//		tf = Typeface.createFromAsset(getAssets(), fontPath);
		slide_me = new SimpleSideDrawer(this);
		slide_me.setRightBehindContentView(R.layout.right_menu);		
		if (!gps.canGetLocation()) {
			gps.showSettingsAlert();
		}
		
	}

	
	
	
	private void ToastMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
	private boolean checkIsAddtable(double latitude2, double longitude2) {

		try {
			List<DbLocationObject> value = db.getAllFreqLocationDataObject();
			for (DbLocationObject valueName : value) {
				String latitude = valueName.getLocationLat().toString();
				String logitude = valueName.getLocationLong().toString();
				String placename = valueName.getLocatonType().toString();
				GlobalVariable.placeNameAdd = placename.trim();
				double lat1 = Double.parseDouble(latitude);
				double lng1 = Double.parseDouble(logitude);
				double distance = UtilityHandler.distFrom(lat1, lng1,
						latitude2, longitude2);
				if (distance > 0 && distance < 200) {
					dis = true;
					return dis;
				} else {
					dis = false;

				}

				// checkAndResult(ddd);

			}

		} catch (Exception e) {
			e.printStackTrace();
			e.getLocalizedMessage();
		}
		return dis;

	}
	/*OnClickListener mclick = new OnClickListener() {
		Intent intent;

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.textView1:
				intent = new Intent(getApplicationContext(),
						AboutcompanyActivity.class);
				startActivity(intent);
				FirstActivity.this.finish();
				break;
			case R.id.textView2:
				intent = new Intent(getApplicationContext(),
						AboutproductActivity.class);
				startActivity(intent);
				FirstActivity.this.finish();
				break;
			case R.id.textView3:
//				intent = new Intent(getApplicationContext(),
//						SettingsActivity.class);
//				startActivity(intent);
//				FirstActivity.this.finish();
				intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("https://play.google.com/store/apps/details?id=com.mobirizer.geosms"));
				startActivity(intent);
				break;
//			case R.id.text_tute:
//				intent = new Intent(getApplicationContext(), TourActivity.class);
//				startActivity(intent);
//				FirstActivity.this.finish();
//				break;
			case R.id.textView4:
				intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("https://play.google.com/store/apps/developer?id=Mobirizer"));
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	};*/

	private void InitialzeArray() {
//		gridview = (GridView) findViewById(R.id.gridview);
		pickAddrList=(ListView) findViewById(R.id.pick_address_list);
		placename = new ArrayList<String>();
		placemsg = new ArrayList<String>();
		alramtype = new ArrayList<String>();
		mapradius = new ArrayList<Integer>();
		db = DataSourceHandler.getInstance(pickAddressContext);
		listImageUseInBackgrond = new ArrayList<Integer>();
		reminderList = new ArrayList<ReminderInfo>();

	}

	private void GetSavePlaceName() {
		try {
			List<DbLocationObject> value = db.getAllFreqLocationDataObject();
			for (DbLocationObject valueName : value) {
				String type = valueName.getLocatonType();
				String msg = valueName.getRepeate();
				String lat1=valueName.getLocationLat();
				String log1=valueName.getLocationLong();
				int rad=valueName.getMapradius();
				String repeat=valueName.getMessage();
				String alarmcat = valueName.getMessage();
				long longid=valueName.getId();
//				System.out.println("alarmtype nd repeat"+valueName.getMessage());
//				System.out.println(valueName.getLocatonType());
//				System.out.println(valueName.getRepeate());
				System.out.println("11111111111111"+repeat);
				System.out.println("22222222222"+valueName.getRepeate());
//				msg=msg.replaceAll(" ", "");
				placename.add(type);
				placemsg.add(msg);
				alramtype.add(alarmcat);
				mapradius.add(rad);
				ReminderInfo reminder = new ReminderInfo();
				reminder.setmLocationName(type);
				reminder.setmMessage(msg);
				reminder.setmLati(lat1);
				reminder.setmLongi(log1);
				reminder.setmMapradius(rad);
				reminder.setmRepeat(repeat);
				reminder.setLocid(longid);
				reminderList.add(reminder);

//				System.out.println("alarmtype length>>>>>>" + alramtype.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
			e.getLocalizedMessage();
		}

	}

	private void CreateStringArray() {
		LOCATIONNAME = new String[placename.size()];
		LOCATIONMSG = new String[placemsg.size()];
		for (int i = 0; i < placename.size(); i++) {
			LOCATIONNAME[i] = placename.get(i).toString();
			LOCATIONNAME[i] = placemsg.get(i).toString();

//			System.out.println(LOCATIONNAME[i]);
		}

	}

//	private void CheckSizeOFString() {
//		int i = LOCATIONNAME.length;
//		Toast.makeText(getApplicationContext(), "length" + i, Toast.LENGTH_LONG)
//				.show();

//	}

//	private void AddnameLogoArray() {
////		LOCO_ICON = new int[LOCATIONNAME.length];
//		for (int i = 0; i < placename.size(); i++) {
//			String s = alramtype.get(i).toString();
////			if (s.equalsIgnoreCase("0")) {
//////				LOCO_ICON[i] = R.drawable.note;
////				// listImageUseInBackgrond.add(R.drawable.note);
////				listImageUseInBackgrond.add(i, R.drawable.notepadselector_second);
//////				listImageUseInBackgrond.add(i,R.drawable.orange_bg);
////				pickAddrList.setBackgroundColor(Color.parseColor("#F26665"));
////			} else if (s.equalsIgnoreCase("1")) {
////				LOCO_ICON[i] = R.drawable.editnote;
//////				listImageUseInBackgrond.add(i, R.drawable.notepadselector);
//////				listImageUseInBackgrond.add(i,R.drawable.orange_bg);
////				// listImageUseInBackgrond.add(R.drawable.editnote);
////				pickAddrList.setBackgroundColor(Color.parseColor("#7bcd4d"));
////			}
////			if(i%2==0){
////				pickAddrList.setBackgroundColor(Color.parseColor("#F26665"));	
//////				pickAddrList.set
////			}
////			else {
////				pickAddrList.setBackgroundColor(Color.parseColor("#F26665"));		
////			}
//		}
//
//	}

	private void prepareListUse() {
//		gridview = (GridView) findViewById(R.id.gridview);
		pickAddrList=(ListView) findViewById(R.id.pick_address_list);
//		imgpin=(ImageView) findViewById(R.id.imageView1);
		mAdapter = new FirstGridViewForReminderList(FirstActivity.this,
				placename, placemsg, alramtype,mapradius, listImageUseInBackgrond);
//		gridview.setAdapter(mAdapter);
		pickAddrList.setAdapter(mAdapter);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {

		super.onPause();
		ClearData();
//		finish();

	}

	@Override
	protected void onResume() {
		super.onResume();
		ClearData();
		GetSavePlaceName();
//		CreateStringArray();
//		CheckSizeOFString();
//		AddnameLogoArray();
		prepareListUse();
	}

	public void ClearData() {
		try {
			placename.clear();
			placemsg.clear();
			alramtype.clear();
			mapradius.clear();
			listImageUseInBackgrond.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//	public void onItemClick(final int mPosition) {		
//			/* Alert Dialog Code Start */
//		
//			AlertDialog.Builder alert = new AlertDialog.Builder(this);
//			alert.setTitle("Do you want to Edit?"); 
//			alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int whichButton) {
//					ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
//					String name = reminderName.getmLocationName();
//					String msg = reminderName.getmMessage();
//					String lat1=reminderName.getmLati();
//					String log1=reminderName.getmLongi();
//					int rad=reminderName.getmMapradius();
//					String repeat=reminderName.getmRepeat();
////					System.out.println("at click edit>>>>"+reminderName.getmRepeat());
//					String latlong=lat1+","+log1;
////					locationnameformap = name;
//					SetDataValueForReminder.dbinsertupdate=true;
//					DbLocationObject locationObj = null;
//					List<DbLocationObject> value = db.getAllFreqLocationDataObject();
//					for (DbLocationObject valueName : value) {						
//						String Nameindb = valueName.getLocatonType();
//						
//						if(Nameindb.equalsIgnoreCase(name)){
//							locationObj = new DbLocationObject();	
//							locationObj.setId(valueName.getId());
//							locationObj.getId();}}
//					Intent setdata = new Intent(FirstActivity.this,SetDataValueForReminder.class);
//					setdata.putExtra("location", name);
//					setdata.putExtra("Message", msg);
//					setdata.putExtra("arrayname", latlong);
//					setdata.putExtra("radius", rad);
//					setdata.putExtra("repeat", repeat);
////					setdata.putExtra("idforupdate", locationObj.getId());
//					SetDataValueForReminder.idforupdate=(int) locationObj.getId();
////					System.out.println("at edit get id===="+locationObj.getId()+"......"+SetDataValueForReminder.idforupdate);
//					startActivity(setdata);
//					FirstActivity.this.finish();
//				} 
//			}); // End of alert.setPositiveButton
//			alert.setNegativeButton("No",
//					new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int whichButton) {
//						dialog.cancel();
//						
//						}
//						
//					}); // End of alert.setNegativeButton
//			AlertDialog alertDialog = alert.create();
//			alertDialog.show();
//
//	}
	public void onItemlongclick(final int mPosition) {		
		final CharSequence[] items={"Edit","Delete"};
		// TODO Auto-generated method stub
		AlertDialog.Builder customalert=new AlertDialog.Builder(FirstActivity.this);
		customalert.setTitle("Select Option").setItems(items, new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
//		Toast.makeText(getApplicationContext(), "U clicked "+items[which], Toast.LENGTH_LONG).show();
		if(items[which].equals("Edit")){
//			Toast.makeText(getApplicationContext(), "U clicked edit", Toast.LENGTH_LONG).show();	
			ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
			String name = reminderName.getmLocationName();
			String msg = reminderName.getmMessage();
			String lat1=reminderName.getmLati();
			String log1=reminderName.getmLongi();
			int rad=reminderName.getmMapradius();
			String repeat=reminderName.getmRepeat();
//			System.out.println("at click edit>>>>"+reminderName.getmRepeat());
			String latlong=lat1+","+log1;
//			locationnameformap = name;
			SetDataValueForReminder.dbinsertupdate=true;
			DbLocationObject locationObj = null;
			List<DbLocationObject> value = db.getAllFreqLocationDataObject();
			for (DbLocationObject valueName : value) {						
				String Nameindb = valueName.getLocatonType();
				if(Nameindb.equalsIgnoreCase(name)){
					locationObj = new DbLocationObject();	
					locationObj.setId(valueName.getId());
					locationObj.getId();}}
			Intent setdata = new Intent(FirstActivity.this,ActionSelectorActivity.class);
			setdata.putExtra("activity",1);
			setdata.putExtra("location", name);
			setdata.putExtra("Message", msg);
			setdata.putExtra("arrayname", latlong);
			setdata.putExtra("radius", rad);
			setdata.putExtra("repeat", repeat);
//			setdata.putExtra("idforupdate", locationObj.getId());
			SetDataValueForReminder.idforupdate=(int) locationObj.getId();
//			System.out.println("at edit get id===="+locationObj.getId()+"......"+SetDataValueForReminder.idforupdate);
//			setdata.putExtra("updateid", SetDataValueForReminder.idforupdate);
			startActivity(setdata);
			FirstActivity.this.finish();
		}
		if(items[which].equals("Delete")){
			AlertDialog.Builder alert = new AlertDialog.Builder(FirstActivity.this);
			alert.setTitle("Do you want to Delete?"); 
			alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
					String name = reminderName.getmLocationName();
//					System.out.println("name in db at select"+name);
					List<DbLocationObject> value = db.getAllFreqLocationDataObject();
					for (DbLocationObject valueName : value) {						
						String Nameindb = valueName.getLocatonType();
//						System.out.println("nameindb at select"+Nameindb);
						if(Nameindb.equalsIgnoreCase(name)){
							//System.out.println("*****"+Nameindb+"....."+valueName.getId());
							DbLocationObject locationObj = new DbLocationObject();	
							locationObj.setId(valueName.getId());
							locationObj.getId();
							DataSourceHandler db = DataSourceHandler
									.getInstance(getApplicationContext());
							db.deleteFreqLocationDataObject(locationObj);
							Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_LONG).show();
							Intent setdata = new Intent(FirstActivity.this,FirstActivity.class);						
							startActivity(setdata);
							FirstActivity.this.finish();
							break;
						}
						
					}	
				} 
			}); // End of alert.setPositiveButton
			alert.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
						
						}
						
					}); // End of alert.setNegativeButton
			AlertDialog alertDialog = alert.create();
			alertDialog.show();	
		}
		}
		});
		customalert.show();
}
	
	public void onItemlick(final int mPosition) {		
		/* Alert Dialog Code Start */
	
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Do you want to Delete?"); 
		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
				String name = reminderName.getmLocationName();
//				System.out.println("name in db at select"+name);
				List<DbLocationObject> value = db.getAllFreqLocationDataObject();
				for (DbLocationObject valueName : value) {						
					String Nameindb = valueName.getLocatonType();
//					System.out.println("nameindb at select"+Nameindb);
					if(Nameindb.equalsIgnoreCase(name)){
						//System.out.println("*****"+Nameindb+"....."+valueName.getId());
						DbLocationObject locationObj = new DbLocationObject();	
						locationObj.setId(valueName.getId());
						locationObj.getId();
						DataSourceHandler db = DataSourceHandler
								.getInstance(getApplicationContext());
						db.deleteFreqLocationDataObject(locationObj);
						Toast.makeText(getApplicationContext(), "Item deleted", Toast.LENGTH_LONG).show();
						Intent setdata = new Intent(FirstActivity.this,FirstActivity.class);						
						startActivity(setdata);
						FirstActivity.this.finish();
						break;
					}
					
				}	
			} 
		}); // End of alert.setPositiveButton
		alert.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
					}
				}); // End of alert.setNegativeButton
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
		

}
	public void onItembulbclick(final int mPosition) {		
		ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
		DbLocationObject locationObj = new DbLocationObject();
		String repeat=reminderName.getmRepeat();
//		String name = reminderName.getmLocationName();
//		locationObj.setId(mPosition);
		long idupdate=reminderName.getLocid();
//		System.out.println("repeat at click>>>>>"+repeat+".."+idupdate);
		if(repeat.equalsIgnoreCase("0")){
			repeat="1";
			reminderName.setmRepeat("1");
//			locationObj.setRepeate("1");
//			locationObj.set
			
		}
		else if(repeat.equalsIgnoreCase("1")){
			repeat="0";
			reminderName.setmRepeat("0");
//			locationObj.setRepeate("0");
		}
		System.out.println("repeat value is>>>>"+reminderName.getmRepeat());
		DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
		locationObj.setId(idupdate);
		locationObj.setRepeate(reminderName.getmRepeat());
		db.updateRepeatLocationDataObject(locationObj);
		Intent setdata = new Intent(FirstActivity.this,FirstActivity.class);						
		startActivity(setdata);
		FirstActivity.this.finish();
//		break;
		System.out.println("get id at click"+locationObj.getId());
//		drawLightImage(mPosition); 
}
//	private void drawLightImage(int position) {
//		// TODO Auto-generated method stub
//		ReminderInfo reminderName = (ReminderInfo) reminderList.get(position);
//		String repeat=reminderName.getmRepeat();
////		if(repeat.equalsIgnoreCase(""+0)){
////			getResources().getDrawable(R.drawable.bulb_s);
////		}
////		else{
////			getResources().getDrawable(R.drawable.bulb_un);	
////		}
//		LOCO_ICON = new int[LOCATIONNAME.length];
//		for (int i = 0; i < placename.size(); i++) {
//			String s = alramtype.get(i).toString();
//System.out.println("at drawimage>>>>>"+s);
//			if (repeat.equalsIgnoreCase("0")) {
////				LOCO_ICON[i] = R.drawable.bulb_s;
//				// listImageUseInBackgrond.add(R.drawable.note);
//				listImageUseInBackgrond.add(i, R.drawable.bulb_un);
//				System.out.println("at drawimage>>>>>");
//			} else if (repeat.equalsIgnoreCase("1")) {
////				LOCO_ICON[i] = R.drawable.editnote;
//				listImageUseInBackgrond.add(i, R.drawable.bulb_s);
//				System.out.println("at drawimage>>>>>");
//				// listImageUseInBackgrond.add(R.drawable.editnote);
//			}
//		}
//	}
	private static long back_pressed;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
	    else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
	    back_pressed = System.currentTimeMillis();
	}
}