package com.ezgreen.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.Environment;
import com.ezgreen.repository.EnvironmentRepository;

@Service
public class EnvironmentService
{
	@Autowired
	private EnvironmentRepository environmentRepository;
	
	@Async
	public CompletableFuture<List<Environment>> fetchAllEnvironmentWithSensors()
	{
		List<Environment> environments = environmentRepository.fetchAllEnvironmentWithSensor();

		return CompletableFuture.completedFuture(environments);
	}
}
