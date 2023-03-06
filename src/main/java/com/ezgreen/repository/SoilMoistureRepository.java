package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.SoilMoisture;

public interface SoilMoistureRepository extends JpaRepository<SoilMoisture, Integer>
{
	@Query("SELECT new com.ezgreen.models.SoilMoisture(" +
			"id," +
			"highSetting," +
			"lowSetting," +
			"sensorId," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM soil_moisture " +
			"ORDER BY updateTs DESC")
	List<SoilMoisture> fetchAllSoilMoistures();
}