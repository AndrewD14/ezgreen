package com.ezgreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.SensorType;

public interface SensorTypeRepository extends JpaRepository<SensorType, Long>
{
	@Query(value = "SELECT " +
			"st.id," +
			"st.type," +
			"st.arduino," +
			"st.created_by," +
			"st.updated_by," +
			"st.created_ts," +
			"st.updated_ts" +
			" FROM sensor_type st " +
			" INNER JOIN sensor s ON s.type_id = st.id " +
			" INNER JOIN plant p ON p.sensor_id = s.id " +
			" WHERE p.id = :plantId",
			nativeQuery = true)
	SensorType fetchSensorTypeWithPlantId(@Param("plantId") Long plantId);
	
	@Query(value = "SELECT " +
			"st.id," +
			"st.type," +
			"st.arduino," +
			"st.created_by," +
			"st.updated_by," +
			"st.created_ts," +
			"st.updated_ts" +
			" FROM sensor_type st " +
			" INNER JOIN sensor s ON s.type_id = st.id " +
			" WHERE s.id = :sensorId",
			nativeQuery = true)
	SensorType fetchSensorTypeWithSensorId(@Param("sensorId") Long sensorId);
}
