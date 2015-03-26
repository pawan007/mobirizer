package com.location.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataStoreHandler extends SQLiteOpenHelper {

	public static final String COLUMN_ID = "_id";

	public static final String TABLE_FREQ_LOCATION = "freqlocationdata";
	public static final String TABLE_RECENT_SEARCH_LOCATION = "search";
	public static final String COLUMN_FREQ_LOCATION_DATE = "date";
	public static final String COLUMN_FREQ_LOCATION_LAT = "latitude";
	public static final String COLUMN_FREQ_LOCATION_LONG = "longitude";
	public static final String COLUMN_FREQ_LOCATION_TYPE = "type";
	public static final String COLUMN_FREQ_LOCATION_PROVIDER = "provider";
	public static final String COLUMN_FREQ_LOCATION_MESSAGE = "message";
	public static final String COLUMN_FREQ_LOCATION_REPEAT = "repeate";
	public static final String COLUMN_FREQ_LOCATION_RADIUS = "radius";
	public static final String COLUMN_FREQ_LOCATION_TASKTYPE = "tasktype";
	public static final String COLUMN_FREQ_LOCATION_TASKNAME = "taskname";
	public static final String COLUMN_FREQ_LOCATION_NAME = "NAME";
	public static final String[] allFreqLocationColumns = { COLUMN_ID,
			COLUMN_FREQ_LOCATION_DATE, COLUMN_FREQ_LOCATION_LAT,
			COLUMN_FREQ_LOCATION_LONG, COLUMN_FREQ_LOCATION_TYPE,
			COLUMN_FREQ_LOCATION_PROVIDER, COLUMN_FREQ_LOCATION_MESSAGE,
			COLUMN_FREQ_LOCATION_REPEAT,COLUMN_FREQ_LOCATION_RADIUS,COLUMN_FREQ_LOCATION_TASKTYPE,COLUMN_FREQ_LOCATION_TASKNAME };

	public static final String[] getallrecentLocationColumns = { COLUMN_ID,
		COLUMN_FREQ_LOCATION_LAT,
		COLUMN_FREQ_LOCATION_LONG, COLUMN_FREQ_LOCATION_NAME,
		};

	// database info
	private static final String DATABASE_NAME = "locationfinderFive.db";
	private static final int DATABASE_VERSION = 5;

	private static final String DATABASE_CREATE_FREQ_LOCATION_TBL = "create table "
			+ TABLE_FREQ_LOCATION
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_FREQ_LOCATION_DATE
			+ " integer, "
			+ COLUMN_FREQ_LOCATION_LAT
			+ " text, "
			+ COLUMN_FREQ_LOCATION_LONG
			+ " text, "
			+ COLUMN_FREQ_LOCATION_TYPE
			+ " text, "
			+ COLUMN_FREQ_LOCATION_PROVIDER
			+ " text ,"
			+ COLUMN_FREQ_LOCATION_MESSAGE
			+ " text,"
			+ COLUMN_FREQ_LOCATION_REPEAT 
			+ " text,"
			+ COLUMN_FREQ_LOCATION_RADIUS 
			+ " text,"
			+COLUMN_FREQ_LOCATION_TASKTYPE
			+ " text,"
			+COLUMN_FREQ_LOCATION_TASKNAME
			+" text);";
	
	private static final String CREATE_SEARCH_HISTORY = "create table "
			+ TABLE_RECENT_SEARCH_LOCATION
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_FREQ_LOCATION_LAT
			+ " text, "
			+ COLUMN_FREQ_LOCATION_LONG
			+ " text, "
			+ COLUMN_FREQ_LOCATION_NAME
			+" text);";

	public DataStoreHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_FREQ_LOCATION_TBL);
		database.execSQL(CREATE_SEARCH_HISTORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}