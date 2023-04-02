package util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ezgreen.models.Board;
import com.ezgreen.models.Environment;
import com.ezgreen.models.Plant;
import com.ezgreen.models.PlantType;
import com.ezgreen.models.PotSize;
import com.ezgreen.models.Relay;
import com.ezgreen.models.Sensor;

@Component
public class ArduinoCommand
{
	private static DateTimeFormatter lightFormatter = DateTimeFormatter.ofPattern("hh;mm");
	private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("YYYY;MM;DD;hh;mm;ss");
	
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
	
//	public static String sendPlant(Plant p, PlantType plantType, PotSize potSize, )
//	{
//		String command = "ap;";
//		
//		command = command + 
//		command = command + "\n";
//		
//		return command;
//	}
	
	public static String removePlant(Plant plant)
	{
		String command = "rp;";
		
		command = command + plant.getId() + "\n";
		
		return command;
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
	
	public static String removeFan(Relay relay)
	{
		String command = "rF;";
		
		command = command + relay.getId() + "\n";
		
		return command;
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
	
	public static String removeHeater(Relay relay)
	{
		String command = "rT;";
		
		command = command + relay.getId() + "\n";
		
		return command;
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
	
	public static String removeHumidifier(Relay relay)
	{
		String command = "rH;";
		
		command = command + relay.getId() + "\n";
		
		return command;
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
	
	public static String removeHumidityTemparature(Sensor sensor)
	{
		String command = "rh;";
		
		command = command + sensor.getId() + "\n";
		
		return command;
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
	
	public static String removeLamp(Relay relay)
	{
		String command = "rL;";
		
		command = command + relay.getId() + "\n";
		
		return command;
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
	
	public static String removeLight(Sensor sensor)
	{
		String command = "rl;";
		
		command = command + sensor.getId() + "\n";
		
		return command;
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
	
	public static String removeWaterLevel(Sensor sensor)
	{
		String command = "re;";
		
		command = command + sensor.getId() + "\n";
		
		return command;
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
	
	public static String removeWaterPump(Relay relay)
	{
		String command = "rW;";
		
		command = command + relay.getId() + "\n";
		
		return command;
	}
	
	public static String sendTime()
	{
		LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC/Greenwich"));
		String command = "ts;";
		
		command = command + now.format(timeFormatter) + "\n";

		return command;
	}
}
