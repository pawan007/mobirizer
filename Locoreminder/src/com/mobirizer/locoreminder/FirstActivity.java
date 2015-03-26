package com.mobirizer.locoreminder;

import gps.GPSTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bin.ReminderInfo;

import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;
import com.location.finder.LocationMatcherService;

public class FirstActivity extends Activity {

	public static FirstActivity mainInstance;
	public static Context pickAddressContext;
	public static String locationnameformap = "";
	private ImageView searchAddress, menubtn, notepad;
	private EditText getAddresstext;
	public ListView pickAddrList;
	public TextView selectedAddress, addreminder;
	public LinearLayout selectedAddressOptionsLayout;
	private Point point;
	private GridView gridview;
	private CustomGridViewAdapter gridAdapter;

	DataSourceHandler db;
	List<DbLocationObject> value;

	public static String[] LOCATIONNAME;
	public static String[] LOCATIONMSG;
	public static ArrayList<String> placename;
	public static ArrayList<String> placemsg;
	public static ArrayList<String> alramtype;
	public static String[] LOCO_NAME;
	public static int[] LOCO_ICON;
	ArrayAdapter<String> adapter;

	ArrayList<ReminderInfo> reminderList;

	private ArrayList<String> listReminderName;
	private ArrayList<Integer> listImageUseInBackgrond;
	FirstGridViewForReminderList mAdapter;
	// for notification
	private NotificationManager myNotificationManager;
	private int notificationIdOne = 111;
	private int notificationIdTwo = 112;
	private int numMessagesOne = 0;
	private int numMessagesTwo = 0;
	Typeface tf;
	GPSTracker gps;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pickAddressContext = FirstActivity.this;
		mainInstance = FirstActivity.this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_firstactivity);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		String fontPath = "fonts/Gabriola_0.ttf";
		tf = Typeface.createFromAsset(getAssets(), fontPath);
		TextView txt = (TextView) findViewById(R.id.input_address);
		txt.setTextSize(25);
		txt.setTypeface(tf);
		GPSTracker gps = new GPSTracker(FirstActivity.this);
		if (!gps.canGetLocation()) {
			gps.showSettingsAlert();
		}
		InitialzeArray();
		GetSavePlaceName();
		CreateStringArray();
		CheckSizeOFString();
		AddnameLogoArray();
		prepareListUse();

		// System.out.println("after setting layout");
		// System.out.println("getaddresstext" + getAddresstext);
		pickAddrList = (ListView) this.findViewById(R.id.pick_address_list);
		addreminder = (TextView) findViewById(R.id.input_address);
		addreminder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent serchLocation = new Intent(FirstActivity.this,
						SearchLocationActivity.class);
				startActivity(serchLocation);
				FirstActivity.this.finish();
			}
		});

		menubtn = (ImageView) findViewById(R.id.img_menu);
		menubtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// System.out.println("inside menubtn click");
				// if (point != null)
				// showPopup(FirstActivity.this, point);
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
				layoutparam.y = 120;
				dialog.getWindow().setAttributes(layoutparam);
				TextView txt1 = (TextView) view.findViewById(R.id.textView1);
				txt1.setTypeface(tf);
				txt1.setOnClickListener(mclick);
				TextView txt2 = (TextView) view.findViewById(R.id.textView2);
				txt2.setTypeface(tf);
				txt2.setOnClickListener(mclick);
				TextView txt3 = (TextView) view.findViewById(R.id.textView3);
				txt3.setTypeface(tf);
				txt3.setOnClickListener(mclick);

				TextView txt4 = (TextView) view.findViewById(R.id.textView4);
				txt4.setTypeface(tf);
				txt4.setOnClickListener(mclick);
				TextView txt5 = (TextView) view.findViewById(R.id.text_tute);
				txt5.setTypeface(tf);
				txt5.setOnClickListener(mclick);
				dialog.show();

			}
		});
		searchAddress = (ImageView) findViewById(R.id.search_address_icon);
		searchAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent serchLocation = new Intent(FirstActivity.this,
						SearchLocationActivity.class);
				startActivity(serchLocation);
				FirstActivity.this.finish();

			}
		});
		pickAddrList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// pickAddrList.clearChoices();
						// pickAddrList.requestLayout();
						ArrayList<Map<String, String>> aAddresses = new ArrayList<Map<String, String>>();
						// List<PickAddressListModel> aAddresses = new
						// ArrayList<PickAddressListModel>();
						ArrayList<String> aLatLngs = new ArrayList<String>();
						// aAddresses =
						// mainInstance.globalVar.GetAddressOptions();
						// aLatLngs = mainInstance.globalVar.GetLatLngOptions();
						String aLatLngStr = aLatLngs.get(position);
						String[] aLatLngElements = aLatLngStr.split(",");
						double aLat = Double.parseDouble(aLatLngElements[0]);
						double aLng = Double.parseDouble(aLatLngElements[1]);
						selectedAddressOptionsLayout
								.setVisibility(View.VISIBLE);
						selectedAddress.setText(aAddresses.get(position).get(
								"address_name"));

						SharedPreferences aSharedPreferences = PreferenceManager
								.getDefaultSharedPreferences(pickAddressContext);
						String phoneNumber = aSharedPreferences.getString(
								"phone_num", "");

						PickListModel aPickListModel = new PickListModel();
						// ArrayList<Map<String, String>> addresses =
						// mainInstance.globalVar.GetAddressOptions();
						// aPickListModel.LoadModel(addresses);
						String[] ids = new String[PickListModel.pickAddressListRowItemArrList
								.size()];
						for (int i = 0; i < ids.length; i++) {
							ids[i] = Integer.toString(i + 1);
						}
						PickListRowItemAdapter aAdapter = new PickListRowItemAdapter(
								pickAddressContext,
								R.layout.pick_address_list_item, ids, position);
						pickAddrList.setAdapter(aAdapter);
						aAdapter.notifyDataSetChanged();
					}
				});

		// Starting lcoation matcher service to gather geofence and periodic
		// location updates
		Intent locationService = new Intent(this, LocationMatcherService.class);
		startService(locationService);
	}

	OnClickListener mclick = new OnClickListener() {
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
				System.out.println("settings screen");
				intent = new Intent(getApplicationContext(),
						SettingsActivity.class);
				startActivity(intent);
				FirstActivity.this.finish();
				break;
			case R.id.text_tute:
				System.out.println("tute screen");
				intent = new Intent(getApplicationContext(), TourActivity.class);
				startActivity(intent);
				FirstActivity.this.finish();
				break;
			case R.id.textView4:
				intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("https://play.google.com/store/search?q=mobirizer&c=apps&hl=en"));
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	};

	private void InitialzeArray() {
		gridview = (GridView) findViewById(R.id.gridview);
		placename = new ArrayList<String>();
		placemsg = new ArrayList<String>();
		alramtype = new ArrayList<String>();
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
				String alarmcat = valueName.getMessage();
				placename.add(type);
				placemsg.add(msg);
				alramtype.add(alarmcat);

				ReminderInfo reminder = new ReminderInfo();
				reminder.setmLocationName(type);
				reminder.setmMessage(msg);
				reminderList.add(reminder);

				System.out.println("alarmtype length>>>>>>" + alramtype.size());
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

			System.out.println(LOCATIONNAME[i]);
		}

	}

	private void CheckSizeOFString() {
		int i = LOCATIONNAME.length;
		Toast.makeText(getApplicationContext(), "length" + i, Toast.LENGTH_LONG)
				.show();

	}

	private void AddnameLogoArray() {
		LOCO_ICON = new int[LOCATIONNAME.length];
		for (int i = 0; i < placename.size(); i++) {
			String s = alramtype.get(i).toString();

			if (s.equalsIgnoreCase("0")) {
				LOCO_ICON[i] = R.drawable.note;
				// listImageUseInBackgrond.add(R.drawable.note);
				listImageUseInBackgrond.add(i, R.drawable.note);
			} else if (s.equalsIgnoreCase("1")) {
				LOCO_ICON[i] = R.drawable.editnote;
				listImageUseInBackgrond.add(i, R.drawable.editnote);
				// listImageUseInBackgrond.add(R.drawable.editnote);
			}
		}

	}

	private void prepareListUse() {
		gridview = (GridView) findViewById(R.id.gridview);
		mAdapter = new FirstGridViewForReminderList(FirstActivity.this,
				placename, placemsg, alramtype, listImageUseInBackgrond);
		gridview.setAdapter(mAdapter);

	}

	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("111111");
	}

	@Override
	protected void onPause() {
		super.onPause();
		ClearData();
		finish();

	}

	@Override
	protected void onResume() {
		super.onResume();
		ClearData();
		GetSavePlaceName();
		CreateStringArray();
		CheckSizeOFString();
		AddnameLogoArray();
		prepareListUse();
	}

	public void ClearData() {
		try {
			placename.clear();
			placemsg.clear();
			alramtype.clear();
			listImageUseInBackgrond.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onItemClick(int mPosition) {
		ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
		String name = reminderName.getmLocationName();
		String msg = reminderName.getmMessage();
		locationnameformap = name;
		Intent setdata = new Intent(FirstActivity.this,
				SetDataValueForReminder.class);
		setdata.putExtra("location", name);
		setdata.putExtra("Message", msg);
		startActivity(setdata);
	}

}