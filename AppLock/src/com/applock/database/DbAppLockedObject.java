package com.applock.database;

public class DbAppLockedObject {
	private long rowid;
	private String name;
	private String packageName;
	private long added;
	private long last;
	private int todaycount;

	public long getId() {
		return rowid;
	}

	public void setId(long id) {
		this.rowid = id;
	}

	public void setName(String val) {
		this.name = val;
	}

	public String getName() {
		return this.name;
	}

	public void setPackage(String val) {
		this.packageName = val;
	}

	public String getPackage() {
		return this.packageName;
	}

	public void setAddedDate(long val) {
		this.added = val;
	}

	public long getAddedDate() {
		return this.added;
	}

	public void setLastDate(long val) {
		this.last = val;
	}

	public long getLastDate() {
		return this.last;
	}

	public int getTodayCount() {
		return this.todaycount;
	}

	public void setTodayCount(int val) {
		this.todaycount = val;
	}
}
