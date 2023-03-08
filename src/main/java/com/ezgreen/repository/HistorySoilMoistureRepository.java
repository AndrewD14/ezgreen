package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.HistorySoilMoisture;

public interface HistorySoilMoistureRepository  extends JpaRepository<HistorySoilMoisture, Long>
{
	@Query("SELECT new com.ezgreen.models.HistorySoilMoisture(" +
			"id," +
			"plantId," +
			"read," +
			"volt," +
			"lowCalibration," +
			"highCalibration," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM HistorySoilMoisture " +
			"ORDER BY updateTs DESC")
	List<HistorySoilMoisture> fetchAllHistorySoilMoistures();
}
