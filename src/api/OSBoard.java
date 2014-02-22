package api;

import java.io.IOException;

import serial.client.SerialClient;
import serial.client.SerialDataListener;

public class OSBoard {
	/**
	 * The SerialClient used to talk to the os via the serial port
	 */
	private SerialClient client;
	
	/**
	 * The serial port number where the rotator is connected to the server
	 * computer
	 */
	private int serialPortNum;
	
	/**
	 * Construct an OSBoard API wrapper
	 * @param client
	 * Serial client for the os
	 * @param serialPortNum
	 * Serial port number
	 */
	public OSBoard(SerialClient client, int serialPortNum) {
		if(serialPortNum < 0 || serialPortNum > 9){
			throw new IllegalArgumentException("Invalid serial port number: " + serialPortNum);
		}

		this.client = client;
		this.serialPortNum = serialPortNum;
	}
	
	/**
	 * Send the hello world query
	 */
	public void sendHello() {
		String helloQuery = "!QUERY,HELLO,A0$";
		try {
			client.write(String.format("%d%s", serialPortNum, helloQuery));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// Test hello world working
		SerialClient client = new SerialClient("10.24.223.192", 2809, "joe", "password23"); 
		if(client.getState() == SerialClient.State.ALIVE) {
			OSBoard os = new OSBoard(client, 0);
			client.addListener(new SerialDataListener() {
				@Override
				public void dataReceived(String data) {
					System.err.println("DATA RECEIVED: " + data);
				}
			});
			os.sendHello();
		}
		
		System.exit(0);
	}
}