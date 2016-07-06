package com.optaplanner.rest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.optaplanner.examples.vehiclerouting.domain.Customer;
import org.optaplanner.examples.vehiclerouting.domain.Depot;
import org.optaplanner.examples.vehiclerouting.domain.Vehicle;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.examples.vehiclerouting.domain.location.AirLocation;
import org.optaplanner.examples.vehiclerouting.domain.location.Location;
import org.optaplanner.examples.vehiclerouting.domain.location.RoadLocation;
import org.optaplanner.examples.vehiclerouting.domain.timewindowed.TimeWindowedCustomer;
import org.optaplanner.examples.vehiclerouting.domain.timewindowed.TimeWindowedDepot;

import com.optaplanner.dto.PlanningProblem;
import com.optaplanner.dto.ProblemSolution;

public class Optaplanner {	
	VehicleRoutingSolution bestSolution;
	ProblemSolver solver;
	Future<VehicleRoutingSolution> future;
	String status;
	
	
	//Start solving the problem in another thread & return response as "Processing"
	public ProblemSolution OptimizeData(PlanningProblem problem, String key) throws InterruptedException, ExecutionException {
		VehicleRoutingSolution solution = GenerateSolution(problem);		
		//Start a thread
		new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                	StartSolving(solution);                    
                } catch (Exception ex) {
                }
            }
        }).start();
		status = "Processing";
		ProblemSolution problemSolution = new ProblemSolution(key, status);			
		return problemSolution;		
	}
	
	//Returns the best solution matching the response format
	public ProblemSolution GetBestSolution(String key){
		ProblemSolution problemSolution;
		
		//If process is going on or terminated by another call, it will not return best solution but will return processing status
		if(status != "Processing" && status != "Cacelled"){
			problemSolution = GenerateBestSolution(bestSolution, key);
		}
		else{
			problemSolution = new ProblemSolution(key, status);
		}
		return problemSolution;
	}
	
	//Terminate solution
	public ProblemSolution terminateSolver(String key){		
		if (future != null && !future.isDone()) {
            future.cancel(true);
            if (future.isCancelled()) {
                solver.cancel();                
                status = "Cancelled";
            }            
        } 				
		return new ProblemSolution(key, status);
	}
		
	private void StartSolving(VehicleRoutingSolution solution) throws InterruptedException, ExecutionException{
		solver = new ProblemSolver(solution);
		ExecutorService pool = Executors.newFixedThreadPool(1);

		//This will call method "Call"
		future = pool.submit(solver);
		
		//This will get response from Call method
		bestSolution = future.get();	
		
		status = "Completed";
	}
	
	//Generate best solution from VehicleRoutingSolution. This method returns the best solutions as per the response expected
	private ProblemSolution GenerateBestSolution(VehicleRoutingSolution bestHelloSolution, String key){
		ProblemSolution problemSolution = new ProblemSolution(key, status);
		problemSolution.setHardScore(bestHelloSolution.getScore().getHardScore());
		problemSolution.setSoftScore(bestHelloSolution.getScore().getSoftScore());
		
		List<com.optaplanner.dto.Vehicle> outVehicleList = new ArrayList<com.optaplanner.dto.Vehicle>(bestHelloSolution.getVehicleList().size());
				
		for(int counter = 0; counter < bestHelloSolution.getVehicleList().size(); counter ++){
			StringBuilder visitSequence = new StringBuilder();

			//Optaplanner Solution Vehicle
			Vehicle vehicle = bestHelloSolution.getVehicleList().get(counter);
			
			//Our POJO vehicle which will returned to client 
			com.optaplanner.dto.Vehicle outVehicle = new com.optaplanner.dto.Vehicle();
			outVehicle.setId(vehicle.getId().intValue());					
			
			visitSequence.append(vehicle.getDepot().getId());
			
			Customer nextCustomer = vehicle.getNextCustomer();
			while(nextCustomer != null){
				visitSequence.append("," + nextCustomer.getId());
				nextCustomer = nextCustomer.getNextCustomer();
			}	
			
			outVehicle.setVisitSequence(visitSequence.toString());
			outVehicleList.add(outVehicle);
		}
		
		problemSolution.setVehicleList(outVehicleList);
		return problemSolution;
	}

	//This method is used to convert PlanningProblem (Request object) into Vehicle Solution which will be used by Optaplanner methods
	private VehicleRoutingSolution GenerateSolution(PlanningProblem problem) {
		VehicleRoutingSolution solution = new VehicleRoutingSolution();
		solution.setLocationList(GetLocations(problem));
		solution.setCustomerList(GetCustomers(problem,solution.getLocationList()));
		solution.setDepotList(GetDepots(problem,solution.getLocationList()));
		solution.setVehicleList(GetVehicles(problem, solution.getDepotList()));
		return solution;
	}
	
	private List<Vehicle> GetVehicles(PlanningProblem problem, List<Depot> depotList){
		int vehicleCount = problem.getVehicleList().size();
		List<Vehicle> vehicleList = new ArrayList<Vehicle>(vehicleCount);
		for (int i = 0; i < vehicleCount; i++) {
			com.optaplanner.dto.Vehicle inputVehicle = problem.getVehicleList().get(i);
			Vehicle vehicle = new Vehicle();
			vehicle.setId(Long.valueOf(inputVehicle.getId()));
			vehicle.setCapacity(inputVehicle.getCapacity());
			vehicle.setReadyTime(inputVehicle.getReadyTime());
			vehicle.setDueTime(inputVehicle.getDueTime());
			int depotId = inputVehicle.getDepotId();
			for(int depotCounter = 0; depotCounter < depotList.size(); depotCounter++){
				if(depotList.get(depotCounter).getId() == depotId){
					vehicle.setDepot(depotList.get(depotCounter));
					break;
				}
			}
			vehicleList.add(vehicle);
		}
		return vehicleList;
	}
	
	private List<Depot> GetDepots(PlanningProblem problem, List<Location> locationList){
		int depotCount = problem.getDepotList().size();
		List<Depot> depotList = new ArrayList<Depot>(depotCount);
		for (int i = 0; i < depotCount; i++) {
			com.optaplanner.dto.Depot inputdepot = problem.getDepotList().get(i);
			Depot depot = problem.getCustomerType().equals("timewindowed") ? new TimeWindowedDepot() : new Depot();
			depot.setId(Long.valueOf(inputdepot.getId()));
			int depotLocationId = ((com.optaplanner.dto.Depot)problem.getDepotList().get(i)).getLocationId();
			if(problem.getCustomerType().equals("timewindowed")){
				TimeWindowedDepot timeWindowedDepot = (TimeWindowedDepot) depot;
				timeWindowedDepot.setReadyTime(((com.optaplanner.dto.Depot)problem.getDepotList().get(i)).getReadyTime());
				timeWindowedDepot.setDueTime(((com.optaplanner.dto.Depot)problem.getDepotList().get(i)).getDueTime());                
			}
			
			for(int locationCounter = 0; locationCounter < locationList.size(); locationCounter++){
				if(locationList.get(locationCounter).getId() == depotLocationId){
					depot.setLocation(locationList.get(locationCounter));
					break;
				}
			}
			depotList.add(depot);
		}
		return depotList;
	}
	
	private List<Customer> GetCustomers(PlanningProblem problem, List<Location> locationList){
		int customerCount = problem.getCustomerList().size();
		List<Customer> customerList = new ArrayList<Customer>(customerCount);
		for (int i = 0; i < customerCount; i++) {
			Customer customer = problem.getCustomerType().equals("timewindowed") ? new TimeWindowedCustomer() : new Customer();
			int customerLocationId = ((com.optaplanner.dto.Customer)problem.getCustomerList().get(i)).getLocationId();
			customer.setId(Long.valueOf(((com.optaplanner.dto.Customer)problem.getCustomerList().get(i)).getId()));			
			for(int locationCounter = 0; locationCounter < locationList.size(); locationCounter++){
				if(locationList.get(locationCounter).getId() == customerLocationId){
					customer.setLocation(locationList.get(locationCounter));
					break;
				}
			}
			customer.setDemand(((com.optaplanner.dto.Customer)problem.getCustomerList().get(i)).getDemant());
			if(problem.getCustomerType().equals("timewindowed")){
				TimeWindowedCustomer timeWindowedCustomer = (TimeWindowedCustomer) customer;
                timeWindowedCustomer.setReadyTime(((com.optaplanner.dto.Customer)problem.getCustomerList().get(i)).getReadyTime());
                timeWindowedCustomer.setDueTime(((com.optaplanner.dto.Customer)problem.getCustomerList().get(i)).getDueTime());
                timeWindowedCustomer.setServiceDuration(((com.optaplanner.dto.Customer)problem.getCustomerList().get(i)).getServiceDuration());
			}
			customerList.add(customer);
		}
		return customerList;
	}

	private List<Location> GetLocations(PlanningProblem problem) {
		int locationCount = problem.getLocationList().size();
		List<Location> locationList = new ArrayList<Location>(locationCount);		
		for (int counter = 0; counter < locationCount; counter++) {
			Location location;
			switch (problem.getDistanceType()) {
			case "AIR_LOCATION":
				location = new AirLocation();
				break;
			case "ROAD_DISTANCE":
				location = new RoadLocation();
				break;
			default:
				throw new IllegalStateException(
						"The distanceType (" + problem.getDistanceType() + ") is not implemented.");
			}
			location.setId(
					Long.valueOf(((com.optaplanner.dto.Location) problem.getLocationList().get(counter)).getId()));
			location.setLatitude(Double.parseDouble(
					((com.optaplanner.dto.Location) problem.getLocationList().get(counter)).getLatitude()));
			location.setLongitude(Double.parseDouble(
					((com.optaplanner.dto.Location) problem.getLocationList().get(counter)).getLongitude()));
			locationList.add(location);
		}
		if (problem.getDistanceType().equals("ROAD_DISTANCE")) {
			for (int i = 0; i < locationCount; i++) {
				RoadLocation location = (RoadLocation) locationList.get(i);
				Map<RoadLocation, Double> travelDistanceMap = new LinkedHashMap <RoadLocation, Double>(locationCount);
				String[] lineTokens = (((com.optaplanner.dto.Location) problem.getLocationList().get(i)).getDistanceMap()).split(",", -1);
				for (int j = 0; j < locationCount; j++) {
                    double travelDistance = Double.parseDouble(lineTokens[j]);
                    if (i == j) {
                        if (travelDistance != 0.0) {
                            throw new IllegalStateException("The travelDistance (" + travelDistance
                                    + ") should be zero.");
                        }
                    } else {
                        RoadLocation otherLocation = (RoadLocation) locationList.get(j);
                        travelDistanceMap.put(otherLocation, travelDistance);
                    }
                }
                location.setTravelDistanceMap(travelDistanceMap);
			}
		}
		return locationList;
	}
}
