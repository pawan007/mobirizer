package com.applock.appmodel;

import java.io.File;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
public class AppModel {
	private final Context mContext;
	private final ApplicationInfo mInfo;
	private String mAppLabel = "";
	private String mAppPckName = "";
	private Drawable mIcon = null;
	private String extraInfo = "";
	private boolean isSystem = false;
	private boolean isSelected = false;
	private long dbRowId = 0;
	private boolean mMounted;
	private final File mApkFile;

	public AppModel(Context context, ApplicationInfo info) {
		mContext = context;
		mInfo = info;

		if (info != null) {
			mApkFile = new File(info.sourceDir);
		} else {
			mApkFile = null;
		}
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public void setDbRowId(long val) {
		this.dbRowId = val;
	}

	public long getDbRowId() {
		return this.dbRowId;
	}

	public ApplicationInfo getAppInfo() {
		return mInfo;
	}

	public String getApplicationPackageName() {
		if (getAppInfo() == null) {
			return this.mAppPckName;
		} else {
			return getAppInfo().packageName;
		}
	}

	public void setApplicationPackageName(String pckName) {
		this.mAppPckName = pckName;
	}

	public String getExtraInfo() {
		return this.extraInfo;
	}

	public void setExtraInfo(String info) {
		this.extraInfo = info;
	}

	public String getLabel() {
		return mAppLabel;
	}

	public void setLabel(String appName) {
		this.mAppLabel = appName;
	}

	public boolean getSystemFlag() {
		return isSystem;
	}

	public void setSystemFlag(boolean state) {
		this.isSystem = state;
	}

	public Drawable getIcon() {
		try {
			if (mIcon == null) {
				if (mApkFile.exists()) {
					mIcon = mInfo.loadIcon(mContext.getPackageManager());
					return mIcon;
				} else {
					mMounted = false;
				}
			} else if (!mMounted) {
				// If the app wasn't mounted but is now mounted, reload
				// its icon.
				if (mApkFile != null) {
					if (mApkFile.exists()) {
						mMounted = true;
						mIcon = mInfo.loadIcon(mContext.getPackageManager());
						return mIcon;
					}
				} else {
					return mIcon;
				}
			} else {
				return mIcon;
			}

			return mContext.getResources().getDrawable(
					android.R.drawable.sym_def_app_icon);
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mContext.getResources().getDrawable(
				android.R.drawable.sym_def_app_icon);
	}

	public void setIcon(Drawable icon) {
		this.mIcon = icon;
	}

	public void loadLabel(Context context) {
		if (mAppLabel == null || !mMounted) {
			if (!mApkFile.exists()) {
				mMounted = false;
				mAppLabel = mInfo.packageName;
			} else {
				mMounted = true;
				CharSequence label = mInfo.loadLabel(context
						.getPackageManager());
				mAppLabel = label != null ? label.toString()
						: mInfo.packageName;
			}
		}
	}
}
