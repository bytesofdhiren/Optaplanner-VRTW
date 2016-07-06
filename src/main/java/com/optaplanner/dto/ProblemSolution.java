package com.optaplanner.dto;

import java.util.List;

public class ProblemSolution {
	private String key;
	private String status;
	private long hardScore;
	private long softScore;
	private List<Vehicle> vehicleList;
	
	public ProblemSolution(String key){
		this.key = key;
	}
	
	public ProblemSolution(String key, String status){
		this.key = key;
		this.status = status;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String id) {
		this.key = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getHardScore() {
		return hardScore;
	}
	public void setHardScore(long hardScore) {
		this.hardScore = hardScore;
	}
	public long getSoftScore() {
		return softScore;
	}
	public void setSoftScore(long softScore) {
		this.softScore = softScore;
	}
	public List<Vehicle> getVehicleList() {
		return vehicleList;
	}
	public void setVehicleList(List<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}
	

}
