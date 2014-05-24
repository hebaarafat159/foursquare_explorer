package com.android.foursquareexplorer.views;

import java.util.List;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.android.foursquareexplorer.Constants;
import com.android.foursquareexplorer.R;
import com.android.foursquareexplorer.business.BusinessController;
import com.android.foursquareexplorer.datamodels.VenueDataModel;
import com.android.foursquareexplorer.util.NetworkUtility;
import com.android.foursquareexplorer.util.PreferencesUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapMainActivity extends MainActivity {

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_main);
		Fragment fragment = getFragmentManager().findFragmentById(
				R.id.map_fragment);

		map = ((MapFragment) fragment).getMap();
		initActivityData(MapMainActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map_main, menu);
		return true;
	}

	private void addListToMap(List<VenueDataModel> list) {
		if (map != null) {
			LatLng location = null;

			for (VenueDataModel venueDataModel : list) {
				location = new LatLng(venueDataModel.location.Latitude,
						venueDataModel.location.Longitude);
				String name = venueDataModel.name;
				map.addMarker(new MarkerOptions()
						.position(location)
						.title(name)
						.snippet(venueDataModel.id)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.place_pin)));
				map.setInfoWindowAdapter(new VenueMapAdapter(
						getLayoutInflater()));
				map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker marker) {
						if (NetworkUtility.isConnected(MapMainActivity.this)) {
							String token = PreferencesUtility
									.getStringPreferencesValue(
											MapMainActivity.this,
											Constants.TOKEN_PREF_KEY);
							// request check in
							if (token != null && !token.equals("")) {
								String id = marker.getSnippet();
								BusinessController.getInstance(
										MapMainActivity.this).checkInVenue(id,
										MapMainActivity.this);
							}// open google play
							else {
								checkin_venue_id = marker.getSnippet();
								Intent intent1 = new Intent(
										MapMainActivity.this,
										LoginWebActivity.class);
								startActivityForResult(intent1,
										Constants.GET_TOKEN_REQUEST_CODE);
							}

						} else {
							showErrorMsg(
									MapMainActivity.this,
									MapMainActivity.this
											.getResources()
											.getString(
													R.string.error_no_connection_msg_text));
						}
					}
				});
			}

		}
	}

	private void addCurrentLocationToMap(String currentLocation) {
		int index = currentLocation.indexOf(",");
		double lat = Double.parseDouble(currentLocation.substring(0, index));
		double log = Double.parseDouble(currentLocation.substring(index + 1));
		LatLng currentLoc = new LatLng(lat, log);

		map.addMarker(new MarkerOptions()
				.position(currentLoc)
				.title("Current Location")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.current_location_icon)));
		// Move the camera instantly to hamburg with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

	}

	@Override
	public void onRequestFinish(final int requestCode, final int responseCode) {
		super.onRequestFinish(requestCode, responseCode);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				switch (requestCode) {
				case Constants.GET_CURRENT_LOCATION_REQUEST_CODE:
					if (responseCode == Constants.RESPONSE_SUCCESS_CODE) {
						showCurrentlocation();
					}

					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public void onRequestFinish(final int requestCode,
			final List<?> dataArrayList) {
		super.onRequestFinish(requestCode, dataArrayList);

		runOnUiThread(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				switch (requestCode) {
				case Constants.GET_NEAREST_VENUES_REQUEST_CODE:
					if (dataArrayList != null && dataArrayList.size() > 0) {
						addListToMap((List<VenueDataModel>) dataArrayList);
					}
					break;
				case Constants.GET_VENUES_FROM_DABASE_REQUEST_CODE:
					showCurrentlocation();
					if (dataArrayList != null && dataArrayList.size() > 0) {
						addListToMap((List<VenueDataModel>) dataArrayList);
					} else {
						String currentLocation = PreferencesUtility
								.getStringPreferencesValue(
										MapMainActivity.this,
										Constants.USER_CURRENT_LOCATION_PREF_KEY);
						/*
						 * check if the application get the data at least one
						 * time
						 */
						if (currentLocation != null) {
							String errorStr = MapMainActivity.this
									.getResources().getString(
											R.string.no_data_found_error_msg)
									+ ", "
									+ MapMainActivity.this
											.getResources()
											.getString(
													R.string.no_network_error_msg);
							showErrorMsg(MapMainActivity.this, errorStr);
						}
					}

					// start get nearest venues
					startGetVenuesRequest(MapMainActivity.this,
							MapMainActivity.this);

					break;
				default:
					break;
				}
			}
		});
	}

	private void showCurrentlocation() {
		String currentLocation = PreferencesUtility.getStringPreferencesValue(
				MapMainActivity.this, Constants.USER_CURRENT_LOCATION_PREF_KEY);
		if (currentLocation != null) {
			addCurrentLocationToMap(currentLocation);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.refresh_menu:
			startGetVenuesRequest(MapMainActivity.this, MapMainActivity.this);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
