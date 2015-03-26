package com.mobirizer.utility;

public class UtilityHandler {

	public static double distFrom(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 6371;// 3958.75 miles;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double lat1R = Math.toRadians(lat1);
		double lat2R = Math.toRadians(lat2);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLng / 2)
				* Math.sin(dLng / 2) * Math.cos(lat1R) * Math.cos(lat2R);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		double meterConversion = 1000.0;
		double result = dist * meterConversion;

		return result;
	}
}
