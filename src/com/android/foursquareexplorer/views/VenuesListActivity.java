package com.android.foursquareexplorer.views;

import java.util.List;

import com.android.foursquareexplorer.Constants;
import com.android.foursquareexplorer.R;
import com.android.foursquareexplorer.datamodels.VenueDataModel;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class VenuesListActivity extends MainActivity {

	private VenueAdapter adapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity_layout);
		listView = (ListView) findViewById(R.id.venues_list_view_id);
		loadingProgressBar = (ProgressBar) findViewById(R.id.progress_bar_view_id);
		adapter = new VenueAdapter(VenuesListActivity.this);
		listView.setVisibility(View.INVISIBLE);
		initActivityData(this);
	}

	@Override
	public void onRequestFinish(int requestCode, int responseCode) {
		super.onRequestFinish(requestCode, responseCode);

	}

	@Override
	public void onRequestFinish(final int requestCode,
			final List<?> dataArrayList) {
		super.onRequestFinish(requestCode, dataArrayList);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				listView.setVisibility(View.VISIBLE);
				switch (requestCode) {
				case Constants.GET_NEAREST_VENUES_REQUEST_CODE:
					if (dataArrayList != null && dataArrayList.size() > 0) {
						initListView(dataArrayList);
					}
					break;
				case Constants.GET_VENUES_FROM_DABASE_REQUEST_CODE:
					if (dataArrayList != null && dataArrayList.size() > 0) {
						initListView(dataArrayList);
					} else {
						String errorStr = VenuesListActivity.this
								.getResources().getString(
										R.string.no_data_found_error_msg)
								+ ", "
								+ VenuesListActivity.this.getResources()
										.getString(
												R.string.no_network_error_msg);
						showErrorMsg(VenuesListActivity.this, errorStr);
					}
					break;
				default:
					break;
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initListView(List<?> dataArrayList) {
		if (dataArrayList != null) {
			System.out.println("NUmber : " + dataArrayList.size());
			if (dataArrayList.size() > 0) {
				adapter.updateAdapterList((List<VenueDataModel>) dataArrayList);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0,
							View venueView, int index, long arg3) {
						VenueDataModel model = (VenueDataModel) venueView
								.getTag();
						displaySelectedVenueData(VenuesListActivity.this, model);
					}
				});
			}
		}
	}

}
