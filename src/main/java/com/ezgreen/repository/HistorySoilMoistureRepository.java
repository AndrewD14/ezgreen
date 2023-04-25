package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.HistorySoilMoisture;

public interface HistorySoilMoistureRepository extends JpaRepository<HistorySoilMoisture, Long>
{
	@Query("SELECT new com.ezgreen.models.HistorySoilMoisture(" +
			"id," +
			"plantId," +
			"sensorId," +
			"read," +
			"volt," +
			"percentage," +
			"lowCalibration," +
			"highCalibration," +
			"lowDesire," +
			"highDesire," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM HistorySoilMoisture " +
			"ORDER BY updateTs DESC")
	List<HistorySoilMoisture> fetchAllHistorySoilMoistures();
	
	@Query(value = "SELECT " +
			"id," +
			"plant_id," +
			"sensor_id," +
			"read," +
			"volt," +
			"percentage," +
			"low_calibration," +
			"high_calibration," +
			"low_desire," +
			"high_desire," +
			"created_by," +
			"updated_by," +
			"created_ts," +
			"updated_ts" +
			" FROM history_soil_moisture " +
			" WHERE plant_id = :plantId " +
			"ORDER BY updated_ts ASC",
			nativeQuery = true)
	List<HistorySoilMoisture> fetchAllHistoryForPlant(@Param("plantId") Long plantId);
	
	@Query(value = "SELECT " +
			"id," +
			"plant_id," +
			"sensor_id," +
			"read," +
			"volt," +
			"percentage," +
			"low_calibration," +
			"high_calibration," +
			"low_desire," +
			"high_desire," +
			"created_by," +
			"updated_by," +
			"created_ts," +
			"updated_ts" +
			" FROM history_soil_moisture " +
			" WHERE sensor_id = :sensorId " +
			"ORDER BY updated_ts ASC",
			nativeQuery = true)
	List<HistorySoilMoisture> fetchAllHistoryForSensor(@Param("sensorId") Long sensorId);
}
