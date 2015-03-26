package com.location.database;

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

	private DataSourceHandler() {

	}

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
	public long insertFreqLocationDataObject(DbLocationObject obj) {
		ContentValues values = new ContentValues();

		values.put(DataStoreHandler.allFreqLocationColumns[1], obj.getDate());
		values.put(DataStoreHandler.allFreqLocationColumns[2],
				obj.getLocationLat());
		values.put(DataStoreHandler.allFreqLocationColumns[3],
				obj.getLocationLong());
		values.put(DataStoreHandler.allFreqLocationColumns[4],
				obj.getLocatonType());
		values.put(DataStoreHandler.allFreqLocationColumns[5],
				obj.getLocatonProvider());
		values.put(DataStoreHandler.allFreqLocationColumns[6], obj.getMessage());
		values.put(DataStoreHandler.allFreqLocationColumns[7], obj.getRepeate());
		values.put(DataStoreHandler.allFreqLocationColumns[8], obj.getMapradius());
		long insertId = myDataBase.insert(DataStoreHandler.TABLE_FREQ_LOCATION,
				null, values);
//		System.out.println("at insert value"+insertId);
		return insertId;
		
	}

	public int updateFreqLocationDataObject(DbLocationObject obj) {
		ContentValues values = new ContentValues();
		values.put(DataStoreHandler.allFreqLocationColumns[1], obj.getDate());
		values.put(DataStoreHandler.allFreqLocationColumns[2],
				obj.getLocationLat());
		values.put(DataStoreHandler.allFreqLocationColumns[3],
				obj.getLocationLong());
		values.put(DataStoreHandler.allFreqLocationColumns[4],
				obj.getLocatonType());
		values.put(DataStoreHandler.allFreqLocationColumns[5],
				obj.getLocatonProvider());
		values.put(DataStoreHandler.allFreqLocationColumns[6], obj.getMessage());
		values.put(DataStoreHandler.allFreqLocationColumns[7], obj.getRepeate());
		values.put(DataStoreHandler.allFreqLocationColumns[8], obj.getMapradius());
		
		int updated = myDataBase.update(DataStoreHandler.TABLE_FREQ_LOCATION,
				values,
				DataStoreHandler.allFreqLocationColumns[0] + "=" + obj.getId(),
				null);
//		System.out.println("values>>>>>>>"+values);
//		System.out.println("at update value is>>>"+obj.getId()+"...."+obj.getRepeate()+"..............."+updated+"...calling update mydatabase"+DataStoreHandler.allFreqLocationColumns[0] + "=" + obj.getId());
//		System.out.println("at update>>>"+DataStoreHandler.allFreqLocationColumns[6]);
		return updated;
	}
	public int updateRepeatLocationDataObject(DbLocationObject obj) {
		ContentValues values = new ContentValues();
			values.put(DataStoreHandler.allFreqLocationColumns[7], obj.getRepeate());
			int updated = myDataBase.update(DataStoreHandler.TABLE_FREQ_LOCATION,
				values,
				DataStoreHandler.allFreqLocationColumns[0] + "=" + obj.getId(),
				null);
//		System.out.println("values>>>>>>>"+values);
//		System.out.println("at update value is>>>"+obj.getId()+"...."+obj.getRepeate()+"..............."+updated+"...calling update mydatabase"+DataStoreHandler.allFreqLocationColumns[0] + "=" + obj.getId());
//		System.out.println("at update>>>"+DataStoreHandler.allFreqLocationColumns[6]);
		return updated;
	}
	public void deleteFreqLocationDataObject(DbLocationObject object) {
		long id = object.getId();
//		long id=1;
		
//		System.out.println("inside delete database"+id);
		myDataBase.delete(DataStoreHandler.TABLE_FREQ_LOCATION,
				DataStoreHandler.COLUMN_ID + " = " + id, null);
		
	}

	public List<DbLocationObject> getAllFreqLocationDataObject() {
		List<DbLocationObject> dataArr = new ArrayList<DbLocationObject>();

		Cursor cursor = myDataBase.query(DataStoreHandler.TABLE_FREQ_LOCATION,
				DataStoreHandler.allFreqLocationColumns, null, null, null,
				null, null);

		if (cursor != null && cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				DbLocationObject data = cursorToFreqLocationDataObject(cursor);
				dataArr.add(data);
				cursor.moveToNext();
			}
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return dataArr;
	}

	private DbLocationObject cursorToFreqLocationDataObject(Cursor cursor) {
		DbLocationObject data = new DbLocationObject();
		data.setId(cursor.getLong(0));
		data.setDate(cursor.getInt(1));
		data.setLocationLat(cursor.getString(2));
		data.setLocationLong(cursor.getString(3));
		data.setLocationType(cursor.getString(4));
		data.setLocationProvider(cursor.getString(5));
		data.setRepeate(cursor.getString(6));
		data.setMessage(cursor.getString(7));
		data.setMapradius(cursor.getInt(8));
		return data;
	}
}
