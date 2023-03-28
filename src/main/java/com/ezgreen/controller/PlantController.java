package com.ezgreen.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.models.Plant;
import com.ezgreen.models.PotSize;
import com.ezgreen.models.Sensor;
import com.ezgreen.repository.PlantRepository;
import com.ezgreen.repository.PotSizeRepository;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.ezgreen.responses.PlantDetailResponse;
import com.ezgreen.responses.PlantsDetailResponse;
import com.ezgreen.responses.PlantResponse;
import com.ezgreen.service.PlantService;
import com.ezgreen.service.PotSizeService;
import com.ezgreen.service.SensorService;

@RestController
@RequestMapping("/api/plant")
public class PlantController
{
	private PlantRepository plantRepository;
	private PlantService plantService;
	private PotSizeService potSizeService;
	private PotSizeRepository potSizeRepository;
	private SensorRepository sensorRepository;
	private SensorService sensorService;
	
	public PlantController(PlantRepository plantRepository, PlantService plantService,
			PotSizeService potSizeService, PotSizeRepository potSizeRepository,
			SensorRepository sensorRepository, SensorService sensorService)
	{
		this.plantRepository = plantRepository;
		this.plantService = plantService;
		this.potSizeService = potSizeService;
		this.potSizeRepository = potSizeRepository;
		this.sensorRepository = sensorRepository;
		this.sensorService = sensorService;
	}
	
	@PutMapping("/")
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
	public ResponseEntity<?> editPlant(@RequestBody String request, @PathVariable(value = "id") Long plantId)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
		
		try
		{
			response = plantService.saveAndEditPlant(request, plantId);
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
	public ResponseEntity<?> getPlantDetailById(@PathVariable(value = "id") Long plantId)
	{
		PlantDetailResponse response = new PlantDetailResponse();

		try
		{
			Plant plant = plantRepository.fetchPlantById(plantId);
			Sensor sensor  = sensorRepository.fetchSensorWithPlantId(plantId);
			PotSize potSize = potSizeRepository.fetchPotSizeWithPlantId(plantId);

			if(plant != null) response.setPlant(plant);
			if(sensor != null) response.setSensor(sensor);
			if(potSize != null) response.setPotSize(potSize);
			
			response.setStatusCode(HttpStatus.OK);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			response.setResponseMessage("Failed pulling plant with detail with id: " + plantId + "; " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/alldetails", produces = "application/json")
	public ResponseEntity<?> getPlantFullDetails() throws Throwable
	{
		PlantsDetailResponse response = new PlantsDetailResponse();
		
		try
		{
			//Kicks of multiple, asynchronous calls
			CompletableFuture<List<Plant>> plants = plantService.fetchNonDeletedPlants();
			CompletableFuture<List<Sensor>> sensors = sensorService.fetchPlantSensors();
			CompletableFuture<List<PotSize>> potSizes = potSizeService.fetchPotSizes();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					plants,
					sensors,
					potSizes
			).join();
			
			response.setPlants(plants.get());
			response.setSensors(sensors.get());
			response.setPotSizes(potSizes.get());
			
			response.setResponseMessage("Pulled all plants with details.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("getAllPlants error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/configoptions", produces = "application/json")
	public ResponseEntity<?> getConfigOptions() throws Throwable
	{
		PlantsDetailResponse response = new PlantsDetailResponse();
		
		try
		{
			//Kicks of multiple, asynchronous calls
			CompletableFuture<List<Sensor>> sensors = sensorService.fetchAvailablePlantSensors();
			CompletableFuture<List<PotSize>> potSizes = potSizeService.fetchPotSizes();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					sensors,
					potSizes
			).join();
			
			response.setSensors(sensors.get());
			response.setPotSizes(potSizes.get());
			
			response.setResponseMessage("Pulled all plant config options.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
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
