package com.ezgreen.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezgreen.models.Board;
import com.ezgreen.models.Environment;
import com.ezgreen.models.Relay;
import com.ezgreen.models.RelayType;
import com.ezgreen.repository.RelayRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.ezgreen.responses.MultipleDetailResponse;
import com.ezgreen.responses.SingleDetailResponse;
import com.ezgreen.service.BoardService;
import com.ezgreen.service.EnvironmentService;
import com.ezgreen.service.RelayService;
import com.ezgreen.service.RelayTypeService;

@RestController
@RequestMapping("/api/relay")
public class RelayController
{
	private BoardService boardService;
	private EnvironmentService environmentService;
	private RelayRepository relayRepository;
	private RelayService relayService;
	private RelayTypeService relayTypeService;

	public RelayController(BoardService boardService, EnvironmentService environmentService, RelayRepository relayRepository,
			RelayService relayService, RelayTypeService relayTypeService)
	{
		this.boardService = boardService;
		this.environmentService = environmentService;
		this.relayRepository = relayRepository;
		this.relayService = relayService;
		this.relayTypeService = relayTypeService;
	}
	
	@PutMapping("/environment/{id}")
	public ResponseEntity<?> addRelayEnvironment(@RequestBody String request, @PathVariable(value = "id") Long relayId)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());

		try
		{
			response = relayService.addRelayEnvironment(request, (long) relayId);
		}
		catch (IOException e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@PutMapping("/")
	public ResponseEntity<?> createRelay(@RequestBody String request)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());

		try
		{
			response = relayService.saveAndEditRelay(request, (long) 0);
		}
		catch (IOException e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}
		
		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editRelay(@RequestBody String request, @PathVariable(value = "id") Long relayId)
	{
		EZGreenResponse response = new EZGreenResponse();

		if (request == null || request.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.toString());
		
		try
		{
			response = relayService.saveAndEditRelay(request, (long) relayId);
		}
		catch (Exception e)
		{
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}

		return ResponseEntity.status(response.getStatusCode()).body(response.getResponseMessage());
	}
	
	@GetMapping(value="/{id}", produces = "application/json")
	public ResponseEntity<?> geRelayDetailById(@PathVariable(value = "id") Long relayId)
	{
		SingleDetailResponse response = new SingleDetailResponse();
		
		try
		{
			CompletableFuture<Board> board = boardService.fetchBoardWithRelay(relayId);
			CompletableFuture<Environment> environment = environmentService.fetchEnvironmentByRelay(relayId);
			CompletableFuture<Relay> relay = relayService.fetchRelayById(relayId);
			CompletableFuture<RelayType> relayType = relayTypeService.fetchRelayTypeByRelay(relayId);
			
			//Wait until they are all done
			CompletableFuture.allOf(
					board,
					environment,
					relay,
					relayType
			).join();
			
			response.setBoard(board.get());
			response.setEnvironment(environment.get());
			response.setRelay(relay.get());
			response.setRelayType(relayType.get());
			
			response.setResponseMessage("Pulled relay with details.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("geRelayDetailById error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/configoptions", produces = "application/json")
	public ResponseEntity<?> getRelayConfigOptions() throws Throwable
	{
		MultipleDetailResponse response = new MultipleDetailResponse();
		
		try
		{
			CompletableFuture<List<Board>> boards = boardService.fetchBoards();
			CompletableFuture<List<Relay>> relays = relayService.fetchAllRelays();
			CompletableFuture<List<RelayType>> relayTypes = relayTypeService.fetchAllRelayTypes();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					boards,
					relays,
					relayTypes
			).join();
			
			response.setBoards(boards.get());
			response.setRelays(relays.get());
			response.setRelayTypes(relayTypes.get());
			
			response.setResponseMessage("Pulled all relay config options.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("getRelayConfigOptions error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/alldetails", produces = "application/json")
	public ResponseEntity<?> getRelaysWithDetails() throws Throwable
	{
		MultipleDetailResponse response = new MultipleDetailResponse();
		
		try
		{
			CompletableFuture<List<Board>> boards = boardService.fetchBoards();
			CompletableFuture<List<Environment>> environments = environmentService.fetchAllEnvironments();
			CompletableFuture<List<Relay>> relays = relayService.fetchAllRelays();
			CompletableFuture<List<RelayType>> relayTypes = relayTypeService.fetchAllRelayTypes();
			
			//Wait until they are all done
			CompletableFuture.allOf(
					boards,
					environments,
					relays,
					relayTypes
			).join();
			
			response.setBoards(boards.get());
			response.setEnvironments(environments.get());
			response.setRelays(relays.get());
			response.setRelayTypes(relayTypes.get());
			
			response.setResponseMessage("Pulled all relays with details.");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("getRelaysWithDetails error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@GetMapping(value="/", produces = "application/json")
	public ResponseEntity<?> getRelays() throws Throwable
	{
		MultipleDetailResponse response = new MultipleDetailResponse();
		
		try
		{
			List<Relay> relays = relayRepository.findAll();
			
			response.setRelays(relays);
			
			response.setResponseMessage("Pulled all relays");
			response.setStatusCode(HttpStatus.OK);
		}
		catch (Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			response.setResponseMessage("getRelays error occur: " + e.getCause());
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
}
