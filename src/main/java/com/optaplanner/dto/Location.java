package com.optaplanner.dto;

public class Location {
	
	private int id;
	private String latitude;
	private String longitude;
	private String distanceMap;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getDistanceMap() {
		return distanceMap;
	}
	public void setDistanceMap(String distanceMap) {
		this.distanceMap = distanceMap;
	}

}
