package com.mobirizer.locoreminder;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class PlacesApi {
	private static String key = "AIzaSyDZcEHHESH5zg7NfKvdp-w5OAE_KUpyPNg";
	public static ArrayList<String> GetAddresses(String theAddress) {
		String new_addr_param = theAddress.replaceAll(" ", "%20");		
		String aPlacesApiUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+ new_addr_param + "&sensor=true&key="+ key;		
		JSONParser aJParser = new JSONParser();
        JSONObject aJObject = null;
        String aJStatus = "";
        JSONArray aJResults = null;
        ArrayList<String> aJAddresses = new ArrayList<String>();
 
        try {
        	aJObject = aJParser.getJSONFromUrl(aPlacesApiUrl);
        	System.out.println("ajobject >>>>>"+aJObject);
	        if(aJObject!=null){
	        	System.out.println("aJObject.length: " + aJObject.length());
	        	
	        	
	        	//check here for status return by json------access denied
	        	aJStatus = aJObject.getString("status");
	        	
	        	System.out.println("aJStatus: " + aJStatus);
	        	if(aJStatus!=null&&aJStatus.length()!=0&&aJStatus.equals("OK")){
	        		
	        		System.out.println("in status=ok if");
	        		aJResults = aJObject.getJSONArray("results");
		        	System.out.println("aJResults.length(): " + aJResults.length());
		        	int aJResultsLength = aJResults.length();
		        	if(aJResultsLength>5){
		        		aJResultsLength = 5;
		        	}
		        	for(int i=0; i<aJResultsLength; i++){
		        		JSONObject aJFormattedAddressObject = aJResults.getJSONObject(i);
		        		String aJFormattedAddress = aJFormattedAddressObject.getString("formatted_address");
		        		String aJName = aJFormattedAddressObject.getString("name");
		        		JSONObject aJGeometry = aJFormattedAddressObject.getJSONObject("geometry");
		        		JSONObject aJLocation = aJGeometry.getJSONObject("location");
		        		String aJLat = aJLocation.getString("lat");
		        		String aJLng = aJLocation.getString("lng");
		        		String aJAddress = aJName + "!" + aJFormattedAddress + ";" + aJLat + "," + aJLng;
		        		aJAddresses.add(aJAddress);
		        	}
	        	}
	        	else{
	        		aJAddresses = null;	        		
	        	}
	        }
	        else{
	        	aJAddresses = null;
        	}
        }catch(JSONException e){
        	System.out.println("JSONException in DistTimeDGoogleAPI: " + e);
        	aJAddresses = null;
        }
        
        return aJAddresses;
		
	}
}