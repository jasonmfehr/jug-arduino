package com.jfehr.jug.arduino.simulator;

import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfehr.jug.arduino.mediator.RemoteBoardCommandEnum;

/**
 * This class was originally intended to be a stand-in for a remote arduino board during integration testing.  However, 
 * it has evolved to being used for regular development and shakeout testing.  It's been cobbled together to approximate 
 * the functionality of an arduino.  Translation: the code might get ugly.
 */
public class ArduinoSimulator extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArduinoSimulator.class);
	private static final int BIND_PORT = 51420;
	
	private final Integer listenPort;
	
	public ArduinoSimulator(Integer listenPort) {
		this.listenPort = listenPort;
	}
	
	@Override
	public void run() {
		ServerSocket socket = null;
		
		try{
			Socket client;
			InputStream is;
			byte command; 
			int numDataBytes = -1;
			int numResponseBytes = -1;
			byte[] input;
			int readCount;
			
			LOGGER.debug("attempting to open a listening {} on port {}", ServerSocket.class.getSimpleName(), this.listenPort);
			socket = new ServerSocket(51420);
			LOGGER.debug("listening on port {}", this.listenPort);
			
			client = socket.accept();
			LOGGER.info("succesfully established communication with {}", ((InetSocketAddress) client.getRemoteSocketAddress()).getHostString());
			is = client.getInputStream();

			//identify which command was sent
			command = (byte)is.read();
			LOGGER.debug("got command [{}]", command);
			for(RemoteBoardCommandEnum e : RemoteBoardCommandEnum.values()){
				if(e.getCommandNumber().byteValue() == command){
					numDataBytes = e.getNumberOfDataBytes();
					numResponseBytes = e.getNumberOfResponseBytes();
					break;
				}
			}
			
			if(numDataBytes == -1 || numResponseBytes == -1){
				throw new SimulatorException("could not identify command with number [" + command + "]");
			}
			
			//read the input data bytes
			Thread.sleep(500); //TODO fix this cheater way of ensuring all bytes are received
			input = new byte[numDataBytes];
			readCount = is.read(input);
			if(readCount != numDataBytes){
				throw new SimulatorException("expected [" + numDataBytes + "] but only read [" + readCount + "]");
			}
			LOGGER.debug("received [{}] data bytes: {}", readCount, Arrays.toString(input));
			
			//write back dummy bytes starting with the command byte
			LOGGER.debug("writing to output stream");
			client.getOutputStream().write(command);
			Thread.sleep(500); //sleep to simulate network delay
			for(int i=1; i<numResponseBytes; i++){
				client.getOutputStream().write(i);
			}
			client.getOutputStream().flush();
		}catch(IOException e) {
			SimulatorException simEx = new SimulatorException(e);
			
			LOGGER.error("failed to communicate with the client", simEx);
			
			throw simEx;
		}catch(InterruptedException e){
			throw new SimulatorException(e);
		}finally{
			if(socket != null){
				try {
					LOGGER.debug("attempting to close the connection");
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				LOGGER.info("connection successfully closed");
			}
		}
	}
	
	public static void main(String[] args) {
		while(true){
			try{
				ArduinoSimulator sim = new ArduinoSimulator(BIND_PORT);
				sim.run();
				sim.join();
			}catch(Exception e){
				if (e.getCause() instanceof BindException){
					System.err.println("Could not open socket on port " + BIND_PORT);
					System.err.println("Shutting down");
					e.printStackTrace();
					return;
				}else{
					e.printStackTrace();
				}
			}
		}
	}

}
