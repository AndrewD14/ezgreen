package com.ezgreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.repository.PlantRepository;
import com.ezgreen.responses.PlantResponse;

@RestController
@RequestMapping("/api/plant")
public class PlantController
{
	
	private PlantRepository plantRepository;
	
	@Autowired
	public PlantController(PlantRepository plantRepository)
	{
		this.plantRepository = plantRepository;
	}
	
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<?> getConfigFields() throws Throwable
	{
		PlantResponse response = new PlantResponse();
		
		try
		{
			response.setPlants(plantRepository.fetchAllPlants());

			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage("Successfully pulled all configs.");
		}
		catch (Exception e)
		{
			response.setResponseMessage("getAllPlants error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
}
