package com.android.foursquareexplorer.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.foursquareexplorer.Constants;
import com.android.foursquareexplorer.database.DBController;
import com.android.foursquareexplorer.datamodels.VenueDataModel;
import com.android.foursquareexplorer.util.NetworkUtility;
import com.android.foursquareexplorer.util.PreferencesUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;

public class BusinessController {
	private static BusinessController instance;
	private Context serviceContext;

	private BusinessController(Context context) {
		serviceContext = context;
	}

	public static BusinessController getInstance(Context context) {
		if (instance == null)
			instance = new BusinessController(context);
		return instance;
	}

	// Request nearest avenues to user location
	public void getNearestVenues(final String currentLocation,
			final BusinessCallBack callBack) {

		if (currentLocation != null) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					// get user version number
					String clientVersion = NetworkUtility
							.getUserVersion(new Date());

					// create request Parames
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair(
							Constants.REQUEST_VENUES_SEARCH_LOCATION_PARAM,
							currentLocation));
					nameValuePairs.add(new BasicNameValuePair(
							Constants.REQUEST_CLIENT_ID_PARAM,
							Constants.CLIENT_ID));
					nameValuePairs.add(new BasicNameValuePair(
							Constants.REQUEST_CLIENT_SECRET_PARAM,
							Constants.CLIENT_SECRET));
					nameValuePairs.add(new BasicNameValuePair(
							Constants.REQUEST_VERSION_PARAM, clientVersion));
					nameValuePairs.add(new BasicNameValuePair(
							Constants.REQUEST_VENUES_RADIUS_PARAM, "200"));

					// send request
					String response = NetworkUtility.sentGetRequest(
							Constants.VENUES_SEARCH_API, nameValuePairs);
					System.out.println("Response" + response);

					if (response != null && !response.equals("")) {
						// extract response object
						String responseStr;
						try {
							responseStr = NetworkUtility
									.extractJSONObjectFromResponseString(
											Constants.RESPONSE_STRING_JSON_KEY,
											response);
							// extract venues list string
							String venueResponseList = NetworkUtility
									.extractJSONArrayFromResponseString(
											Constants.RESPONSE_VENUES_ARRAY_JSON_KEY,
											responseStr);

							GsonBuilder gsonBuilder = new GsonBuilder();
							Gson gson = gsonBuilder.create();
							VenueDataModel[] venueList = gson.fromJson(
									venueResponseList, VenueDataModel[].class);
							List<VenueDataModel> venueDataModels = null;
							if (venueList != null) {
								venueDataModels = (List<VenueDataModel>) Arrays
										.asList(venueList);
							}

							if (venueDataModels != null
									&& venueDataModels.size() > 0) {
								DBController.getInstance(serviceContext)
										.addVenues(venueDataModels);
							}
							System.out.println(venueList.length);
							callBack.onRequestFinish(
									Constants.GET_NEAREST_VENUES_REQUEST_CODE,
									venueDataModels);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}

	// Add user to venue
	public void checkInVenue(final String venueId,
			final BusinessCallBack callBack) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// get user version number
				String clientVersion = NetworkUtility
						.getUserVersion(new Date());

				// get oauth token
				String token = PreferencesUtility.getStringPreferencesValue(
						serviceContext, Constants.TOKEN_PREF_KEY);
				// create request Parames
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("venueId", venueId));

				// nameValuePairs.add(new BasicNameValuePair(
				// Constants.REQUEST_SIGNATURE_PARAM, signature));
				nameValuePairs.add(new BasicNameValuePair(
						Constants.REQUEST_OAUTH_TOKEN_PARAM, token));
				nameValuePairs.add(new BasicNameValuePair(
						Constants.REQUEST_VERSION_PARAM, clientVersion));

				// send request
				// String url = Constants.CHECK_IN_API + venueId;
				String url = Constants.CHECK_IN_ADD_API;
				String response = NetworkUtility.sentPostRequest(url,
						nameValuePairs);
				System.out.println("Response" + response);

				if (response != null && !response.equals("")) {

					try {
						String resultCode = NetworkUtility
								.extractJSONObjectFromResponseString(
										Constants.RESPONSE_META_DATA_JSON_KEY,
										response);
						JSONObject mainJsonObject = new JSONObject(resultCode);
						String code = mainJsonObject
								.getString(Constants.RESPONSE_CODE_JSON_KEY);
						callBack.onRequestFinish(
								Constants.CHECKIN_REQUEST_CODE,
								Integer.parseInt(code));
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		}).start();
	}

	public void getCurrentLocation(Context context,
			final BusinessCallBack callBack) {

		// create class object
		GPSTracker gps = new GPSTracker(context);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();

			if (latitude != 0.0 && longitude != 0.0) {
				String locationStr = latitude + "," + longitude;
				PreferencesUtility.saveStringPreferences(context,
						Constants.USER_CURRENT_LOCATION_PREF_KEY, locationStr);
				callBack.onRequestFinish(
						Constants.GET_CURRENT_LOCATION_REQUEST_CODE,
						Constants.RESPONSE_SUCCESS_CODE);
			} else {
				callBack.onRequestFinish(
						Constants.GET_CURRENT_LOCATION_REQUEST_CODE,
						Constants.RESPONSE_ERROR_CODE);
			}
		}

	}

	public void getVenuesFromDB(final BusinessCallBack callBack) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				List<VenueDataModel> venueDataModels = DBController
						.getInstance(serviceContext).getAllVenues();

				callBack.onRequestFinish(
						Constants.GET_VENUES_FROM_DABASE_REQUEST_CODE,
						venueDataModels);
			}
		}).start();
	}
}
