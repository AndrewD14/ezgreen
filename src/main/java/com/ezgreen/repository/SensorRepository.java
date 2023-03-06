package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Integer>
{
	@Query("SELECT new com.ezgreen.models.Sensor(" +
			"id," +
			"type," +
			"port," +
			"board," +
			"lowCalibration," +
			"highCalibration," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM sensor " +
			"ORDER BY updateTs DESC")
	List<Sensor> fetchAllSensors();
}