package com.ezgreen.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.models.Environment;
import com.ezgreen.models.Plant;
import com.ezgreen.models.PotSize;
import com.ezgreen.models.Sensor;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.responses.SensorsDetailResponse;
import com.ezgreen.service.EnvironmentService;
import com.ezgreen.service.PlantService;
import com.ezgreen.service.SensorService;

@RestController
@RequestMapping("/api/sensor")
public class SensorController
{
	private PlantService plantService;
	private SensorService sensorService;
	private SensorRepository sensorRepository;
	private EnvironmentService environmentService;
	
	public SensorController(PlantService plantService, SensorRepository sensorRepository, SensorService sensorService, EnvironmentService environmentService)
	{
		this.plantService = plantService;
		this.sensorRepository = sensorRepository;
		this.sensorService = sensorService;
		this.environmentService = environmentService;
	}
	
	@GetMapping(value="/withalldetails", produces = "application/json")
	public ResponseEntity<?> getSensorsWithPlant() throws Throwable
	{
		SensorsDetailResponse response = new SensorsDetailResponse();
		
		try
		{
			//Kicks of multiple, asynchronous calls
			CompletableFuture<List<Plant>> plants = plantService.fetchPlantsWithSensor();
			CompletableFuture<List<Sensor>> sensors = sensorService.fetchAllSensors();
			CompletableFuture<List<Environment>> environments = environmentService.fetchAllEnvironmentWithSensors();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					plants,
					sensors,
					environments
			).join();
			
			response.setPlants(plants.get());
			response.setSensors(sensors.get());
			response.setEnvironments(environments.get());
			
			response.setResponseMessage("Pulled all sensors with details that have sensors.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			response.setResponseMessage("getAllSensors error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<?> getSensors() throws Throwable
	{
		SensorsDetailResponse response = new SensorsDetailResponse();
		
		try
		{
			response.setSensors(sensorRepository.fetchAllSensors());

			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage("Successfully pulled all sensors.");
		}
		catch (Exception e)
		{
			response.setResponseMessage("getAllSensors error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
}
