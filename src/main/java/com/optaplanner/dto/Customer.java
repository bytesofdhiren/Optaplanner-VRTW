package com.optaplanner.dto;

public class Customer {
	private int id;
	private int locationId;
	private int demand;
	private long readyTime;
    private long dueTime;
    private long serviceDuration;
	
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
	public int getDemand() {
		return demand;
	}
	public void setDemand(int demand) {
		this.demand = demand;
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
	public long getServiceDuration() {
		return serviceDuration;
	}
	public void setServiceDuration(long serviceDuration) {
		this.serviceDuration = serviceDuration;
	}
}
