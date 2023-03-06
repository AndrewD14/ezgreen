package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.HistoryHumidity;

public interface HistoryHumidityRepository extends JpaRepository<HistoryHumidity, Integer>
{
	@Query("SELECT new com.ezgreen.models.HistoryHumidity(" +
			"id," +
			"read," +
			"humidity," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM history_humidity " +
			"ORDER BY updateTs DESC")
	List<HistoryHumidity> fetchAllHistoryHumidities();
}
