package com.ezgreen.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController
{
	@Autowired
	private SimpMessageSendingOperations simpMessageSendingOperations;
	
	@SuppressWarnings("unchecked")
	@MessageMapping("/subscribe")
	public void sendSpecificPlant(Long id)
	{
		JSONObject message = new JSONObject();

		message.put("message", "New soil reading for plant " + id);
		
		simpMessageSendingOperations.convertAndSend("/topic/plant/" + id, message);
	}
}
