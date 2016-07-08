package com.optaplanner.rest;

import java.util.concurrent.Callable;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;
import org.optaplanner.core.api.domain.solution.Solution;

public class ProblemSolver implements Callable {
	
	Solver<VehicleRoutingSolution> solver; 	
	VehicleRoutingSolution solution;
	
	private VehicleRoutingSolution bestHelloSolution;	
	
	public VehicleRoutingSolution getBestHelloSolution() {
		return bestHelloSolution;
	}

	public void setBestHelloSolution(VehicleRoutingSolution bestHelloSolution) {
		this.bestHelloSolution = bestHelloSolution;
	}

	public ProblemSolver(VehicleRoutingSolution solution){
		this.solution = solution;
	}
	
	public boolean cancel() {
        if (solver != null && !solver.isTerminateEarly() && solver.isSolving()) {
            return solver.terminateEarly();
        }
        return false;
    }
	
	@Override
	public Object call() throws Exception { 
		SolverFactory<VehicleRoutingSolution> solverFactory = SolverFactory.createFromXmlResource("org/optaplanner/examples/vehiclerouting/solver/vehicleRoutingSolverConfig.xml");
		solver = solverFactory.buildSolver();
		RegisterForIntermediateSolution();
		solver.solve(solution);
		bestHelloSolution = (VehicleRoutingSolution) solver.getBestSolution();		
		return bestHelloSolution;
	}
	
	private void RegisterForIntermediateSolution(){		
		solver.addEventListener(new SolverEventListener<VehicleRoutingSolution>() {

			@Override
			public void bestSolutionChanged(BestSolutionChangedEvent<VehicleRoutingSolution> event) {
				if (solver.isEveryProblemFactChangeProcessed()) {
                    // final is also needed for thread visibility
					bestHelloSolution = event.getNewBestSolution();
                    // Migrate it to the event thread                    
                }				
			}
			
		});		
	}

}
