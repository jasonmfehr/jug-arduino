package com.jfehr.jug.arduino.simulator;

public class SimulatorException extends RuntimeException {

	private static final long serialVersionUID = -380147472240639212L;

	public SimulatorException(String message) {
		super(message);
	}

	public SimulatorException(Throwable cause) {
		super(cause);
	}

	public SimulatorException(String message, Throwable cause) {
		super(message, cause);
	}

}
