package com.ezgreen.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.connection.Arduino;
import com.ezgreen.models.Sensor;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.responses.EZGreenResponse;

@Service
public class SensorService
{	
	private Arduino arduino;
	private SensorRepository sensorRepository;
	
	public SensorService(Arduino arduino, SensorRepository sensorRepository)
	{
		this.arduino = arduino;
		this.sensorRepository = sensorRepository;
	}
	
	public void getCalibration(EZGreenResponse response, String request) throws IOException
	{
		JSONObject requestJson = new JSONObject(request);
		String command = "";
		
		command = command + requestJson.getString("type") + ";";
		command = command + requestJson.getInt("serialBus") + ";";
		command = command + requestJson.getInt("board") + ";";
		command = command + requestJson.getInt("port") + ";";
		
		if(arduino.checkGood())
		{
			arduino.writeCallCalibration(command, response);
		}
		else
		{
			Random rand = new Random();			
			double upperbound = 3.5;
		    	
	    	response.setResponseMessage("{\"responseMessage\": " + rand.nextDouble(upperbound) + "}");
	    	response.setStatusCode(HttpStatus.OK);
		}
	}
	
	public EZGreenResponse addSensorEnvironment(String request, Long sensorId) throws IOException
	{
		EZGreenResponse response = new EZGreenResponse();
		Sensor sensor = sensorRepository.fetchById(sensorId);
		JSONObject requestJson = new JSONObject(request);
		
		sensor.setEnvironmentId(requestJson.getInt("environmentId"));
		sensor.setUpdateBy(requestJson.getString("username"));
		sensor.setUpdateTs(LocalDateTime.now(ZoneOffset.UTC));
		
		try
		{
			sensorRepository.save(sensor);
			
			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(Long.toString(sensor.getId()));
		}
		catch(Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("saveAndEditSensor error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	public EZGreenResponse saveAndEditSensor(String request, Long sensorId) throws IOException
	{
		EZGreenResponse response = new EZGreenResponse();
		Sensor sensor = new Sensor();
		JSONObject requestJson = new JSONObject(request);
		
		if(sensorId != null && sensorId != 0)
		{
			sensor = sensorRepository.fetchById(sensorId);
		}
		else
		{
			sensor.setCreateBy(requestJson.getString("username"));
			sensor.setCreateTs(LocalDateTime.now(ZoneOffset.UTC));
			sensor.setDelete(0);
		}
		
		sensor.setBoardId(requestJson.getLong("boardId"));
		sensor.setNumber(requestJson.getInt("number"));
		sensor.setPort(requestJson.getInt("port"));
		sensor.setTypeId(requestJson.getInt("typeId"));
		sensor.setLowCalibration(!requestJson.isNull("lowCalibration") ? requestJson.getDouble("lowCalibration") : null);
		sensor.setHighCalibration(!requestJson.isNull("highCalibration") ? requestJson.getDouble("highCalibration") : null);
		sensor.setUpdateBy(requestJson.getString("username"));
		sensor.setUpdateTs(LocalDateTime.now(ZoneOffset.UTC));
		
		try
		{
			sensorRepository.save(sensor);
			
			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(Long.toString(sensor.getId()));
		}
		catch(Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("saveAndEditSensor error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	@Async
	public CompletableFuture<Sensor> fetchSensorWithId(Long Id)
	{
		Sensor sensor = sensorRepository.fetchById(Id);

		return CompletableFuture.completedFuture(sensor);
	}
	
	@Async
	public CompletableFuture<List<Sensor>> fetchAllSensors()
	{
		List<Sensor> sensors = sensorRepository.findAll();

		return CompletableFuture.completedFuture(sensors);
	}
	
	@Async
	public CompletableFuture<List<Sensor>> fetchPlantSensors()
	{
		List<Sensor> sensors = sensorRepository.fetchAllPlantSensors();

		return CompletableFuture.completedFuture(sensors);
	}
	
	@Async
	public CompletableFuture<List<Sensor>> fetchAllEnvironmentSensors()
	{
		List<Sensor> sensors = sensorRepository.fetchAllEnvironmentSensors();

		return CompletableFuture.completedFuture(sensors);
	}
	
	@Async
	public CompletableFuture<List<Sensor>> fetchAvailablePlantSensors()
	{
		List<Sensor> sensors = sensorRepository.fetchAllAvailablePlantSensors();

		return CompletableFuture.completedFuture(sensors);
	}
	
	@Async
	public CompletableFuture<List<Sensor>> fetchAvailableEnvironmentSensors()
	{
		List<Sensor> sensors = sensorRepository.fetchAllAvailableEnvironmentSensors();

		return CompletableFuture.completedFuture(sensors);
	}
}
