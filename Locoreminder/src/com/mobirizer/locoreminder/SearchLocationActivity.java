package com.mobirizer.locoreminder;

import globalVariable.GlobalVariable;
import gps.GPSTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.SearchActivityAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bin.SearchValue;

import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;
import com.mobirizer.utility.UtilityHandler;

import conditionCheck.ConnectionDetector;

public class SearchLocationActivity extends Activity implements OnClickListener {

	ImageView imgbtn, imgaddyourLocation;
	EditText edtloaction;
	ListView listview;
	// JSONArray contacts = null;
	static final String TAG_CONTACTS = "results";
	static final String TAG_NAME = "formatted_address";
	ArrayList<HashMap<String, String>> contactList;
	ArrayList<String> namelist;
	private ArrayAdapter<String> listAdapter;

	android.app.ProgressDialog pDialog;

	String aJStatus = "";
	JSONArray aJResults = null;
	JSONObject aJObject = null;
	ArrayList<String> aJAddresses = new ArrayList<String>();
	GPSTracker gps;
	public static double latitude, longitude;

	// this context will use when we work with Alert Dialog
	final Context context = this;

	ConnectionDetector connectionDetector;
	DataSourceHandler db;
	List<DbLocationObject> value;
	boolean dis;

	ArrayList<SearchValue> serchValueList;
	SearchActivityAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.serch_locatiom);
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initilizeView();
		String fontPath = "fonts/Gabriola_0.ttf";
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		TextView txt = (TextView) findViewById(R.id.edtSerchPLace);
		txt.setTextSize(25);
		txt.setTypeface(tf);
		imgbtn.setOnClickListener(this);
		imgaddyourLocation.setOnClickListener(this);
		contactList = new ArrayList<HashMap<String, String>>();
		serchValueList = new ArrayList<SearchValue>();
		namelist = new ArrayList<String>();
		setimageForGps();

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// ListView Clicked item value
				String itemValue = (String) listview
						.getItemAtPosition(position);
				String arrayname = aJAddresses.get(position).toString();
				Intent setdata = new Intent(SearchLocationActivity.this,
						SetDataValueForReminder.class);
				setdata.putExtra("location", itemValue);
				setdata.putExtra("arrayname", arrayname);
				startActivity(setdata);

			}
		});
	}

	private void initilizeView() {
		imgbtn = (ImageView) findViewById(R.id.imgbtnSerch);
		imgaddyourLocation = (ImageView) findViewById(R.id.imgBtnaddYourLocatiom);
		listview = (ListView) findViewById(R.id.lstserchplace);
		edtloaction = (EditText) findViewById(R.id.edtSerchPLace);

		connectionDetector = new ConnectionDetector(getApplicationContext());
		db = DataSourceHandler.getInstance(getApplicationContext());

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtnSerch:
			String location = "";
			location = edtloaction.getText().toString().trim();
			if (location.equalsIgnoreCase("")) {
				ToastMessage("Type Street,City,State");
			} else {
				boolean internet = connectionDetector.isConnectingToInternet();
				if (internet == true) {
					GetContacts getcontact = new GetContacts();
					getcontact.execute(location);
				} else {
					ToastMessage("NO Internet");
				}

			}
			break;
		case R.id.imgBtnaddYourLocatiom:
			// create class object
			gps = new GPSTracker(SearchLocationActivity.this);

			// check if GPS enabled
			if (gps.canGetLocation()) {
				imgaddyourLocation.setImageDrawable(getResources().getDrawable(
						R.drawable.gps2));
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();

				if (latitude == 0 || longitude == 0) {
					ToastMessage("NO Gps find");
				} else {
					boolean vom = checkIsAddtable(latitude, longitude);
					if (!checkIsAddtable(latitude, longitude)) {
						ShowDialogBox();
					} else {
						ToastMessage(GlobalVariable.placeNameAdd + ":"
								+ "Near Reminder exists ");
					}
				}

			} else {
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				gps.showSettingsAlert();
			}

			break;

		}
	}

	private class GetContacts extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			namelist.clear();
			pDialog = new ProgressDialog(SearchLocationActivity.this);
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
			mAdapter = new SearchActivityAdapter(SearchLocationActivity.this,
					namelist);
			listview.setAdapter(mAdapter);

		}

		@Override
		protected Void doInBackground(String... params) {
			String key = "AIzaSyDAApso7SntglBnRvFh7MZ0gRs8IsbasFA";
			String new_addr_param = params[0].toString();
			String aPlacesApiUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="
					+ new_addr_param + "&sensor=true&key=" + key;
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response

			String jsonStr = sh.makeServiceCall(aPlacesApiUrl,
					ServiceHandler.GET);
			try {
				aJObject = new JSONObject(jsonStr);
				if (aJObject != null) {
					System.out.println("aJObject.length: " + aJObject.length());
					aJStatus = aJObject.getString("status");
					System.out.println("aJStatus: " + aJStatus);
					if (aJStatus != null && aJStatus.length() != 0
							&& aJStatus.equals("OK")) {
						System.out.println("in status=ok if");
						aJResults = aJObject.getJSONArray("results");
						System.out.println("aJResults.length(): "
								+ aJResults.length());
						int aJResultsLength = aJResults.length();
						if (aJResultsLength > 5) {
							aJResultsLength = 5;
						}

						namelist.clear();
						aJAddresses.clear();

						for (int i = 0; i < aJResultsLength; i++) {
							JSONObject aJFormattedAddressObject = aJResults
									.getJSONObject(i);
							String aJFormattedAddress = aJFormattedAddressObject
									.getString("formatted_address");
							String aJName = aJFormattedAddressObject
									.getString("name");
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
							aJAddresses.add(aJAddress);
							namelist.add(aJName);

						}
					} else {
						aJAddresses = null;
					}
				} else {
					aJAddresses = null;
				}
			} catch (JSONException e) {
				System.out.println("JSONException in DistTimeDGoogleAPI: " + e);
				aJAddresses = null;
			}

			return null;
		}

	}

	private void ShowDialogBox() {
		/* Alert Dialog Code Start */
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setTitle("Reminder"); // Set Alert
									// dialog title
									// here
		alert.setMessage("Enter Reminder Name"); // Message here

		// Set an EditText view to get user input
		final EditText input = new EditText(context);
		alert.setView(input);

		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// You will get as string input data in this
				// variable.
				// here we convert the input to a string and
				// show in a toast.
				String srt = input.getEditableText().toString();
				String aJAddress = latitude + "," + longitude;
				Toast.makeText(context, srt, Toast.LENGTH_LONG).show();
				Intent i = new Intent(context, SetDataValueForReminder.class);
				i.putExtra("location", srt);
				i.putExtra("arrayname", aJAddress);
				context.startActivity(i);
			} // End of onClick(DialogInterface dialog, int
				// whichButton)
		}); // End of alert.setPositiveButton
		alert.setNegativeButton("CANCEL",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						dialog.cancel();
					}
				}); // End of alert.setNegativeButton
		AlertDialog alertDialog = alert.create();
		alertDialog.show();

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

	private void setimageForGps() {
		gps = new GPSTracker(SearchLocationActivity.this);
		imgaddyourLocation = (ImageView) findViewById(R.id.imgBtnaddYourLocatiom);
		if (gps.canGetLocation()) {
			imgaddyourLocation.setImageDrawable(getResources().getDrawable(
					R.drawable.gps2));
		} else {
			imgaddyourLocation.setImageDrawable(getResources().getDrawable(
					R.drawable.gps3));
		}

	}

	public void ToastMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			Intent i = new Intent(SearchLocationActivity.this,
					FirstActivity.class);
			startActivity(i);
			finish();
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	public void onItemClick(int mPosition) {

		ToastMessage("Click");
	}

}
