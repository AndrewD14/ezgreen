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
		
		test.open();
		test.write("I am god.");
		System.out.println(test.read());
		test.close();
	}
}
