package serial.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import serial.SerialUtils;

/**
 * A reader that sits and waits for serial data to come in and then ships it off to the server's clients
 * 
 * @author Adam Campbell
 */
public class SerialServerDataReader extends SerialDataReader {
	/**
	 * The server to which this reader belongs
	 */
	private SerialServer server;

	/**
	 * The serial port number
	 */
	private int serialPortNum;

	/**
	 * The client number to respond to
	 */
	public int CLIENT_NUM = -1;

	/**
	 * Construct a serial data reader that reads from the given serial port, and outputs to the given server with the given serial port
	 * number
	 * 
	 * @param serialIn
	 *            The input stream for the serial port
	 * @param server
	 *            The server that this reader belongs to
	 * @param serialPortNum
	 *            The serial port number
	 */
	public SerialServerDataReader(InputStream serialIn, SerialServer server, int serialPortNum) {
		super(serialIn);
		this.server = server;
		this.serialPortNum = serialPortNum;
	}

	/**
	 * Handle the serial data received and ship it off to the server's clients
	 * 
	 * @param data
	 *            The serial data received
	 * @throws IOException
	 *             If something goes wrong writing to the server's clients
	 */
	@Override
	public void handleSerialDataReceived(String data) throws IOException {
		// Back up the data locally
		SerialUtils.backupData(data, SerialUtils.getJarDirectory() + "Data-Logs/Serial-Data/Port-" + serialPortNum + "/");

		// Write the data to the client
		if (CLIENT_NUM > 0 && server.getClient(CLIENT_NUM) != null) {
			server.getClient(CLIENT_NUM).getOutputStream().write((data + "\n").getBytes());
		} else {
			System.out.println("Invalid client number: " + CLIENT_NUM + " ; " + data + " " + Arrays.toString(data.getBytes()));
		}
	}
}