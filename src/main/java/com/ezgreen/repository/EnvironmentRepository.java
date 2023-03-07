package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.Environment;

public interface EnvironmentRepository extends JpaRepository<Environment, Integer>
{
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
	List<Environment> fetchAllEnvironment();
}
