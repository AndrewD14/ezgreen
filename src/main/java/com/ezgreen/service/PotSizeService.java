package com.ezgreen.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.PotSize;
import com.ezgreen.repository.PotSizeRepository;

@Service
public class PotSizeService
{
	@Autowired
	private PotSizeRepository potSizeRepository;
	
	@Async
	public CompletableFuture<List<PotSize>> fetchPotSizes()
	{
		List<PotSize> potSizes = potSizeRepository.fetchAllPotSizes();

		return CompletableFuture.completedFuture(potSizes);
	}
}
