package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.Plant;

public interface PlantRepository extends JpaRepository<Plant, Long>
{
	@Query("SELECT p FROM Plant p WHERE p.id = :plantId")
	Plant fetchPlantById(@Param("plantId") Long plantId);
	
	@Query("SELECT p FROM Plant p WHERE p.sensorId = :sensorId")
	Plant fetchPlantBySensorId(@Param("sensorId") Long sensorId);
	
	@Query("SELECT new com.ezgreen.models.Plant(" +
			"id," +
			"name," +
			"number," +
			"potSizeId," +
			"plantTypeId," +
			"highMoisture," +
			"lowMoisture," +
			"sensorId," +
			"dateObtain," +
			"monitor," +
			"dead," +
			"delete," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM Plant " +
			"ORDER BY updateTs DESC")
	List<Plant> fetchAllPlants();
	
	@Query("SELECT new com.ezgreen.models.Plant(" +
			"id," +
			"name," +
			"number," +
			"potSizeId," +
			"plantTypeId," +
			"highMoisture," +
			"lowMoisture," +
			"sensorId," +
			"dateObtain," +
			"monitor," +
			"dead," +
			"delete," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM Plant " +
			" WHERE delete != 1 " +
			" ORDER BY updateTs DESC")
	List<Plant> fetchAllNonDeletedPlants();
	
	List<Plant> findBySensorIdIsNotNull();
}
