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

import com.ezgreen.models.Environment;
import com.ezgreen.models.Plant;
import com.ezgreen.models.Sensor;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.ezgreen.responses.MultipleDetailResponse;
import com.ezgreen.responses.SingleDetailResponse;
import com.ezgreen.service.EnvironmentService;
import com.ezgreen.service.PlantService;
import com.ezgreen.service.SensorService;

import jakarta.servlet.http.HttpServletResponse;

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
	
	@PutMapping("/")
	public ResponseEntity<?> createSensor(@RequestBody String request)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());

		try
		{
			response = sensorService.saveAndEditSensor(request, (long) 0);
		}
		catch (IOException e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editSensor(@RequestBody String request, @PathVariable(value = "id") Long sensorId)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
		
		try
		{
			response = sensorService.saveAndEditSensor(request, (long) sensorId);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}

		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@PostMapping(value="/calibration", produces = "application/json")
	public void getSensorCalibration(HttpServletResponse response, @RequestBody String request)
	{
		boolean done = false;
		
		try
		{
			sensorService.getCalibration(response, request);
			
			do
			{
				done = response.isCommitted();
			}while(!done);
		}
		catch (Exception e)
		{
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			
			try
			{
				response.getWriter().println("getSensorCalibration error occur: " + e.getCause());
			}
			catch(Exception error)
			{
				System.out.println("Error sending error to http response: " + error.getCause());
			}
		}		
	}
	
	@GetMapping(value="/withalldetails", produces = "application/json")
	public ResponseEntity<?> getSensorsWithDetails() throws Throwable
	{
		MultipleDetailResponse response = new MultipleDetailResponse();
		
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
	
	@GetMapping(value="/{id}", produces = "application/json")
	public ResponseEntity<?> getSensor(@PathVariable(value = "id") Long sensorId) throws Throwable
	{
		SingleDetailResponse response = new SingleDetailResponse();
		
		try
		{
			CompletableFuture<Environment> environment = environmentService.fetchEnvironmentBySensor(sensorId);
			CompletableFuture<Plant> plant = plantService.fetchPlantBySensor(sensorId);
			CompletableFuture<Sensor> sensor = sensorService.fetchSensorWithId(sensorId);
			
			CompletableFuture.allOf(
					plant,
					sensor,
					environment
			).join();
			
			response.setEnvironment(environment.get());
			response.setPlant(plant.get());
			response.setSensor(sensor.get());
			
			response.setResponseMessage("Pulled sensor with details.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			response.setResponseMessage("getSensor error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<?> getSensors() throws Throwable
	{
		MultipleDetailResponse response = new MultipleDetailResponse();
		
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
