package com.ezgreen.connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
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
					String value = "";
					int bytesAvailable = port.bytesAvailable();
					
					byte[] buffer = new byte[bytesAvailable];
					System.out.println("Bytes available: " + bytesAvailable);
					
					int bytesRead = port.readBytes(buffer, buffer.length);
					System.out.println("Bytes read: " + bytesRead);
					value = new String(buffer, StandardCharsets.UTF_8);
					
				    String[] data = value.split(";");
				    System.out.println("Data from Adruino: " + value);
				    System.out.println("Response index: " + data[1]);
				    int idx = Integer.parseInt(data[1]);
				    System.out.println("Parsed index: " + idx);
				    System.out.println("Value to send: " + data[0]);
				    HttpServletResponse response = responses.get(idx);
				    
				    if(response == null) return;
				    
				    JSONObject message = new JSONObject();
				    
				    try
				    {
				    	responses.remove(idx);
				    	
				    	message.put("statusCode", 200);
				    	message.put("responseMessage", data[0]);
				    	response.setStatus(HttpStatus.OK.value());
				    	response.getWriter().print(message.toJSONString());
				    	response.getWriter().flush();
				    }
				    catch(Exception e)
					{
				    	response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
						
						try
						{
							response.getWriter().println("{\"statusCode\": 500, \"responseMessage\": \"Adruino data listener writer error occur.\"}");
							System.out.println("Adruino data listener writer error occur: " + e.getCause());
						}
						catch(Exception error)
						{
							System.out.println("Error sending error to http response: " + error.getCause());
						}
					}
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
	
	public boolean checkGood()
	{
		if(port == null) return false;
		
		return port.isOpen();
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
		
		command =  "cs;" + command + idx + "\n";
		System.out.println("Command sent is: " + command);
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
