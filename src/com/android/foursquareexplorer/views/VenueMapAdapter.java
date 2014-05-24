package com.android.foursquareexplorer.views;

import android.view.LayoutInflater;
import android.view.View;

import com.android.foursquareexplorer.R;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import android.widget.TextView;

public class VenueMapAdapter implements InfoWindowAdapter {
	LayoutInflater inflater = null;

	public VenueMapAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View popup = inflater.inflate(R.layout.map_place_popup, null);

		TextView tv = (TextView) popup.findViewById(R.id.title);

		tv.setText(marker.getTitle());
		tv = (TextView) popup.findViewById(R.id.snippet);
		tv.setText(marker.getSnippet());

		return (popup);
	}

}
