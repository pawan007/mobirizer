package com.foruriti.geosms;

import gps.GPSTracker;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foruriti.locosms.R;
import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;
import com.navdrawer.SimpleSideDrawer;

import conditionCheck.ConnectionDetector;
//

//called from first activity... provide all detail related entry task to database
public class ActionSelectorActivity extends Activity{
	SimpleSideDrawer simpleslide;
	private ImageView messagetext, locationpin, contactmessage,actionmenu;
	private Button action_chooser_ok;
	private TextView locationvalue,contactvalue;
	private EditText messagevalue;
	public static String messagetextvalue="",phoneno="",phoneno_name="";
	String valueLocation, LOgiLatiValue, ValueMessage,late,repeatvalue;
	int valueradius,updateid;
	// for action chooser from left side
	private SharedPreferences sharedPreferences;
	//for map
	GPSTracker gps;
	private ConnectionDetector connectionDetector;
	private double latitude,longitude;
	private boolean dis;
	private List<Address> tempaddress;
	private String getcompadd="";
	private Geocoder geocoder;
	private ProgressDialog pDialog; 
	protected LocationManager locationManager;
	private SavePreferences savePreferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.action_chooser);
		simpleslide = new SimpleSideDrawer(this);
		simpleslide.setRightBehindContentView(R.layout.right_menu);
		savePreferences = new SavePreferences();
		initialiseView();
		initialiseValue();
		locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		//call map screen from click map icon 
		locationpin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(ActionSelectorActivity.this,
//						SetDataValueForReminder.class);
//				startActivity(i);
//				finish();
				
				messagetextvalue=messagevalue.getText().toString();
				callMappage();
			}
		});
		contactmessage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fetchcontact(v);
				
				// ShowwifiDialogBox();
			}
		});

		action_chooser_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				savevalueinDatabase();
//				Intent i = new Intent(ActionSelectorActivity.this,
//						FirstActivity.class);
//				startActivity(i);
//				finish();
			}
		});
//		
	}
	protected void savevalueinDatabase() {
		// TODO Auto-generated method stub
		
		messagetextvalue=messagevalue.getText().toString();
//		System.out.println("messagetextvalue at save"+messagetextvalue.length());
		if(SetDataValueForReminder.dbinsertupdate==false){
		if(!(messagetextvalue.length()<=0)  && SetDataValueForReminder.arrayName!=null && SetDataValueForReminder.completeADD!=null ){
		// adding to database
			String arraypass = SetDataValueForReminder.arrayName;
			String[] Location = arraypass.split(",");
		DbLocationObject locationObj = new DbLocationObject();
		locationObj.setLocationLat(Location[0]);
		locationObj.setLocationLong(Location[1]);
		locationObj.setLocationType(SetDataValueForReminder.completeADD);
		locationObj.setLocationProvider("GPS");
		locationObj.setRepeate("1");
		String locomsgno=messagetextvalue;
		
		if(Location[0].equalsIgnoreCase(""+gps.getLatitude())){
		savePreferences.SavePreferences(ActionSelectorActivity.this, "lat_at_currentadd_atinsert", ""+gps.getLatitude());}
		savePreferences.SavePreferences(ActionSelectorActivity.this, "currentadd_atinsert", SetDataValueForReminder.completeADD);
		if(phoneno!=null||phoneno.length()!=0){
			locomsgno=locomsgno+"*"+phoneno;
//			System.out.println("locomsg at submit"+locomsgno);
		}
		if(locomsgno.contains("*") && locomsgno.contains("$")){
		locationObj.setMessage(locomsgno);
//		locationObj.setRepeate(SetDataValueForReminder.repeat);
		locationObj.setMapradius(SetDataValueForReminder.mapradius);
//		System.out.println("befor add>>>>>>"+locomsgno+SetDataValueForReminder.idforupdate+"...."+locationObj.getId());

		// insert data to data base
		SetDataValueForReminder.dbinsertupdate=false;
		DataSourceHandler db = DataSourceHandler
				.getInstance(getApplicationContext());
		db.insertFreqLocationDataObject(locationObj);
		Intent i = new Intent(ActionSelectorActivity.this,
				FirstActivity.class);
		startActivity(i);
		finish();
		}
		else{
			ToastMessage("Please insert correct contact");
		}}
		else{
			ToastMessage("Please insert all feild");
		}}
		if(SetDataValueForReminder.dbinsertupdate==true){
//			System.out.println("at save>>>>>"+messagetextvalue+"...."+SetDataValueForReminder.arrayName+"...."+SetDataValueForReminder.completeADD+".."+ValueMessage);
			if(!(messagetextvalue.length()<=0)  && SetDataValueForReminder.arrayName!=null && SetDataValueForReminder.completeADD!=null ){
			// adding to database
				String arraypass = SetDataValueForReminder.arrayName;
				String[] Location = arraypass.split(",");
			DbLocationObject locationObj = new DbLocationObject();
			locationObj.setLocationLat(Location[0]);
			locationObj.setLocationLong(Location[1]);
			locationObj.setLocationType(SetDataValueForReminder.completeADD);
			locationObj.setLocationProvider("GPS");
			locationObj.setRepeate("1");
			locationObj.setId(SetDataValueForReminder.idforupdate);
			locationObj.getId();
			String locomsgno=messagetextvalue;
			if(Location[0].equalsIgnoreCase(""+gps.getLatitude())){
			savePreferences.SavePreferences(ActionSelectorActivity.this, "lat_at_currentadd_atinsert", ""+gps.getLatitude());}
			savePreferences.SavePreferences(ActionSelectorActivity.this, "currentadd_atinsert", SetDataValueForReminder.completeADD);
			if(phoneno!=null||phoneno.length()!=0){
				locomsgno=locomsgno+"*"+phoneno;
//				System.out.println("locomsg at submit"+locomsgno);
			}
			if(locomsgno.contains("*") && locomsgno.contains("$")){
			locationObj.setMessage(locomsgno);
//			locationObj.setRepeate(SetDataValueForReminder.repeat);
			locationObj.setMapradius(SetDataValueForReminder.mapradius);
//			System.out.println("befor add>>>>>>"+locomsgno+SetDataValueForReminder.idforupdate+"...."+locomsgno);

			// insert data to data base
			SetDataValueForReminder.dbinsertupdate=false;
			DataSourceHandler db = DataSourceHandler
					.getInstance(getApplicationContext());
			db.updateFreqLocationDataObject(locationObj);
			Intent i = new Intent(ActionSelectorActivity.this,
					FirstActivity.class);
			startActivity(i);
			finish();
			}
			else{
				ToastMessage("Please insert correct contact");
			}}
			else{
				ToastMessage("Please insert all feild");
			}}
		
	}
	private static final int CONTACT_PICKER_RESULT = 1001;
	private void fetchcontact(View view) {
//		ActionSelectorActivity in=(ActionSelectorActivity) getParent();
		final Uri uriContact = ContactsContract.Contacts.CONTENT_URI;
		Intent intentPickContact = new Intent(Intent.ACTION_PICK, uriContact);
	    startActivityForResult(intentPickContact, CONTACT_PICKER_RESULT);
	    
//	    System.out.println("CONTACT_PICKER_RESULT"+CONTACT_PICKER_RESULT+"....."+Contacts.CONTENT_URI);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if(resultCode == RESULT_OK){
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
//		       phoneno = cursorNum.getString(columnIndex_number);
		       phoneno = cursorNum.getString(columnIndex_number);
		       phoneno_name=columnIndex_name;
//		       System.out.println("phoneno>>>>>>>"+phoneno+"...."+columnIndex_name);
		       
//		       textPhone.setText(stringNumber);
		       phoneno=phoneno_name+"$"+phoneno;
		       if(phoneno!=null && phoneno.length()>0){
		   		contactvalue.setText("Message send to "+"\n"+phoneno_name);
		   		}
		       
		      }
		      
		     }else{
//		      textPhone.setText("NO Phone Number");
		     }
		     
		     
		    }else{
		     Toast.makeText(getApplicationContext(), "NO data!", Toast.LENGTH_LONG).show();
		    }
		   }
		  }
	}
	
//protected void setMessagetextBox() {
//		// TODO Auto-generated method stub
//		/* Alert Dialog Code Start */
//		AlertDialog.Builder alert = new AlertDialog.Builder(this);
//		// Set Alert
//									// dialog title
//									// here
////		alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		
//		alert.setMessage("Please enter Your message"); // Message here
//
//		// Set an EditText view to get user input
//		final EditText input = new EditText(this);
//		alert.setView(input);
//
//		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//				messagetextvalue= input.getEditableText().toString();
//				messagevalue.setText(messagetextvalue);
//			} // End of onClick(DialogInterface dialog, int
//				// whichButton)
//		}); // End of alert.setPositiveButton
//		alert.setNegativeButton("CANCEL",
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int whichButton) {
//						// Canceled.
//						dialog.cancel();
//					}
//				}); // End of alert.setNegativeButton
//		AlertDialog alertDialog = alert.create();
//		alertDialog.show();
//
//	}


	//value to set after provide name at getedittextactivity
	private void initialiseValue() {
		geocoder=new Geocoder(ActionSelectorActivity.this, Locale.getDefault());
		gps = new GPSTracker(ActionSelectorActivity.this);
		connectionDetector=new ConnectionDetector(getApplicationContext());
		if(SetDataValueForReminder.dbinsertupdate==true){
//			if(phoneno!=null && phoneno.length()>0){
			Intent location = getIntent();
			if (location != null) {
				SetDataValueForReminder.completeADD=valueLocation = location.getExtras().getString("location");
				SetDataValueForReminder.arrayName=LOgiLatiValue = location.getExtras().getString("arrayname");
				ValueMessage = location.getExtras().getString("Message");
				valueradius=location.getExtras().getInt("radius");
				repeatvalue=location.getExtras().getString("repeat");
//				updateid=location.getExtras().getInt("updateid",0);
				if (valueLocation != null) {
//					edtLocation.setText(valueLocation);
					locationvalue.setText(valueLocation);
				}
//				if (LOgiLatiValue != null) {
//					arrayName = LOgiLatiValue;
//				}
				if (ValueMessage != null) {
//					edtMessage.setText(ValueMessage);
					contactvalue.setText("Message send to"+"\n"+ValueMessage.substring(ValueMessage.indexOf('*')+1, ValueMessage.indexOf('$')));
					messagevalue.setText(ValueMessage.substring(0, ValueMessage.indexOf('*')));
					messagetextvalue=ValueMessage.substring(0, ValueMessage.indexOf('*'));
					phoneno=ValueMessage.substring(ValueMessage.indexOf('*')+1,ValueMessage.length());
				}
//				if(valueradius != 0){
////					(aArriveProgress+1)*200;
//					mapradius=valueradius;
//				}
	//System.out.println("at latlong value edit"+arrayName);
//				latS=Double.parseDouble(arrayName.substring(0, arrayName.indexOf(",")));
//				longS=Double.parseDouble(arrayName.substring(arrayName.indexOf(",")+1));	
			}
				
//				}
//				if(messagetextvalue!=null && messagetextvalue.length()>0){
//					messagevalue.setText(messagetextvalue);
//				}
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ActionSelectorActivity.this);
				String str=sharedPreferences.getString("completeaddress",SetDataValueForReminder.completeADD );
//				System.out.println("str"+str);
//				if(SetDataValueForReminder.completeADD!=null && SetDataValueForReminder.completeADD.length()>0){
//					locationvalue.setText(SetDataValueForReminder.completeADD.substring(0, 30));
//				}
				if(valueLocation!=null && valueLocation.length()!=0){
//					System.out.println("length>>>>>"+str.length());
					locationvalue.setText(valueLocation);
				}
				else{
					DisplayMetrics displayMetrics = getApplicationContext().getResources()
		                    .getDisplayMetrics();
					int screenWidthInPix = displayMetrics.widthPixels;
//					System.out.println("screen width>>"+screenWidthInPix);
					if(screenWidthInPix<650){
					locationvalue.setText("        Pick a location");	}
					else{
					locationvalue.setText("               Pick a location");	
					}
				}
		}else{
//			Intent location = getIntent();
//			if(location!=null){
//			updateid=location.getExtras().getInt("updateid", 0);
//			System.out.println("updateid>>>"+updateid);}
			if(phoneno!=null && phoneno.length()>0){
				contactvalue.setText("Message send to"+"\n"+phoneno_name);
				}
				if(messagetextvalue!=null && messagetextvalue.length()>0){
					messagevalue.setText(messagetextvalue);
				}
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ActionSelectorActivity.this);
				String str=sharedPreferences.getString("completeaddress",SetDataValueForReminder.completeADD );
//				System.out.println("str"+str);
//				if(SetDataValueForReminder.completeADD!=null && SetDataValueForReminder.completeADD.length()>0){
//					locationvalue.setText(SetDataValueForReminder.completeADD.substring(0, 30));
//				}
				if(str!=null && str.length()!=0){
//					System.out.println("length>>>>>"+str.length());
					locationvalue.setText(str);
				}
				else{
					DisplayMetrics displayMetrics = getApplicationContext().getResources()
		                    .getDisplayMetrics();
					int screenWidthInPix = displayMetrics.widthPixels;
//					System.out.println("screen width>>"+screenWidthInPix);
					if(screenWidthInPix<650){
					locationvalue.setText("        Pick a location");	}
					else{
					locationvalue.setText("               Pick a location");	
					}
				}
		}
		

	}
//inittialise all value from layout
	private void initialiseView() {
		// TODO Auto-generated method stub
		messagevalue= (EditText) findViewById(R.id.message_detail);
		locationvalue=(TextView) findViewById(R.id.picklocation);
		contactvalue=(TextView) findViewById(R.id.contact_no);
		messagetext = (ImageView) findViewById(R.id.editmessage_task);
		locationpin=(ImageView)findViewById(R.id.locationpin);
		contactmessage = (ImageView) findViewById(R.id.contactformessage);
//		actionmenu=(ImageView) findViewById(R.id.actionselector_menu);
		action_chooser_ok = (Button) findViewById(R.id.action_chooser_ok);
		
	}
	protected void callMappage() {
		// TODO Auto-generated method stub
		gps = new GPSTracker(ActionSelectorActivity.this);
		boolean internetcheck = connectionDetector.isConnectingToInternet();
		if(internetcheck==true){
		// check if GPS enabled
		if (gps.canGetLocation()) {
//			imgaddyourLocation.setImageDrawable(getResources().getDrawable(
//					R.drawable.gps2));
			latitude = gps.getLatitude();
			longitude = gps.getLongitude();

			if (latitude == 0 || longitude == 0) {
				ToastMessage("NO Gps fond");
			} else {
				Intent i = new Intent(ActionSelectorActivity.this,
						SetDataValueForReminder.class);
				i.putExtra("activity", 2);
				if(SetDataValueForReminder.dbinsertupdate==true){
				i.putExtra("activity",1);
				i.putExtra("location", valueLocation);
				i.putExtra("Message", ValueMessage);
				i.putExtra("arrayname", LOgiLatiValue);
				i.putExtra("radius", valueradius);
				i.putExtra("repeat", repeatvalue);
				}
				startActivity(i);
				finish();
//				getAddress getaddress=new getAddress();
//				getaddress.execute("");
				
			}

		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}
		}else{
			ToastMessage("Please check Internet connection");
		}	
	}
	private void ToastMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}
//	private class getAddress extends AsyncTask<String, Void, String> {
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			pDialog = new ProgressDialog(ActionSelectorActivity.this,R.style.Theme_MyDialog);
//			pDialog.setMessage("Please wait...");
//			pDialog.setCancelable(true);
//			pDialog.show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			
////		templat=marker.getPosition().longitude;
////		templng=marker.getPosition().longitude;
//		
//		 
//		 try {
//			tempaddress = geocoder.getFromLocation(latitude, longitude, 1);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 if(tempaddress!=null){
//			 getcompadd=tempaddress.get(0).getAddressLine(0)+"\n"+tempaddress.get(0).getAddressLine(1)+"\n"+tempaddress.get(0).getAddressLine(2);
//			
//		 }
////		 else{
////			 edtLocation.setText(valueLocation); 
////		 }
//				
//			
//			return getcompadd;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			if(tempaddress!=null){
//				 getcompadd=tempaddress.get(0).getAddressLine(0)+"\n"+tempaddress.get(0).getAddressLine(1)+"\n"+tempaddress.get(0).getAddressLine(2);
//				
//			
//			String aJAddress = latitude + "," + longitude;
//			Intent i = new Intent(getApplicationContext(), SetDataValueForReminder.class);
////			System.out.println("getcompadd>>>>"+getcompadd);
//			i.putExtra("location", getcompadd);
//			i.putExtra("arrayname", aJAddress);
//			startActivity(i);
//			if (pDialog.isShowing())
//				pDialog.dismiss();
//			finish();
//			}
//			else{
//				String aJAddress = latitude + "," + longitude;
//				Intent i = new Intent(getApplicationContext(), SetDataValueForReminder.class);
//				System.out.println("getcompadd>>>>"+getcompadd);
//				i.putExtra("location", "");
//				i.putExtra("arrayname", aJAddress);
//				startActivity(i);
//				if (pDialog.isShowing())
//					pDialog.dismiss();
//				finish();
//			}
//			
//		}
//
//	}
	



	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		SetDataValueForReminder.dbinsertupdate=false;
		Intent i = new Intent(ActionSelectorActivity.this, FirstActivity.class);
		startActivity(i);
		finish();

	}


	

}
