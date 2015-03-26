package com.mobirizer.autotasker;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.applock.appmodel.AppModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;



@SuppressLint("ViewHolder")
public class ApplicationListforlockActivity extends Activity {
//	private String getapplist="";
	private ArrayList<String> applicationName = null;
	private SavePreferences savePreferences;
//	private SharedPreferences sharedPreferences;
	ImageView doneImageView = null;
	public static ArrayList<AppModel> items = null;
	ListView listview = null;
	private String applistfromintentdb="";
	ArrayList<String> list = null;
	private String keyvalue_locklist = "getapplist";
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_applicationlistforlock);
		Intent packagenamefromedit=getIntent();
		if(packagenamefromedit!=null){
			try {
				applistfromintentdb=packagenamefromedit.getExtras().getString("Message");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				applistfromintentdb="";
			}
			System.out.println("111111package name at list"+applistfromintentdb);
		}
		savePreferences = new SavePreferences();
//		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		listview = (ListView) findViewById(R.id.appListView);
		doneImageView = (ImageView)findViewById(R.id.btndone);
		listcount=(TextView) findViewById(R.id.listcount);
		applicationName = new ArrayList<String>();
		
		Applistasyntask applistasyntask = new Applistasyntask(ApplicationListforlockActivity.this);
		applistasyntask.execute();
		
		doneImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				savePreferences.SavePreferences(ApplicationListforlockActivity.this, keyvalue_locklist, getAppList(applicationName));
				System.out.println("<<<<<<<<<111111111"+getAppList(applicationName));
//				if(LocationActivity.dbinsertupdate==true)
//				{
//					savePreferences.SavePreferences(ApplicatinListActivity.this, keyvalue_locklist, getAppList(applicationName));
//					getapplist=sharedPreferences.getString(keyvalue_locklist, "");
//					if(getapplist.length()>1 && getapplist.contains("."))
//					{
//						FirstActivity.placemsgforedit = getapplist;
//					}
//					else{
//						FirstActivity.placemsgforedit="";
//					}
					Intent intent = new Intent();
					intent.putExtra("Package", getAppList(applicationName));
					setResult(RESULT_OK, intent);
//				}
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
					if(!applicationName.isEmpty() && applicationName.contains(pkg))
					{
					app.setSelected(true);
						}
					else{
						app.setSelected(false);
//						listcount.setText(appcount+"Application selected");
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
	private void getdefaultAppList()
	{
		String tempArray[] = null;
//		String str = sharedPreferences.getString(keyvalue_locklist,"");
		String str="";
if(applistfromintentdb!=null && applistfromintentdb.length()>1){
	str=applistfromintentdb;
}
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
	private class Applistasyntask extends AsyncTask<Void, Void, Void>
	{
		private ProgressDialog progressDialog;
		public Applistasyntask(Context applicationContext)
		{
			// TODO Auto-generated constructor stub
			progressDialog =new ProgressDialog(ApplicationListforlockActivity.this);
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

			AppListAdapter adapter = new AppListAdapter(ApplicationListforlockActivity.this, list);
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
		public View getView(int position, View convertView, ViewGroup parent) {
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

			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked)
				{
					Integer positionId = (Integer) buttonView.getTag();
					if (isChecked) 
					{
						items.get(positionId).setSelected(true);
						applicationName.add(items.get(positionId).getApplicationPackageName());
						appcount=appcount+1;
						listcount.setText(""+appcount+" Application selected");

					}
					else
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
	private String getAppList(ArrayList<String> list)
	{
		String appList = "";
		for(String str : list)
		{
			appList = appList+"#"+str;
		}
		return appList;
	}
}
