package com.ezgreen.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.responses.EZGreenResponse;


@RestController
@RequestMapping("/api/config")
public class ConfigController
{
	@Value("${server.port}")
	private int port;
	
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<?> getRulesConfigFields() throws Throwable
	{

		JSONObject results = new JSONObject();
		EZGreenResponse response = new EZGreenResponse();
		
		try
		{
			results = new JSONObject("{'port':" + port + "}");

			response.setStatusCode(HttpStatus.OK);
			response.setResponseMessage(results.toString());
		}
		catch (Exception e)
		{
			response.setResponseMessage("RulesConfigFields error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
}
