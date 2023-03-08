package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.HistoryTemp;

public interface HistoryTempRepository extends JpaRepository<HistoryTemp, Long>
{
	@Query("SELECT new com.ezgreen.models.HistoryTemp(" +
			"id," +
			"read," +
			"temp," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM HistoryTemp " +
			"ORDER BY updateTs DESC")
	List<HistoryTemp> fetchAllHistoryTemps();
}
