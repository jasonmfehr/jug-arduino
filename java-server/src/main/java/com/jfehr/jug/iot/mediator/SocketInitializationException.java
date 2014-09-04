package com.jfehr.jug.iot.mediator;

public class SocketInitializationException extends RuntimeException {

	private static final long serialVersionUID = -3716200199513382485L;

	public SocketInitializationException(String remoteBoardIP, Integer remoteBoardPort, Throwable cause) {
		super("could not create socket to remote client [" + remoteBoardIP + ":" + remoteBoardPort + "]", cause);
	}

}
