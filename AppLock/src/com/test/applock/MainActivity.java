package com.test.applock;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.applock.core.AppModel;
import com.applock.core.CoreScreenService;
import com.applock.database.DataSourceHandler;
import com.applock.database.DbAppLockedObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static ArrayList<AppModel> items = null;

	public static final Comparator<AppModel> ALPHA_COMPARATOR = new Comparator<AppModel>() {
		private final Collator sCollator = Collator.getInstance();

		@Override
		public int compare(AppModel object1, AppModel object2) {
			return sCollator.compare(object1.getLabel(), object2.getLabel());
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Start app locking service
		Intent service = new Intent(this, CoreScreenService.class);
		startService(service);

		String appPackageName = getPackageName();
		PackageManager mPm = getApplicationContext().getPackageManager();
		List<ApplicationInfo> apps = mPm.getInstalledApplications(0);
		if (apps == null) {
			apps = new ArrayList<ApplicationInfo>();
		}
		items = new ArrayList<AppModel>();
		for (int i = 0; i < apps.size(); i++) {
			String pkg = apps.get(i).packageName;
			if (getApplicationContext().getPackageManager()
					.getLaunchIntentForPackage(pkg) != null
					&& pkg.equalsIgnoreCase(appPackageName) == false) {
				AppModel app = new AppModel(getApplicationContext(),
						apps.get(i));
				app.setSystemFlag(false);
				app.loadLabel(getApplicationContext());
				items.add(app);
			}
		}
		Collections.sort(items, ALPHA_COMPARATOR);

		final ListView listview = (ListView) findViewById(R.id.appListView);
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < items.size(); ++i) {
			list.add("");
		}
		final AppListAdapter adapter = new AppListAdapter(this, list);
		listview.setAdapter(adapter);
	}

	class AppListAdapter extends ArrayAdapter<String> {
		private final Context mContext;

		public AppListAdapter(Context context, List<String> values) {
			super(context, R.layout.list_app_item, values);
			this.mContext = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_app_item, parent,
					false);

			// Gather app info
			String appName = items.get(position).getLabel();
			String appPckgName = items.get(position)
					.getApplicationPackageName();
			Drawable appIcon = items.get(position).getIcon();

			// Apply app icon
			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.appIconImgView);
			imageView.setImageDrawable(appIcon);

			// Apply app title
			TextView textView = (TextView) rowView
					.findViewById(R.id.appNameTextView);
			textView.setText(appName);

			// Apply checkbox
			CheckBox checkBox = (CheckBox) rowView
					.findViewById(R.id.blockedCheckBox);
			DataSourceHandler db = DataSourceHandler.getInstance(mContext);
			List<DbAppLockedObject> lockedAppList = db
					.getAllAppLockedDataObject();
			boolean isFound = false;
			long rowId = -1;
			for (DbAppLockedObject dbObj : lockedAppList) {
				if (dbObj.getPackage().equalsIgnoreCase(appPckgName)) {
					isFound = true;
					rowId = dbObj.getId();
					break;
				}
			}
			items.get(position).setDbRowId(rowId);
			checkBox.setTag(new Integer(position));
			checkBox.setChecked(isFound);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					DataSourceHandler db = DataSourceHandler
							.getInstance(mContext);
					Integer positionId = (Integer) buttonView.getTag();
					if (isChecked) {
						// add to db
						DbAppLockedObject obj = new DbAppLockedObject();
						obj.setName(items.get(positionId).getLabel());
						obj.setPackage(items.get(positionId)
								.getApplicationPackageName());
						db.insertAppLockedDataObject(obj);
					} else {
						// remove from db
						DbAppLockedObject obj = new DbAppLockedObject();
						obj.setId(items.get(positionId).getDbRowId());
						db.deleteAppLockedDataObject(obj);
					}
				}
			});

			return rowView;
		}
	}
}
