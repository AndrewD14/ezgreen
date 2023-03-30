package com.ezgreen.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.connection.Arduino;
import com.ezgreen.models.Sensor;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.responses.EZGreenResponse;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class SensorService
{	
	private SensorRepository sensorRepository;
	private Arduino arduino;
	
	public SensorService(SensorRepository sensorRepository, Arduino arduino)
	{
		this.sensorRepository = sensorRepository;
		this.arduino = arduino;
	}
	
	public void getCalibration(HttpServletResponse response, String request) throws IOException
	{
		JSONObject requestJson = new JSONObject(request);
		String command = "";
		
		command = command + requestJson.getString("type") + ";";
		command = command + requestJson.getInt("serialBus") + ";";
		command = command + requestJson.getInt("board") + ";";
		command = command + requestJson.getInt("port") + ";";
		
		if(arduino.checkGood()) arduino.writeCallCalibration(command, response);
		else
		{
			Random rand = new Random();
			
			double upperbound = 3.5;
			
			response.setStatus(HttpStatus.OK.value());
		    
		    try
		    {
		    	response.getWriter().println("{\"statusCode\": 200, \"responseMessage\": " + rand.nextDouble(upperbound) + "}");
		    }
		    catch(Exception e)
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
		}
		
		sensor.setBoard(requestJson.getInt("board"));
		sensor.setPort(requestJson.getInt("port"));
		sensor.setType(requestJson.getString("type"));
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
		List<Sensor> sensors = sensorRepository.fetchAllSensors();

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
