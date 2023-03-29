package com.ezgreen.connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Arduino
{
	@Value("${arduino.serial.port}")
	private String commPort;
	
	private SerialPort port;
	private List<HttpServletResponse> responses;
	
	public Arduino()
	{
		responses = new ArrayList<HttpServletResponse>();
	}
	
	@PostConstruct
	public void open() throws InterruptedException
	{
		if(port == null)
		{
			System.out.println("Using comm port: " + commPort);
			
			port = SerialPort.getCommPort(commPort);
			
			port.openPort();
			port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
			port.setComPortParameters(115200, 8, 1, 0);
			Thread.sleep(1000);			
			
			port.addDataListener(new SerialPortDataListener()
			{
				@Override
				public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
				   
				@Override
				public void serialEvent(SerialPortEvent event)
				{
					if(event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;
				      
					byte[] newData = new byte[port.bytesAvailable()];
				    
					port.readBytes(newData, newData.length);
				    
				    String[] data = new String(newData, StandardCharsets.UTF_8).split(";");
				    
				    HttpServletResponse response = responses.get(Integer.parseInt(data[1]));
				    response.setStatus(getListeningEvents());
				    
				    try
				    {
				    	response.getWriter().println(data[0]);
				    }
				    catch(Exception e)
					{
				    	response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
						
						try
						{
							response.getWriter().println("getSensorCalibration error occur: " + e.getCause());
						}
						catch(Exception error)
						{
							System.out.println("Error sending error to http response: " + error.getCause());
						}
					}
				    
				    responses.remove(Integer.parseInt(data[1]));
				}
			});
			
			/*
			port.addDataListener(new SerialPortDataListener() {				   
				   @Override
				   public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_WRITTEN; }
				   
				   @Override
				   public void serialEvent(SerialPortEvent event)
				   {
				      if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_WRITTEN)
				         System.out.println("All bytes were successfully transmitted!");
				   }
			});
			*/
			//port.addDataListener(new ArduinoListener());
		}	
	}
	
	public void close()
	{
		if(port != null) port.closePort();
	}
	
	public void write(String command) throws IOException
	{
	    byte[] bytes = command.getBytes(StandardCharsets.UTF_8);

	    port.writeBytes(bytes, bytes.length);
	}
	
	public void writeCallCalibration(String command, HttpServletResponse response) throws IOException
	{
		responses.add(response);
		
		int idx = responses.size() - 1;
		
		command =  "cs;" + command + idx + ";";
		
	    byte[] bytes = command.getBytes(StandardCharsets.UTF_8);

	    port.writeBytes(bytes, bytes.length);
	}
	
	/*public String read() throws IOException
	{
		String value = "";
		int bytesAvailable = port.bytesAvailable();
		
		if(bytesAvailable == -1) return null;
		
		byte[] buffer = new byte[bytesAvailable];
		System.out.println("Bytes available: " + bytesAvailable);
		
		int bytesRead = port.readBytes(buffer, buffer.length);
		System.out.println("Bytes read: " + bytesRead);
		value = new String(buffer, StandardCharsets.UTF_8);
		
		return value;
	}*/
}
