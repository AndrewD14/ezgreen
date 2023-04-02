package com.ezgreen.app;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ezgreen.connection.ResponseListCleaner;
import com.ezgreen.util.ArduinoCommand;

import jakarta.annotation.PostConstruct;

@Component
public class ArduinoInit
{
	@Autowired
	private ArduinoCommand command;
	
	@Autowired
	private ResponseListCleaner cleaner;
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	@PostConstruct
	public void run()
	{
		try
		{
			command.init();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
		
		if(command.checkArduino()) scheduler.scheduleAtFixedRate(cleaner, 1000, 1000, TimeUnit.MILLISECONDS);
	}
}
