package com.mobirizer.autotasker;

import java.util.ArrayList;
import java.util.Map;

public class PickListModel {

	public static ArrayList<PickAddressListRowItem> pickAddressListRowItemArrList;
	ArrayList<Map<String, String>> addresses;
	public void LoadModel(ArrayList<Map<String, String>> theAddresses) {
		String aText1 = "";
		String aText2 = "";
		addresses = theAddresses;
		pickAddressListRowItemArrList = new ArrayList<PickAddressListRowItem>();
		if(addresses!=null&&!addresses.isEmpty()){
			for(int i=0; i<addresses.size(); i++){
				aText1 = addresses.get(i).get("address_name");
				aText2 = addresses.get(i).get("address_location");
				pickAddressListRowItemArrList.add(new PickAddressListRowItem(i+1, aText1, aText2, "check_icon.png"));
			}
		}
	}

	public static PickAddressListRowItem GetById(int theId){

		for(PickAddressListRowItem aItem : pickAddressListRowItemArrList) {
			if (aItem.id == theId) {
				return aItem;
			}
		}
		return null;
	}

}

