package com.ezgreen.connection;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ezgreen.controller.WebsocketController;
import com.ezgreen.models.HistorySoilMoisture;
import com.ezgreen.models.Plant;
import com.ezgreen.models.Sensor;
import com.ezgreen.repository.HistorySoilMoistureRepository;
import com.ezgreen.repository.PlantRepository;
import com.ezgreen.repository.SensorRepository;
import com.ezgreen.responses.EZGreenResponse;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

@Component
public class ArduinoListener implements SerialPortDataListener
{
	private WebsocketController websocket;
	private HistorySoilMoistureRepository historySoilMoistureRepository;
	private PlantRepository plantRepository;
	private SensorRepository sensorRepository;
	
	private SerialPort port;
	private List<EZGreenResponse> responses;
	
	public ArduinoListener(WebsocketController websocket, HistorySoilMoistureRepository historySoilMoistureRepository,
			PlantRepository plantRepository, SensorRepository sensorRepository)
	{
		this.websocket = websocket;
		this.historySoilMoistureRepository = historySoilMoistureRepository;
		this.plantRepository = plantRepository;
		this.sensorRepository = sensorRepository;
		
		responses = new ArrayList<EZGreenResponse>();
	}
	
	@Override
	public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
	   
	@Override
	synchronized public void serialEvent(SerialPortEvent event)
	{
		if(event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;
		String value = "";
		int bytesAvailable = port.bytesAvailable();
		
		byte[] buffer = new byte[bytesAvailable];
		System.out.println("Bytes available: " + bytesAvailable);
		
		int bytesRead = port.readBytes(buffer, buffer.length);
		System.out.println("Bytes read: " + bytesRead);
		value = (new String(buffer, StandardCharsets.UTF_8)).trim();
		
		System.out.println("Arduino response: " + value);
		
	    String[] data = value.split(";");

	    if(data[0].equals("cs")) sendSoilMoistureCalibration(data);
	    else if(data[0].equals("pm")) sendSoilMoistureUpdate(data);
//	    else
//	    {
//	    	System.out.println("Arduino response: " + value);
//	    }
	}
	
	public List<EZGreenResponse> getResponses()
	{
		return responses;
	}
	
	public void setPort(SerialPort port)
	{
		this.port = port;
	}
	
	public int addResponse(EZGreenResponse response)
	{
		responses.add(response);
		
		return responses.size() - 1;
	}
	
	public void sendSoilMoistureCalibration(String[] data)
	{
		EZGreenResponse response = null;
    	
    	try
	    {	    		
    		int idx = Integer.parseInt(data[2].trim());

		    response = responses.get(idx);
	    	
	    	responses.remove(idx);
	    	
	    	response.setResponseMessage("{\"responseMessage\": " + (Double.parseDouble(data[1].trim()) / 100.00) + "}");
	    	response.setStatusCode(HttpStatus.OK);
	    }
	    catch(Exception e)
		{
	    	System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
			
			if(response != null)
			{
				response.setResponseMessage("Error formatting message from Arduino.");
				response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	synchronized public void sendSoilMoistureUpdate(String[] data)
	{
		HistorySoilMoisture hsm = new HistorySoilMoisture();
		
		long id = Long.parseLong(data[1]); //plant id
		Double voltage = Double.valueOf(data[2]) / 100; //soil moisture (volts) (int)
		//int waterAttempts = Integer.parseInt(data[3]); //how many water attempts (int)
		LocalDateTime timestamp = LocalDateTime.ofEpochSecond(Long.parseLong(data[4]), 0, ZoneOffset.UTC); //unix time
		LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
		
		try
		{
			Plant plant = plantRepository.fetchPlantById(id);
			System.out.println("Plant ID: " + plant.getId());
			System.out.println("Sensor ID: " + plant.getSensorId());
			Sensor sensor = sensorRepository.fetchById(plant.getSensorId());
			
			Double percentage = (sensor.getLowCalibration() - voltage) / (sensor.getLowCalibration() - sensor.getHighCalibration()) * 100;

			hsm.setHighCalibration(sensor.getHighCalibration());
			hsm.setHighDesire(plant.getHighMoisture());
			hsm.setLowCalibration(sensor.getLowCalibration());
			hsm.setLowDesire(plant.getLowMoisture());
			hsm.setPercentage(percentage);
			hsm.setPlantId(id);
			hsm.setRead(timestamp);
			hsm.setSensorId(sensor.getId());
			hsm.setVolt(voltage);
			hsm.setCreateBy("system");
			hsm.setCreateTs(now);
			hsm.setUpdateBy("system");
			hsm.setUpdateTs(now);
			
			historySoilMoistureRepository.save(hsm);

			websocket.sendSpecificPlant(id);
		}
		catch(Exception e)
		{
			System.out.println("Error!!! " + e.getMessage());
			System.out.println("Error!!! " + e.getCause());
		}
	}
}
