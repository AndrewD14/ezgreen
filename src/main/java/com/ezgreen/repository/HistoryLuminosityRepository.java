package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.HistoryLuminosity;

public interface HistoryLuminosityRepository extends JpaRepository<HistoryLuminosity, Integer>
{
	@Query("SELECT new com.ezgreen.models.HistoryLuminosity(" +
			"id," +
			"read," +
			"lux," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM HistoryLuminosity " +
			"ORDER BY updateTs DESC")
	List<HistoryLuminosity> fetchAllHistoryLuminosities();
}
