package com.location.service;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import com.kl.kitlocate.interfaces.KLLocation;
import com.kl.kitlocate.interfaces.KitLocate;
import com.mobirizer.autotasker.LockActivity;
import com.mobirizer.autotasker.SavePreferences;



public class LocationMatcherService extends Service {
	private Context mContext = null;
	private Timer mTimer = null;
	private ActivityManager mActivityManager = null;
	private static String temptopapp="";
	final String deviveAdminPckgName = "com.android.settings.DeviceAdminAdd";
	final String lockActName = "com.location.finder";
	SavePreferences savePreferences;
	private SharedPreferences sharedPreferences;
	String tempappforcompare="";
	private static boolean isMyLockScreen = false;
	private final static String packagefrommydb="packagefrommydb";
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mContext = this.getApplicationContext();
		System.out.println("11111111111111 Start Service");
		// Init Kitlocate library
		KitLocate.initKitLocate(this,"0f2aaf97-3c27-4f42-8329-f9c17f4ddd34",KitLocateCallbacks.class);
		savePreferences = new SavePreferences();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// Start Periodic location update
		KLLocation.registerPeriodicLocation(this.getApplicationContext());
		KLLocation.setPeriodicMinimumTimeInterval(this.getApplicationContext(),5);



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
		mTimer.schedule(new TopAppCheckerTask(), 10, 600);
		//		}
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
		public void run()
		{
//			boolean appMatched = false;
//			boolean showAppLockScreen = false;
			final String appPckgName = mContext.getPackageName();
			ActivityManager am = (ActivityManager) mContext
					.getSystemService(ACTIVITY_SERVICE);
			RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
			if(foregroundTaskInfo.topActivity.getPackageName().equalsIgnoreCase("com.android.launcher")||foregroundTaskInfo.topActivity.getPackageName().equalsIgnoreCase("com.miui.home")||foregroundTaskInfo.topActivity.getPackageName().contains("launcher"))
			{
				temptopapp="";
				isMyLockScreen = false;
				setLockValue(temptopapp);
			}
			String topPckgName = foregroundTaskInfo.topActivity
					.getPackageName();
			String topActName = foregroundTaskInfo.topActivity.getClassName();
			if(appPckgName.equalsIgnoreCase(topPckgName) && !isMyLockScreen)
			{
				isMyLockScreen = true;
			Intent lockScreen = new Intent(mContext, LockActivity.class);
			lockScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(lockScreen);
				
			}
//			if (topActName.equalsIgnoreCase(lockActName))
//			{
//				return;
//			}
			String appPackagefromlistbd=sharedPreferences.getString(packagefrommydb,"");
			if(appPackagefromlistbd.contains(topPckgName))
			{
					if(!topPckgName.equalsIgnoreCase(getLockvalue()))
					{
						temptopapp=topPckgName;
						setLockValue(temptopapp);
						Intent lockScreen = new Intent(mContext, LockActivity.class);
						lockScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(lockScreen);
					}
				}
			}
	}
	private RunningAppProcessInfo getForegroundApp() 
	{
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
	@SuppressLint("NewApi")
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
				//				}
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				stopScreenWatcher();
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
//	}

private void setLockValue(String value)
{
   savePreferences.SavePreferences(getApplicationContext(),"lockapplist", value);
}
private String getLockvalue()
{
	String listapp=sharedPreferences.getString("lockapplist", "");
	return listapp;
}
}

