package com.applock.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CommonSharedPreferences {

	private static final String sharedPrefsFile = "CommonSharedPrefs";

	public static void saveStringPreferences(String key, String value,
			Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(sharedPrefsFile,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static String loadStringSavedPreferences(String key, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(sharedPrefsFile,
				Context.MODE_PRIVATE);
		String name = sp.getString(key, "");
		return name;
	}

	public static void saveIntPreferences(String key, int value, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(sharedPrefsFile,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static int loadIntSavedPreferences(String key, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(sharedPrefsFile,
				Context.MODE_PRIVATE);
		int name = sp.getInt(key, -1);
		return name;
	}

	public static void saveBooleanPreferences(String key, boolean value,
			Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(sharedPrefsFile,
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static boolean loadBooleanSavedPreferences(String key, Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences(sharedPrefsFile,
				Context.MODE_PRIVATE);
		boolean name = sp.getBoolean(key, false);
		return name;
	}

}