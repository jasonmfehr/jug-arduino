package com.jfehr.jug.arduino.mediator;

public class SocketInitializationException extends RuntimeException {

	private static final long serialVersionUID = -3716200199513382485L;

	public SocketInitializationException(String arduinoIP, Integer arduinoPort, Throwable cause) {
		super("could not create socket to remote client [" + arduinoIP + ":" + arduinoPort + "]", cause);
	}

}
