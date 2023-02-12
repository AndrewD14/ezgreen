package com.ezgreen.connection;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

public class Arduino
{
	private SerialPort port;
	
	public Arduino()
	{

	}
	
	public void open() throws InterruptedException
	{
		if(port == null)
		{
			port = SerialPort.getCommPort("COM6");
			
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
				    System.out.println();
				    System.out.println(new String(newData, StandardCharsets.UTF_8));
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
	
	public String read() throws IOException
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
	}
}
