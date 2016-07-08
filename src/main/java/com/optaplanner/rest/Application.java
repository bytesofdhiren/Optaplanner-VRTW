package com.optaplanner.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.optaplanner.dto.Configuration;


@SpringBootApplication
public class Application {
	
    public static void main(String[] args) {
    	Configuration.terminationSecondsSpentLimit = Long.valueOf(args[0]);
        SpringApplication.run(Application.class, args);                
    }      
}
