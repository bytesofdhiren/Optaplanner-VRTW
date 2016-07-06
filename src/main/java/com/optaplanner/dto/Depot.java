package com.optaplanner.dto;

public class Depot {
	private int id;
	private int locationId;
	private long readyTime;
	private long dueTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public long getReadyTime() {
		return readyTime;
	}
	public void setReadyTime(long readyTime) {
		this.readyTime = readyTime;
	}
	public long getDueTime() {
		return dueTime;
	}
	public void setDueTime(long dueTime) {
		this.dueTime = dueTime;
	}
	
}
