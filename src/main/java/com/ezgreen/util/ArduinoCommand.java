package com.ezgreen.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;

import com.ezgreen.connection.Arduino;
import com.ezgreen.models.Board;
import com.ezgreen.models.Environment;
import com.ezgreen.models.Plant;
import com.ezgreen.models.PlantType;
import com.ezgreen.models.PotSize;
import com.ezgreen.models.Relay;
import com.ezgreen.models.RelayType;
import com.ezgreen.models.Sensor;
import com.ezgreen.repository.BoardRepository;
import com.ezgreen.repository.EnvironmentRepository;
import com.ezgreen.repository.PlantRepository;
import com.ezgreen.repository.RelayRepository;
import com.ezgreen.repository.RelayTypeRepository;
import com.ezgreen.service.BoardService;
import com.ezgreen.service.EnvironmentService;
import com.ezgreen.service.PlantService;
import com.ezgreen.service.PlantTypeService;
import com.ezgreen.service.PotSizeService;
import com.ezgreen.service.RelayService;
import com.ezgreen.service.RelayTypeService;
import com.ezgreen.service.SensorService;

@Component
public class ArduinoCommand
{
	private Arduino arduino;
	private BoardRepository boardRepository;
	private BoardService boardService;
	private EnvironmentRepository environmentRepository;
	private EnvironmentService environmentService;
	private PlantRepository plantRepository;
	private PlantService plantService;
	private PlantTypeService plantTypeService;
	private PotSizeService potSizeService;
	private SensorService sensorService;
	private RelayRepository relayRepository;
	private RelayService relayService;
	private RelayTypeService relayTypeService;
	
	private static DateTimeFormatter lightFormatter = DateTimeFormatter.ofPattern("hh;mm");
	private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("YYYY;MM;dd;hh;mm;ss");
	
	public ArduinoCommand(Arduino arduino, BoardRepository boardRepository, BoardService boardService,
			EnvironmentRepository environmentRepository, EnvironmentService environmentService,
			PlantRepository plantRepository, PlantService plantService, PlantTypeService plantTypeService, PotSizeService potSizeService,
			SensorService sensorService, RelayService relayService, RelayRepository relayRepository,
			RelayTypeService relayTypeService)
	{
		this.arduino = arduino;
		this.boardRepository = boardRepository;
		this.boardService = boardService;
		this.environmentService = environmentService;
		this.environmentRepository = environmentRepository;
		this.plantRepository = plantRepository;
		this.plantService = plantService;
		this.plantTypeService = plantTypeService;
		this.potSizeService = potSizeService;
		this.sensorService = sensorService;
		this.relayService = relayService;
		this.relayRepository = relayRepository;
		this.relayTypeService = relayTypeService;
	}
	
	public boolean checkArduino()
	{
		return arduino.checkGood();
	}
	
	public void init() throws Exception
	{
		String command = sendTime();
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
		
		initBoardCount();
		initEnvironments();
		initRelays();
		initPlants();
		
		System.out.print("done\n");
		if(arduino.checkGood()) arduino.write("done\n");
		else System.out.println("Arduino port is not working.");
	}
	
	public void initPlants() throws Exception
	{
		List<Plant> plants = plantRepository.fetchAllNonDeletedPlants();
		Iterator<Plant> itr = plants.iterator();
		
		while(itr.hasNext())
		{
			Plant plant = itr.next();
			
			processPlant(plant);
		}
	}
	
	public void initBoardCount() throws Exception
	{
		List<Board> boards = boardRepository.findAll();
		
		String command = sendBoardCount(boards);
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public void initEnvironments() throws Exception
	{
		List<Environment> environments = environmentRepository.findAll();
		Iterator<Environment> itr = environments.iterator();
		
		processEnvironmentCount(environments);
		
		while(itr.hasNext())
		{
			Environment environment = itr.next();
			
			processEnvironment(environment);
		}
	}
	
	public void initRelays() throws Exception
	{
		List<Relay> relays = relayRepository.findByEnvironmentIdIsNotNull();
		Iterator<Relay> itr = relays.iterator();
		
		while(itr.hasNext())
		{
			Relay relay = itr.next();
			
			processRelay(relay);
		}
	}
	
	public void processEnvironmentCount(List<Environment> environments) throws Exception
	{
		String command = sendEnvrionmentCount(environments);
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public void processPlant(Plant plant) throws Exception
	{	
		if(plant.getMonitor() == 0) return;
		
		PlantType plantType;
		PotSize potSize;
		Board sensorBoard;
		Sensor sensor;
		Relay waterValve;
		Board waterValveBoard;
		Relay waterPump;
		
		CompletableFuture<PlantType> plantTypeJob = plantTypeService.fetchPlantTypeById(plant.getPlantTypeId());
		CompletableFuture<PotSize> potSizeJob = potSizeService.fetchPotSizeById(plant.getPlantTypeId());
		CompletableFuture<Sensor> sensorJob = sensorService.fetchSensorWithId(plant.getSensorId());
		CompletableFuture<Relay> waterValveJob = relayService.fetchRelayById(plant.getValveId());
		CompletableFuture<Relay> waterPumpJob = relayService.fetchWaterPumpByPlant(plant.getId());
		
		CompletableFuture.allOf(
				plantTypeJob,
				potSizeJob,
				sensorJob,
				waterValveJob,
				waterPumpJob
		).join();
		
		plantType = plantTypeJob.get();
		potSize = potSizeJob.get();
		sensor = sensorJob.get();
		waterValve = waterValveJob.get();
		waterPump = waterPumpJob.get();
		
		CompletableFuture<Board> sensorBoardJob = boardService.fetchById(sensor.getBoardId());
		CompletableFuture<Board> waterValveBoardJob = boardService.fetchById(waterValve.getBoardId());
		
		CompletableFuture.allOf(
				sensorBoardJob,
				waterValveBoardJob
		).join();
		
		sensorBoard = sensorBoardJob.get();
		waterValveBoard = waterValveBoardJob.get();
		
		String command = sendPlant(plant, plantType, potSize, sensorBoard, sensor, waterValve, waterValveBoard, waterPump);
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public void processRelay(Relay relay) throws Exception
	{
		Board board;
		Environment environment;
		RelayType relayType;
		String command;			
		
		CompletableFuture<Board> boardJob = boardService.fetchById(relay.getBoardId());
		CompletableFuture<Environment> environmentJob = environmentService.fetchById(relay.getEnvironmentId());
		CompletableFuture<RelayType> relayTypeJob = relayTypeService.fetchById(relay.getTypeId());
		
		CompletableFuture.allOf(
				boardJob,
				environmentJob,
				relayTypeJob
		).join();
		
		board = boardJob.get();
		environment = environmentJob.get();
		relayType = relayTypeJob.get();
		
		if(relayType.getArduino().equals("T"))
		{
			command = sendHeater(relay, environment, board);
			
			System.out.print(command);
			if(arduino.checkGood()) arduino.write(command);
			else System.out.println("Arduino port is not working.");
		}
		else if(relayType.getArduino().equals("F"))
		{
			command = sendFan(relay, environment, board);
			
			System.out.print(command);
			if(arduino.checkGood()) arduino.write(command);
			else System.out.println("Arduino port is not working.");
		}
		else if(relayType.getArduino().equals("H"))
		{
			command = sendHumidifier(relay, environment, board);
			
			System.out.print(command);
			if(arduino.checkGood()) arduino.write(command);
			else System.out.println("Arduino port is not working.");
		}
		else if(relayType.getArduino().equals("L"))
		{
			command = sendLamp(relay, environment, board);
			
			System.out.print(command);
			if(arduino.checkGood()) arduino.write(command);
			else System.out.println("Arduino port is not working.");
		}
		else if(relayType.getArduino().equals("W"))
		{
			command = sendWaterPump(relay, environment, board);
			
			System.out.print(command);
			if(arduino.checkGood()) arduino.write(command);
			else System.out.println("Arduino port is not working.");
		}
	}
	
	public void processEnvironment(Environment environment) throws Exception
	{
		String command;
		
		if(environment.getSensorType() == 2)
		{
			command = sendDesireHumidity(environment);
			
			System.out.print(command);
			if(arduino.checkGood()) arduino.write(command);
			else System.out.println("Arduino port is not working.");
			
			command = sendTempSetting(environment);
			
			System.out.print(command);
			if(arduino.checkGood()) arduino.write(command);
			else System.out.println("Arduino port is not working.");
		}
		else if(environment.getSensorType() == 3)
		{
			command = sendLampSetting(environment);
			
			System.out.print(command);
			if(arduino.checkGood()) arduino.write(command);
			else System.out.println("Arduino port is not working.");
		}
	}
	
	public static String sendBoardCount(List<Board> boards)
	{
		String command = "bc;";
		int bus = 0;
		int count = 0;
		
		Iterator<Board> itr = boards.iterator();
		
		while(itr.hasNext())
		{
			Board b = itr.next();
			
			if(bus == 0) bus = b.getBus();
			else if(bus != b.getBus())
			{
				command = command + count + ";";
				
				count = 0;
				bus = b.getBus();
			}
			
			count++;
		}
		
		command = command + count + "\n";
		
		return command;
	}
	
	public static String sendEnvrionmentCount(List<Environment> environments)
	{
		String command = "zn;";
		
		command = command + environments.size() + "\n";
		
		return command;
	}
	
	public static String sendLampSetting(Environment environment)
	{
		String command = "sL;";
		
		command = command + environment.getId() + ";";
		command = command + environment.getTimeStart().format(lightFormatter) + ";";
		command = command + environment.getTimeEnd().format(lightFormatter) + "\n";
		
		return command;
	}
	
	public static String sendDesireHumidity(Environment environment)
	{
		String command = "sH;";
		
		command = command + environment.getId() + ";";
		command = command + environment.getHighDesire() + "\n";
		
		return command;
	}
	
	public static String sendTempSetting(Environment environment)
	{
		String command = "sT;";
		
		command = command + environment.getId() + ";";
		command = command + environment.getTarget() + ";";
		command = command + environment.getHighDesire() + ";";
		command = command + environment.getLowDesire() + "\n";
		
		return command;
	}
	
	public static String sendPlant(Plant plant, PlantType plantType, PotSize potSize, Board board, Sensor sensor,
			Relay relay, Board relayBoard, Relay waterPump)
	{
		String command = "ap;";
		
		command = command + formatPlant(plant, plantType, potSize, board, sensor, relay, relayBoard, waterPump);
		
		return command;
	}
	
	public static String updatePlant(Plant plant, PlantType plantType, PotSize potSize, Board board, Sensor sensor,
			Relay relay, Board relayBoard, Relay waterPump)
	{
		String command = "up;";
		
		command = command + formatPlant(plant, plantType, potSize, board, sensor, relay, relayBoard, waterPump);
		
		return command;
	}
	
	private static String formatPlant(Plant plant, PlantType plantType, PotSize potSize, Board board, Sensor sensor,
			Relay relay, Board relayBoard, Relay waterPump)
	{
		String command = "";
		
		Double desireLowVolt = sensor.getLowCalibration() - plant.getLowMoisture()/100.00 * (sensor.getLowCalibration() - sensor.getHighCalibration());
		Double desireHighVolt = sensor.getLowCalibration() - plant.getHighMoisture()/100.00 * (sensor.getLowCalibration() - sensor.getHighCalibration());
		
		command = command + plant.getId() + ";";
		command = command + board.getBus() + ";";
		command = command + board.getNumber() + ";";
		command = command + sensor.getPort() + ";";
		command = command + sensor.getLowCalibration() + ";";
		command = command + sensor.getHighCalibration() + ";";
		command = command + desireLowVolt + ";";
		command = command + desireHighVolt + ";";
		command = command + potSize.getId() + ";";
		command = command + plantType.getArduino() + ";";
		command = command + relay.getRelay() + ";";
		command = command + relayBoard.getNumber() + ";";
		command = command + waterPump.getId() + "\n";
		
		return command;
	}
	
	public void removePlant(Plant plant) throws Exception
	{
		String command = "rp;";
		
		command = command + plant.getId() + "\n";
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public static String sendFan(Relay relay, Environment environment, Board board)
	{
		String command = "aF;";
		
		command = command + relay.getId() + ";";
		command = command + environment.getId() + ";";
		command = command + board.getBus() + ";";
		command = command + relay.getRelay() + ";";
		command = command + board.getNumber() + "\n";
		
		return command;
	}
	
	public void removeFan(Relay relay) throws Exception
	{
		String command = "rF;";
		
		command = command + relay.getId() + "\n";
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public static String sendHeater(Relay relay, Environment environment, Board board)
	{
		String command = "aT;";
		
		command = command + relay.getId() + ";";
		command = command + environment.getId() + ";";
		command = command + board.getBus() + ";";
		command = command + relay.getRelay() + ";";
		command = command + board.getNumber() + "\n";
		
		return command;
	}
	
	public void removeHeater(Relay relay) throws Exception
	{
		String command = "rT;";
		
		command = command + relay.getId() + "\n";
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public static String sendHumidifier(Relay relay, Environment environment, Board board)
	{
		String command = "aH;";
		
		command = command + relay.getId() + ";";
		command = command + environment.getId() + ";";
		command = command + board.getBus() + ";";
		command = command + relay.getRelay() + ";";
		command = command + board.getNumber() + "\n";
		
		return command;
	}
	
	public void removeHumidifier(Relay relay) throws Exception
	{
		String command = "rH;";
		
		command = command + relay.getId() + "\n";
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public static String sendHumidityTemparature(Sensor sensor, Environment environment, Board board)
	{
		String command = "ah;";
		
		command = command + sensor.getId() + ";";
		command = command + environment.getId() + ";";
		command = command + board.getBus() + ";";
		command = command + board.getNumber() + ";";
		command = command + sensor.getPort() + "\n";
		
		return command;
	}
	
	public void removeHumidityTemparature(Sensor sensor) throws Exception
	{
		String command = "rh;";
		
		command = command + sensor.getId() + "\n";
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public static String sendLamp(Relay relay, Environment environment, Board board)
	{
		String command = "aL;";
		
		command = command + relay.getId() + ";";
		command = command + environment.getId() + ";";
		command = command + board.getBus() + ";";
		command = command + relay.getRelay() + ";";
		command = command + board.getNumber() + "\n";
		
		return command;
	}
	
	public void removeLamp(Relay relay) throws Exception
	{
		String command = "rL;";
		
		command = command + relay.getId() + "\n";
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public static String sendLight(Sensor sensor, Environment environment, Board board)
	{
		String command = "al;";
		
		command = command + sensor.getId() + ";";
		command = command + environment.getId() + ";";
		command = command + board.getBus() + ";";
		command = command + board.getNumber() + ";";
		command = command + sensor.getPort() + "\n";
		
		return command;
	}
	
	public void removeLight(Sensor sensor) throws Exception
	{
		String command = "rl;";
		
		command = command + sensor.getId() + "\n";
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public static String sendWaterLevel(Sensor sensor, Environment environment, Board board)
	{
		String command = "aw;";
		
		command = command + sensor.getId() + ";";
		command = command + environment.getId() + ";";
		command = command + board.getBus() + ";";
		command = command + board.getNumber() + ";";
		command = command + sensor.getPort() + "\n";
		
		return command;
	}
	
	public void removeWaterLevel(Sensor sensor) throws Exception
	{
		String command = "re;";
		
		command = command + sensor.getId() + "\n";
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public static String sendWaterPump(Relay relay, Environment environment, Board board)
	{
		String command = "aW;";
		
		command = command + relay.getId() + ";";
		command = command + environment.getId() + ";";
		command = command + board.getBus() + ";";
		command = command + relay.getRelay() + ";";
		command = command + board.getNumber() + "\n";
		
		return command;
	}
	
	public void removeWaterPump(Relay relay) throws Exception
	{
		String command = "rW;";
		
		command = command + relay.getId() + "\n";
		
		System.out.print(command);
		if(arduino.checkGood()) arduino.write(command);
		else System.out.println("Arduino port is not working.");
	}
	
	public static String sendTime()
	{
		LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
		String command = "ts;";
		
		command = command + now.format(timeFormatter) + "\n";

		return command;
	}
}
