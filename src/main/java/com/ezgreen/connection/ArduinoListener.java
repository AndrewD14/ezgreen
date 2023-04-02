package com.ezgreen.connection;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class ArduinoListener implements SerialPortDataListener
{
	private SerialPort port;
	private List<HttpServletResponse> responses;
	
	public ArduinoListener()
	{
		responses = new ArrayList<HttpServletResponse>();
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
		value = new String(buffer, StandardCharsets.UTF_8);
		
	    String[] data = value.split(";");

	    if(data[0].equals("cs"))
	    {
		    int idx = Integer.parseInt(data[2]);
		    HttpServletResponse response = responses.get(idx);
	    
		    if(response == null) return;
	    
		    JSONObject message = new JSONObject();
	    
		    try
		    {
		    	responses.remove(idx);
		    	
		    	message.put("statusCode", 200);
		    	message.put("responseMessage", data[1]);
		    	
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
	    else
	    {
	    	System.out.println("Arduino response: " + value);
	    }
	}
	
	public void setPort(SerialPort port)
	{
		this.port = port;
	}
	
	public int addResponse(HttpServletResponse response)
	{
		responses.add(response);
		
		return responses.size() - 1;
	}
}
