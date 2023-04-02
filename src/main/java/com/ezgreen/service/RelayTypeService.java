package com.ezgreen.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.RelayType;
import com.ezgreen.repository.RelayTypeRepository;

@Service
public class RelayTypeService
{
	@Autowired
	private RelayTypeRepository relayTypeRepository;
	
	@Async
	public CompletableFuture<RelayType> fetchRelayTypeByRelay(Long relayId)
	{
		RelayType relayType = relayTypeRepository.fetchRelayTypeByRelay(relayId);
		
		return CompletableFuture.completedFuture(relayType);
	}
	
	@Async
	public CompletableFuture<List<RelayType>> fetchAllRelayTypes()
	{
		List<RelayType> relayTypes = relayTypeRepository.findAll();
		
		return CompletableFuture.completedFuture(relayTypes);
	}
}
