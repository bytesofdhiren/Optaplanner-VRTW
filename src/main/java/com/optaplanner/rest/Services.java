package com.optaplanner.rest;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.optaplanner.dto.PlanningProblem;
import com.optaplanner.dto.ProblemSolution;

@RestController
public class Services {

	// Map holds the optaplanner objects between the requests
	private final HashMap<String, Object> map = new HashMap<>();

	@RequestMapping(value = "/startSolving", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProblemSolution> startSolving(@RequestBody PlanningProblem problem)
			throws InterruptedException, ExecutionException {
		Optaplanner planner = new Optaplanner();

		// This key is used as a reference to optaplanner process
		final String key = RandomStringUtils.randomAlphanumeric(30);
		map.put(key, planner);

		// Start solving the problem
		ProblemSolution problemSolution = planner.OptimizeData(problem, key);

		return new ResponseEntity<>(problemSolution, HttpStatus.OK);
	}

	@RequestMapping(value = "/getSolution/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProblemSolution> getSolution(@PathVariable(value = "key") String key) {
		if (map.containsKey(key)) {
			// Get the optaplanner object based on key & remove the object from
			// memory
			Optaplanner hw = (Optaplanner) map.get(key);
			
			//If process is going on or terminated by another call, it will not remove the key
			
			if (hw != null) {
				// Return the best solution
				ProblemSolution solution = hw.GetBestSolution(key);
				if (hw.status != "Processing" && hw.status != "Cacelled") {
					map.remove(key);
				}
				return new ResponseEntity<>(solution, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
	}

	@RequestMapping(value = "/terminateSolver/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProblemSolution> terminateSolver(@PathVariable(value = "key") String key) {
		if (map.containsKey(key)) {
			Optaplanner hw = (Optaplanner) map.get(key);
			map.remove(key);
			if (hw != null) {

				return new ResponseEntity<>(hw.terminateSolver(key), HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
	}

}
