package bin;

public class ReminderInfo {

	private String mLocationName;
	private String mMessage;
	private String mLati;
	private String mLongi;
	private String mRepeat;
	private int mradius;
	private int mTasktype;
	private String mTaskName;
	
	public String getmTaskName() {
		return mTaskName;
	}

	public void setmTaskName(String mTaskName) {
		this.mTaskName = mTaskName;
	}

	private long locid;
	public long getLocid() {
		return locid;
	}

	public void setLocid(long locid) {
		this.locid = locid;
	}

	public String getmLocationName() {
		return mLocationName;
	}

	public void setmLocationName(String mLocationName) {
		this.mLocationName = mLocationName;
	}

	public String getmMessage() {
		return mMessage;
	}

	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public String getmLati() {
		return mLati;
	}

	public void setmLati(String mLati) {
		this.mLati = mLati;
	}

	public String getmLongi() {
		return mLongi;
	}
	public void setmRepeat(String mRept) {
//		System.out.println("at setmrepeat???????"+mRept);
		this.mRepeat = mRept;
	}

	public String getmRepeat() {
		
		return mRepeat;
	}
	public void setmLongi(String mLongi) {
		this.mLongi = mLongi;
	}
public int getmMapradius(){
	return mradius;
}
public void setmMapradius(int mRadius){
	this.mradius=mRadius;
}
public int getmTask() {
	return mTasktype;
}

public void setmTask(int mTask) {
	this.mTasktype = mTask;
}
}
