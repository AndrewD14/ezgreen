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

import com.ezgreen.models.Board;
import com.ezgreen.models.Plant;
import com.ezgreen.models.PlantType;
import com.ezgreen.models.PotSize;
import com.ezgreen.models.Relay;
import com.ezgreen.models.RelayType;
import com.ezgreen.models.Sensor;
import com.ezgreen.models.SensorType;
import com.ezgreen.repository.PlantRepository;
import com.ezgreen.repository.PlantTypeRepository;
import com.ezgreen.repository.PotSizeRepository;
import com.ezgreen.repository.RelayRepository;
import com.ezgreen.repository.RelayTypeRepository;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.repository.SensorTypeRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.ezgreen.responses.MultipleDetailResponse;
import com.ezgreen.responses.SingleDetailResponse;
import com.ezgreen.service.BoardService;
import com.ezgreen.service.PlantService;
import com.ezgreen.service.PlantTypeService;
import com.ezgreen.service.PotSizeService;
import com.ezgreen.service.RelayService;
import com.ezgreen.service.RelayTypeService;
import com.ezgreen.service.SensorService;
import com.ezgreen.service.SensorTypeService;

@RestController
@RequestMapping("/api/plant")
public class PlantController
{
	private BoardService boardService;
	private PlantRepository plantRepository;
	private PlantService plantService;
	private PlantTypeRepository plantTypeRepository;
	private PlantTypeService plantTypeService;
	private PotSizeService potSizeService;
	private PotSizeRepository potSizeRepository;
	private RelayRepository relayRepository;
	private RelayService relayService;
	private RelayTypeRepository relayTypeRepository;
	private RelayTypeService relayTypeService;
	private SensorRepository sensorRepository;
	private SensorService sensorService;
	private SensorTypeRepository sensorTypeRepository;
	private SensorTypeService sensorTypeService;
	
	public PlantController(BoardService boardService,
			PlantRepository plantRepository, PlantService plantService,
			PlantTypeRepository plantTypeRepository, PlantTypeService plantTypeService,
			PotSizeService potSizeService, PotSizeRepository potSizeRepository,
			RelayRepository relayRepository, RelayService relayService,
			RelayTypeRepository relayTypeRepository, RelayTypeService relayTypeService,
			SensorRepository sensorRepository, SensorService sensorService,
			SensorTypeRepository sensorTypeRepository, SensorTypeService sensorTypeService)
	{
		this.boardService = boardService;
		this.plantRepository = plantRepository;
		this.plantService = plantService;
		this.plantTypeRepository = plantTypeRepository;
		this.plantTypeService = plantTypeService;
		this.potSizeService = potSizeService;
		this.potSizeRepository = potSizeRepository;
		this.relayRepository = relayRepository;
		this.relayService = relayService;
		this.relayTypeRepository = relayTypeRepository;
		this.relayTypeService = relayTypeService;
		this.sensorRepository = sensorRepository;
		this.sensorService = sensorService;
		this.sensorTypeRepository = sensorTypeRepository;
		this.sensorTypeService = sensorTypeService;
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
	
	@PutMapping("/environment/{id}")
	public ResponseEntity<?> addPlantEnvironment(@RequestBody String request, @PathVariable(value = "id") Long plantId)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());

		try
		{
			response = plantService.addPlantEnvironment(request, (long) plantId);
		}
		catch (IOException e)
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
		SingleDetailResponse response = new SingleDetailResponse();

		try
		{
			Plant plant = plantRepository.fetchPlantById(plantId);
			PlantType plantType = plantTypeRepository.fetchPlantTypeWithPlantId(plantId);
			PotSize potSize = potSizeRepository.fetchPotSizeWithPlantId(plantId);
			Relay relay = relayRepository.fetchRelayByPlantId(plantId);
			RelayType relayType = relayTypeRepository.fetchRelayTypeByPlant(plantId);
			Sensor sensor = sensorRepository.fetchSensorWithPlantId(plantId);
			SensorType sensorType = sensorTypeRepository.fetchSensorTypeWithPlantId(plantId);

			if(plant != null) response.setPlant(plant);
			if(plantType != null) response.setPlantType(plantType);
			if(potSize != null) response.setPotSize(potSize);
			if(relay != null) response.setRelay(relay);
			if(relayType != null) response.setRelayType(relayType);
			if(sensor != null) response.setSensor(sensor);
			if(sensorType != null) response.setSensorType(sensorType);
			
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
		MultipleDetailResponse response = new MultipleDetailResponse();
		
		try
		{
			//Kicks of multiple, asynchronous calls
			CompletableFuture<List<Plant>> plants = plantService.fetchNonDeletedPlants();
			CompletableFuture<List<PlantType>> plantTypes = plantTypeService.fetchPlantTypes();
			CompletableFuture<List<PotSize>> potSizes = potSizeService.fetchPotSizes();
			CompletableFuture<List<Relay>> relays = relayService.fetchPlantRelays();
			CompletableFuture<List<RelayType>> relayTypes = relayTypeService.fetchAllRelayTypes();
			CompletableFuture<List<Sensor>> sensors = sensorService.fetchPlantSensors();
			CompletableFuture<List<SensorType>> sensorTypes = sensorTypeService.fetchSensorTypes();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					plants,
					plantTypes,
					potSizes,
					relays,
					relayTypes,
					sensors,
					sensorTypes
			).join();
			
			response.setPlants(plants.get());
			response.setPlantTypes(plantTypes.get());
			response.setPotSizes(potSizes.get());
			response.setRelays(relays.get());
			response.setRelayTypes(relayTypes.get());
			response.setSensors(sensors.get());
			response.setSensorTypes(sensorTypes.get());
			
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
		MultipleDetailResponse response = new MultipleDetailResponse();
		
		try
		{
			//Kicks of multiple, asynchronous calls
			CompletableFuture<List<Board>> boards = boardService.fetchBoards();
			CompletableFuture<List<Plant>> plants = plantService.fetchNonDeletedPlants();
			CompletableFuture<List<PlantType>> plantTypes = plantTypeService.fetchPlantTypes();
			CompletableFuture<List<PotSize>> potSizes = potSizeService.fetchPotSizes();
			CompletableFuture<List<Relay>> relays = relayService.fetchPlantRelays();
			CompletableFuture<List<RelayType>> relayTypes = relayTypeService.fetchAllRelayTypes();
			CompletableFuture<List<Sensor>> sensors = sensorService.fetchPlantSensors();
			CompletableFuture<List<SensorType>> sensorTypes = sensorTypeService.fetchSensorTypes();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					boards,
					plants,
					plantTypes,
					potSizes,
					relays,
					relayTypes,
					sensors,
					sensorTypes
			).join();
			
			response.setBoards(boards.get());
			response.setPlants(plants.get());
			response.setPlantTypes(plantTypes.get());
			response.setPotSizes(potSizes.get());
			response.setRelays(relays.get());
			response.setRelayTypes(relayTypes.get());
			response.setSensors(sensors.get());
			response.setSensorTypes(sensorTypes.get());
			
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
		MultipleDetailResponse response = new MultipleDetailResponse();
		
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
