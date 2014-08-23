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
	
	public Socket buildSocket(String arduinoIP, Integer arduinoPort) {
		LOGGER.debug("building new socket for remote client [{}:{}]", arduinoIP, arduinoPort);
		try {
			return new Socket(arduinoIP, arduinoPort);
		} catch (UnknownHostException e) {
			throw new SocketInitializationException(arduinoIP, arduinoPort, e);
		} catch (IOException e) {
			throw new SocketInitializationException(arduinoIP, arduinoPort, e);
		}
	}

}
