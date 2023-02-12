package com.ezgreen.connection;

import jssc.SerialPortException;

import java.util.Base64;

import jssc.SerialPort;

public class Arduino
{
	private SerialPort serialPort;
	
	public Arduino()
	{
		
	}
	
	public void open()
	{
		if(serialPort == null) serialPort = new SerialPort("COM1");
		
		try
		{
	        serialPort.openPort();//Open serial port
	        serialPort.setParams(SerialPort.BAUDRATE_9600, 
	                             SerialPort.DATABITS_8,
	                             SerialPort.STOPBITS_1,
	                             SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
	    }
	    catch (SerialPortException ex)
		{
	        System.out.println(ex);
	    }
	}
	
	public void close()
	{
		try
		{
			if(serialPort != null) serialPort.closePort();
	    }
	    catch (SerialPortException ex)
		{
	        System.out.println(ex);
	    }
	}
	
	public void write(String command)
	{
	    try
	    {
	        serialPort.writeBytes(command.getBytes());//Write data to port
	    }
	    catch (SerialPortException ex)
	    {
	        System.out.println(ex);
	    }
	}
	
	public String read()
	{
		String value = null;
		
		try
		{
			byte[] buffer = serialPort.readBytes(10);//Read 10 bytes from serial port
			
			value = Base64.getEncoder().encodeToString(buffer);
	    }
	    catch (SerialPortException ex)
		{
	        System.out.println(ex);
	    }
		
		return value;
	}
}
