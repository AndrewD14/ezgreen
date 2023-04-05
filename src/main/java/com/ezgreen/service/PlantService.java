package com.ezgreen.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.Plant;
import com.ezgreen.repository.HistorySoilMoistureRepository;
import com.ezgreen.repository.PlantRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.ezgreen.util.ArduinoCommand;

@Service
public class PlantService
{
	@Autowired
	@Lazy
	private ArduinoCommand command;
	
	@Autowired
	private PlantRepository plantRepository;
	
	@Autowired
	private HistorySoilMoistureRepository historySoilMoisterRepository;
	
	public EZGreenResponse saveAndEditPlant(String request, Long plantId) throws IOException
	{
		EZGreenResponse response = new EZGreenResponse();
		Plant plant = new Plant();
		JSONObject requestJson = new JSONObject(request);
		
		
		if(plantId != null && plantId != 0)
		{
			plant = plantRepository.fetchPlantById(plantId);
		}
		else
		{
			plant.setCreateBy(requestJson.getString("username"));
			plant.setCreateTs(LocalDateTime.now(ZoneOffset.UTC));
		}
		
		Integer currentMonitor = (plant.getMonitor() != null ? plant.getMonitor() : 0);
		
		plant.setDateObtain(!requestJson.isNull("dateObtain") ? LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(requestJson.getString("dateObtain"))) : null);
		plant.setDead(requestJson.getInt("dead"));
		plant.setDelete(requestJson.getInt("delete"));
		plant.setHighMoisture(requestJson.getDouble("high"));
		plant.setLowMoisture(requestJson.getDouble("low"));
		plant.setMonitor(plant.getEnvironmentId() != null && !requestJson.isNull("monitor") ? requestJson.getInt("monitor") : 0);
		plant.setName(requestJson.getString("name"));
		plant.setNumber(!requestJson.isNull("number") ? requestJson.getInt("number") : null);
		plant.setPlantTypeId(requestJson.getLong("plantTypeId"));
		plant.setPotSizeId(requestJson.getLong("potSizeId"));
		plant.setSensorId(!requestJson.isNull("sensorId") ? requestJson.getLong("sensorId") : null);
		plant.setValveId(!requestJson.isNull("valveId") ? requestJson.getLong("valveId") : null);
		plant.setUpdateBy(requestJson.getString("username"));
		plant.setUpdateTs(LocalDateTime.now(ZoneOffset.UTC));
		
		try
		{
			plantRepository.save(plant);
			
			if(plant.getMonitor() == 1 && currentMonitor == 1 && plantId != null && plantId != 0) command.processPlant(plant, true);
			else if(plant.getMonitor() == 1) command.processPlant(plant, false);
			else if(currentMonitor == 1 && plant.getMonitor() == 0) command.removePlant(plant);
			
			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(Long.toString(plant.getId()));
		}
		catch(Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("saveAndEditPlant error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	public EZGreenResponse addPlantEnvironment(String request, Long plantId) throws IOException
	{
		EZGreenResponse response = new EZGreenResponse();
		Plant plant = plantRepository.fetchPlantById(plantId);
		JSONObject requestJson = new JSONObject(request);
		
		plant.setEnvironmentId(requestJson.getLong("environmentId"));
		plant.setUpdateBy(requestJson.getString("username"));
		plant.setUpdateTs(LocalDateTime.now(ZoneOffset.UTC));
		
		try
		{
			plantRepository.save(plant);
			
			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(Long.toString(plant.getId()));
		}
		catch(Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("addPlantEnvironment error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	public EZGreenResponse activate(String request, Long plantId) throws IOException
	{
		EZGreenResponse response = new EZGreenResponse();
		Plant plant = plantRepository.fetchPlantById(plantId);
		JSONObject requestJson = new JSONObject(request);
		
		plant.setMonitor(1);
		plant.setUpdateTs(LocalDateTime.now());
		plant.setUpdateBy(requestJson.getString("username"));
		
		try
		{
			plantRepository.save(plant);
			
			command.processPlant(plant, false);
			
			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(Long.toString(plant.getId()));
		}
		catch(Exception e)
		{
			response.setResponseMessage("activate plant error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	public EZGreenResponse deactivate(String request, Long plantId) throws IOException
	{
		EZGreenResponse response = new EZGreenResponse();
		Plant plant = plantRepository.fetchPlantById(plantId);
		JSONObject requestJson = new JSONObject(request);
		
		plant.setMonitor(0);
		plant.setUpdateTs(LocalDateTime.now());
		plant.setUpdateBy(requestJson.getString("username"));

		try
		{
			plantRepository.save(plant);
			
			command.removePlant(plant);
			
			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(Long.toString(plant.getId()));
		}
		catch(Exception e)
		{
			response.setResponseMessage("deactivate plant error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	public EZGreenResponse delete(String request, Long plantId) throws IOException
	{
		EZGreenResponse response = new EZGreenResponse();
		Plant plant = plantRepository.fetchPlantById(plantId);
		JSONObject requestJson = new JSONObject(request);
		
		plant.setSensorId(null);
		plant.setEnvironmentId(null);
		plant.setValveId(null);
		plant.setMonitor(0);
		plant.setUpdateTs(LocalDateTime.now());
		plant.setDelete(1);
		plant.setUpdateBy(requestJson.getString("username"));
		
		try
		{
			command.removePlant(plant);
			
			plantRepository.save(plant);
			
			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(Long.toString(plant.getId()));
		}
		catch(Exception e)
		{
			response.setResponseMessage("delete plant error occur:"+ e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	@Async
	public CompletableFuture<List<Plant>> fetchPlantByEnvironment(Long environmentId)
	{
		List<Plant> plants = plantRepository.fetchAllNonDeletePlantsByEnvironmentId(environmentId);

		return CompletableFuture.completedFuture(plants);
	}
	
	@Async
	public CompletableFuture<Plant> fetchPlantBySensor(Long sensorId)
	{
		Plant plant = plantRepository.fetchPlantBySensorId(sensorId);

		return CompletableFuture.completedFuture(plant);
	}
	
	@Async
	public CompletableFuture<List<Plant>> fetchNonDeletedPlants()
	{
		List<Plant> plants = plantRepository.fetchAllNonDeletedPlants();

		return CompletableFuture.completedFuture(plants);
	}
	
	@Async
	public CompletableFuture<List<Plant>> fetchPlantsWithSensor()
	{
		List<Plant> plants = plantRepository.findBySensorIdIsNotNull();

		return CompletableFuture.completedFuture(plants);
	}
}
