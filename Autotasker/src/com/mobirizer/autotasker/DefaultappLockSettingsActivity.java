package com.mobirizer.autotasker;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.applock.appmodel.AppModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class DefaultappLockSettingsActivity extends Activity{
	public static String getapplistforsetting="",totallockedapp="",appPackagefromlistbd;
//	private Button patternsetting;
	private SavePreferences savePreferences;
	private SharedPreferences sharedPreferences;
	ImageView savebutton;
	ListView listview ;
	ArrayList<String> list;
	private ArrayList<String> applicationName = null;
	public static ArrayList<AppModel> items = null;
	private String keyvalue_defaultlocklist="getapplistforsetting";
	private int appcount=0;
	private TextView listcount;
	private AdView adView;
	public static final Comparator<AppModel> ALPHA_COMPARATOR = new Comparator<AppModel>() {
		private final Collator sCollator = Collator.getInstance();

		@Override
		public int compare(AppModel object1, AppModel object2) {
			return sCollator.compare(object1.getLabel(), object2.getLabel());
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_defaultapplocksetting);
		savePreferences = new SavePreferences();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		applicationName = new ArrayList<String>();
		listcount=(TextView) findViewById(R.id.listcount);
		listview = (ListView) findViewById(R.id.appListView);
		Applistasyntask applistasyntask = new Applistasyntask(getApplicationContext());
		applistasyntask.execute();

//		patternsetting.setOnClickListener(new View.OnClickListener()
//		{
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//					enterSavedPattern();
//				showCreatePattern();
//				//					enterForgotSavedPattern();
//			}
//
//		});
		savebutton=(ImageView) findViewById(R.id.img_settingsave);
		savebutton.setOnClickListener(new View.OnClickListener() 
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				savePreferences.SavePreferences(DefaultappLockSettingsActivity.this, keyvalue_defaultlocklist, convertArrayListtoString(applicationName));
				finish(); 
			}
		});


		adView = new AdView(this);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(getResources().getString(R.string.Banner_ad_id));
		LinearLayout layout = (LinearLayout) findViewById(R.id.add_layout);
		layout.setVisibility(View.VISIBLE);
		layout.addView(adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

	}

	private void getdefaultAppList()
	{
		String tempArray[] = null;
		String str = sharedPreferences.getString(keyvalue_defaultlocklist,"");
		
		if(str!= null && !str.equalsIgnoreCase(""))
		{
			tempArray = str.split("#");		
		}
	
		if(applicationName!= null && !applicationName.isEmpty())
		{
			applicationName.clear();
		}
		
		if(tempArray!= null)
		{
			for(String tempStr : tempArray)
			{
				applicationName.add(tempStr);
			}
		}
		
	}
	
	private void getPackageInfo()
	{
		String appPackageName = getPackageName();
		PackageManager mPm = getApplicationContext().getPackageManager();
		List<ApplicationInfo> apps = mPm.getInstalledApplications(0);
		
		items = new ArrayList<AppModel>();
		
		if(apps!=null && !apps.isEmpty())
		{
			for (int i = 0; i < apps.size(); i++)
			{
				String pkg = apps.get(i).packageName;
				if (mPm.getLaunchIntentForPackage(pkg) != null && pkg.equalsIgnoreCase(appPackageName) == false)
				{
					AppModel app = new AppModel(getApplicationContext(),
							apps.get(i));
					app.setSystemFlag(false);
					app.loadLabel(getApplicationContext());
					
					if(applicationName.contains(pkg))
					{
						app.setSelected(true);
					}
					else
					{
						app.setSelected(false);
					}
					if(app.isSelected()){
						appcount=appcount+1;
						System.out.println("11111app count at list"+appcount);
						
					}
					items.add(app);
				}
			}
		}
		Collections.sort(items, ALPHA_COMPARATOR);

		list = new ArrayList<String>();
		for (int i = 0; i < items.size(); ++i) {
			list.add("");
		}

	}
	private class Applistasyntask extends AsyncTask<Void, Void, Void>
	{
		private ProgressDialog progressDialog;
		public Applistasyntask(Context applicationContext)
		{
			// TODO Auto-generated constructor stub
			progressDialog =new ProgressDialog(DefaultappLockSettingsActivity.this);
			progressDialog.setMessage("Please wait........");
			progressDialog.setCancelable(false);
			progressDialog.show();

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//				 progress.setMessage("Downloading Music :) ");

		}
		@Override
		protected Void doInBackground(Void... params) 
		{
			getdefaultAppList();
			getPackageInfo();
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(progressDialog!=null && progressDialog.isShowing())
			{
				progressDialog.dismiss();
			}
			AppListAdapter adapter = new AppListAdapter(DefaultappLockSettingsActivity.this, list);
			listview.setAdapter(adapter);
			listcount.setText(""+appcount+" Application selected");
		}

	}


	class AppListAdapter extends ArrayAdapter<String> {
		private final Context mContext;

		public AppListAdapter(Context context, List<String> values) {
			super(context, R.layout.list_app_item, values);
			this.mContext = context;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_app_item, parent,
					false);
			// Apply app icon
			ImageView imageView = (ImageView) rowView
					.findViewById(R.id.appIconImgView);
			imageView.setImageDrawable(items.get(position).getIcon());

			// Apply app title
			TextView textView = (TextView) rowView
					.findViewById(R.id.appNameTextView);
			textView.setText(items.get(position).getLabel());

			// Apply checkbox
			CheckBox checkBox = (CheckBox) rowView
					.findViewById(R.id.blockedCheckBox);
			if(items.get(position).isSelected())
			{
				checkBox.setChecked(true);
			}
			else
			{
				checkBox.setChecked(false);
			}
			long rowId = -1;
			items.get(position).setDbRowId(rowId);
			checkBox.setTag(position);

			//				checkBox.setChecked(isFound);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					//						
					Integer positionId = (Integer) buttonView.getTag();
					if (isChecked)
					{
						items.get(positionId).setSelected(true);
						appcount=appcount+1;
						listcount.setText(""+appcount+" Application selected");
						// add to db
						applicationName.add(items.get(positionId).getApplicationPackageName());
					} else
					{
						items.get(positionId).setSelected(false);
						applicationName.remove(items.get(positionId).getApplicationPackageName());
						appcount=appcount-1;
						listcount.setText(""+appcount+" Application selected");
					}
				}
			});

			return rowView;
		}
	}

	private String convertArrayListtoString(ArrayList<String> list)
	{
		String appList = "";
		for(String str : list)
		{
			appList = appList+"#"+str;
		}
		return appList;
	}

//	private void showCreatePattern()
//	{
//		Settings.Security.setAutoSavePattern(SettingsActivity.this, true);
//		Intent intent = new Intent(LockPatternActivity.ACTION_CREATE_PATTERN,
//				null, SettingsActivity.this, LockPatternActivity.class);
//		startActivityForResult(intent, 1);
//	}
}
