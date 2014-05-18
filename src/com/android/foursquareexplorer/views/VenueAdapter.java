package com.android.foursquareexplorer.views;

import java.util.List;

import com.android.foursquareexplorer.R;
import com.android.foursquareexplorer.datamodels.VenueDataModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VenueAdapter extends BaseAdapter {

	private List<VenueDataModel> list;
	private Context adapterContext;

	public VenueAdapter(Context context) {
		adapterContext = context;
	}

	public VenueAdapter(Context context, List<VenueDataModel> venues) {
		adapterContext = context;
		list = venues;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int index) {
		return list.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = LayoutInflater.from(adapterContext).inflate(
					R.layout.venue_list_item_layout, null);
		}
		VenueDataModel model = list.get(index);
		// set tag
		convertView.setTag(model);
		// display data
		TextView nameTextView = (TextView) convertView
				.findViewById(R.id.name_text_view_id);
		nameTextView.setText(model.name);

		TextView locationTextView = (TextView) convertView
				.findViewById(R.id.location_text_view_id);
		String locationStr = Double.toString(model.location.Latitude) + " , "
				+ Double.toString(model.location.Longitude);
		locationTextView.setText(locationStr);

		TextView countryTextView = (TextView) convertView
				.findViewById(R.id.country_text_view_id);
		countryTextView.setText(model.location.country);
		return convertView;
	}

	public void updateAdapterList(List<VenueDataModel> venues) {
		list = venues;
		notifyDataSetChanged();
	}

}
