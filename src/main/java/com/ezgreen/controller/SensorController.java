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

import com.ezgreen.models.Board;
import com.ezgreen.models.Environment;
import com.ezgreen.models.Plant;
import com.ezgreen.models.Sensor;
import com.ezgreen.models.SensorType;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.ezgreen.responses.MultipleDetailResponse;
import com.ezgreen.responses.SingleDetailResponse;
import com.ezgreen.service.BoardService;
import com.ezgreen.service.EnvironmentService;
import com.ezgreen.service.PlantService;
import com.ezgreen.service.SensorService;
import com.ezgreen.service.SensorTypeService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/sensor")
public class SensorController
{
	private BoardService boardService;
	private EnvironmentService environmentService;
	private PlantService plantService;
	private SensorService sensorService;
	private SensorRepository sensorRepository;
	private SensorTypeService sensorTypeService;
	
	public SensorController(BoardService boardService, EnvironmentService environmentService, PlantService plantService,
			SensorRepository sensorRepository, SensorService sensorService, SensorTypeService sensorTypeService)
	{
		this.boardService = boardService;
		this.environmentService = environmentService;
		this.plantService = plantService;
		this.sensorRepository = sensorRepository;
		this.sensorService = sensorService;
		this.sensorTypeService = sensorTypeService;
	}
	
	@PutMapping("/environment/{id}")
	public ResponseEntity<?> addSensorEnvironment(@RequestBody String request, @PathVariable(value = "id") Long sensorId)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());

		try
		{
			response = sensorService.addSensorEnvironment(request, (long) sensorId);
		}
		catch (IOException e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
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
	
	@GetMapping(value="/alldetails", produces = "application/json")
	public ResponseEntity<?> getSensorsWithDetails() throws Throwable
	{
		MultipleDetailResponse response = new MultipleDetailResponse();
		
		try
		{
			//Kicks of multiple, asynchronous calls
			CompletableFuture<List<Board>> boards = boardService.fetchBoards();
			CompletableFuture<List<Environment>> environments = environmentService.fetchAllEnvironmentWithSensors();
			CompletableFuture<List<Plant>> plants = plantService.fetchPlantsWithSensor();
			CompletableFuture<List<Sensor>> sensors = sensorService.fetchAllSensors();
			CompletableFuture<List<SensorType>> sensorTypes = sensorTypeService.fetchSensorTypes();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					boards,
					environments,
					plants,
					sensors,
					sensorTypes
			).join();
			
			response.setBoards(boards.get());
			response.setEnvironments(environments.get());
			response.setPlants(plants.get());
			response.setSensors(sensors.get());
			response.setSensorTypes(sensorTypes.get());
			
			
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
			CompletableFuture<Board> board = boardService.fetchBoardWithSensor(sensorId);
			CompletableFuture<Environment> environment = environmentService.fetchEnvironmentBySensor(sensorId);
			CompletableFuture<Plant> plant = plantService.fetchPlantBySensor(sensorId);
			CompletableFuture<Sensor> sensor = sensorService.fetchSensorWithId(sensorId);
			CompletableFuture<SensorType> sensorType = sensorTypeService.fetchSensorTypeWithSensor(sensorId);
			
			CompletableFuture.allOf(
					board,
					environment,
					plant,
					sensor,
					sensorType
			).join();
			
			response.setBoard(board.get());
			response.setEnvironment(environment.get());
			response.setPlant(plant.get());
			response.setSensor(sensor.get());
			response.setSensorType(sensorType.get());
			
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
			response.setSensors(sensorRepository.findAll());

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
