package com.location.database;

public class DbLocationObject {
	private long rowid;
	private long date;
	private String locationLat;
	private String locationLong;
	private String locationType;
	private String locationProvider;
	private String repeate;
	private String message;
	private int radius;

	public long getRowid() {
		return rowid;
	}

	public void setRowid(long rowid) {
		this.rowid = rowid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRepeate() {
		return repeate;
	}

	public void setRepeate(String repeate) {
		System.out.println("repeat value>>>>"+repeate);
		this.repeate = repeate;
	}
	public long getId() {
		return rowid;
	}
	public void setId(long id) {
		this.rowid = id;
	}
	public void setLocationLat(String val) {
		this.locationLat = val;
	}
	public String getLocationLat() {
		return this.locationLat;
	}
	public void setLocationLong(String val) {
		this.locationLong = val;
	}

	public String getLocationLong() {
		return this.locationLong;
	}

	public void setLocationType(String locType) {
		this.locationType = locType;
	}

	public String getLocatonType() {
		return this.locationType;
	}

	public void setLocationProvider(String val) {
		this.locationProvider = val;
	}

	public String getLocatonProvider() {
		return this.locationProvider;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	public int getMapradius(){
		return radius;
		
	}
	public void setMapradius(int rvalue){
		this.radius=rvalue;
	}
}
