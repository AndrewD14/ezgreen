package com.ezgreen.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController
{
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@SuppressWarnings("unchecked")
	//@SubscribeMapping("/topic/plant/{plantId}")
	public void sendSpecificPlant(Long id)
	{
		JSONObject message = new JSONObject();

		message.put("message", "New soil reading for plant " + id);
		
		try
		{
			System.out.println("Sending message");
			simpMessagingTemplate.convertAndSend("/topic/plant/" + id, message);
		}
		catch(Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
		}
	}
}
