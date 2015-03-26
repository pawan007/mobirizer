package com.applock.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataStoreHandler extends SQLiteOpenHelper {

	public static final String COLUMN_ID = "_id";

	public static final String TABLE_APP_LOCKED = "applockedlist";
	public static final String COLUMN_APP_LKD_NAME = "name";
	public static final String COLUMN_APP_LKD_PCKG = "package";
	public static final String COLUMN_APP_LKD_ADDED = "added";
	public static final String COLUMN_APP_LKD_LAST = "last";
	public static final String COLUMN_APP_LKD_TDCOUNT = "today";
	public static final String[] allAppLockedColumns = { COLUMN_ID,
			COLUMN_APP_LKD_NAME, COLUMN_APP_LKD_PCKG, COLUMN_APP_LKD_ADDED,
			COLUMN_APP_LKD_LAST, COLUMN_APP_LKD_TDCOUNT };

	// database info
	private static final String DATABASE_NAME = "locationfinder.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE_APP_LOCKED_TBL = "create table "
			+ TABLE_APP_LOCKED
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_APP_LKD_NAME
			+ " text, "
			+ COLUMN_APP_LKD_PCKG
			+ " text, "
			+ COLUMN_APP_LKD_ADDED
			+ " integer, "
			+ COLUMN_APP_LKD_LAST
			+ " integer, " + COLUMN_APP_LKD_TDCOUNT + " integer );";

	public DataStoreHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_APP_LOCKED_TBL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}