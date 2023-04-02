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
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.number," +
			"s.type_id," +
			"s.board_id," +
			"s.port," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.environment_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" INNER JOIN sensor_type st ON s.type_id = st.id " +
			" WHERE st.arduino = 'm'",
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
			"s.environment_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" LEFT OUTER JOIN plant p ON s.id = p.sensor_id " +
			" INNER JOIN sensor_type st ON st.id = s.type_id " +
			" WHERE st.type = 'Soil moisture' " +
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
			"s.environment_id," +
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
			"s.environment_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" LEFT OUTER JOIN environment e ON s.environment_id = e.id " +
			" INNER JOIN sensor_type st ON st.id = sensor_type_id " +
			" WHERE st.type != 'Soil moisture' " +
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
			"s.environment_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" WHERE s.environment_id = :environmentId",
			nativeQuery = true)
	List<Sensor> fetchSensorsWithEnvironmentId(@Param("environmentId") Long environmentId);
	
	@Query(value = "SELECT " +
			"s.id," +
			"s.number," +
			"s.type_id," +
			"s.board_id," +
			"s.port," +
			"s.low_calibration," +
			"s.high_calibration," +
			"s.environment_id," +
			"s.delete," +
			"s.created_by," +
			"s.updated_by," +
			"s.created_ts," +
			"s.updated_ts" +
			" FROM sensor s " +
			" LEFT OUTER JOIN environment e ON s.environment_id = e.id " +
			" INNER JOIN sensor_type st ON st.id = s.type_id " +
			" WHERE st.type != 'Soil moisture'",
			nativeQuery = true)
	List<Sensor> fetchAllEnvironmentSensors();
}