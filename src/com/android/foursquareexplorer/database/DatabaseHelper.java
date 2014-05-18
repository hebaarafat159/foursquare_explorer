package com.android.foursquareexplorer.database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.foursquareexplorer.Constants;
import com.android.foursquareexplorer.datamodels.LocationDataModel;
import com.android.foursquareexplorer.datamodels.VenueDataModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	@SuppressWarnings("rawtypes")
	private Map<String, Class> classArray;

	private Map<String, Dao<?, Integer>> daoArray;

	public DatabaseHelper(Context context) {
		super(context, Constants.DATABASE_NAME_KEY, null,
				Constants.DATABASE_VERSION);
		daoArray = new HashMap<String, Dao<?, Integer>>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		for (String key : classArray.keySet()) {
			Log.i("-------------- key = ", key + "");
			try {
				TableUtils.createTableIfNotExists(connectionSource,
						classArray.get(key));

			} catch (SQLException e) {
				Log.e(DatabaseHelper.class.getName(), "Can't create database",
						e);
				continue;
			}
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int arg2, int arg3) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, LocationDataModel.class,
					true);
			TableUtils.dropTable(connectionSource, VenueDataModel.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings("rawtypes")
	public Map<String, Class> getClassArray() {
		return classArray;
	}

	public void setClassArray(
			@SuppressWarnings("rawtypes") Map<String, Class> classArray) {
		this.classArray = classArray;
	}

	/**
	 * Returns the Database Access Object (DAO) for our SimpleData class. It
	 * will create it or just give the cached value.
	 */
	@SuppressWarnings("unchecked")
	public Dao<?, Integer> getDao(String key) throws SQLException {
		if (daoArray.get(key) == null) {
			daoArray.put(key, getDao(classArray.get(key)));
		}
		return daoArray.get(key);
	}

}
