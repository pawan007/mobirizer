package com.applock.core;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.applock.database.DataSourceHandler;
import com.applock.database.DbAppLockedObject;
import com.test.applock.AppLockedScreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;

public class CoreScreenService extends Service {

	private Context mContext = null;
	private Timer mTimer = null;
	private ActivityManager mActivityManager = null;

	final String deviveAdminPckgName = "com.android.settings.DeviceAdminAdd";
	final String lockActName = "com.test.applock.AppLockedScreen";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Storing user app context
		mContext = this.getApplicationContext();

		// Check current screen state off-on
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		boolean isScreenOn = pm.isScreenOn();
		if (isScreenOn) {
			startScreenWatcher();
		}

		// Register screen broadcast event receivers
		registerReceiver(screenBroadCastReceiver, new IntentFilter(
				Intent.ACTION_SCREEN_ON));
		registerReceiver(screenBroadCastReceiver, new IntentFilter(
				Intent.ACTION_SCREEN_OFF));

		return START_STICKY;
	}

	private void startScreenWatcher() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
		}
		mTimer = new Timer();
		mTimer.schedule(new TopAppCheckerTask(), 500, 1000);
	}

	private void stopScreenWatcher() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer.purge();
			mTimer = null;
		}
	}

	// Timertask to handle app in foreground
	class TopAppCheckerTask extends TimerTask {
		@Override
		public void run() {
			boolean appMatched = false;
			final String appPckgName = mContext.getPackageName();

			// Gather db added app list
			DataSourceHandler db = DataSourceHandler.getInstance(mContext);
			List<DbAppLockedObject> lockedAppList = db
					.getAllAppLockedDataObject();

			ActivityManager am = (ActivityManager) mContext
					.getSystemService(ACTIVITY_SERVICE);
			RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
			String topPckgName = foregroundTaskInfo.topActivity
					.getPackageName();
			String topActName = foregroundTaskInfo.topActivity.getClassName();
			if (topActName.equalsIgnoreCase(lockActName)) {
				return;
			}

			//matching database entries with current top activity
			for (DbAppLockedObject dbAppLockedObject : lockedAppList) {
				String lockAppPkg = dbAppLockedObject.getPackage();

				if (topPckgName.equalsIgnoreCase(appPckgName) == false
						&& (topPckgName.equalsIgnoreCase(lockAppPkg) || topActName
								.equalsIgnoreCase(deviveAdminPckgName))) {
					appMatched = true;
					break;
				}
			}

			boolean showAppLockScreen = false;
			if (appMatched) {
				showAppLockScreen = true;
			} else if (topActName.equalsIgnoreCase(deviveAdminPckgName)) {
				showAppLockScreen = true;
			}

			if (showAppLockScreen) {
				// appMatched with locked apps
				Intent lockScreen = new Intent(mContext, AppLockedScreen.class);
				lockScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(lockScreen);
			}
		}
	}

	private RunningAppProcessInfo getForegroundApp() {
		RunningAppProcessInfo result = null, info = null;

		if (mActivityManager == null)
			mActivityManager = (ActivityManager) mContext
					.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> l = mActivityManager
				.getRunningAppProcesses();
		Iterator<RunningAppProcessInfo> i = l.iterator();
		while (i.hasNext()) {
			info = i.next();
			if (info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
					&& !isRunningService(info.processName)) {
				result = info;
				break;
			}
		}
		return result;
	}

	private ComponentName getActivityForApp(RunningAppProcessInfo target) {
		ComponentName result = null;
		ActivityManager.RunningTaskInfo info;

		if (target == null)
			return null;

		if (mActivityManager == null)
			mActivityManager = (ActivityManager) mContext
					.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> l = mActivityManager
				.getRunningTasks(9999);
		Iterator<ActivityManager.RunningTaskInfo> i = l.iterator();

		while (i.hasNext()) {
			info = i.next();
			if (info.baseActivity.getPackageName().equals(target.processName)) {
				result = info.topActivity;
				break;
			}
		}

		return result;
	}

	private boolean isRunningService(String processname) {
		if (processname == null || processname.isEmpty())
			return false;

		RunningServiceInfo service;

		if (mActivityManager == null)
			mActivityManager = (ActivityManager) mContext
					.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> l = mActivityManager.getRunningServices(9999);
		Iterator<RunningServiceInfo> i = l.iterator();
		while (i.hasNext()) {
			service = i.next();
			if (service.process.equals(processname))
				return true;
		}

		return false;
	}

	// Broadcast receiver to gather screen events
	BroadcastReceiver screenBroadCastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				startScreenWatcher();
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				stopScreenWatcher();
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
