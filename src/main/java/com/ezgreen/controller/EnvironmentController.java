package com.ezgreen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.repository.EnvironmentRepository;
import com.ezgreen.responses.EnvironmentResponse;

@RestController
@RequestMapping("/api/environment")
public class EnvironmentController
{
	private EnvironmentRepository environmentRepository;
	
	public EnvironmentController(EnvironmentRepository environmentRepository)
	{
		this.environmentRepository = environmentRepository;
	}
	
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<?> getEnvironments() throws Throwable
	{
		EnvironmentResponse response = new EnvironmentResponse();
		
		try
		{
			response.setEnvironments(environmentRepository.fetchAllEnvironment());

			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage("Successfully pulled all environment sensors.");
		}
		catch (Exception e)
		{
			response.setResponseMessage("getAllEnvironments error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
}
