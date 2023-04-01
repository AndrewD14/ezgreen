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
	
	@Query(value="SELECT " +
			" e.id," +
			" e.name," +
			" e.sensor_type," +
			" e.low_desire," +
			" e.high_desire," +
			" e.target," +
			" e.humidity," +
			" e.time_start," +
			" e.time_end," +
			" e.delete," +
			" e.created_by," +
			" e.updated_by," +
			" e.created_ts," +
			" e.updated_ts " +
			" FROM environment e " +
			" INNER JOIN sensor s ON e.id = s.environment_id " +
			" WHERE s.id = :sensorId",
			nativeQuery = true)
	Environment fetchEnvironmentBySensor(@Param("sensorId") Long sensorId);
	
	@Query(value = "SELECT DISTINCT " +
			"e.id," +
			"e.name," +
			"e.sensor_type," +
			"e.low_desire," +
			"e.high_desire," +
			"e.target," +
			"e.humidity," +
			"e.time_start," +
			"e.time_end," +
			"e.delete," +
			"e.created_by," +
			"e.updated_by," +
			"e.created_ts," +
			"e.updated_ts" +
			" FROM environment e " +
			" INNER JOIN sensor s ON e.id = s.environment_id ",
			nativeQuery = true)
	List<Environment> fetchAllEnvironmentWithSensor();
}
