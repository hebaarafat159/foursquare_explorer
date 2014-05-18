package com.android.foursquareexplorer.views;

import com.android.foursquareexplorer.Constants;
import com.android.foursquareexplorer.R;
import com.android.foursquareexplorer.util.PreferencesUtility;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class LoginWebActivity extends MainActivity {
	private WebView webView;
	private WebViewClient wvClient;
	protected WebChromeClient webChromClient;
	protected static final int WEB_LOADING_TIME_OUT_MILLIS = 5 * 1000;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity_layout);
		webView = (WebView) findViewById(R.id.login_webview_id);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setDomStorageEnabled(true);

		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			fixNewAndroid(webView);
		}

		setWebViewClient();
		setWebChromCLient();
	}

	@TargetApi(16)
	protected void fixNewAndroid(WebView webView) {
		try {
			webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		String url = Constants.ACCESS_TOKEN_URL + "?"
				+ Constants.REQUEST_CLIENT_ID_PARAM + "=" + Constants.CLIENT_ID
				+ "&" + Constants.REQUEST_TOKEN_TYPE_PARAM + "="
				+ Constants.REQUEST_TOKEN_TYPE_VALUE + "&"
				+ Constants.REQUEST_REDIRECT_URI_PARAM + "="
				+ Constants.REDIRECT_ACCESS_TOKEN_URL;

		webView.loadUrl(url);
		super.onStart();
	}

	protected void setWebViewClient() {
		wvClient = new MainWebViewClient();
		webView.setWebViewClient(wvClient);
	}

	protected void setWebChromCLient() {
		webChromClient = new MainChromeClient();
		webView.setWebChromeClient(webChromClient);
	}

	protected class MainWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, final String url, Bitmap favicon) {
			System.out.println("Start Page .. " + url);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(
							LoginWebActivity.this,
							getResources().getString(R.string.waiting_msg_text),
							Toast.LENGTH_LONG).show();
					handelResponseResult(url);
				}
			});

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			System.out.println("Finish Page .. " + url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			System.out.println("Error Received " + failingUrl);
		}
	}

	protected class MainChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			return true;

		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {

			return true;
		}

		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, JsPromptResult result) {

			return true;
		}

	}

	private void handelResponseResult(String url) {
		if (url.startsWith(Constants.REDIRECT_ACCESS_TOKEN_URL)) {
			int index = Constants.REDIRECT_ACCESS_TOKEN_URL.length()
					+ Constants.REQUEST_TOKEN_TYPE_PARAM.length() + 1;
			String token = url.substring(index);
			token = token.trim();
			PreferencesUtility.saveStringPreferences(LoginWebActivity.this,
					Constants.TOKEN_PREF_KEY, token);
			finish();
		} else if (url.startsWith(Constants.REDIRECT_CODE_URL)) {
			String code = url.substring(Constants.REDIRECT_CODE_URL.length());
			code = code.trim();
			// getIntent().putExtra("token_value", code);
			// setResult(RESULT_OK, getIntent());
			finish();
		}
	}

}
