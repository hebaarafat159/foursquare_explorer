package com.android.foursquareexplorer.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.foursquareexplorer.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.widget.Toast;

public class NetworkUtility {
	public static boolean isConnected(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {

			return true;
		} else {
			return false;
		}
	}

	public static void showNoInternetDialog(Context context) {

		Toast.makeText(
				context,
				context.getResources().getString(
						R.string.error_no_connection_msg_text),
				Toast.LENGTH_LONG).show();

	}

	public static boolean isConnectedToWifi(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected()) {

			return true;
		} else {
			return false;
		}
	}

	public static boolean isConnectedToMobileNetwork(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * extract json array string from response
	 * 
	 * @param key
	 * @param response
	 * @throws JSONException
	 */
	public static String extractJSONArrayFromResponseString(String key,
			String response) throws JSONException {
		JSONObject mainJsonObject = new JSONObject(response);
		return mainJsonObject.getJSONArray(key).toString();
	}

	/**
	 * extract json array string from response
	 * 
	 * @param key
	 * @param response
	 * @throws JSONException
	 */
	public static String extractJSONObjectFromResponseString(String key,
			String response) throws JSONException {
		JSONObject mainJsonObject = new JSONObject(response);
		return mainJsonObject.getJSONObject(key).toString();
	}

	// sent GET request
	@SuppressWarnings("finally")
	public static String sentGetRequest(String url, List<NameValuePair> params) {
		HttpResponse response = null;
		String responseStr = "";
		try {
			// Create http client object to send request to server
			HttpClient client = new DefaultHttpClient();
			// add params to url
			String urlStr = url + "?";
			if (params != null && params.size() > 0) {
				for (int j = 0; j < params.size(); j++) {
					NameValuePair valuePair = params.get(j);
					urlStr += valuePair.getName() + "=" + valuePair.getValue();
					if (j < params.size() - 1) {
						urlStr += "&";
					}
				}

			}
			// Create Request to server and get response
			HttpGet httpget = new HttpGet();
			httpget.setURI(new URI(urlStr));
			response = client.execute(httpget);
			// handle response
			if (response != null) {
				InputStream inputStream = response.getEntity().getContent();
				if (inputStream != null) {
					responseStr = parseResponseValue(inputStream);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return responseStr;
		}
	}

	// sent Post request
	@SuppressWarnings({ "finally", "finally", "finally" })
	public static String sentPostRequest(String url, List<NameValuePair> params) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		String responseStr = "";
		try {
			// Add your data
			httppost.setEntity(new UrlEncodedFormEntity(params));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			// handle response
			if (response != null) {
				InputStream inputStream = response.getEntity().getContent();
				if (inputStream != null) {
					responseStr = parseResponseValue(inputStream);
				}
			}
			System.out.println("Response" + responseStr);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			return responseStr;
		}
	}

	public static String parseResponseValue(InputStream inputStream)
			throws IOException {
		String responseStr = "";
		// start listening to the stream
		Scanner inStream = new Scanner(inputStream);
		// process the stream and store it in StringBuilder
		while (inStream.hasNextLine())
			responseStr += (inStream.nextLine());
		inputStream.close();
		return responseStr;
	}

	// format date to create version number
	public static String getUserVersion(Date date) {
		return DateFormat.format("yyyyMMdd", date).toString();
	}
}
