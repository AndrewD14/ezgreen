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
	
	List<Relay> findByEnvironmentIdIsNotNull();
	
	@Query(value="SELECT " +
			"r.id," +
			"r.number," +
			"r.type_id," +
			"r.board_id," +
			"r.relay," +
			"r.environment_id," +
			"r.delete," +
			"r.created_by," +
			"r.updated_by," +
			"r.created_ts," +
			"r.updated_ts" +
			" FROM relay r " +
			" INNER JOIN plant p ON p.valve_id = r.id " +
			" WHERE p.id = :plantId",
			nativeQuery = true)
	Relay fetchRelayByPlantId(@Param("plantId") Long plantId);
	
	@Query(value="SELECT " +
			"r.id," +
			"r.number," +
			"r.type_id," +
			"r.board_id," +
			"r.relay," +
			"r.environment_id," +
			"r.delete," +
			"r.created_by," +
			"r.updated_by," +
			"r.created_ts," +
			"r.updated_ts" +
			" FROM relay r " +
			" INNER JOIN environment e ON r.environment_id = e.id " +
			" INNER JOIN plant p ON p.environment_id = e.id " +
			" INNER JOIN relay_type rt ON r.type_id = rt.id " +
			" WHERE rt.arduino = 'W' " +
			" AND p.id = :plantId",
			nativeQuery = true)
	Relay fetchWaterPumpByPlantId(@Param("plantId") Long plantId);
	
	@Query(value="SELECT " +
			"r.id," +
			"r.number," +
			"r.type_id," +
			"r.board_id," +
			"r.relay," +
			"r.environment_id," +
			"r.delete," +
			"r.created_by," +
			"r.updated_by," +
			"r.created_ts," +
			"r.updated_ts" +
			" FROM relay r " +
			" INNER JOIN relay_type rt ON r.type_id = rt.id " +
			" WHERE rt.arduino = 'V'",
			nativeQuery = true)
	List<Relay> fetchPlantRelays();
}
