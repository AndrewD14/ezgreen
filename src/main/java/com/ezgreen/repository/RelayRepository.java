package com.ezgreen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.Relay;

public interface RelayRepository extends JpaRepository<Relay, Long>
{
	@Query("SELECT r FROM Relay r WHERE r.environmentId = :environmentId")
	List<Relay> fetchRelaysByEnvironmentId(@Param("environmentId") Long environmentId);
}
