package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.PotSize;

public interface PotSizeRepository extends JpaRepository<PotSize, Long>
{
	@Query("SELECT new com.ezgreen.models.PotSize(" +
			"id," +
			"name," +
			"size," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM PotSize " +
			"ORDER BY updateTs DESC")
	List<PotSize> fetchAllPotSizes();
	
	@Query(value = "SELECT " +
			"ps.id," +
			"ps.name," +
			"ps.size," +
			"ps.created_by," +
			"ps.updated_by," +
			"ps.created_ts," +
			"ps.updated_ts" +
			" FROM pot_size ps " +
			" INNER JOIN plant p ON p.pot_size_id = ps.id " +
			" WHERE p.id = :plantId",
			nativeQuery = true)
	PotSize fetchPotSizeWithPlantId(@Param("plantId") Long plantId);
}