package com.optaplanner.dto;

public class Vehicle {
	private int id;
	private int depotId;
	private int capacity;
	private long readyTime;
	private long dueTime;
	private String visitSequence;

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

	public String getVisitSequence() {
		return visitSequence;
	}

	public void setVisitSequence(String visitSequence) {
		this.visitSequence = visitSequence;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDepotId() {
		return depotId;
	}

	public void setDepotId(int depotId) {
		this.depotId = depotId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}
