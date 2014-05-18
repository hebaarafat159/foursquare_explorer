package com.android.foursquareexplorer.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.foursquareexplorer.datamodels.DataModel;
import com.android.foursquareexplorer.datamodels.LocationDataModel;
import com.android.foursquareexplorer.datamodels.VenueDataModel;

import android.content.Context;

public class DBController {

	private static DBController instance;
	@SuppressWarnings("unused")
	private Context dbContext;
	private DatabaseHelper databaseHelper;
	@SuppressWarnings("rawtypes")
	private Map<String, Class> modelsMap;

	@SuppressWarnings("rawtypes")
	private DBController(Context context) {
		dbContext = context;
		modelsMap = new HashMap<String, Class>();

		modelsMap.put(VenueDataModel.class.getSimpleName(),
				VenueDataModel.class);
		modelsMap.put(LocationDataModel.class.getSimpleName(),
				LocationDataModel.class);

		databaseHelper = new DatabaseHelper(context);
		databaseHelper.setClassArray(modelsMap);
	}

	public static DBController getInstance(Context context) {
		if (instance == null)
			instance = new DBController(context);
		return instance;
	}

	/**
	 * @return
	 * @uml.property name="databaseHelper"
	 */
	public DatabaseHelper getDatabaseHelper() {
		return databaseHelper;
	}

	// set model deo
	private void setModelDeo(DataModel<?> dataModel) {
		try {
			String resourceName = dataModel.getClass().getSimpleName();
			dataModel.setDao(databaseHelper.getDao(resourceName));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {

		}

	}

	// add venues list to database
	public void addVenues(List<VenueDataModel> venues) {
		if (venues != null && venues.size() > 0) {
			for (VenueDataModel venueDataModel : venues) {
				insertOrUpdateVenue(venueDataModel);
			}
		}
	}

	// insert or update one venue in database
	private void insertOrUpdateVenue(VenueDataModel model) {
		// set venue model deo
		setModelDeo(model);

		// set location deo
		setModelDeo(model.location);

		// inser or update data
		try {
			// create or update location
			model.location.id = model.id;
			model.location.getDao().createOrUpdate(model.location);
			// create or update venue
			model.getDao().createOrUpdate(model);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<VenueDataModel> getAllVenues() {
		List<VenueDataModel> retList = null;
		try {
			retList = (List<VenueDataModel>) databaseHelper.getDao(
					VenueDataModel.class.getSimpleName()).queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		return retList;

	}
}
