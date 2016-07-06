package com.optaplanner.rest;

import java.util.concurrent.Callable;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.examples.vehiclerouting.domain.VehicleRoutingSolution;

public class ProblemSolver implements Callable {
	
	Solver<VehicleRoutingSolution> solver; 	
	VehicleRoutingSolution solution;
	
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
		solver.solve(solution);
		VehicleRoutingSolution bestHelloSolution = (VehicleRoutingSolution) solver.getBestSolution();
		return bestHelloSolution;
	}

}
