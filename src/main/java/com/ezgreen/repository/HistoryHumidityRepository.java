package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.HistoryHumidity;

public interface HistoryHumidityRepository extends JpaRepository<HistoryHumidity, Long>
{
	@Query("SELECT new com.ezgreen.models.HistoryHumidity(" +
			"id," +
			"read," +
			"humidity," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM HistoryHumidity " +
			"ORDER BY updateTs DESC")
	List<HistoryHumidity> fetchAllHistoryHumidities();
}
