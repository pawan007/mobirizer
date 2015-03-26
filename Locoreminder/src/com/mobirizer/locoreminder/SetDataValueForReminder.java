package com.mobirizer.locoreminder;

import globalVariable.GlobalVariable;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.location.database.DataSourceHandler;
import com.location.database.DbLocationObject;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SetDataValueForReminder extends FragmentActivity implements
		OnClickListener {
	EditText edtLocation, edtMessage;
	public static SetDataValueForReminder Instance;
	String valueLocation, LOgiLatiValue, ValueMessage;
	ImageView btnSubmitt;
	RadioButton radioRoutineButton;
	RadioGroup radioRoutineGrop;
	public static FragmentManager fragmentManager;
	public static String repeat;
	String latitude;
	public static String arrayName;
	DataSourceHandler db;

	// add here for pin
	GoogleMap googleMap;
	MarkerOptions markerOptions;
	LatLng latLng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		fragmentManager = getSupportFragmentManager();
		setContentView(R.layout.set_data_value_for_reimnder);
		SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.location_map);

		initilizeView();
		googleMap = supportMapFragment.getMap();
		btnSubmitt.setOnClickListener(this);

		Intent location = getIntent();
		if (location != null) {
			valueLocation = location.getExtras().getString("location");
			LOgiLatiValue = location.getExtras().getString("arrayname");
			ValueMessage = location.getExtras().getString("Message");
			if (valueLocation != null) {
				edtLocation.setText(valueLocation);
			}
			if (LOgiLatiValue != null) {
				arrayName = LOgiLatiValue;
			}
			if (ValueMessage != null) {
				edtMessage.setText(ValueMessage);
			}
		}
		new GeocoderTask().execute(valueLocation);
	}

	private void initilizeView() {
		edtLocation = (EditText) findViewById(R.id.edtLocation);
		edtMessage = (EditText) findViewById(R.id.edtmessage);
		btnSubmitt = (ImageView) findViewById(R.id.btnSubmitt);
		radioRoutineGrop = (RadioGroup) findViewById(R.id.radiorutine);
		db = DataSourceHandler.getInstance(SetDataValueForReminder.this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSubmitt:
			int selectedId = radioRoutineGrop.getCheckedRadioButtonId();
			radioRoutineButton = (RadioButton) findViewById(selectedId);
			String bullonChhose = radioRoutineButton.getText().toString();
			if (bullonChhose.equalsIgnoreCase("Daily")) {
				repeat = "1";
			}
			if (bullonChhose.equalsIgnoreCase("One Time")) {
				repeat = "0";
			}

			if (CheckStringNameToDataBase(edtLocation.getText().toString()
					.trim()) != false) {
				ToastMessage("Name Already exits");
			} else {
				String arraypass = arrayName;
				String[] Location = arraypass.split(",");

				// adding to database
				DbLocationObject locationObj = new DbLocationObject();
				locationObj.setLocationLat(Location[0]);
				locationObj.setLocationLong(Location[1]);
				locationObj.setLocationType(edtLocation.getText().toString());
				locationObj.setLocationProvider("GPS");
				locationObj.setMessage(edtMessage.getText().toString());
				locationObj.setRepeate(repeat);

				// insert data to data base
				DataSourceHandler db = DataSourceHandler
						.getInstance(getApplicationContext());
				db.insertFreqLocationDataObject(locationObj);

				Intent firstActivity = new Intent(SetDataValueForReminder.this,
						FirstActivity.class);
				startActivity(firstActivity);

				break;
			}

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

				} else {
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

	public void ToastMessage(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// mainInstance.GetAlertDialog(SettingsActivity.this, "onKeyDown",
			// APP_NAME, "Okay To Close?", "Yes", null, "No");
			Intent intent = new Intent(SetDataValueForReminder.this,
					FirstActivity.class);
			startActivity(intent);
			SetDataValueForReminder.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// An AsyncTask class for accessing the GeoCoding Web Service
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

		@Override
		protected List<Address> doInBackground(String... locationName) {
			// Creating an instance of Geocoder class
			Geocoder geocoder = new Geocoder(getBaseContext());
			List<Address> addresses = null;

			try {
				// Getting a maximum of 3 Address that matches the input text
				addresses = geocoder.getFromLocationName(locationName[0], 3);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {

			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(getBaseContext(), "No Location found",
						Toast.LENGTH_SHORT).show();
			}

			googleMap.clear();

			for (int i = 0; i < addresses.size(); i++) {

				Address address = (Address) addresses.get(i);

				latLng = new LatLng(address.getLatitude(),
						address.getLongitude());

				String addressText = String.format(
						"%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address
								.getCountryName());

				markerOptions = new MarkerOptions();
				markerOptions.position(latLng);
				markerOptions.title(addressText);

				googleMap.addMarker(markerOptions);

				// Locate the first location
				if (i == 0)
					googleMap.animateCamera(CameraUpdateFactory
							.newLatLng(latLng));
			}
		}
	}
}
