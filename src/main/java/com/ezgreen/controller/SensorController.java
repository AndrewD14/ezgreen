package com.ezgreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.repository.SensorRepository;
import com.ezgreen.responses.SensorResponse;

@RestController
@RequestMapping("/api/sensor")
public class SensorController
{
	private SensorRepository sensorRepository;
	
	@Autowired
	public SensorController(SensorRepository sensorRepository)
	{
		this.sensorRepository = sensorRepository;
	}
	
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<?> getConfigFields() throws Throwable
	{
		SensorResponse response = new SensorResponse();
		
		try
		{
			response.setSensors(sensorRepository.fetchAllSensors());

			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage("Successfully pulled all configs.");
		}
		catch (Exception e)
		{
			response.setResponseMessage("getAllSensors error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
}
