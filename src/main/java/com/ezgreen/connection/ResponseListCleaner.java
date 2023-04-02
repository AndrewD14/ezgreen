package com.ezgreen.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResponseListCleaner implements Runnable
{
	@Autowired
	private ArduinoListener listener;
	
	@Override
	public void run()
	{
		listener.getResponses().removeIf(response -> (response == null));
	}
}
