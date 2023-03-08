package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.PotSize;

public interface PotSizeRepository extends JpaRepository<PotSize, Long>
{
	@Query("SELECT new com.ezgreen.models.PotSize(" +
			"id," +
			"size," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM PotSize " +
			"ORDER BY updateTs DESC")
	List<PotSize> fetchAllPotSizes();
}