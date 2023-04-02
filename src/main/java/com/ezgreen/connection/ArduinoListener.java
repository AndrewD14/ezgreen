package com.ezgreen.connection;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ezgreen.responses.EZGreenResponse;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class ArduinoListener implements SerialPortDataListener
{
	private SerialPort port;
	private List<EZGreenResponse> responses;
	
	public ArduinoListener()
	{
		responses = new ArrayList<EZGreenResponse>();
	}
	
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
		value = (new String(buffer, StandardCharsets.UTF_8)).trim();
		
	    String[] data = value.split(";");

	    if(data[0].equals("cs"))
	    {
	    	EZGreenResponse response = null;
	    	
	    	try
		    {
	    		System.out.println("Arduino response: " + value);
	    		System.out.println("Responses list size: " + responses.size());
	    		System.out.println("Response index returned: " + data[2]);
	    		System.out.println("Response value returned: " + data[1]);
	    		
	    		int idx = Integer.parseInt(data[2].trim());
	    		System.out.println("Parsed index: " + idx);
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
	    else
	    {
	    	System.out.println("Arduino response: " + value);
	    }
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
}
