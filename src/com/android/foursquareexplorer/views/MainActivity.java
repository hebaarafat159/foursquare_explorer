package com.android.foursquareexplorer.views;

import java.util.List;

import com.android.foursquareexplorer.Constants;
import com.android.foursquareexplorer.R;
import com.android.foursquareexplorer.business.BusinessCallBack;
import com.android.foursquareexplorer.business.BusinessController;
import com.android.foursquareexplorer.datamodels.VenueDataModel;
import com.android.foursquareexplorer.util.NetworkUtility;
import com.android.foursquareexplorer.util.PreferencesUtility;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity implements BusinessCallBack {

	protected ProgressBar loadingProgressBar;
	protected String checkin_venue_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map_main, menu);
		return true;
	}

	public void showProgressBar() {
		if (loadingProgressBar != null) {
			loadingProgressBar.setVisibility(View.VISIBLE);
		}
	}

	public void hideProgressBar() {
		if (loadingProgressBar != null) {
			loadingProgressBar.setVisibility(View.INVISIBLE);
		}
	}

	public void showErrorMsg(Context context, String str) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		// Setting Dialog Message
		alertDialog.setMessage(str);

		alertDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});

		// On pressing Settings button
		alertDialog.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		// Showing Alert Message
		alertDialog.show();
	}

	// display selected venue dialog
	public void displaySelectedVenueData(final Context context,
			final VenueDataModel model) {
		checkin_venue_id = model.id;
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		// add venue name
		dialog.setMessage(model.name);

		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});

		dialog.setNegativeButton(
				getResources().getString(R.string.checkin_btn_text),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (NetworkUtility.isConnected(MainActivity.this)) {
							String token = PreferencesUtility
									.getStringPreferencesValue(
											MainActivity.this,
											Constants.TOKEN_PREF_KEY);
							// request check in
							if (token != null && !token.equals("")) {
								BusinessController.getInstance(
										MainActivity.this).checkInVenue(
										model.id, MainActivity.this);
							}// open google play
							else {

								Intent intent1 = new Intent(MainActivity.this,
										LoginWebActivity.class);
								startActivityForResult(intent1,
										Constants.GET_TOKEN_REQUEST_CODE);
							}

						} else {
							showErrorMsg(
									MainActivity.this,
									MainActivity.this
											.getResources()
											.getString(
													R.string.error_no_connection_msg_text));
						}
					}
				});
		dialog.show();
	}

	// init activity data
	protected void initActivityData(Context context) {
		BusinessController.getInstance(context).getVenuesFromDB(this);
	}

	@Override
	public void onRequestFinish(final int requestCode,
			final List<?> dataArrayList) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				hideProgressBar();
			}
		});
	}

	@Override
	public void onRequestFinish(final int requestCode, final int responseCode) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				switch (requestCode) {
				case Constants.CHECKIN_REQUEST_CODE:
					if (responseCode == Constants.RESPONSE_SUCCESS_CODE) {
						Toast.makeText(
								MainActivity.this,
								getResources().getString(
										R.string.checkin_success_response_text),
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(
								MainActivity.this,
								getResources().getString(
										R.string.server_error_text),
								Toast.LENGTH_LONG).show();
					}
					checkin_venue_id = "";
					break;
				case Constants.GET_CURRENT_LOCATION_REQUEST_CODE:
					if (responseCode == Constants.RESPONSE_SUCCESS_CODE) {
						String currentLocation = PreferencesUtility
								.getStringPreferencesValue(
										MainActivity.this,
										Constants.USER_CURRENT_LOCATION_PREF_KEY);
						if (currentLocation != null) {
							// showProgressBar();
							BusinessController.getInstance(MainActivity.this)
									.getNearestVenues(currentLocation,
											MainActivity.this);
						}
					} else {
						showErrorMsg(MainActivity.this, getResources()
								.getString(R.string.no_gps_error_msg_text));
					}

					break;
				default:
					break;
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constants.GET_TOKEN_REQUEST_CODE:
			System.out.println("Token Authonication : ");
			if (checkin_venue_id != null && !checkin_venue_id.equals("")) {
				BusinessController.getInstance(MainActivity.this).checkInVenue(
						checkin_venue_id, MainActivity.this);
			}
			break;
		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startGetVenuesRequest(Context context,
			BusinessCallBack businessCallBack) {
		// request nearest venues
		if (NetworkUtility.isConnected(context)
				&& NetworkUtility.isGPSConnected(context)) {
			BusinessController.getInstance(context).getCurrentLocation(context,
					businessCallBack);
		} // in case of no Internet connection
		else if (!NetworkUtility.isConnected(context)) {
			showErrorMsg(
					context,
					context.getResources().getString(
							R.string.no_network_error_msg));
		} // in case of no GPS connection
		else if (!NetworkUtility.isGPSConnected(context)) {
			showErrorMsg(
					context,
					context.getResources().getString(
							R.string.no_gps_error_msg_text));
		}
	}
}
