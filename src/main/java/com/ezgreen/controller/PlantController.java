package com.ezgreen.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.models.PlantFullDetail;
import com.ezgreen.repository.PlantJoinRepository;
import com.ezgreen.repository.PlantRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.ezgreen.responses.PlantDetailResponse;
import com.ezgreen.responses.PlantResponse;
import com.ezgreen.service.PlantService;

@RestController
@RequestMapping("/api/plant")
public class PlantController
{
	private PlantRepository plantRepository;
	private PlantJoinRepository plantJoinRepository;
	private PlantService plantService;
	
	public PlantController(PlantRepository plantRepository, PlantJoinRepository plantJoinRepository, PlantService plantService)
	{
		this.plantRepository = plantRepository;
		this.plantJoinRepository = plantJoinRepository;
		this.plantService = plantService;
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createPlant(@RequestBody String request)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());

		try
		{
			response = plantService.saveAndEditPlant(request, (long) 0);
		}
		catch (IOException e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editPlant(@RequestBody String request, @PathVariable(value = "id") Long ruleId)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
		
		try
		{
			response = plantService.saveAndEditPlant(request, ruleId);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}

		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@PutMapping("/activate/{id}")
	public ResponseEntity<?> activatePlant(@RequestBody String request, @PathVariable(value = "id") Long plantId)
	{
		EZGreenResponse response = new EZGreenResponse();

		try
		{
			response = plantService.activate(request, plantId);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@PutMapping("/deactivate/{id}")
	public ResponseEntity<?> deactivatePlant(@RequestBody String request, @PathVariable(value = "id") Long plantId)
	{
		EZGreenResponse response = new EZGreenResponse();

		try
		{
			response = plantService.deactivate(request, plantId);
		}
		catch (Exception e)
		{
			System.out.println("error: " + e.getMessage());
			System.out.println("error: " + e.getCause());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@PutMapping("/delete/{id}")
	public ResponseEntity<?> deletePlant(@RequestBody String request, @PathVariable(value = "id") Long plantId)
	{
		EZGreenResponse response = new EZGreenResponse();

		try
		{
			response = plantService.delete(request, plantId);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
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
