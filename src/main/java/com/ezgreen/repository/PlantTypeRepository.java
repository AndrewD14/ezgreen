package com.ezgreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.PlantType;

public interface PlantTypeRepository extends JpaRepository<PlantType, Long>
{
	@Query(value = "SELECT " +
			"pt.id," +
			"pt.name," +
			"pt.arduino," +
			"pt.created_by," +
			"pt.updated_by," +
			"pt.created_ts," +
			"pt.updated_ts" +
			" FROM plant_type pt " +
			" INNER JOIN plant p ON p.plant_type_id = pt.id " +
			" WHERE p.id = :plantId",
			nativeQuery = true)
	PlantType fetchPlantTypeWithPlantId(@Param("plantId") Long plantId);
}
