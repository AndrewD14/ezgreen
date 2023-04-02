package com.ezgreen.connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ezgreen.responses.EZGreenResponse;
import com.fazecast.jSerialComm.SerialPort;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Arduino
{
	@Value("${arduino.serial.port}")
	private String commPort;
	
	private SerialPort port;
	private ArduinoListener listener;
	
	public Arduino(ArduinoListener listener)
	{
		this.listener = listener;
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
			
			listener.setPort(port);
			
			port.addDataListener(listener);
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
	
	public void writeCallCalibration(String command, EZGreenResponse response) throws IOException
	{
		int idx = listener.addResponse(response);
		
		command =  "cs;" + command + idx + "\n";
		System.out.println("Command sent is: " + command);
	    byte[] bytes = command.getBytes(StandardCharsets.UTF_8);

	    port.writeBytes(bytes, bytes.length);
	}
}
