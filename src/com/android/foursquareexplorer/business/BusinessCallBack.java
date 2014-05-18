package com.android.foursquareexplorer.business;

import java.util.List;

public interface BusinessCallBack {

	public void onRequestFinish(int requestCode, List<?> dataArrayList);

	public void onRequestFinish(int requestCode, int responseCode);

}
