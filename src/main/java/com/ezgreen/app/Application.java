package com.ezgreen.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.ezgreen")
@EntityScan("com.ezgreen.models")
@EnableJpaRepositories("com.ezgreen.repository")
public class Application
{
	public static void main(String[] args)
	{
		for(int i = 0; i < args.length; i++) System.out.println("property " + i + ": " + args[i]);
		SpringApplication.run(Application.class, args);
	}
}
