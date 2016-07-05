package com.optaplanner.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.optaplanner.dto.Employee;
import com.optaplanner.dto.Greeting;
import com.optaplanner.dto.PlanningProblem;
import com.optaplanner.dto.ProblemSolution;

@RestController
public class Services {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    Map<Integer, Employee> empData = new HashMap<Integer, Employee>();
    
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping(value = "/startSolving", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProblemSolution> startSolving(@RequestBody PlanningProblem problem) {		
		ProblemSolution solution = new ProblemSolution();
		solution.setId(10);
		solution.setStatus("Processing");
		return new ResponseEntity<>(solution, HttpStatus.OK);
	}
}
