package com.ezgreen.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.models.Environment;
import com.ezgreen.models.Sensor;
import com.ezgreen.models.SensorType;
import com.ezgreen.repository.EnvironmentRepository;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.repository.SensorTypeRepository;
import com.ezgreen.responses.MultipleDetailResponse;
import com.ezgreen.responses.SingleDetailResponse;
import com.ezgreen.service.EnvironmentService;
import com.ezgreen.service.SensorService;
import com.ezgreen.service.SensorTypeService;

@RestController
@RequestMapping("/api/environment")
public class EnvironmentController
{
	private EnvironmentRepository environmentRepository;
	private EnvironmentService environmentService;
	private SensorService sensorService;
	private SensorRepository sensorRepository;
	private SensorTypeRepository sensorTypeRepository;
	private SensorTypeService sensorTypeService;
	
	public EnvironmentController(EnvironmentRepository environmentRepository, EnvironmentService environmentService,
			SensorService sensorService, SensorRepository sensorRepository, SensorTypeRepository sensorTypeRepository,
			SensorTypeService sensorTypeService)
	{
		this.environmentRepository = environmentRepository;
		this.environmentService = environmentService;
		this.sensorService = sensorService;
		this.sensorRepository = sensorRepository;
		this.sensorTypeRepository = sensorTypeRepository;
		this.sensorTypeService = sensorTypeService;
	}
	
	@GetMapping(value="/{id}", produces = "application/json")
	public ResponseEntity<?> getPlantDetailById(@PathVariable(value = "id") Long environmentId)
	{
		SingleDetailResponse response = new SingleDetailResponse();

		try
		{
			Environment environment = environmentRepository.fetchById(environmentId);
			List<Sensor> sensors  = sensorRepository.fetchSensorsWithEnvironmentId(environmentId);
			List<SensorType> sensorTypes = sensorTypeRepository.findAll();
			
			if(environment != null) response.setEnvironment(environment);
			response.setSensors(sensors);
			response.setSensorTypes(sensorTypes);
			
			response.setStatusCode(HttpStatus.OK);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			response.setResponseMessage("Failed pulling environment with detail with id: " + environmentId + "; " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/alldetails", produces = "application/json")
	public ResponseEntity<?> getEnvironmentFullDetails() throws Throwable
	{
		MultipleDetailResponse response = new MultipleDetailResponse();
		
		try
		{
			//Kicks of multiple, asynchronous calls
			CompletableFuture<List<Environment>> environments = environmentService.fetchAllEnvironments();
			CompletableFuture<List<Sensor>> sensors = sensorService.fetchAllEnvironmentSensors();
			CompletableFuture<List<SensorType>> sensorTypes = sensorTypeService.fetchSensorTypes();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					environments,
					sensors,
					sensorTypes
			).join();
			
			response.setEnvironments(environments.get());
			response.setSensors(sensors.get());
			response.setSensorTypes(sensorTypes.get());
			
			response.setResponseMessage("Pulled all environment with details.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("getEnvironmentFullDetails error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<?> getEnvironments() throws Throwable
	{
		MultipleDetailResponse response = new MultipleDetailResponse();
		
		try
		{
			response.setEnvironments(environmentRepository.fetchAllEnvironments());

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
