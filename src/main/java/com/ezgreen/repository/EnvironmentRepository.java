package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.Environment;

public interface EnvironmentRepository extends JpaRepository<Environment, Long>
{
	@Query("SELECT e FROM Environment e WHERE id = :environmentId")
	Environment fetchById(@Param("environmentId") Long environmentId);
	
	@Query("SELECT e FROM Environment e WHERE e.sensorId = :sensorId")
	Environment fetchEnvironmentBySensor(@Param("sensorId") Long sensorId);
	
	@Query("SELECT new com.ezgreen.models.Environment(" +
			"id," +
			"location," +
			"sensorId," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM Environment " +
			" WHERE sensorId IS NOT NULL " +
			"ORDER BY updateTs DESC")
	List<Environment> fetchAllEnvironmentWithSensor();
	
	@Query("SELECT new com.ezgreen.models.Environment(" +
			"id," +
			"location," +
			"sensorId," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM Environment " +
			"ORDER BY updateTs DESC")
	List<Environment> fetchAllEnvironments();
}
