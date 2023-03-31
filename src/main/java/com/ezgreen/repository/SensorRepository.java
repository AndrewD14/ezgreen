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
			"number," +
			"typeId," +
			"boardId," +
			"port," +
			"lowCalibration," +
			"highCalibration," +
			"zoneId," +
			"delete," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM Sensor " +
			"ORDER BY updateTs DESC")
	List<Sensor> fetchAllSensors();
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.number," +
			"s.type_id," +
			"s.board_id," +
			"s.port," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.zone_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" INNER JOIN plant p ON s.id = p.sensor_id",
			nativeQuery = true)
	List<Sensor> fetchAllPlantSensors();
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.number," +
			"s.type_id," +
			"s.board_id," +
			"s.port," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.zone_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" INNER JOIN environment e ON (s.zone_id = e.zone_id AND e.sensor_type = s.type_id)",
			nativeQuery = true)
	List<Sensor> fetchAllEnvironmentSensors();
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.number," +
			"s.type_id," +
			"s.board_id," +
			"s.port," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.zone_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" LEFT OUTER JOIN plant p ON s.id = p.sensor_id " +
			" WHERE s.type = 'Soil moisture' " +
			" AND p.id IS NULL",
			nativeQuery = true)
	List<Sensor> fetchAllAvailablePlantSensors();
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.number," +
			"s.type_id," +
			"s.board_id," +
			"s.port," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.zone_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" LEFT OUTER JOIN environment e ON s.id = e.sensor_id " +
			" WHERE s.type != 'Soil moisture' " +
			" AND e.id IS NULL",
			nativeQuery = true)
	List<Sensor> fetchAllAvailableEnvironmentSensors();
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.number," +
			"s.type_id," +
			"s.board_id," +
			"s.port," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.zone_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" INNER JOIN plant p ON p.sensor_id = s.id " +
			" WHERE p.id = :plantId",
			nativeQuery = true)
	Sensor fetchSensorWithPlantId(@Param("plantId") Long plantId);
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.number," +
			"s.type_id," +
			"s.board_id," +
			"s.port," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.zone_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" INNER JOIN environment e ON (s.zone_id = e.zone_id AND e.sensor_type = s.type_id) " +
			" WHERE e.id = :environmentId",
			nativeQuery = true)
	List<Sensor> fetchSensorsWithEnvironmentId(@Param("environmentId") Long environmentId);
}