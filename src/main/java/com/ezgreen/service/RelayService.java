package com.ezgreen.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.Relay;
import com.ezgreen.repository.RelayRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.ezgreen.util.ArduinoCommand;

@Service
public class RelayService
{
	@Autowired
	@Lazy
	private ArduinoCommand command;
	
	@Autowired
	private RelayRepository relayRepository;
	
	public EZGreenResponse addRelayEnvironment(String request, Long relayId) throws IOException
	{
		EZGreenResponse response = new EZGreenResponse();
		Relay relay = relayRepository.findById(relayId).get();
		JSONObject requestJson = new JSONObject(request);
		
		relay.setEnvironmentId(requestJson.getLong("environmentId"));
		relay.setUpdateBy(requestJson.getString("username"));
		relay.setUpdateTs(LocalDateTime.now(ZoneOffset.UTC));
		
		try
		{
			relayRepository.save(relay);
			
			command.processRelay(relay);
			
			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(Long.toString(relay.getId()));
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
	
	public EZGreenResponse saveAndEditRelay(String request, Long relayId) throws IOException
	{
		EZGreenResponse response = new EZGreenResponse();
		Relay relay = new Relay();
		JSONObject requestJson = new JSONObject(request);
		
		if(relayId != null && relayId != 0)
		{
			relay = relayRepository.findById(relayId).get();
		}
		else
		{
			relay.setCreateBy(requestJson.getString("username"));
			relay.setCreateTs(LocalDateTime.now(ZoneOffset.UTC));
			relay.setDelete(0);
		}
		
		relay.setTypeId(requestJson.getLong("type"));
		relay.setBoardId(requestJson.getLong("boardId"));
		relay.setNumber(requestJson.getInt("number"));
		relay.setRelay(requestJson.getInt("relay"));
		relay.setUpdateBy(requestJson.getString("username"));
		relay.setUpdateTs(LocalDateTime.now(ZoneOffset.UTC));
		
		try
		{
			relayRepository.save(relay);
			
			if(relay.getEnvironmentId() != null) command.processRelay(relay);
			
			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(Long.toString(relay.getId()));
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
	public CompletableFuture<Relay> fetchRelayById(Long relayId)
	{
		Optional<Relay> relay = relayRepository.findById(relayId);
		
		return CompletableFuture.completedFuture(relay.get());
	}
	
	@Async
	public CompletableFuture<Relay> fetchWaterPumpByPlant(Long plantId)
	{
		Relay relay = relayRepository.fetchWaterPumpByPlantId(plantId);
		
		return CompletableFuture.completedFuture(relay);
	}
	
	@Async
	public CompletableFuture<List<Relay>> fetchRelayByEnvironmentId(Long environmentId)
	{
		List<Relay> relays = relayRepository.fetchRelaysByEnvironmentId(environmentId);
		
		return CompletableFuture.completedFuture(relays);
	}
	
	@Async
	public CompletableFuture<List<Relay>> fetchPlantRelays()
	{
		List<Relay> relays = relayRepository.fetchPlantRelays();
		
		return CompletableFuture.completedFuture(relays);
	}
	
	@Async
	public CompletableFuture<List<Relay>> fetchAllRelays()
	{
		List<Relay> relays = relayRepository.findAll();
		
		return CompletableFuture.completedFuture(relays);
	}
}
