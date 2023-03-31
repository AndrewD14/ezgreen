package com.ezgreen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezgreen.models.Board;

public interface BoardRepository extends JpaRepository<Board, Long>
{
	@Query(value = "SELECT " +
			"b.id," +
			"b.bus," +
			"b.number" +
			" FROM board b " +
			" INNER JOIN sensor s ON s.board_id = b.id " +
			" WHERE s.id = :sensorId",
			nativeQuery = true)
	Board fetchBoardWithSensorId(@Param("sensorId") Long sensorId);
}
