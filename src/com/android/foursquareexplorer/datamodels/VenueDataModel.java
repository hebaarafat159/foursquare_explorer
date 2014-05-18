package com.android.foursquareexplorer.datamodels;

import com.android.foursquareexplorer.Constants;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Constants.VENUES_TABLE_NAME_KEY)
public class VenueDataModel extends DataModel<VenueDataModel> {

	@SerializedName(Constants.VENUE_ID_KEY)
	@DatabaseField(columnName = Constants.VENUE_ID_KEY, id = true)
	public String id;

	@SerializedName(Constants.VENUE_NAME_KEY)
	@DatabaseField(columnName = Constants.VENUE_NAME_KEY, defaultValue = "")
	public String name;

	@SerializedName(Constants.VENUE_LOCATION_KEY)
	@DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
	public LocationDataModel location;
}
