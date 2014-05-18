package com.android.foursquareexplorer.datamodels;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

public class DataModel<T> {

	protected Dao<T, Integer> dao;
	protected QueryBuilder<T, Integer> queryBuilder;

	@SuppressWarnings("unchecked")
	public void setDao(Dao<?, Integer> dao) {
		this.dao = ((Dao<T, Integer>) dao);
		queryBuilder = (QueryBuilder<T, Integer>) dao.queryBuilder();
	}

	public Dao<T, Integer> getDao() {
		return dao;
	}
}
