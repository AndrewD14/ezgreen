package com.ezgreen.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import com.fazecast.jSerialComm.SerialPort;

public class Arduino
{
	private SerialPort port;
	
	public Arduino()
	{
		SerialPort[] ports = SerialPort.getCommPorts();
		
		for (SerialPort port: ports) {
			System.out.println(port.getSystemPortName());
		}
	}
	
	public void open()
	{
		if(port == null)
		{
			port = SerialPort.getCommPorts()[0];
			
			port.openPort();
			port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 50, 50);
			port.setComPortParameters(115200, 8, 1, 0);
		}
		
	}
	
	public void close()
	{
		if(port != null) port.closePort();
	}
	
	public void write(String command) throws IOException
	{
	    OutputStream out = port.getOutputStream();
	    
	    out.write(command.getBytes());
	}
	
	public String read() throws IOException
	{
		String value = "";
		InputStream in = port.getInputStream();
		int b;
		
		do
		{
			b = in.read();
			
			if(b != -1) value = value + (char)b;
		}while(b != -1);
		
		return value;
	}
}
