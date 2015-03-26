package com.applock.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;

public class DataSourceHandler {

	private SQLiteDatabase myDataBase;
	private DataStoreHandler dbHelper;

	private static DataSourceHandler mInstance = null;

	public static DataSourceHandler getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new DataSourceHandler();
			mInstance.getDataSourceHandler(ctx);
		}
		return mInstance;
	}

	private void getDataSourceHandler(Context context) {
		try {
			dbHelper = new DataStoreHandler(context);
			myDataBase = dbHelper.getWritableDatabase();
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}
	}

	private void close() {
		try {
			myDataBase.close();
		} catch (Exception e) {
		}
		try {
			dbHelper.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Freq location base functions
	 */
	public long insertAppLockedDataObject(DbAppLockedObject obj) {
		ContentValues values = new ContentValues();
		values.put(DataStoreHandler.allAppLockedColumns[1], obj.getName());
		values.put(DataStoreHandler.allAppLockedColumns[2], obj.getPackage());
		values.put(DataStoreHandler.allAppLockedColumns[3], obj.getAddedDate());
		values.put(DataStoreHandler.allAppLockedColumns[4], obj.getLastDate());
		values.put(DataStoreHandler.allAppLockedColumns[5], obj.getTodayCount());

		long insertId = myDataBase.insert(DataStoreHandler.TABLE_APP_LOCKED,
				null, values);
		return insertId;
	}

	public int updateAppLockedDataObject(DbAppLockedObject obj) {
		ContentValues values = new ContentValues();
		values.put(DataStoreHandler.allAppLockedColumns[1], obj.getName());
		values.put(DataStoreHandler.allAppLockedColumns[2], obj.getPackage());
		values.put(DataStoreHandler.allAppLockedColumns[3], obj.getAddedDate());
		values.put(DataStoreHandler.allAppLockedColumns[4], obj.getLastDate());
		values.put(DataStoreHandler.allAppLockedColumns[5], obj.getTodayCount());

		int updated = myDataBase.update(DataStoreHandler.TABLE_APP_LOCKED,
				values,
				DataStoreHandler.allAppLockedColumns[0] + "=" + obj.getId(),
				null);
		return updated;
	}

	public void deleteAppLockedDataObject(DbAppLockedObject object) {
		long id = object.getId();
		myDataBase.delete(DataStoreHandler.TABLE_APP_LOCKED,
				DataStoreHandler.COLUMN_ID + " = " + id, null);
	}

	public List<DbAppLockedObject> getAllAppLockedDataObject() {
		List<DbAppLockedObject> dataArr = new ArrayList<DbAppLockedObject>();

		Cursor cursor = myDataBase.query(DataStoreHandler.TABLE_APP_LOCKED,
				DataStoreHandler.allAppLockedColumns, null, null, null, null,
				null);

		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				DbAppLockedObject data = cursorToAppLockedDataObject(cursor);
				dataArr.add(data);
				cursor.moveToNext();
			}
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return dataArr;
	}

	private DbAppLockedObject cursorToAppLockedDataObject(Cursor cursor) {
		DbAppLockedObject data = new DbAppLockedObject();
		data.setId(cursor.getLong(0));
		data.setName(cursor.getString(1));
		data.setPackage(cursor.getString(2));
		data.setAddedDate(cursor.getLong(3));
		data.setLastDate(cursor.getLong(4));
		data.setTodayCount(cursor.getInt(5));
		return data;
	}
}
