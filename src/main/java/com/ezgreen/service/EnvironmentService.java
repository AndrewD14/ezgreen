package com.ezgreen.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.Environment;
import com.ezgreen.repository.EnvironmentRepository;
import com.ezgreen.responses.EZGreenResponse;

@Service
public class EnvironmentService
{
	@Autowired
	private EnvironmentRepository environmentRepository;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
	
	public EZGreenResponse saveAndEditEnvironment(String request, Long environmentId) throws IOException
	{
		EZGreenResponse response = new EZGreenResponse();
		Environment environment = new Environment();
		JSONObject requestJson = new JSONObject(request);
		
		if(environmentId != null && environmentId != 0)
		{
			environment = environmentRepository.findById(environmentId).get();
		}
		else
		{
			environment.setCreateBy(requestJson.getString("username"));
			environment.setCreateTs(LocalDateTime.now(ZoneOffset.UTC));
			environment.setDelete(0);
		}
		
		environment.setHighDesire((!requestJson.isNull("highDesire") ? requestJson.getDouble("highDesire") : null));
		environment.setHumidity((!requestJson.isNull("humidity") ? requestJson.getDouble("humidity") : null));
		environment.setLowDesire((!requestJson.isNull("lowDesire") ? requestJson.getDouble("lowDesire") : null));
		environment.setName(requestJson.getString("name"));
		environment.setSensorType(requestJson.getLong("sensorType"));
		environment.setTarget(requestJson.getDouble("target"));
		environment.setTimeEnd((!requestJson.isNull("timeEnd") ? LocalTime.parse(requestJson.getString("timeEnd"), formatter) : null));
		environment.setTimeStart((!requestJson.isNull("timeStart") ? LocalTime.parse(requestJson.getString("timeStart"), formatter) : null));
		environment.setUpdateBy(requestJson.getString("username"));
		environment.setUpdateTs(LocalDateTime.now(ZoneOffset.UTC));
		
		try
		{
			environmentRepository.save(environment);
			
			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(Long.toString(environment.getId()));
		}
		catch(Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("saveAndEditRelay error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	@Async
	public CompletableFuture<Environment> fetchEnvironmentByRelay(Long relayId)
	{
		Environment environment = environmentRepository.fetchEnvironmentByRelay(relayId);

		return CompletableFuture.completedFuture(environment);
	}
	
	@Async
	public CompletableFuture<Environment> fetchEnvironmentBySensor(Long sensorId)
	{
		Environment environment = environmentRepository.fetchEnvironmentBySensor(sensorId);

		return CompletableFuture.completedFuture(environment);
	}
	
	@Async
	public CompletableFuture<List<Environment>> fetchAllEnvironmentWithSensors()
	{
		List<Environment> environments = environmentRepository.fetchAllEnvironmentWithSensor();

		return CompletableFuture.completedFuture(environments);
	}
	
	@Async
	public CompletableFuture<List<Environment>> fetchAllEnvironments()
	{
		List<Environment> environments = environmentRepository.findAll();

		return CompletableFuture.completedFuture(environments);
	}
}
