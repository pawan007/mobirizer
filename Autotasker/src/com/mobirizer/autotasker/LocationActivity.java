package com.mobirizer.autotasker;

import gps.ConnectionDetector;
import gps.GPSTracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import bin.SearchActivityAdapter;
import bin.SearchValue;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
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
public class LocationActivity extends FragmentActivity implements OnClickListener,GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener, LocationListener{
	private EditText edtLocation;
	private int valueradius,activityvalue;
	public static int aArriveProgress,setArriveRangeProgress,mapradius;
	private SavePreferences savePreferences;
	private ImageView btnSubmitt,serachicon,recentview;
	public static FragmentManager fragmentManager;
	private String completeADD="",country = "",LOgiLatiValue="",arrayName="",aJStatus = "",tempSearchvalue="";
	private double latS,longS;
	private byte radiusProgressbarvalue;
	private boolean addcheck=false;
	public static boolean dbinsertupdate=false;
	private SeekBar setArriveFence;
	// add here for pin
	private GoogleMap googleMap;
	private MarkerOptions markerOptions;
	private CircleOptions c_options;
	private Marker marker;
	private LatLng latLng;
	private Circle shape;
	//variable from searchloc
	private JSONArray aJResults = null;
	private JSONObject aJObject = null;
	private ArrayList<String> aJAddresses = new ArrayList<String>();
	private GPSTracker gps;
	private ConnectionDetector connectionDetector;
	private ArrayList<SearchValue> serchValueList;
	private SearchActivityAdapter mAdapter;
	private ArrayList<String> namelist;
	private ArrayList<String> namelistforshow;
	ListView listview;
	private ArrayList<String> myTempList;
	private ProgressDialog pDialog; 
	SharedPreferences sharedPreferences;
	DbLocationObject searchresult;
	/** The view to show the ad. */
	private AdView adView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		fragmentManager = getSupportFragmentManager();
		setContentView(R.layout.activity_location);
		connectionDetector = new ConnectionDetector(getApplicationContext());
		sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		gps = new GPSTracker(LocationActivity.this);
		searchresult = new DbLocationObject();
		initilizeView();
		Intent location = getIntent();
		if (location != null) {
			activityvalue=location.getExtras().getInt("activity");
		}
		if(activityvalue==2){
//			latS=gps.getLatitude();
//			 longS=gps.getLongitude();
			 LOgiLatiValue = location.getExtras().getString("arrayname");
//			 System.out.println("1111111111111111*****"+LOgiLatiValue);
			 if (LOgiLatiValue != null) {
					arrayName = LOgiLatiValue;
					latS=Double.parseDouble(arrayName.substring(0, arrayName.indexOf(",")));
					longS=Double.parseDouble(arrayName.substring(arrayName.indexOf(",")+1));
//					System.out.println("1111111111111111**111***"+arrayName+"..lats"+latS+"..."+longS);
				}
		callMappage();
		}
		else if(activityvalue==1){
			LOgiLatiValue = location.getExtras().getString("arrayname");
			valueradius=location.getExtras().getInt("radius");
			if (LOgiLatiValue != null) {
				arrayName = LOgiLatiValue;
			}
			if(valueradius != 0){
				mapradius=valueradius;
			}
			latS=Double.parseDouble(arrayName.substring(0, arrayName.indexOf(",")));
			longS=Double.parseDouble(arrayName.substring(arrayName.indexOf(",")+1));
			callMappage();
			}
		serachicon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideKeyboard(v);
				edtLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			        @Override
			        public void onFocusChange(View v, boolean hasFocus) {
			            if (!hasFocus) {
			                hideKeyboard(v);
			            }
			        }
			    });
//				String str=edtLocation.getText().toString();
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
		recentview.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showRecentVieweditem();
			}
		});
		btnSubmitt.setOnClickListener(this);
		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(getResources().getString(R.string.Banner_ad_id));
		LinearLayout layout = (LinearLayout) findViewById(R.id.add_layout);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);	
	}
private void showRecentVieweditem(){
	try {
		DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
		List<DbLocationObject> value = db.getAllRecentSearchDataObject();
//		if(value.size()==0){
		if(myTempList!=null && !myTempList.isEmpty()){
			myTempList.clear();
		}
		for (DbLocationObject valueName : value) {
			String type = valueName.getLocatonType();
//			System.out.println("11111value at showrecent"+type);
//			String lat1=valueName.getLocationLat();
//			String log1=valueName.getLocationLong();
			if(myTempList.size()>9){
				myTempList.remove(0);
//				db.deleteFreqLocationDataObject(object)
			}
			myTempList.add(type);
			
		}
		Collections.reverse(myTempList);
		final Dialog dialog = new Dialog(this);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.showsearchlistdialog);
		ListView templist=(ListView) dialog.findViewById(R.id.tempsearchlist);
		RecentlySearchItemAdapter adapter=new RecentlySearchItemAdapter(this, myTempList);
		templist.setAdapter(adapter);
	     dialog.setCanceledOnTouchOutside(true);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
	            @Override
	            public void onCancel(DialogInterface dialog) {
	            }
	        });
			templist.setOnItemClickListener(new OnItemClickListener() 
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
//					System.out.println("1111latitude and longitude at click???"+myTempList.get(position));
					try {
					completeADD=myTempList.get(position).toString().substring(0,myTempList.get(position).toString().indexOf("*")+1);
					latS=Double.parseDouble(myTempList.get(position).toString().substring(myTempList.get(position).toString().indexOf("*")+1, myTempList.get(position).toString().lastIndexOf(",")));
					longS=Double.parseDouble(myTempList.get(position).toString().substring(myTempList.get(position).toString().lastIndexOf(",")+1));
						latLng=new LatLng(latS, longS);
						getCompleteAddress getcompaddress=new getCompleteAddress();
						getcompaddress.execute("");
						edtLocation.setText("");
						googleMap.clear();
						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
						LocationActivity.this.setMarker(completeADD,latLng,latS, longS);
//						System.out.println("111111at clicked searchlist value>>>>>>"+completeADD+"..."+latLng+"..."+latS+"..."+longS);
						dialog.dismiss();
						shape.remove();
						shape=null;
						}
						catch(Exception e){
						e.printStackTrace();	
						}
				}
			});

			if(!dialog.isShowing()){
				dialog.show();
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	private void initilizeView() {
		edtLocation = (EditText) findViewById(R.id.edtlocation);
		btnSubmitt = (ImageView) findViewById(R.id.btnSubmitt);
		serachicon=(ImageView) findViewById(R.id.search_icon);
		recentview=(ImageView) findViewById(R.id.img_recentlyview);
		serchValueList = new ArrayList<SearchValue>();
		namelist = new ArrayList<String>();
		namelistforshow= new ArrayList<String>();
		myTempList=new ArrayList<String>();
	}
	private void initradiusdata(){
	savePreferences = new SavePreferences();
	setArriveFence = (SeekBar)this.findViewById(R.id.set_radius);
	setArriveFence.setMax(5);
	setArriveFence.setProgress(0);

	if(initMap()){
		gotoLocation(latS, longS, 15);
	}else{
//		Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
	}
	StartOnResumeFragments();

	
	
	}




	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSubmitt:
//			if(dbinsertupdate==false){
				 savePreferences.SavePreferences(LocationActivity.this, "completeadd", completeADD);
				Intent intent = new Intent(LocationActivity.this,AddTaskActivity.class);
//				intent.putExtra("activity",2);
				intent.putExtra("location", completeADD);
				intent.putExtra("arrayname", arrayName);
				intent.putExtra("radius", mapradius);
				intent.putExtra("lats", latS);
				intent.putExtra("longs", longS);
//				intent.putExtra("repeat", repeatvalue);
				setResult(RESULT_OK, intent);
//				if(tempSearchlist.size()>1){
//					savePreferences.SavePreferences(getApplicationContext(), "listsize", tempSearchlist.size());
//					}
//					else{
//					savePreferences.SavePreferences(getApplicationContext(),"listsize",savePreferences.getPreferences_integer(getApplicationContext(), "listsize")+1);	
//					}
				finish();
				break;

		}
			

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
	private void StartOnResumeFragments() {
		try {
			
			if(dbinsertupdate==false){
			aArriveProgress = 0;
			}
			else if(dbinsertupdate==true){
				getradiusProgressbarvalue(valueradius);
				aArriveProgress=radiusProgressbarvalue;	
			}
			setArriveFence.setProgress(aArriveProgress);
						if(dbinsertupdate==false){
						mapradius=200;
						}else if(dbinsertupdate==true){
						mapradius=valueradius;	
						}
						shape = drawCircle(latLng);
			OnSeekBarChangeListener aSetArriveRangeListener = new OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					setArriveRangeProgress = progress;
					int aArriveFence = -1;
						aArriveFence = (progress+1)*200;
						if(aArriveFence>0){
							mapradius=((progress+1)*200);
					}
						if(shape!=null){
						shape.remove();
						shape = null;
						shape = drawCircle(latLng);
						}
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					savePreferences.SavePreferences(LocationActivity.this, "radius_value", setArriveRangeProgress);
				}
			};
			setArriveFence.setOnSeekBarChangeListener(aSetArriveRangeListener);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
			SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.location_map);
			googleMap = mapFrag.getMap();
			if(latLng==null){
			latLng = new LatLng(latS,longS);
			}
			markerOptions.position(new LatLng(latS, longS));
			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
			googleMap.addMarker(markerOptions);
				googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
			if (googleMap != null ) {				
					googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {
						@Override
						public void onMapLongClick(LatLng ll) {
								googleMap.clear();
								shape.remove();
								shape = null;
									latS=ll.latitude;
									longS=ll.longitude;
									latLng = new LatLng(latS,longS);
									LocationActivity.this.setMarker(completeADD,ll,latS, longS);
									getAddressonLongpressed getaddressOnlongpressed=new getAddressonLongpressed();
									getaddressOnlongpressed.execute("");
						}
					});

					googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
						@Override
						public boolean onMarkerClick(Marker marker) {
//							String msg = marker.getTitle() + " (" + marker.getPosition().latitude + 
//									"," + marker.getPosition().longitude + ")";
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
	private void setMarker(String locality,LatLng ll,double lat, double lng) {
//		latLng = new LatLng(lat,lng);
//		MarkerOptions options = new MarkerOptions()	
		try {
		markerOptions.title(locality);
		markerOptions.position(new LatLng(lat,lng));
		markerOptions.anchor(.5f, .5f);
		markerOptions.draggable(true);
		if (marker != null) {
			removeEverything();
		}
		marker = googleMap.addMarker(markerOptions);
		if(shape!=null){
		shape.remove();
		shape=null;		
		shape = drawCircle(ll);
		}
		else{
			marker.remove();
			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
			googleMap.addMarker(markerOptions);
			shape = drawCircle(ll);	
		}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
			shape = drawCircle(new LatLng(latLng.latitude, latLng.longitude));
		}
	}
	private Circle drawCircle(LatLng ll) {
		c_options.center(ll);
		c_options.radius(mapradius);
		c_options.fillColor(0x40da392c);
//		c_options.strokeColor(Color.parseColor("#00B6EF"));
		c_options.strokeColor(getResources().getColor(R.color.circle_color));
		c_options.strokeWidth(1);
		return googleMap.addCircle(c_options);
	}
	private void removeEverything() {
		if(marker!=null){
		marker.remove();
		marker = null;}
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
public void hideKeyboard(View view) {
    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
}
private void ToastMessage(String msg) {
	Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
}

	private class getCompleteAddress extends AsyncTask<String, Void, String> {
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(LocationActivity.this,R.style.Theme_MyDialog);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(true);
		pDialog.show();
//		System.out.println("at onpreexecute>>>>>>"+latS);
	}
	@Override
	protected String doInBackground(String... params) {
		 	String address1 = "";
			String  address2 = "";
		    String  city = "";
		    String state = "";
		    String  PIN = "";
		     String full_address = "";
		     completeADD="";
		     try {
		         JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" +latS + ","
		                 + longS + "&sensor=true");
		         if(jsonObj!=null){
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
		                         address1 = long_name;
		                     } else if (Type.equalsIgnoreCase("route")) {
		                         address1 = address1 + long_name;
		                     } else if (Type.equalsIgnoreCase("sublocality")) {
		                         address2 = long_name;
		                     } else if (Type.equalsIgnoreCase("locality")) {
		                         // Address2 = Address2 + long_name + ", ";
		                         city = long_name;
		                     } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
//		                         county = long_name;
		                     } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
		                         state = long_name;
		                     } else if (Type.equalsIgnoreCase("country")) {
		                         country = long_name;
		                     } else if (Type.equalsIgnoreCase("postal_code")) {
		                         PIN = long_name;
		                     }
		                 }
		                 full_address = address1 +","+city+","+state+","+country+","+PIN;
		                 completeADD=full_address;
		             }
		         }
		         }
		         else{
		        	 Toast.makeText(getApplicationContext(), "No Address found! please try again", Toast.LENGTH_SHORT).show();	 
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
		try {
			initradiusdata();
				arrayName= latS + "," + longS;
				completeADD=result;
				if(completeADD.charAt(0)==','||completeADD.charAt(1)==','){
					completeADD = completeADD.substring(completeADD.indexOf(",")+1,completeADD.length());
					
				}
				if(completeADD.length()>0 && arrayName.length()>0){
				addsearchValuetoList(completeADD, arrayName);
				}
				System.out.println("1111****at clicked on search list"+completeADD);
				
				
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
	}
}
public  JSONObject getJSONfromURL(String url) {
    // initialize
//    InputStream is = null;
    String result = "";
    JSONObject jObject = null;
    ServiceHandler sh=null;
    // http post
    sh = new ServiceHandler();
	try {
	result = sh.makeServiceCall(url,ServiceHandler.GET);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
// try parse the string to a JSON object
	if(result!=null){
    try {
        jObject = new JSONObject(result);
    } catch (JSONException e) {
    }
	}
    return jObject;
}

private void addsearchValuetoList(String address,String latlng){
	
		tempSearchvalue=address+"*"+latlng;
		System.out.println("111111111value adding?????????????????"+tempSearchvalue);
		if(CheckPlacetorecentViewed(tempSearchvalue)==false){
		String arraypass = latlng;
		String[] Location = arraypass.split(",");
		searchresult.setLocationLat(Location[0]);
		searchresult.setLocationLong(Location[1]);
		searchresult.setLocationType(tempSearchvalue);
		System.out.println("111111111value adding?????????????????");
		DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
		db.insertRecentSearchLocationData(searchresult);
		}
}
private boolean CheckPlacetorecentViewed(String string) {
try {
	DataSourceHandler db = DataSourceHandler.getInstance(getApplicationContext());
	List<DbLocationObject> value = db.getAllRecentSearchDataObject();
	for (DbLocationObject valueName : value) {
		String placename = valueName.getLocatonType().toString().substring(0,valueName.getLocatonType().toString().indexOf(","));
		System.out.println("111111111value adding?????????????????"+placename);
		if (placename.equalsIgnoreCase(string.substring(0,string.indexOf(",")))) {
			System.out.println("111111111value adding????????insise condition?????????"+string);
			addcheck = true;
			return addcheck;
		} 
		else {
			addcheck = false;
		}
	}
} catch (Exception e) {
	e.printStackTrace();
	e.getLocalizedMessage();
}
return addcheck;
}
@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
	
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
		pDialog = new ProgressDialog(LocationActivity.this,R.style.Theme_MyDialog);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (pDialog.isShowing())
			pDialog.dismiss();
		if(namelist!=null && namelistforshow !=null ){
		mAdapter = new SearchActivityAdapter(LocationActivity.this,
				namelist,namelistforshow);
		setSearchlistValue(namelist,namelistforshow,mAdapter);
		}
		else {
			if (pDialog.isShowing())
				pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(), "No search keyword found! please try again", Toast.LENGTH_SHORT).show();			
				}
			});
		}
		
		
	}
	

	
	
	@Override
	protected Void doInBackground(String... params) {
		//old
//		String  key = "AIzaSyBqgYEHDuNhcZz7ChAEsNytWseb-eEi5Q0";
//		new
			String key = "AIzaSyDtbjdfwjLt1qj2FhLvmF4uCS2250kPREc";
			String new_addr_param = params[0].toString();
			new_addr_param=new_addr_param.replaceAll(" ", "%20");
			String aPlacesApiUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+new_addr_param+"&sensor=true&key="+key;
			ServiceHandler sh = new ServiceHandler();
			String jsonStr="";
			try {
				jsonStr = sh.makeServiceCall(aPlacesApiUrl,ServiceHandler.GET);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(jsonStr!=null){
				aJObject = new JSONObject(jsonStr);
				if (aJObject != null ) {
					aJStatus = aJObject.getString("status");
					if (aJStatus != null && aJStatus.length() != 0
							&& aJStatus.equals("OK")) {
						aJResults = aJObject.getJSONArray("results");
						int aJResultsLength = aJResults.length();
						if (aJResultsLength > 5) {
							aJResultsLength = 5;
						}
						aJAddresses.clear();
						for (int i = 0; i < aJResultsLength; i++) {
							JSONObject aJFormattedAddressObject = aJResults.getJSONObject(i);
							String aJFormattedAddress = aJFormattedAddressObject.getString("formatted_address");
							String aJName = aJFormattedAddressObject.getString("name");
							JSONObject aJGeometry = aJFormattedAddressObject.getJSONObject("geometry");
							JSONObject aJLocation = aJGeometry.getJSONObject("location");
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
				}}
				else
				{
					aJAddresses = null;
					namelist=null;
					namelistforshow=null;
				}
			} catch (JSONException e) {
//				System.out.println("JSONException in DistTimeDGoogleAPI: " + e);
				aJAddresses = null;
			}
		return null;
	}
}
public void setSearchlistValue(ArrayList<String> namelist2,
		ArrayList<String> namelistforshow2, SearchActivityAdapter mAdapter2) {
	// TODO Auto-generated method stub
	try {
		searchDialogList(mAdapter2);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void searchDialogList(SearchActivityAdapter mAdapter) {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Select an address");
  	builder.setCancelable(true);
//  	builder.setPositiveButton("OK", null);
  	builder.setAdapter(mAdapter, new DialogInterface.OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog, int position) {
			// TODO Auto-generated method stub
			String arrayname1 = aJAddresses.get(position).toString();
//			System.out.println("ajaddress.getpos"+aJAddresses.get(position)+"..."+arrayname1);
			latS=Double.parseDouble(arrayname1.substring(0, arrayname1.indexOf(",")));
			longS=Double.parseDouble(arrayname1.substring(arrayname1.indexOf(",")+1));
			try {
			latLng=new LatLng(latS, longS);
			
			getCompleteAddress getcompaddress=new getCompleteAddress();
			getcompaddress.execute("");
			
			edtLocation.setText("");
			googleMap.clear();
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
			LocationActivity.this.setMarker(completeADD,latLng,latS, longS);
			System.out.println("111111at clicked searchlist value>>>>>>"+completeADD+"..."+latLng+"..."+latS+"..."+longS);
			shape.remove();
			shape=null;
			}
			
			catch(Exception e){
			e.printStackTrace();	
			}
		}
  	});
	AlertDialog alert = builder.create();
	alert.show();
}
public void onItemClick(int mPosition) {
	// TODO Auto-generated method stub
	System.out.println("on click >>>>"+latS+"..."+longS);
}

private class getAddressonLongpressed extends AsyncTask<String, Void, String> {
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(LocationActivity.this,R.style.Theme_MyDialog);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(true);
		pDialog.show();
	}
	@Override
	protected String doInBackground(String... params) {
		 	String address1 = "";
			String  address2 = "";
		    String  city = "";
		    String state = "";
		    String  PIN = "";
		     String full_address = "";
		     completeADD="";
		     try {
		         JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" +latS + ","
		                 + longS + "&sensor=true");
		         if(jsonObj!=null){
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
		                         address1 = long_name;
		                     } else if (Type.equalsIgnoreCase("route")) {
		                         address1 = address1 + long_name;
		                     } else if (Type.equalsIgnoreCase("sublocality")) {
		                         address2 = long_name;
		                     } else if (Type.equalsIgnoreCase("locality")) {
		                         // Address2 = Address2 + long_name + ", ";
		                         city = long_name;
		                     } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
//		                         county = long_name;
		                     } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
		                         state = long_name;
		                     } else if (Type.equalsIgnoreCase("country")) {
		                         country = long_name;
		                     } else if (Type.equalsIgnoreCase("postal_code")) {
		                         PIN = long_name;
		                     }
		                 }
		                 full_address = address1 +","+city+","+state+","+country+","+PIN;
		             }
		         }
		         }
		         else{
		        	 Toast.makeText(getApplicationContext(), "No Address found! please try again", Toast.LENGTH_SHORT).show();	 
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
		try {
//			if(dbinsertupdate==false){
//				mapradius=200;
//			}
			setArriveFence.setProgress(aArriveProgress);
				arrayName= latS + "," + longS;
				completeADD=result;
				if(completeADD.charAt(0)==','||completeADD.charAt(1)==','){
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
	}
	
}
//@Override
//public void onBackPressed() {
//	// TODO Auto-generated method stub
//	super.onBackPressed();
//	if(tempSearchlist.size()>1){
//	savePreferences.SavePreferences(getApplicationContext(), "listsize", tempSearchlist.size());
//	}
//	else{
//	savePreferences.SavePreferences(getApplicationContext(),"listsize",savePreferences.getPreferences_integer(getApplicationContext(), "listsize")+1);	
//	}
//}
}
