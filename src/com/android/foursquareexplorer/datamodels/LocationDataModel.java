package com.android.foursquareexplorer.datamodels;

import com.android.foursquareexplorer.Constants;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Constants.LOCATION_TABLE_NAME_KEY)
public class LocationDataModel extends DataModel<LocationDataModel> {

	@DatabaseField(columnName = Constants.VENUE_ID_KEY, id = true)
	public String id;

	@SerializedName(Constants.VENUE_LOCATION_LATITUDE_KEY)
	@DatabaseField(columnName = Constants.VENUE_LOCATION_LATITUDE_KEY)
	public double Latitude;

	@SerializedName(Constants.VENUE_LOCATION_LONGITUDE_KEY)
	@DatabaseField(columnName = Constants.VENUE_LOCATION_LONGITUDE_KEY)
	public double Longitude;

	@SerializedName(Constants.VENUE_LOCATION_DISTANCE_KEY)
	@DatabaseField(columnName = Constants.VENUE_LOCATION_DISTANCE_KEY)
	public double distance;

	@SerializedName(Constants.VENUE_LOCATION_CC_KEY)
	@DatabaseField(columnName = Constants.VENUE_LOCATION_CC_KEY, defaultValue = "")
	public String cc;

	@SerializedName(Constants.VENUE_LOCATION_COUNTRY_KEY)
	@DatabaseField(columnName = Constants.VENUE_LOCATION_COUNTRY_KEY, defaultValue = "")
	public String country;
}
