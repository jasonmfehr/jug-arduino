package com.jfehr.jug.arduino.mediator;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SocketFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(SocketFactory.class);
	private static final int REMOTE_BOARD_TIMEOUT = 10000; //number of milliseconds to wait for a response from the remote board
	
	public Socket buildSocket(String arduinoIP, Integer arduinoPort) {
		LOGGER.debug("building new socket for remote client [{}:{}]", arduinoIP, arduinoPort);
		try {
			Socket newSocket = new Socket(arduinoIP, arduinoPort);
			newSocket.setSoTimeout(REMOTE_BOARD_TIMEOUT);
			
			return newSocket;
		} catch (UnknownHostException e) {
			throw new SocketInitializationException(arduinoIP, arduinoPort, e);
		} catch (IOException e) {
			throw new SocketInitializationException(arduinoIP, arduinoPort, e);
		}
	}

}
