package com.ezgreen.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.Zone;
import com.ezgreen.repository.ZoneRepository;

@Service
public class ZoneService
{
	@Autowired
	private ZoneRepository zoneRepository;
	
	@Async
	public CompletableFuture<List<Zone>> fetchSensorTypes()
	{
		List<Zone> zones = zoneRepository.findAll();

		return CompletableFuture.completedFuture(zones);
	}
}
