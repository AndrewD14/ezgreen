package com.ezgreen.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.PlantType;
import com.ezgreen.repository.PlantTypeRepository;

@Service
public class PlantTypeService
{
	@Autowired
	private PlantTypeRepository plantTypeRepository;
	
	@Async
	public CompletableFuture<List<PlantType>> fetchPlantTypes()
	{
		List<PlantType> plantTypes = plantTypeRepository.findAll();

		return CompletableFuture.completedFuture(plantTypes);
	}
	
	@Async
	public CompletableFuture<PlantType> fetchPlantTypeById(Long plantTypeId)
	{
		PlantType plantType = plantTypeRepository.findById(plantTypeId).get();

		return CompletableFuture.completedFuture(plantType);
	}
}
