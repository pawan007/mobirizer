package com.mobirizer.locoreminder;

import java.io.IOException;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		// Getting reference to btn_find of the layout activity_main
		Button btn_find = (Button) findViewById(R.id.btn_find);

		// Defining button click event listener for the find button
		OnClickListener findClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Getting reference to EditText to get the user input location
				EditText etLocation = (EditText) findViewById(R.id.et_location);

				// Getting user input location
				String location = etLocation.getText().toString();

				if (location != null && !location.equals("")) {
					new GeocoderTask().execute(location);
				}
			}
		};

		// Setting button click event listener for the find button
		btn_find.setOnClickListener(findClickListener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
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
				addresses = geocoder.getFromLocationName(locationName[0], 4);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return addresses;
		}

		@Override
		protected void onPostExecute(List<Address> addresses) {

			// Getting Reference to MapView of the layout activity_main
			MapView mapView = (MapView) findViewById(R.id.map_view);

			// Setting ZoomControls
			mapView.setBuiltInZoomControls(true);

			// Getting MapController for the MapView
			MapController mc = mapView.getController();

			// Getting Drawable object corresponding to a resource image
			Drawable drawable = getResources().getDrawable(R.drawable.marker);

			// Getting Overlays of the map
			List<Overlay> overlays = mapView.getOverlays();

			// Creating an ItemizedOverlay
			LocationOverlay locationOverlay = new LocationOverlay(drawable,
					getBaseContext());

			// Clearing the overlays
			overlays.clear();

			if (addresses == null || addresses.size() == 0) {
				Toast.makeText(getBaseContext(), "No Location found",
						Toast.LENGTH_SHORT).show();

				// Redraws the map to clear the overlays
				mapView.invalidate();
			}

			// Adding Markers on Google Map for each matching address
			for (int i = 0; i < addresses.size(); i++) {

				Address address = (Address) addresses.get(i);

				// Creating an instance of GeoPoint, to display in Google Map
				GeoPoint p = new GeoPoint(
				// (int)(address.getLatitude()*1E6),
				// (int)(address.getLongitude()*1E6)
						(int) (address.getLatitude()),
						(int) (address.getLongitude()));

				String addressText = String.format(
						"%s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address
								.getCountryName());

				System.out.println(">>>>>>>>>>>>>" + addressText + "......"
						+ address.getLatitude() + "  longitude..."
						+ address.getLongitude());
				// Creating an OverlayItem to mark the point
				OverlayItem overlayItem = new OverlayItem(p, "Location",
						addressText);

				// Adding the OverlayItem in the LocationOverlay
				locationOverlay.addOverlay(overlayItem);

				// Adding locationOverlay to the overlay
				overlays.add(locationOverlay);

				// Locate the first location
				if (i == 0)
					mc.animateTo(p);
			}

			// Redraws the map
			mapView.invalidate();

		}
	}
}
