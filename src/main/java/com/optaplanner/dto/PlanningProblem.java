package com.optaplanner.dto;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class PlanningProblem implements Serializable{	
	
	private int id;
	private String distanceType;
	private String customerType;
	private List<Location> locationList;
	private List<Depot> depotList;
	private List<Vehicle> vehicleList;
	private List<Customer> customerList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDistanceType() {
		return distanceType;
	}
	public void setDistanceType(String distanceType) {
		this.distanceType = distanceType;
	}
	public List<Location> getLocationList() {
		return locationList;
	}
	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}
	public List<Depot> getDepotList() {
		return depotList;
	}
	public void setDepotList(List<Depot> depotList) {
		this.depotList = depotList;
	}
	public List<Customer> getCustomerList() {
		return customerList;
	}
	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}
	public List<Vehicle> getVehicleList() {
		return vehicleList;
	}
	public void setVehicleList(List<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	
	
}