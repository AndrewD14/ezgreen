package com.ezgreen.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.Relay;
import com.ezgreen.repository.RelayRepository;

@Service
public class RelayService
{
	@Autowired
	private RelayRepository relayRepository;
	
	@Async
	public CompletableFuture<List<Relay>> fetchAllRelays()
	{
		List<Relay> relays = relayRepository.findAll();
		
		return CompletableFuture.completedFuture(relays);
	}
}
