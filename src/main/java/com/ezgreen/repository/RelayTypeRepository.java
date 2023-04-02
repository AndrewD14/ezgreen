package com.ezgreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.RelayType;

public interface RelayTypeRepository extends JpaRepository<RelayType, Long>
{
	@Query(value="SELECT " +
			" rt.id," +
			" rt.type," +
			" rt.arduino," +
			" rt.created_by," +
			" rt.updated_by," +
			" rt.created_ts," +
			" rt.updated_ts " +
			" FROM relay_type rt " +
			" INNER JOIN relay r ON rt.id = r.type_id " +
			" WHERE r.id = :relayId",
			nativeQuery = true)
	RelayType fetchRelayTypeByRelay(@Param("relayId") Long relayId);
	
	@Query(value="SELECT " +
			" rt.id," +
			" rt.type," +
			" rt.arduino," +
			" rt.created_by," +
			" rt.updated_by," +
			" rt.created_ts," +
			" rt.updated_ts " +
			" FROM relay_type rt " +
			" INNER JOIN relay r ON rt.id = r.type_id " +
			" INNER JOIN plant p ON r.id = p.valve_id " +
			" WHERE p.id = :plantId",
			nativeQuery = true)
	RelayType fetchRelayTypeByPlant(@Param("plantId") Long plantId);
}
