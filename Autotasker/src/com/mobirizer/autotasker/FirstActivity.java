package com.mobirizer.autotasker;

import gps.ConnectionDetector;
import gps.GPSTracker;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import bin.ReminderInfo;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;
import com.location.service.LocationMatcherService;




public class FirstActivity extends Activity {
	private ConnectionDetector connectionDetector;
	private GPSTracker gps;
	public static Context mainInstance;
	private SavePreferences savePreferences;
	private TextView aboutCompanybtn,aboutProductbtn,settingbtn,morebtn;
	private ImageView addlocation,menubtn;
	private ListView pickAddrList;
	DataSourceHandler db;
	List<DbLocationObject> value;
	public static String[] LOCATIONNAME;
	public static String[] LOCATIONMSG;
	public static ArrayList<String> placename;
	public static ArrayList<String> placemsg;
	public static ArrayList<String> taskname;
	public static ArrayList<String> alramtype;
	public static ArrayList<Integer> mapradius;
	public static ArrayList<Integer> tasktype;
	ArrayAdapter<String> adapter;
	ArrayList<ReminderInfo> reminderList;
	FirstactivityadapterForTaskList mAdapter;
	private RelativeLayout layout_list;
	private LinearLayout menuLayout;
	private boolean internetcheck;
	// for notification
	/** The view to show the ad.  */
	private AdView adView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//		pickAddressContext = FirstActivity.this;
		mainInstance = getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first);
		savePreferences=new SavePreferences();
		menuLayout = (LinearLayout)findViewById(R.id.menu_layout);
		aboutProductbtn = (TextView)findViewById(R.id.aboutProductbtn);
		aboutCompanybtn = (TextView)findViewById(R.id.aboutCompany);
		settingbtn = (TextView)findViewById(R.id.settingbtn);
		morebtn = (TextView)findViewById(R.id.morebtn);
		aboutCompanybtn.setOnClickListener(mclick);
		aboutProductbtn.setOnClickListener(mclick);
		settingbtn.setOnClickListener(mclick);
		morebtn.setOnClickListener(mclick);
		if(savePreferences.getPreferences_integer(mainInstance, "first_launch")==1){
			Intent intent=new Intent(FirstActivity.this,TourActivity.class);
			savePreferences.SavePreferences(getApplicationContext(),"first_launch", 2);
			startActivity(intent);
		}
		initialiseVariable();
		InitialzeArray();
		GetSavePlaceName();
		prepareListUse();
		addlocation=(ImageView) findViewById(R.id.addlocation);
		addlocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setmenuInvisible();
				Intent i=new Intent(FirstActivity.this,AddTaskActivity.class);
				startActivity(i);
			}
		});
		menubtn = (ImageView) findViewById(R.id.img_menu);
		menubtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//				// TODO Auto-generated method stub
				setMenuVisibility();
			}
		});
		layout_list.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setmenuInvisible();
			}
		});
		// Starting lcoation matcher service to gather geofence and periodic location updates
		Intent locationService = new Intent(this, LocationMatcherService.class);
		startService(locationService);
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(getResources().getString(R.string.Banner_ad_id));
		LinearLayout layout = (LinearLayout) findViewById(R.id.add_layout);
		layout.setVisibility(View.VISIBLE);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}
	private void initialiseVariable() {
		// TODO Auto-generated method stub
		gps = new GPSTracker(FirstActivity.this);
		connectionDetector=new ConnectionDetector(getApplicationContext());
		if (!gps.canGetLocation()) {
			gps.showSettingsAlert(this);
		}
//		layout_addtask=(LinearLayout) findViewById(R.id.top_layout);
		layout_list=(RelativeLayout) findViewById(R.id.list_layout);

	}
	private void setMenuVisibility()
	{
		if(!menuLayout.isShown())
			menuLayout.setVisibility(View.VISIBLE);
		else
			menuLayout.setVisibility(View.GONE);
	}
	private void setmenuInvisible(){
		if(menuLayout.isShown())
			menuLayout.setVisibility(View.GONE);	
	}
	private void ToastMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	OnClickListener mclick = new OnClickListener() {
		Intent intent;

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.aboutCompany:
				intent = new Intent(getApplicationContext(),
						AboutcompanyActivity.class);
				setmenuInvisible();
				startActivity(intent);
				//				FirstActivity.this.finish();
				break;
			case R.id.aboutProductbtn:
				intent = new Intent(getApplicationContext(),
						AboutproductActivity.class);
				setmenuInvisible();
				startActivity(intent);
				//				FirstActivity.this.finish();
				break;
			case R.id.settingbtn:
				intent = new Intent(getApplicationContext(),
						ApplicationSettingActivity.class);
				setmenuInvisible();
				startActivity(intent);
				break;
			case R.id.morebtn:
				intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("https://play.google.com/store/apps/developer?id=Mobirizer"));
				setmenuInvisible();
				startActivity(intent);
				break;

			default:
				break;
			}
		}
	};
	private void InitialzeArray() {
		placename = new ArrayList<String>();
		placemsg = new ArrayList<String>();
		alramtype = new ArrayList<String>();
		taskname = new ArrayList<String>();
		mapradius = new ArrayList<Integer>();
		tasktype = new ArrayList<Integer>();
		db = DataSourceHandler.getInstance(mainInstance);
		reminderList = new ArrayList<ReminderInfo>();
	}
	private void GetSavePlaceName() {
		try {
			List<DbLocationObject> value = db.getAllFreqLocationDataObject();
			if(value.size()==0){
				ClearData();
			}
			for (DbLocationObject valueName : value) {
				String type = valueName.getLocatonType();
				String msg = valueName.getRepeate();
				String lat1=valueName.getLocationLat();
				String log1=valueName.getLocationLong();
				int rad=valueName.getMapradius();
				int task=valueName.getTasktype();
				String repeat=valueName.getMessage();
				String taskName = valueName.getTaskname();
				long longid=valueName.getId();
//				System.out.println("alarmtype nd repeat"+valueName.getTasktype()+valueName.getMapradius());
				placename.add(type);
				placemsg.add(msg);
				alramtype.add(repeat);
				taskname.add(taskName);
				mapradius.add(rad);
				tasktype.add(task);
				ReminderInfo reminder = new ReminderInfo();
				reminder.setmLocationName(type);
				reminder.setmMessage(msg);
				reminder.setmLati(lat1);
				reminder.setmLongi(log1);
				reminder.setmMapradius(rad);
				reminder.setmTask(task);
				reminder.setmTaskName(taskName);
				reminder.setmRepeat(repeat);
				reminder.setLocid(longid);
				reminderList.add(reminder);
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getLocalizedMessage();
		}
	}
	private void prepareListUse() {
		pickAddrList=(ListView) findViewById(R.id.pick_address_list);
		mAdapter = new FirstactivityadapterForTaskList(FirstActivity.this,placename, placemsg,alramtype, taskname,mapradius, tasktype);
		pickAddrList.setAdapter(mAdapter);
//		pickAddrList.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				System.out.println("111111111111111long presssssssssssssssssssssssss"+position);
//				onItemlongclick(position);
//				return true;
//			}
//		});
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onPause() {
		super.onPause();
		ClearData();
	}
	@Override
	protected void onResume() {
		super.onResume();
		ClearData();
		InitialzeArray();
		GetSavePlaceName();
		prepareListUse();
	}
	public void ClearData() {
		try {
			placename.clear();
			placemsg.clear();
			alramtype.clear();
			taskname.clear();
			mapradius.clear();
			tasktype.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void onItemlongclick(final int mPosition) {		
//		final CharSequence[] items={"Edit","Delete"};
//		// TODO Auto-generated method stub
//		AlertDialog.Builder customalert=new AlertDialog.Builder(FirstActivity.this);
//		customalert.setTitle("Select Option").setItems(items, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				if(items[which].equals("Edit")){
//					if(internetcheck==true){
//					ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
//					String name = reminderName.getmLocationName();
//					String msg = reminderName.getmMessage();
//					String lat1=reminderName.getmLati();
//					String log1=reminderName.getmLongi();
//					int rad=reminderName.getmMapradius();
//					String repeat=reminderName.getmRepeat();
//					int dbid=(int) reminderName.getLocid();
//					int tasktype=reminderName.getmTask();
//					String taskname=reminderName.getmTaskName();
//					String latlong=lat1+","+log1;
//					LocationActiviry.dbinsertupdate=true;
//					Intent setdata = new Intent(FirstActivity.this,AddTaskActivity.class);
//					setdata.putExtra("activity",1);
//					setdata.putExtra("location", name);
//					setdata.putExtra("Message", msg);
//					setdata.putExtra("arrayname", latlong);
//					setdata.putExtra("radius", rad);
//					setdata.putExtra("repeat", repeat);
//					setdata.putExtra("updateid", rad);
//					setdata.putExtra("taskid", tasktype);
//					setdata.putExtra("taskname", taskname);
//					setdata.putExtra("updateid", dbid);
//					startActivity(setdata);
//					}
//					else{
//						ToastMessage("Please check Internet connection");	
//					}
//				}
//				if(items[which].equals("Delete")){
//					AlertDialog.Builder alert = new AlertDialog.Builder(FirstActivity.this);
//					alert.setTitle("Do you want to Delete?"); 
//					alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int whichButton) {
//							ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
//							long dbid=reminderName.getLocid();
//							DbLocationObject locationObj = new DbLocationObject();	
//							locationObj.setId(dbid);
//							locationObj.getId();
//							DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
//							db.deleteFreqLocationDataObject(locationObj);
//							ToastMessage("Item deleted");
//							ClearData();
//							InitialzeArray();
//							GetSavePlaceName();
//							prepareListUse();
//						} 
//					}); // End of alert.setPositiveButton
//					alert.setNegativeButton("No",
//							new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int whichButton) {
//							dialog.cancel();
//						}
//					}); // End of alert.setNegativeButton
//					AlertDialog alertDialog = alert.create();
//					alertDialog.show();	
//				}
//			}
//		});
//		customalert.show();

		final Dialog dialog = new Dialog(FirstActivity.this);
		RelativeLayout edittask,deletetask;
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.edit_deletedialog);
		dialog.setCanceledOnTouchOutside(true);
		edittask=(RelativeLayout) dialog.findViewById(R.id.relativelayout_edittask);
		deletetask=(RelativeLayout) dialog.findViewById(R.id.relativelayout_deletetask);
		edittask.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(internetcheck==true){
					ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
					String name = reminderName.getmLocationName();
					String msg = reminderName.getmMessage();
					String lat1=reminderName.getmLati();
					String log1=reminderName.getmLongi();
					int rad=reminderName.getmMapradius();
					String repeat=reminderName.getmRepeat();
					int dbid=(int) reminderName.getLocid();
					int tasktype=reminderName.getmTask();
					String taskname=reminderName.getmTaskName();
					String latlong=lat1+","+log1;
					LocationActivity.dbinsertupdate=true;
					Intent setdata = new Intent(FirstActivity.this,AddTaskActivity.class);
					setdata.putExtra("activity",1);
					setdata.putExtra("location", name);
					setdata.putExtra("Message", msg);
					setdata.putExtra("arrayname", latlong);
					setdata.putExtra("radius", rad);
					setdata.putExtra("repeat", repeat);
					setdata.putExtra("updateid", rad);
					setdata.putExtra("taskid", tasktype);
					setdata.putExtra("taskname", taskname);
					setdata.putExtra("updateid", dbid);
					dialog.dismiss();
					startActivity(setdata);
//					}
//					else{
//						ToastMessage("Please check Internet connection");	
//					}	
				
			}
		});
		deletetask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				AlertDialog.Builder alert = new AlertDialog.Builder(FirstActivity.this);
				alert.setTitle("Do you want to Delete?"); 
				alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
						long dbid=reminderName.getLocid();
						DbLocationObject locationObj = new DbLocationObject();	
						locationObj.setId(dbid);
						locationObj.getId();
						DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
						db.deleteFreqLocationDataObject(locationObj);
						ToastMessage("Item deleted");
						ClearData();
						InitialzeArray();
						GetSavePlaceName();
						prepareListUse();
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
		});
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                System.out.println("????????Back button pressed?");
            }
        });
//		dialogButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(tempstate.length()>0){
//					messagetextvalue=tempstate;
//					dialog.dismiss();
//				tasktype=4;
//				action_name.setText("Wi-Fi : "+messagetextvalue);
//				}
//			}
//		});

		dialog.show();	
	
	}
	public void onItembulbclick(final int mPosition) {		
		ReminderInfo reminderName = (ReminderInfo) reminderList.get(mPosition);
		DbLocationObject locationObj = new DbLocationObject();
		String repeat=reminderName.getmRepeat();
		long idupdate=reminderName.getLocid();
		if(repeat.equalsIgnoreCase("0")){
			repeat="1";
			reminderName.setmRepeat("1");
		}
		else if(repeat.equalsIgnoreCase("1")){
			repeat="0";
			reminderName.setmRepeat("0");
		}
		System.out.println("repeat value is>>>>"+reminderName.getmRepeat());
		DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
		locationObj.setId(idupdate);
		locationObj.setRepeate(reminderName.getmRepeat());
		db.updateRepeatLocationDataObject(locationObj);
		ClearData();
		GetSavePlaceName();
		prepareListUse();
		System.out.println("get id at click"+locationObj.getId());
}
	public void onItemclick(final int mPosition) {
		setmenuInvisible();
		
	}
	private static long back_pressed;
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		setmenuInvisible();
		if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
		else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
		back_pressed = System.currentTimeMillis();
	}
}