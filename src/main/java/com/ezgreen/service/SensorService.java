package com.ezgreen.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.Sensor;
import com.ezgreen.repository.SensorRepository;

@Service
public class SensorService
{	
	@Autowired
	private SensorRepository sensorRepository;
	
	@Async
	public CompletableFuture<List<Sensor>> fetchAllSensors()
	{
		List<Sensor> sensors = sensorRepository.fetchAllSensors();

		return CompletableFuture.completedFuture(sensors);
	}
	
	@Async
	public CompletableFuture<List<Sensor>> fetchPlantSensors()
	{
		List<Sensor> sensors = sensorRepository.fetchAllPlantSensors();

		return CompletableFuture.completedFuture(sensors);
	}
	
	@Async
	public CompletableFuture<List<Sensor>> fetchAvailablePlantSensors()
	{
		List<Sensor> sensors = sensorRepository.fetchAllAvailablePlantSensors();

		return CompletableFuture.completedFuture(sensors);
	}
}
