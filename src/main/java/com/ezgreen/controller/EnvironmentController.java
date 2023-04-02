package com.ezgreen.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.models.Environment;
import com.ezgreen.models.Relay;
import com.ezgreen.models.RelayType;
import com.ezgreen.models.Sensor;
import com.ezgreen.models.SensorType;
import com.ezgreen.repository.EnvironmentRepository;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.repository.SensorTypeRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.ezgreen.responses.MultipleDetailResponse;
import com.ezgreen.responses.SingleDetailResponse;
import com.ezgreen.service.EnvironmentService;
import com.ezgreen.service.RelayService;
import com.ezgreen.service.RelayTypeService;
import com.ezgreen.service.SensorService;
import com.ezgreen.service.SensorTypeService;

@RestController
@RequestMapping("/api/environment")
public class EnvironmentController
{
	private EnvironmentRepository environmentRepository;
	private EnvironmentService environmentService;
	private RelayService relayService;
	private RelayTypeService relayTypeService;
	private SensorService sensorService;
	private SensorRepository sensorRepository;
	private SensorTypeRepository sensorTypeRepository;
	private SensorTypeService sensorTypeService;
	
	public EnvironmentController(EnvironmentRepository environmentRepository, EnvironmentService environmentService,
			RelayService relayService, RelayTypeService relayTypeService,
			SensorService sensorService, SensorRepository sensorRepository, SensorTypeRepository sensorTypeRepository,
			SensorTypeService sensorTypeService)
	{
		this.environmentRepository = environmentRepository;
		this.environmentService = environmentService;
		this.relayService = relayService;
		this.relayTypeService = relayTypeService;
		this.sensorService = sensorService;
		this.sensorRepository = sensorRepository;
		this.sensorTypeRepository = sensorTypeRepository;
		this.sensorTypeService = sensorTypeService;
	}
	
	@PutMapping("/")
	public ResponseEntity<?> createEnvironment(@RequestBody String request)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());

		try
		{
			response = environmentService.saveAndEditEnvironment(request, (long) 0);
		}
		catch (IOException e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editEnvironment(@RequestBody String request, @PathVariable(value = "id") Long environmentId)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
		
		try
		{
			response = environmentService.saveAndEditEnvironment(request, (long) environmentId);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}

		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@GetMapping(value="/configoptions", produces = "application/json")
	public ResponseEntity<?> getEnvironmentConfigOptions() throws Throwable
	{
		MultipleDetailResponse response = new MultipleDetailResponse();
		
		try
		{
			//Kicks of multiple, asynchronous calls
			CompletableFuture<List<Environment>> environments = environmentService.fetchAllEnvironments();
			CompletableFuture<List<Relay>> relays = relayService.fetchAllRelays();
			CompletableFuture<List<RelayType>> relayTypes = relayTypeService.fetchAllRelayTypes();
			CompletableFuture<List<SensorType>> sensorTypes = sensorTypeService.fetchSensorTypes();
			CompletableFuture<List<Sensor>> sensors = sensorService.fetchAllEnvironmentSensors();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					environments,
					relays,
					relayTypes,
					sensors,
					sensorTypes
			).join();
			
			response.setEnvironments(environments.get());
			response.setRelays(relays.get());
			response.setRelayTypes(relayTypes.get());
			response.setSensorTypes(sensorTypes.get());
			response.setSensors(sensors.get());
			
			response.setResponseMessage("Pulled all environment config options.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("getEnvironmentConfigOptions error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/{id}", produces = "application/json")
	public ResponseEntity<?> getEnvironmentDetailById(@PathVariable(value = "id") Long environmentId)
	{
		SingleDetailResponse response = new SingleDetailResponse();

		try
		{
			Environment environment = environmentRepository.fetchById(environmentId);
			CompletableFuture<List<Relay>> relays = relayService.fetchRelayByEnvironmentId(environmentId);
			CompletableFuture<List<RelayType>> relayTypes = relayTypeService.fetchAllRelayTypes();
			List<Sensor> sensors  = sensorRepository.fetchSensorsWithEnvironmentId(environmentId);
			SensorType sensorType = sensorTypeRepository.fetchSensorTypeWithEnvironmentId(environmentId);
			List<SensorType> sensorTypes = sensorTypeRepository.findAll();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					relays,
					relayTypes
			).join();
			
			response.setEnvironment(environment);
			response.setRelays(relays.get());
			response.setRelayTypes(relayTypes.get());
			response.setSensors(sensors);
			response.setSensorType(sensorType);
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
			CompletableFuture<List<Relay>> relays = relayService.fetchAllRelays();
			CompletableFuture<List<RelayType>> relayTypes = relayTypeService.fetchAllRelayTypes();
			CompletableFuture<List<Sensor>> sensors = sensorService.fetchAllEnvironmentSensors();
			CompletableFuture<List<SensorType>> sensorTypes = sensorTypeService.fetchSensorTypes();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					environments,
					relays,
					relayTypes,
					sensors,
					sensorTypes
			).join();
			
			response.setEnvironments(environments.get());
			response.setRelays(relays.get());
			response.setRelayTypes(relayTypes.get());
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
			response.setEnvironments(environmentRepository.findAll());

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
