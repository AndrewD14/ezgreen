package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.HistoryWaterLevel;

public interface HistoryWaterLevelRepository extends JpaRepository<HistoryWaterLevel, Long>
{
	@Query("SELECT new com.ezgreen.models.HistoryWaterLevel(" +
			"id," +
			"read," +
			"cm," +
			"lowCalibration," +
			"highCalibration," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM HistoryWaterLevel " +
			"ORDER BY updateTs DESC")
	List<HistoryWaterLevel> fetchAllHistoryWaterLevels();
}
