package com.android.foursquareexplorer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtility {

	/**
	 * save string value in preferences
	 * 
	 * @param key
	 * @param value
	 */
	public static void saveStringPreferences(Context context, String key,
			String value) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.commit();

	}

	/**
	 * return string preferences value
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getStringPreferencesValue(Context context, String key) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getString(key, null);
	}

	/**
	 * save string value in preferences
	 * 
	 * @param key
	 * @param value
	 */
	public static void saveIntegerPreferences(Context context, String key,
			int value) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * return string preferences value
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static int getIntegerPreferencesValue(Context context, String key) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getInt(key, -1);
	}

	/**
	 * save boolean value in preferences
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBooleanPreferencesValue(Context context, String key,
			boolean value) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * return boolean preferences value
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBooleanPreferencesValue(Context context, String key) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPref.getBoolean(key, false);
	}

}
