package com.ezgreen.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.models.Plant;
import com.ezgreen.models.PotSize;
import com.ezgreen.models.Sensor;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.responses.SensorsDetailResponse;
import com.ezgreen.service.SensorService;

@RestController
@RequestMapping("/api/sensor")
public class SensorController
{
	private SensorService sensorService;
	private SensorRepository sensorRepository;
	
	public SensorController(SensorRepository sensorRepository, SensorService sensorService)
	{
		this.sensorRepository = sensorRepository;
		this.sensorService = sensorService;
	}
	
	@GetMapping(value="/withplants", produces = "application/json")
	public ResponseEntity<?> getSensorsWithPlant() throws Throwable
	{
		SensorsDetailResponse response = new SensorsDetailResponse();
		
		try
		{
			//Kicks of multiple, asynchronous calls
			CompletableFuture<List<Plant>> plants = sensorService.fetchPlantSensors();
			CompletableFuture<List<Sensor>> sensors = sensorService.fetchAllSensors();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					plants,
					sensors
			).join();
			
			response.setPlants(plants.get());
			response.setSensors(sensors.get());
			
			response.setResponseMessage("Pulled all sensors with plants that have sensors.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
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
