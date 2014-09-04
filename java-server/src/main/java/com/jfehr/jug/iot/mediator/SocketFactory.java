package com.jfehr.jug.iot.mediator;

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
	
	public Socket buildSocket(String remoteBoardIP, Integer remoteBoardPort) {
		LOGGER.debug("building new socket for remote client [{}:{}]", remoteBoardIP, remoteBoardPort);
		try {
			Socket newSocket = new Socket(remoteBoardIP, remoteBoardPort);
			newSocket.setSoTimeout(REMOTE_BOARD_TIMEOUT);
			
			return newSocket;
		} catch (UnknownHostException e) {
			throw new SocketInitializationException(remoteBoardIP, remoteBoardPort, e);
		} catch (IOException e) {
			throw new SocketInitializationException(remoteBoardIP, remoteBoardPort, e);
		}
	}

}
