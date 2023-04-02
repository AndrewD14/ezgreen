package com.ezgreen.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ezgreen.util.ArduinoCommand;

import jakarta.annotation.PostConstruct;

@Component
public class ArduinoInit
{
	@Autowired
	private ArduinoCommand command;
	
	@PostConstruct
	public void Test()
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
	}
}
