package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.Greenhouse;

public interface GreenhouseRepository extends JpaRepository<Greenhouse, Integer>
{
	@Query("SELECT new com.ezgreen.models.Greenhouse(" +
			"id," +
			"location," +
			"sensorId," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM Greenhouse " +
			"ORDER BY updateTs DESC")
	List<Greenhouse> fetchAllGreenhouse();
}
