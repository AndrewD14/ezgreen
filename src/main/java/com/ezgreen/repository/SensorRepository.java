package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long>
{	
	@Query("SELECT s FROM Sensor s WHERE id = :sensorId")
	Sensor fetchById(@Param("sensorId") Long sensorId);
	
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
			") FROM Sensor " +
			"ORDER BY updateTs DESC")
	List<Sensor> fetchAllSensors();
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.type," +
			"s.port," +
			"s.board," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM Sensor s " +
			" INNER JOIN plant p ON s.id = p.sensor_id",
			nativeQuery = true)
	List<Sensor> fetchAllPlantSensors();
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.type," +
			"s.port," +
			"s.board," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM Sensor s " +
			" LEFT OUTER JOIN plant p ON s.id = p.sensor_id " +
			" WHERE s.type = 'Soil moisture' " +
			" AND p.id IS NULL",
			nativeQuery = true)
	List<Sensor> fetchAllAvailablePlantSensors();
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.type," +
			"s.port," +
			"s.board," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" INNER JOIN plant p ON p.sensor_id = s.id " +
			" WHERE p.id = :plantId",
			nativeQuery = true)
	Sensor fetchSensorWithPlantId(@Param("plantId") Long plantId);
}