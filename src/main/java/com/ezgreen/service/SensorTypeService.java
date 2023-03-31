package com.ezgreen.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.SensorType;
import com.ezgreen.repository.SensorTypeRepository;

@Service
public class SensorTypeService
{
	@Autowired
	private SensorTypeRepository sensorTypeRepository;
	
	@Async
	public CompletableFuture<List<SensorType>> fetchSensorTypes()
	{
		List<SensorType> sensorTypes = sensorTypeRepository.findAll();

		return CompletableFuture.completedFuture(sensorTypes);
	}
	
	@Async
	public CompletableFuture<SensorType> fetchSensorTypeWithSensor(Long sensorId)
	{
		SensorType sensorType = sensorTypeRepository.fetchSensorTypeWithSensorId(sensorId);

		return CompletableFuture.completedFuture(sensorType);
	}
}
