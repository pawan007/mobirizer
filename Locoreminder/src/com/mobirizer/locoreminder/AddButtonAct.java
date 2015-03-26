package com.mobirizer.locoreminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AddButtonAct extends AsyncTask<String, Void, String>{
	FirstActivity mainInstance = FirstActivity.mainInstance;
//	FirstActivity pickAddressInstance = FirstActivity.pickAddressInstance;
	Context pickAddressContext = FirstActivity.pickAddressContext;
	ListView listview;
	com.mobirizer.locoreminder.ProgressDialog progressDialog;
	String inputAddrStr;
	String recentAddr = "";
	SharedPreferences sharedPreferences;
	String phoneNumber = "";
	ArrayList<String> addressesAndLatLngs = new ArrayList<String>();
	ArrayList<Map<String, String>> addresses = new ArrayList<Map<String, String>>();
	// public List<PickAddressListModel> addresses = new ArrayList<PickAddressListModel>();
	ArrayList<String> latLngs = new ArrayList<String>();

	public AddButtonAct() {
		
		super();
		System.out.println("inside addbutton act");
	}

	@Override
	protected void onPreExecute() {
		
		InputMethodManager inputManager = (InputMethodManager)pickAddressContext.getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(mainInstance.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		progressDialog = new com.mobirizer.locoreminder.ProgressDialog();
//		progressDialog = new ProgressDialog();
		progressDialog.GetDialog(pickAddressContext,"Verifying Address ...");
	}

	@Override
	protected String doInBackground(String... theInputAddrStrs) {
		System.out.println("AddButtonAct AsyncTask.");
		ConnectivityManager aConnectivityManager = (ConnectivityManager)mainInstance.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo aNetworkInfo = aConnectivityManager.getActiveNetworkInfo();
		if(aNetworkInfo==null){
			progressDialog.DismissDialog();
			mainInstance.runOnUiThread(new Runnable() {
				public void run() {
					NetworkAlert.ShowNetworkAlert(pickAddressContext);
				}
			});
		}
		else{
			boolean isConnected = aNetworkInfo.isConnectedOrConnecting();
			if(!isConnected){
				progressDialog.DismissDialog();
				mainInstance.runOnUiThread(new Runnable() {
					public void run() {
						NetworkAlert.ShowNetworkAlert(pickAddressContext);
					}
				});
			}
			else{
				inputAddrStr = theInputAddrStrs[0];

				new SavePreferences();
				sharedPreferences = PreferenceManager.getDefaultSharedPreferences(pickAddressContext);

				try{ 

					if(inputAddrStr!=null&&inputAddrStr.length()!=0){
						addressesAndLatLngs = PlacesApi.GetAddresses(inputAddrStr);
						System.out.println("addressesAndLatLngs>>>>>>>>"+addressesAndLatLngs);
						if(addressesAndLatLngs==null||addressesAndLatLngs.isEmpty()){
							progressDialog.DismissDialog();
							mainInstance.runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(pickAddressContext, "Please enter a valid address.", Toast.LENGTH_LONG).show();
								}
							});
							return "FAIL";
						}
						for(int i=0; i<addressesAndLatLngs.size(); i++){
							String aAddress = addressesAndLatLngs.get(i);
							String[] aAddressElements = aAddress.split(";");
							String[] aAddressNameElements = aAddressElements[0].split("!");
							addresses.add(putData( aAddressNameElements[0], aAddressNameElements[1] ));
							/* PickAddressListModel aPickAddressListModel = new PickAddressListModel();
							aPickAddressListModel.setName(aAddressNameElements[0]);
							aPickAddressListModel.setAddress(aAddressNameElements[1]);
							aPickAddressListModel.setSelected(false); */
							// addresses.add(aPickAddressListModel);
							latLngs.add(aAddressElements[1]);
						}
						//for save and match from history
//						mainInstance.globalVar.SetAddressOptions(addresses);
//						mainInstance.globalVar.SetLatLngOptions(latLngs);
//						pickAddressInstance.runOnUiThread(new Runnable() {
//							public void run() {
//								pickAddressInstance.historyAddressText.setVisibility(View.GONE);
//								pickAddressInstance.historyAddrList.setVisibility(View.GONE);
//								pickAddressInstance.pickAddressText.setVisibility(View.VISIBLE);
//								pickAddressInstance.pickAddrList.setVisibility(View.VISIBLE);
//								PickListModel aPickListModel = new PickListModel();
//								aPickListModel.LoadModel(addresses);
//								String[] ids = new String[PickListModel.pickAddressListRowItemArrList.size()];
//								for (int i= 0; i < ids.length; i++){
//									ids[i] = Integer.toString(i+1);
//								}
//								PickListRowItemAdapter aAdapter = new PickListRowItemAdapter(pickAddressContext, R.layout.pick_address_list_item, ids, -1);
//								pickAddressInstance.pickAddrList.setAdapter(aAdapter);
//								aAdapter.notifyDataSetChanged();
//								/* ArrayAdapter<PickAddressListModel> adapter = new PickAddressListAdapter(pickAddressInstance, addresses);
//								pickAddressInstance.pickAddrList.setAdapter(adapter); */
//								aAdapter.notifyDataSetChanged();
//								progressDialog.DismissDialog();
//							}    
//						});
					}
					else{
						System.out.println("inside else block for addresssearch");
						progressDialog.DismissDialog();
						mainInstance.runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(pickAddressContext, "Please enter a valid address!", Toast.LENGTH_LONG).show();
							}
						});
					}
				}
				catch(Exception e){
					System.out.println("inside catch block for addresssearch");
					System.out.println("Exception: " + e);
//					progressDialog.DismissDialog();
					mainInstance.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(pickAddressContext, "Please enter a valid address!", Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}
		return "OK";
	}

	protected void onProgressUpdate(Integer... progress) {

	}

	protected void onPostExecute(Long result) { 

	}

	private HashMap<String, String> putData(String address_name, String address_location) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put("address_name", address_name);
		item.put("address_location", address_location);
		return item;
	}
}