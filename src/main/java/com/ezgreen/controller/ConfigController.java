package com.ezgreen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.repository.ConfigRepository;
import com.ezgreen.responses.ConfigResponse;

@RestController
@RequestMapping("/api/config")
public class ConfigController
{
	private ConfigRepository configRepository;
	
	public ConfigController(ConfigRepository configRepository)
	{
		this.configRepository = configRepository;
	}
	
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<?> getConfigFields() throws Throwable
	{
		ConfigResponse response = new ConfigResponse();
		
		try
		{
			response.setConfigs(configRepository.fetchAllConfigs());

			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage("Successfully pulled all configs.");
		}
		catch (Exception e)
		{
			response.setResponseMessage("getConfigFields error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
}
