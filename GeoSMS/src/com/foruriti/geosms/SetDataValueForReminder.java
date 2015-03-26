package com.foruriti.geosms;

import globalVariable.GlobalVariable;
import gps.GPSTracker;
import java.io.IOException;
import java.io.InputStream;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import adapter.SearchActivityAdapter;
import android.R.color;
import android.R.integer;
import android.R.string;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import bin.SearchValue;


import com.foruriti.locosms.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;




import conditionCheck.ConnectionDetector;

public class SetDataValueForReminder extends FragmentActivity implements
		OnClickListener,GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener{
	private EditText edtLocation, edtMessage;
	private String stringNumber="";
	public static SetDataValueForReminder Instance;
	static String valueLocation, LOgiLatiValue, ValueMessage,late,repeatvalue;
	int valueradius,activityvalue;
	public static int idforupdate;
	private SavePreferences savePreferences;
	ImageView btnSubmitt,serachicon;
//	RadioButton radioRoutineButton,r1,r2;
//	RadioGroup radioRoutineGrop;
	public static FragmentManager fragmentManager;
	public static String repeat,completeADD="",country = "";
//	String longitude,latitude;
	public static String arrayName;
	DataSourceHandler db;
	double latS,longS;
	LocationClient mLocationClient;
	public static boolean dbinsertupdate=false;
	private TextView showArriveFence;
	private SeekBar setArriveFence;
	public static int aArriveProgress;
	public static int setArriveRangeProgress,mapradius;
	private String geoFenceText = "Radius: $ Meters";
	private SharedPreferences sharedPreferences;
	// add here for pin
	GoogleMap googleMap;
	MarkerOptions markerOptions;
	CircleOptions c_options;
	Marker marker;
	LatLng latLng;
	Circle shape;
	private List<Address> tempaddresses;
	private double templat,templng;
	private String cNumber;
	public static  float accuracylevel=50000;
	public static boolean soundstatus=true,vibratestatus=true;
	//variable from searchloc
	String aJStatus = "";
	JSONArray aJResults = null;
	JSONObject aJObject = null;
	ArrayList<String> aJAddresses = new ArrayList<String>();
	GPSTracker gps;
	ConnectionDetector connectionDetector;
	
	List<DbLocationObject> value;
	private List<Address> tempadd;
	ArrayList<SearchValue> serchValueList;
	SearchActivityAdapter mAdapter;
	ArrayList<String> namelist;
	ArrayList<String> namelistforshow;
	public static double Latitude, Longitude;
	ListView listview;
	AlertDialog alertDialog;
	private double latitude,longitude;
	private boolean dis;
	private List<Address> tempaddress;
	private String getcompadd="";
	private Geocoder geocoder;
	private ProgressDialog pDialog; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		fragmentManager = getSupportFragmentManager();
		setContentView(R.layout.set_data_value_for_reimnder);
		geocoder = new Geocoder(SetDataValueForReminder.this, Locale.getDefault());
		connectionDetector = new ConnectionDetector(getApplicationContext());
		gps = new GPSTracker(SetDataValueForReminder.this);
		initilizeView();
		Intent location = getIntent();
		if (location != null) {
			activityvalue=location.getExtras().getInt("activity");
		}
		if(activityvalue==2){
			latS=gps.getLatitude();
			 longS=gps.getLongitude();
		callMappage();
		}
		else if(activityvalue==1){
			valueLocation = location.getExtras().getString("location");
			LOgiLatiValue = location.getExtras().getString("arrayname");
			ValueMessage = location.getExtras().getString("Message");
			valueradius=location.getExtras().getInt("radius");
			repeatvalue=location.getExtras().getString("repeat");
			System.out.println("??????????????????"+repeatvalue+"..."+valueradius);
//			late=location.getExtras().getString("latitude");
			
//			if (valueLocation != null) {
//				edtLocation.setText(valueLocation);
//			}
			if (LOgiLatiValue != null) {
				arrayName = LOgiLatiValue;
			}
			if (ValueMessage != null) {
//				edtMessage.setText(ValueMessage);
			}
			if(valueradius != 0){
//				(aArriveProgress+1)*200;
				mapradius=valueradius;
				System.out.println("map radius>>>>>"+mapradius);
			}
//System.out.println("at latlong value edit"+arrayName);
			latS=Double.parseDouble(arrayName.substring(0, arrayName.indexOf(",")));
			longS=Double.parseDouble(arrayName.substring(arrayName.indexOf(",")+1));
//			StartOnResumeFragments();
//			initradiusdata();
			callMappage();
			}
			
//		}
//		System.out.println("lats>>>>"+latS+"......longs"+longS);
		serachicon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str=edtLocation.getText().toString();
				String location = "";
				location = edtLocation.getText().toString().trim();
				if (location.equalsIgnoreCase("")) {
					ToastMessage("Type Street,City,State");
				} else {
					boolean internet = connectionDetector.isConnectingToInternet();
					if (internet == true) {
//						namelist.clear();
//						namelistforshow.clear();
						GetAdress getcontact = new GetAdress();
						getcontact.execute(location);
						
					} else {
						ToastMessage("NO Internet");
					}

				}
			}
		});
		btnSubmitt.setOnClickListener(this);
			
	}

	private void initilizeView() {
//		edtLocation = (EditText) findViewById(R.id.edtLocation);
		edtLocation = (EditText) findViewById(R.id.edtlocation);
//		edtMessage = (EditText) findViewById(R.id.edtmessage);
		btnSubmitt = (ImageView) findViewById(R.id.btnSubmitt);
		serachicon=(ImageView) findViewById(R.id.search_icon);
		serchValueList = new ArrayList<SearchValue>();
		namelist = new ArrayList<String>();
		namelistforshow= new ArrayList<String>();
//		radioRoutineGrop = (RadioGroup) findViewById(R.id.radiorutine);
//		r1=(RadioButton) findViewById(R.id.radioDaily);
//		
//		r2=(RadioButton) findViewById(R.id.radiOneTime);
//		if(repeatvalue==null){
//		r2.setChecked(true);}
		db = DataSourceHandler.getInstance(SetDataValueForReminder.this);

	}
	private void initradiusdata(){
	Instance = SetDataValueForReminder.this;
//	settingsContext = SettingsActivity.this;
	savePreferences = new SavePreferences();
//	showArriveFence = (TextView)this.findViewById(R.id.radius_distance);
	setArriveFence = (SeekBar)this.findViewById(R.id.set_radius);
//	setArriveFence.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar));
	setArriveFence.setMax(5);
	setArriveFence.setProgress(0);

	if(initMap()){
//		Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT).show();
		gotoLocation(latS, longS, 15);
		
		googleMap.setMyLocationEnabled(true);
		mLocationClient = new LocationClient(this,this,this);
		mLocationClient.connect();
	}else{
//		Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
	}
	StartOnResumeFragments();
//	shape.remove();
//	shape=null;
//	shape=drawCircle(latLng);
	
	
	}




	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSubmitt:
//			if(!edtMessage.getText().toString().equalsIgnoreCase("")|| !edtMessage.getText().toString().equalsIgnoreCase(null) && !edtLocation.getText().toString().equalsIgnoreCase("")||!edtLocation.getText().toString().equalsIgnoreCase(null)){
			if(dbinsertupdate==false){
				System.out.println("at here edit.....0"+dbinsertupdate);
				System.out.println("completeadd>> at check"+completeADD);
			if (CheckStringNameToDataBase(completeADD
					.trim()) != false) {
				ToastMessage("Name Already exits");
			} else {
//				String arraypass = arrayName;
//				String[] Location = arraypass.split(",");
//				double locallatitude=Double.parseDouble(Location[0]);
//				double locallongitude=Double.parseDouble(Location[1]);
//				if(completeADD==""){
//				 try {
//					tempaddresses = geocoder.getFromLocation(locallatitude, locallongitude, 1);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				 if(tempaddresses!=null){
//					completeADD=tempaddresses.get(0).getAddressLine(0)+tempaddresses.get(0).getAddressLine(1)+tempaddresses.get(0).getAddressLine(2);
//				
//				 }
//				 else{
//					 completeADD=sharedPreferences.getString("completeadd", "");
////					 completeADD="not get your location" ;
//				 }}
//				 System.out.println("value at submit>>>>"+arrayName+"....completeadd"+completeADD);
				 savePreferences.SavePreferences(SetDataValueForReminder.this, "completeadd", completeADD);
				Intent intent = new Intent(SetDataValueForReminder.this,
						ActionSelectorActivity.class);
				startActivity(intent);
				finish();
//				SetDataValueForReminder.this.finish();}
//				else{
//				ToastMessage("Please enter Location and message");	
//				}
				break;
//			}
			
		}}else{
			 savePreferences.SavePreferences(SetDataValueForReminder.this, "completeadd", completeADD);
				Intent intent = new Intent(SetDataValueForReminder.this,
						ActionSelectorActivity.class);
//				valueLocation = location.getExtras().getString("location");
//				LOgiLatiValue = location.getExtras().getString("arrayname");
//				ValueMessage = location.getExtras().getString("Message");
//				valueradius=location.getExtras().getInt("radius");
//				repeatvalue=location.getExtras().getString("repeat");
				intent.putExtra("activity",1);
				intent.putExtra("location", completeADD);
				intent.putExtra("Message", ValueMessage);
				intent.putExtra("arrayname", arrayName);
				intent.putExtra("radius", valueradius);
				intent.putExtra("repeat", repeatvalue);
				startActivity(intent);
				finish();
		}
//			else{
//				System.out.println("at here edit.....1");
////				int selectedId = radioRoutineGrop.getCheckedRadioButtonId();
////				radioRoutineButton = (RadioButton) findViewById(selectedId);
////				String bullonChhose = radioRoutineButton.getText().toString();
////				if (bullonChhose.equalsIgnoreCase("Every time")) {
////					repeat = "1";
////				}
////				if (bullonChhose.equalsIgnoreCase("One Time")) {
////					repeat = "0";
////				}
//
////				if (CheckStringNameToDataBase(edtLocation.getText().toString()
////						.trim()) != false) {
////					ToastMessage("Name Already exits");
////				} else {
//					
//					if(arrayName!=null ){
//					// adding to database
//						String arraypass = arrayName;
//						String[] Location = arraypass.split(",");
//					DbLocationObject locationObj = new DbLocationObject();
//					locationObj.setLocationLat(Location[0]);
//					locationObj.setLocationLong(Location[1]);
//					if(completeADD!=""){
//						locationObj.setLocationType(completeADD);
//					}else{
//						locationObj.setLocationType(completeADD);
//					}
////					locationObj.setLocationType(edtLocation.getText().toString());
//					locationObj.setLocationProvider("GPS");
//					locationObj.setMessage(ValueMessage);
////					locationObj.setRepeate(repeat);
//					locationObj.setMapradius(mapradius);
//					locationObj.setId(idforupdate);
//					locationObj.getId();
//					System.out.println("get id at setdata"+locationObj.getId());
////					DataSourceHandler db = DataSourceHandler
////							.getInstance(getApplicationContext());
////					db.updateFreqLocationDataObject(locationObj);
//					dbinsertupdate=false;
////					Intent firstActivity = new Intent(SetDataValueForReminder.this,
////							FirstActivity.class);
////					startActivity(firstActivity);
////					SetDataValueForReminder.this.finish();
//					 savePreferences.SavePreferences(SetDataValueForReminder.this, "completeadd", completeADD);
//						Intent intent = new Intent(SetDataValueForReminder.this,
//								ActionSelectorActivity.class);
//						intent.putExtra("updateid", idforupdate);
//						startActivity(intent);
//						finish();
//					}
//					else{
//						ToastMessage("Please try again to fetch your location");		
//					}
//					break;
////				}	
//			}
		
		}
			

	}

	private boolean CheckStringNameToDataBase(String string) {

		try {
			List<DbLocationObject> value = db.getAllFreqLocationDataObject();
			for (DbLocationObject valueName : value) {
				String placename = valueName.getLocatonType().toString().trim();
				if (placename.equalsIgnoreCase(string)) {
					GlobalVariable.dis = true;
					return GlobalVariable.dis;

				} 
				else {
					GlobalVariable.dis = false;

				}

				// checkAndResult(ddd);

			}
			

		} catch (Exception e) {
			e.printStackTrace();
			e.getLocalizedMessage();
		}
		return GlobalVariable.dis;

	}

	
	public void onResume(){
		super.onResume();
//		StartOnResumeFragments();
		
	}
	public void onResumeFragments(){
		super.onResumeFragments();
//		StartOnResumeFragments();
	}
	
	
	public void onStop(){
		super.onStop();
	}
	@Override
	protected void onPause() {
		super.onPause();
//		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// mainInstance.GetAlertDialog(SettingsActivity.this, "onKeyDown",
			// APP_NAME, "Okay To Close?", "Yes", null, "No");
			dbinsertupdate=false;
			Intent intent = new Intent(SetDataValueForReminder.this,
					FirstActivity.class);
			startActivity(intent);
			SetDataValueForReminder.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void StartOnResumeFragments() {
		try {
			sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SetDataValueForReminder.this);
			if(dbinsertupdate==false){
			aArriveProgress = 0;
			System.out.println("aArriveProgress"+aArriveProgress);
//			showArriveFence.setText(geoFenceText.replace("$", Integer.toString((aArriveProgress+1)*200)));
			}
			else if(dbinsertupdate==true){
				getradiusProgressbarvalue(valueradius);
				aArriveProgress=radiusProgressbarvalue;	
//				setArriveFence.setProgress(aArriveProgress);
				
//				showArriveFence.setText(geoFenceText.replace("$",""+valueradius));
			}
			setArriveFence.setProgress(aArriveProgress);
//			showArriveFence.setText(geoFenceText.replace("$", Integer.toString((aArriveProgress+1)*200)));
						if(dbinsertupdate==false){
						mapradius=200;
						}else if(dbinsertupdate==true){
						mapradius=valueradius;	
						}
//						shape.remove();
						shape = drawCircle(latLng);
			OnSeekBarChangeListener aSetArriveRangeListener = new OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					setArriveRangeProgress = progress;
					int aArriveFence = -1;
//					if(geoFenceChecked){
						aArriveFence = (progress+1)*200;
						
						if(aArriveFence>0){
//							showArriveFence.setText(geoFenceText.replace("$", Integer.toString(aArriveFence)));
//						}
							mapradius=((progress+1)*200);
					}
						if(shape!=null){
						shape.remove();
						shape = null;
//						c_options.radius(mapradius);
						shape = drawCircle(latLng);
						}
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					savePreferences.SavePreferences(SetDataValueForReminder.this, "radius_value", setArriveRangeProgress);
//					
				}
				
			};
			setArriveFence.setOnSeekBarChangeListener(aSetArriveRangeListener);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	byte radiusProgressbarvalue;
	private int getradiusProgressbarvalue(int value){
		if(value<=200){
			radiusProgressbarvalue=0;
		}
		else if(value>200 && value<=400){
			radiusProgressbarvalue=1;	
		}
		else if(value>400 && value<=600){
			radiusProgressbarvalue=2;	
		}
		else if(value>600 && value<=800){
			radiusProgressbarvalue=3;	
		}
		else if(value>800 && value<=1000){
			radiusProgressbarvalue=4;	
		}
		else if(value>1000){
			radiusProgressbarvalue=5;	
		}
		return radiusProgressbarvalue;
	}
	private boolean initMap() {
		markerOptions = new MarkerOptions();
		c_options = new CircleOptions();	
		if (googleMap == null) {
			SupportMapFragment mapFrag =
					(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map);
			googleMap = mapFrag.getMap();
			latLng = new LatLng(latS,longS);
//			System.out.println("inside latlong obj"+latS+markerOptions);
			
			markerOptions.position(new LatLng(latS, longS));
			markerOptions.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.marker));

			googleMap.addMarker(markerOptions);
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
			if (googleMap != null ) {				
					googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {
						@Override
						public void onMapLongClick(LatLng ll) {
							latS=latLng.latitude;
							longS=latLng.longitude;
//							 getCompleteAddress getcompaddress=new getCompleteAddress();
//							 getcompaddress.execute("");
//								Geocoder gc = new Geocoder(SetDataValueForReminder.this);								
//								List<Address> list = null;
//								try {
//									list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
//								
//								Address add = list.get(0);
								googleMap.clear();
								System.out.println("calling>>>setmarker>>>>"+latS+"..."+longS);
								if(completeADD!="" && completeADD!=null){
								SetDataValueForReminder.this.setMarker(completeADD, country,latS, longS);}
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();							
//								System.out.println("inside catch block---------------------------------");								
//							}
						}
					});

					googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

						@Override
						public boolean onMarkerClick(Marker marker) {
							String msg = marker.getTitle() + " (" + marker.getPosition().latitude + 
									"," + marker.getPosition().longitude + ")";
								marker.setTitle(completeADD);
							return false;
						}
					});

					googleMap.setOnMarkerDragListener(new OnMarkerDragListener() {

						@Override
						public void onMarkerDragStart(Marker marker) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onMarkerDragEnd(Marker marker) {
							shape.remove();
							shape = null;
							latS=latLng.latitude;
							longS=latLng.longitude;
							 getCompleteAddress getcompaddress=new getCompleteAddress();
							 getcompaddress.execute("");
							
						}

						@Override
						public void onMarkerDrag(Marker marker) {
							// TODO Auto-generated method stub
							
							try {
								shape.remove();
								shape = null;
								latLng=marker.getPosition();
								shape = drawCircle(latLng);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								latLng=marker.getPosition();
								shape = drawCircle(latLng);
							}
							
						}
					});			

			}
		}
		return (googleMap != null);
	}
	private void setMarker(String locality, String country, double lat, double lng) {
//		latLng = new LatLng(lat,lng);

//		MarkerOptions options = new MarkerOptions()	
		
		try {
		markerOptions.title(locality);
		markerOptions.position(new LatLng(lat,lng));

		
		markerOptions.anchor(.5f, .5f);
		markerOptions.draggable(true);
		
		if (country.length() > 0) {
			markerOptions.snippet(country);
		}

		if (marker != null) {
			removeEverything();
		}

		
		marker = googleMap.addMarker(markerOptions);
		System.out.println("shape is"+shape+markerOptions);
		if(shape!=null){
		shape.remove();
		shape=null;		
//		markerOptions=null;
//		markerOptions=new MarkerOptions();
		shape = drawCircle(latLng);
		}
		else{
			System.out.println("inside else");
//			markerOptions.position(new LatLng(latS, longS));
//			marker = googleMap.addMarker(markerOptions);
			marker.remove();
			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
			googleMap.addMarker(markerOptions);
			shape = drawCircle(latLng);	
			
		}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("inside exception setmarker"+e);
			markerOptions.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.marker));
//			SetDataValueForReminder.this.setMarker(list.get(0).getLocality(), list.get(0).getCountryName(),ll.latitude, ll.longitude);
//			latLng = new LatLng(ll.latitude,ll.longitude);
			shape = drawCircle(new LatLng(lat, lng));
		}
	}
	private Circle drawCircle(LatLng ll) {
		c_options.center(ll);
		c_options.radius(mapradius);
//		.fillColor(0x330000FF)
//		c_options.strokeColor(Color.parseColor("#00B6EF"));
		c_options.strokeColor(Color.RED);
		c_options.strokeWidth(3);
		return googleMap.addCircle(c_options);
	}

	private void removeEverything() {
		marker.remove();
		marker = null;
		if(shape!=null){
		shape.remove();
		shape = null;}
	}
	private void gotoLocation(double lat, double lng,
			float zoom) {
		LatLng ll = new LatLng(lat, lng);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
		googleMap.moveCamera(update);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
protected void callMappage() {
	// TODO Auto-generated method stub
	
//			getAddress getaddress=new getAddress();
//			getaddress.execute("");
	 getCompleteAddress getcompaddress=new getCompleteAddress();
	 getcompaddress.execute("");
}
private void ToastMessage(String msg) {
	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
}
//private class getAddress extends AsyncTask<String, Void, String> {
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//		pDialog = new ProgressDialog(SetDataValueForReminder.this,R.style.Theme_MyDialog);
//		pDialog.setMessage("Please wait...");
//		pDialog.setCancelable(false);
//		pDialog.show();
//		System.out.println("at onpreexecute>>>>>>"+latS);
//	}
//	@Override
//	protected String doInBackground(String... params) {
////	templat=marker.getPosition().longitude;
////	templng=marker.getPosition().longitude;
//	 try {
//		tempaddress = geocoder.getFromLocation(latS, longS, 1);
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	 if(tempaddress!=null){
//		 getcompadd=tempaddress.get(0).getAddressLine(0)+"\n"+tempaddress.get(0).getAddressLine(1)+"\n"+tempaddress.get(0).getAddressLine(2);
//	 }
//		
//		return getcompadd;
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//		// TODO Auto-generated method stub
//		super.onPostExecute(result);
//		if(tempaddress!=null){
//			 getcompadd=tempaddress.get(0).getAddressLine(0)+"\n"+tempaddress.get(0).getAddressLine(1)+"\n"+tempaddress.get(0).getAddressLine(2);
//			
//		initradiusdata();
////		String aJAddress = latitude + "," + longitude;
//			arrayName= gps.getLatitude() + "," + gps.getLongitude();
//		if (pDialog.isShowing())
//			pDialog.dismiss();
//		}
//		else{
////			String aJAddress = latitude + "," + longitude;
//			arrayName= gps.getLatitude() + "," + gps.getLongitude();
//			 completeADD="palam";
//		initradiusdata();
//			if (pDialog.isShowing())
//				pDialog.dismiss();
//		}
////		StartOnResumeFragments();
//	}
//
//}
private class getCompleteAddress extends AsyncTask<String, Void, String> {
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(SetDataValueForReminder.this,R.style.Theme_MyDialog);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);
		pDialog.show();
//		System.out.println("at onpreexecute>>>>>>"+latS);
	}
	@Override
	protected String doInBackground(String... params) {
		 String address1 = "";
		    String  address2 = "";
		    String  city = "";
		    String state = "";
//		    String country = "";
		    String county = "";
		    String  PIN = "";
		     String full_address = "";
		     completeADD="";
		     try {
		         JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" +latS + ","
		                 + longS + "&sensor=true");
//		         System.out.println("status is>>>>"+jsonObj.getString("status"));
		         String Status = jsonObj.getString("status");
		        
		         if (Status.equalsIgnoreCase("OK")) {
		             JSONArray Results = jsonObj.getJSONArray("results");
		             JSONObject zero = Results.getJSONObject(0);
		             JSONArray address_components = zero.getJSONArray("address_components");

		             for (int i = 0; i < address_components.length(); i++) {
		                 JSONObject zero2 = address_components.getJSONObject(i);
		                 String long_name = zero2.getString("long_name");
		                 JSONArray mtypes = zero2.getJSONArray("types");
		                 String Type = mtypes.getString(0);

		                 if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
		                     if (Type.equalsIgnoreCase("street_number")) {
		                         address1 = long_name + " ";
		                         System.out.println("at forloop>>>>"+address1);
		                     } else if (Type.equalsIgnoreCase("route")) {
		                         address1 = address1 + long_name;
		                         System.out.println("at forloop>>>>111"+address1);
		                     } else if (Type.equalsIgnoreCase("sublocality")) {
		                         address2 = long_name;
		                         System.out.println("at forloop>>>>222"+address2);
		                     } else if (Type.equalsIgnoreCase("locality")) {
		                         // Address2 = Address2 + long_name + ", ";
		                         city = long_name;
		                         System.out.println("at forloop>>>>333"+city);
		                     } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
		                         county = long_name;
		                     } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
		                         state = long_name;
		                     } else if (Type.equalsIgnoreCase("country")) {
		                         country = long_name;
		                     } else if (Type.equalsIgnoreCase("postal_code")) {
		                         PIN = long_name;
		                     }
		                    
		                 }

//		                 full_address = address1 +","+address2+","+city+","+state+","+country+","+PIN;
		                 full_address = address1 +","+city+","+state+","+country+","+PIN;
		                 
		             }
		         }

		     } catch (Exception e) {
		         e.printStackTrace();
		     }
		           return full_address;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result!=null){
//			 getcompadd=tempaddress.get(0).getAddressLine(0)+"\n"+tempaddress.get(0).getAddressLine(1)+"\n"+tempaddress.get(0).getAddressLine(2);
//			System.out.println("address at onpost>>>>>"+result);
		try {
			initradiusdata();
//		String aJAddress = latitude + "," + longitude;
				arrayName= latS + "," + longS;
				completeADD=result;
				if(completeADD.charAt(0)==','||completeADD.charAt(1)==','){
					System.out.println("check comma"+completeADD.indexOf(','));
					completeADD = completeADD.substring(completeADD.indexOf(",")+1,completeADD.length());
				}
			if (pDialog.isShowing())
				pDialog.dismiss();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		}
		else{
			if (pDialog.isShowing())
				pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(), "No Address found! please try again", Toast.LENGTH_SHORT).show();			
				}
			});
		}
//		StartOnResumeFragments();
	}

}
public  JSONObject getJSONfromURL(String url) {

    // initialize
    InputStream is = null;
    String result = "";
    JSONObject jObject = null;
    ServiceHandler sh=null;
    // http post
    sh = new ServiceHandler();
    
	try {
		result = sh.makeServiceCall(url,ServiceHandler.GET);
//		System.out.println(">>>>>>>>212222222"+result);
//		System.out.println("jsonstr at do in background"+jsonStr);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();

	}
    // try parse the string to a JSON object
	if(result!=null){
    try {
        jObject = new JSONObject(result);
    } catch (JSONException e) {
        Log.e("log_tag", "Error parsing data " + e.toString());
    }
	}
    return jObject;


}
@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub
	
}
private class GetAdress extends AsyncTask<String, Void, Void> {

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
//		System.out.println("namelist>>>>>>>>>>>>>>preexecute"+namelist);
		if(aJAddresses==null){
			aJAddresses = new ArrayList<String>();
		}
		 if(namelist==null){
			namelist = new ArrayList<String>();
			
		}
		 if(namelistforshow==null){
			namelistforshow= new ArrayList<String>();
		}
		if(namelist!=null && namelistforshow!=null){
		namelist.clear();
		namelistforshow.clear();
		
		}
//		aJAddresses.clear();
		
		pDialog = new ProgressDialog(SetDataValueForReminder.this,R.style.Theme_MyDialog);
		pDialog.setMessage("Please wait...");
	
		pDialog.setCancelable(false);
		pDialog.show();

	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (pDialog.isShowing())
			pDialog.dismiss();
		/*
		 * listAdapter = new ArrayAdapter<String>(getApplicationContext(),
		 * android.R.layout.simple_list_item_1, 0, namelist);
		 */
//		System.out.println("value at onpostexecute>>>>>>>"+namelist+"......"+namelistforshow+"adapter is!!!"+mAdapter);
		if(namelist!=null && namelistforshow !=null){
		mAdapter = new SearchActivityAdapter(SetDataValueForReminder.this,
				namelist,namelistforshow);
		
//		listview.setAdapter(mAdapter);
//		CharSequence[] items = {"Red", "Green", "Blue"};
//		searchDialogList(items);
		
		
		}
		else {
			if (pDialog.isShowing())
				pDialog.dismiss();
			
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(), "No search keyword found! please try again", Toast.LENGTH_SHORT).show();			
				}
			});
//			Toast.makeText(getApplicationContext(), "No search keyword found! please try again", Toast.LENGTH_SHORT).show();			
		}
		
		
		calllistValu(namelist,namelistforshow,mAdapter);
	}
	

	
	
	@Override
	protected Void doInBackground(String... params) {
		
			//key generated by surya for searching...but in mainifest other
		String  key = "AIzaSyDAApso7SntglBnRvFh7MZ0gRs8IsbasFA";
//			String key = "AIzaSyDZcEHHESH5zg7NfKvdp-w5OAE_KUpyPNg";
//			System.out.println(">>>>>>>>"+params);
			String new_addr_param = params[0].toString();
			new_addr_param=new_addr_param.replaceAll(" ", "%20");
//			url from pawan sir https://maps.googleapis.com/maps/api/place/textsearch/json?query=newdelhi&sensor=true&key=AIzaSyDtbjdfwjLt1qj2FhLvmF4uCS2250kPREc
//			String aPlacesApiUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+new_addr_param+"&sensor=true&key="+key;//old url
			String aPlacesApiUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+new_addr_param+"&sensor=true&key="+key;
//			System.out.println(">>>>>>>>1111111111"+new_addr_param);
			ServiceHandler sh = new ServiceHandler();
			// Making a request to url and getting response

			String jsonStr="";
			try {
				jsonStr = sh.makeServiceCall(aPlacesApiUrl,ServiceHandler.GET);
//				System.out.println(">>>>>>>>212222222"+jsonStr);
//				System.out.println("jsonstr at do in background"+jsonStr);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

			}
			try {

				if(jsonStr!=null){
//					System.out.println("at executing do in background>>>>>>>"+jsonStr);
				aJObject = new JSONObject(jsonStr);
				if (aJObject != null ) {
//					System.out.println("aJObject.length: " + aJObject.length());
					aJStatus = aJObject.getString("status");
					if (aJStatus != null && aJStatus.length() != 0
							&& aJStatus.equals("OK")) {
						aJResults = aJObject.getJSONArray("results");

						int aJResultsLength = aJResults.length();
						if (aJResultsLength > 5) {
							aJResultsLength = 5;
						}
//						System.out.println("at ajstatus is ok"+aJResultsLength);
//						namelist.clear();
//						namelistforshow.clear();
//						aJAddresses.clear();

						for (int i = 0; i < aJResultsLength; i++) {
							JSONObject aJFormattedAddressObject = aJResults
									.getJSONObject(i);
							String aJFormattedAddress = aJFormattedAddressObject
									.getString("formatted_address");
							String aJName = aJFormattedAddressObject
									.getString("name");
//							String aJNamedetail = aJFormattedAddressObject.optString("name");
							JSONObject aJGeometry = aJFormattedAddressObject
									.getJSONObject("geometry");
							JSONObject aJLocation = aJGeometry
									.getJSONObject("location");
							String aJLat = aJLocation.getString("lat");
							String aJLng = aJLocation.getString("lng");
							String aJAddress = aJLat + "," + aJLng;
							SearchValue bin = new SearchValue();
							bin.setFullName(aJFormattedAddress);
							bin.setName(aJName);
							bin.setGeoPoint(aJAddress);
							serchValueList.add(bin);
							if(aJAddress!=null && aJFormattedAddress!=null && namelist!=null ){
							aJAddresses.add(aJAddress);
							namelist.add(aJFormattedAddress);
							}
							if(namelistforshow!=null && aJName!=null){
							namelistforshow.add(aJName+":");
							}
						}
					} else {
						aJAddresses = null;
						namelist=null;
						namelistforshow=null;
					}
				} else {
					aJAddresses = null;
					namelist=null;
					namelistforshow=null;
//					serchValueList=null;
//					Toast.makeText(context, "No search keyword found! please try again", Toast.LENGTH_LONG).show();	
				}}
				else
				{
					aJAddresses = null;
					namelist=null;
					namelistforshow=null;
//					serchValueList=null;
//					Toast.makeText(getApplicationContext(), "No search keyword found! please try again", Toast.LENGTH_LONG).show();		
				}
			} catch (JSONException e) {
				System.out.println("JSONException in DistTimeDGoogleAPI: " + e);
				aJAddresses = null;
				
			}

			
		
		return null;
	}

}
public void calllistValu(ArrayList<String> namelist2,
		ArrayList<String> namelistforshow2, SearchActivityAdapter mAdapter2) {
	// TODO Auto-generated method stub
//	System.out.println("value at call list value"+namelist2+".."+namelistforshow2+"..."+mAdapter2);
	try {
		searchDialogList(mAdapter2);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void searchDialogList(SearchActivityAdapter mAdapter) {
//	final CharSequence[] items = {"Red", "Green", "Blue"};
//	ArrayList<String> namelist=new ArrayList<String>();
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Select an address");
  	builder.setCancelable(false);
  	builder.setPositiveButton("OK", null);
  	builder.setAdapter(mAdapter, new DialogInterface.OnClickListener(){

		@Override
		public void onClick(DialogInterface dialog, int position) {
			// TODO Auto-generated method stub
			String arrayname1 = aJAddresses.get(position).toString();
//			Toast.makeText(getApplicationContext(), "clicked"+position, Toast.LENGTH_SHORT).show();	
			System.out.println("ajaddress.getpos"+aJAddresses.get(position)+"..."+arrayname1);
			latS=Double.parseDouble(arrayname1.substring(0, arrayname1.indexOf(",")));
			longS=Double.parseDouble(arrayname1.substring(arrayname1.indexOf(",")+1));
			System.out.println(" lats and longs here"+latS+"..."+longS);
//			Geocoder gc = new Geocoder(SetDataValueForReminder.this);								
//			List<Address> list = null;
			try {
//				list = gc.getFromLocation(latS,longS, 1);
			latLng=new LatLng(latS, longS);
//			Address add = list.get(0);
//			completeADD="";
//			System.out.println("addddddddddddddddd"+add.getAddressLine(0)+add.getAddressLine(1)+add.getAddressLine(2));
//			completeADD=add.getAddressLine(0)+add.getAddressLine(1)+add.getAddressLine(2);
			getCompleteAddress getcompaddress=new getCompleteAddress();
			 getcompaddress.execute("");
			edtLocation.setText("");
			googleMap.clear();
//			markerOptions.position(new LatLng(latS, longS));
//			googleMap.addMarker(markerOptions);
			System.out.println("shat===="+shape);
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
			SetDataValueForReminder.this.setMarker(completeADD, country,latS, longS);
			shape.remove();
			shape=null;
			}
			catch(Exception e){
			e.printStackTrace();	
			}
		}
  		
  	});
//	builder.setSingleChoiceItems(mAdapter, -1, new DialogInterface.OnClickListener() {
//	    public void onClick(DialogInterface dialog, int item) {
//	        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
//	    }
//	});
	AlertDialog alert = builder.create();
	alert.show();
}

public void onItemClick(int mPosition) {
	// TODO Auto-generated method stub
	
}

}
