package com.ezgreen.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ezgreen.connection.Arduino;


@SpringBootApplication
public class Application
{

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
		
		Arduino test = new Arduino();
		
		try
		{
			test.open();
			test.write("uP;12;22.31");
			System.out.println(test.read());
			//test.close();
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
	}
}
