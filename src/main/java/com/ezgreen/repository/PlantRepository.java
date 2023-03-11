package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.Plant;

public interface PlantRepository extends JpaRepository<Plant, Long>
{
	@Query("SELECT plant FROM Plant plant WHERE plant.id = :plantId")
	Plant fetchPlantById(@Param("plantId") Long plantId);
	
	@Query("SELECT new com.ezgreen.models.Plant(" +
			"id," +
			"name," +
			"number," +
			"potSize," +
			"highMoisture," +
			"lowMoisture," +
			"sensor," +
			"dateObtain," +
			"monitor," +
			"dead," +
			"delete," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM PlantJoin " +
			"ORDER BY updateTs DESC")
	List<Plant> fetchAllPlants();
}
