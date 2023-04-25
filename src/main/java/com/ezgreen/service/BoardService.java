package com.ezgreen.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ezgreen.models.Board;
import com.ezgreen.repository.BoardRepository;

@Service
public class BoardService
{
	@Autowired
	private BoardRepository boardRepository;
	
	@Async
	public CompletableFuture<List<Board>> fetchBoards()
	{
		List<Board> boards = boardRepository.findAll();
		
		return CompletableFuture.completedFuture(boards);
	}
	
	@Async
	public CompletableFuture<Board> fetchBoardWithSensor(Long sensorId)
	{
		Board board = boardRepository.fetchBoardWithSensorId(sensorId);
		
		return CompletableFuture.completedFuture(board);
	}
	
	@Async
	public CompletableFuture<Board> fetchBoardWithRelay(Long relayId)
	{
		Board board = boardRepository.fetchBoardWithRelayId(relayId);
		
		return CompletableFuture.completedFuture(board);
	}
	
	@Async
	public CompletableFuture<Board> fetchById(Long boardId)
	{
		Board board = boardRepository.findById(boardId).get();
		
		return CompletableFuture.completedFuture(board);
	}
}
