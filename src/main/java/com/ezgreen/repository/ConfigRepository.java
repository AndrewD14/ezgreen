package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezgreen.models.Config;

public interface ConfigRepository extends JpaRepository<Config, Integer>
{
	@Query("SELECT new com.ezgreen.models.Config(" +
			"id," +
			"name," +
			"createBy," +
			"updateBy," +
			"createTs," +
			"updateTs" +
			") FROM Config " +
			"ORDER BY updateTs DESC")
	List<Config> fetchAllConfigs();
}
