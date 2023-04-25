package com.ezgreen.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.HistorySoilMoisture;
import com.ezgreen.repository.HistorySoilMoistureRepository;

@Service
public class HistorySoilMoistureService
{
	private HistorySoilMoistureRepository historyRepository;
	
	public HistorySoilMoistureService(HistorySoilMoistureRepository historyRepository)
	{
		this.historyRepository = historyRepository;
	}
	
	@Async
	public CompletableFuture<List<HistorySoilMoisture>> fetchByPlant(Long plantId)
	{
		List<HistorySoilMoisture> histories = historyRepository.fetchAllHistoryForPlant(plantId);

		return CompletableFuture.completedFuture(histories);
	}
	
	@Async
	public CompletableFuture<List<HistorySoilMoisture>> fetchbySensor(Long sensorId)
	{
		List<HistorySoilMoisture> histories = historyRepository.fetchAllHistoryForSensor(sensorId);

		return CompletableFuture.completedFuture(histories);
	}
}
