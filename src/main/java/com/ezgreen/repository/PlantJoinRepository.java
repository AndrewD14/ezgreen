package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.PlantFullDetail;
import com.ezgreen.models.PlantJoin;

public interface PlantJoinRepository extends JpaRepository<PlantJoin, Long>
{	
	@Query("SELECT new com.ezgreen.models.PlantFullDetail(" +
			"p.id," +
			"p.name," +
			"p.number," +
			"p.highMoisture," +
			"p.lowMoisture," +
			"p.dateObtain," +
			"p.monitor," +
			"p.dead," +
			"p.delete," +
			"p.createBy," +
			"p.updateBy," +
			"p.createTs," +
			"p.updateTs," +
			"ps.size," +
			"s.id," +
			"s.type" +
        ")"
        + " FROM PlantJoin p "
        + " INNER JOIN PotSize ps ON p.potSize = ps.id "
        + " LEFT OUTER JOIN Sensor s ON p.sensor = s.id "
        + " WHERE p.id = :plantId")
	PlantFullDetail fetchPlantFullById(@Param("plantId") Long plantId);
	
	@Query("SELECT new com.ezgreen.models.PlantFullDetail(" +
			"p.id," +
			"p.name," +
			"p.number," +
			"p.highMoisture," +
			"p.lowMoisture," +
			"p.dateObtain," +
			"p.monitor," +
			"p.dead," +
			"p.delete," +
			"p.createBy," +
			"p.updateBy," +
			"p.createTs," +
			"p.updateTs," +
			"ps.size," +
			"s.id," +
			"s.type" +
        ")"
        + " FROM PlantJoin p "
        + " INNER JOIN PotSize ps ON p.potSize = ps.id "
        + " LEFT OUTER JOIN Sensor s ON p.sensor = s.id "
        + " ORDER BY p.updateTs DESC"
	) List<PlantFullDetail> fetchAllPlantFullDetails();
}