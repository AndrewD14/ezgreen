package com.ezgreen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.models.PlantFullDetail;
import com.ezgreen.repository.PlantJoinRepository;
import com.ezgreen.repository.PlantRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.ezgreen.responses.PlantDetailResponse;
import com.ezgreen.responses.PlantResponse;

@RestController
@RequestMapping("/api/plant")
public class PlantController
{
	private PlantRepository plantRepository;
	private PlantJoinRepository plantJoinRepository;
	
	public PlantController(PlantRepository plantRepository, PlantJoinRepository plantJoinRepository)
	{
		this.plantRepository = plantRepository;
		this.plantJoinRepository = plantJoinRepository;
	}
	
	@GetMapping(value="/{id}", produces = "application/json")
	public ResponseEntity<?> getPlantFullById(@PathVariable(value = "id") Long plantId)
	{
		EZGreenResponse response = new EZGreenResponse();

		try
		{
			PlantFullDetail plant = plantJoinRepository.fetchPlantFullById(plantId);

			if(plant != null) response.setResponseMessage(plant.toString());
			else response.setResponseMessage("{}");
			
			response.setStatusCode(HttpStatus.OK);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			response.setResponseMessage("Failed pulling plant with detail with id: " + plantId + "; " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@GetMapping(value="/alldetails", produces = "application/json")
	public ResponseEntity<?> getPlantFullDetails() throws Throwable
	{
		PlantDetailResponse response = new PlantDetailResponse();
		
		try
		{
			response.setPlants(plantJoinRepository.fetchAllPlantFullDetails());
			
			response.setResponseMessage("Pulled all plants with details.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			response.setResponseMessage("getAllPlants error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<?> getPlants() throws Throwable
	{
		PlantResponse response = new PlantResponse();
		
		try
		{
			response.setPlants(plantRepository.fetchAllPlants());

			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage("Successfully pulled all plants.");
		}
		catch (Exception e)
		{
			response.setResponseMessage("getAllPlants error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
}
