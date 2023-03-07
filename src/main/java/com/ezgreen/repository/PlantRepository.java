package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.Plant;

public interface PlantRepository extends JpaRepository<Plant, Integer>
{
	@Query("SELECT new com.ezgreen.models.Plant(" +
			"id," +
			"name," +
			"number," +
			"potSizeId," +
			"highMoisture," +
			"lowMoisture," +
			"sensorId," +
			"dateObtain," +
			"dead," +
			"delete," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM plant " +
			"ORDER BY updateTs DESC")
	List<Plant> fetchAllPlants();
}
