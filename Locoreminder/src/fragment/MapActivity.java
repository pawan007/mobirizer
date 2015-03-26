package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobirizer.locoreminder.R;
import com.mobirizer.locoreminder.SetDataValueForReminder;

public class MapActivity extends Fragment {
	private static View view;
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */

	private static GoogleMap mMap;
	private static Double latitude, longitude;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		latitude = 26.78;
		longitude = 72.56;

		setUpMapIfNeeded();

		return inflater.inflate(R.layout.map_fragment, container, false);
	}

	/***** Sets up the map if it is possible to do so *****/
	public static void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			try {
				mMap = ((SupportMapFragment) SetDataValueForReminder.fragmentManager
						.findFragmentById(R.id.location_map)).getMap();
				// Check if we were successful in obtaining the map.
			} catch (Exception e) {
				e.printStackTrace();
				e.getLocalizedMessage();
			}
			if (mMap != null)
				setUpMap();
		}
	}

	private static void setUpMap() {
		// For showing a move to my loction button
		mMap.setMyLocationEnabled(true);
		// For dropping a marker at a point on the Map
		mMap.addMarker(new MarkerOptions()
				.position(new LatLng(latitude, longitude)).title("My Home")
				.snippet("Home Address"));
		// For zooming automatically to the Dropped PIN Location
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				latitude, longitude), 12.0f));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		// TODO Auto-generated method stub
		if (mMap != null)
			setUpMap();

		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			/*
			 * mMap = ((SupportMapFragment)
			 * SetDataValueForReminder.fragmentManager
			 * .findFragmentById(R.id.location_map)).getMap();
			 */
			mMap = ((SupportMapFragment) SetDataValueForReminder.fragmentManager
					.findFragmentById(R.id.location_map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null)
				setUpMap();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mMap != null) {
			SetDataValueForReminder.fragmentManager
					.beginTransaction()
					.remove(SetDataValueForReminder.fragmentManager
							.findFragmentById(R.id.location_map)).commit();
			mMap = null;
		}
	}
}
