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
			test.write("uM;12;22.31;");
//			Thread.sleep(5000);
//			String result = test.read();
//			
//			if(result == null) System.out.println("Nothing was read from the Audrino.");
//			else System.out.println(result);
			
			//test.close();
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
	}
}
