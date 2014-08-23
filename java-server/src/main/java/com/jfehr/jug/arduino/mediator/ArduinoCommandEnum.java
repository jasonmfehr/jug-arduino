package com.jfehr.jug.arduino.mediator;

public enum ArduinoCommandEnum {

	SET_LED(Byte.valueOf((byte)1));
	
	private final Byte commandNumber;
	
	private ArduinoCommandEnum(Byte commandNumber) {
		this.commandNumber = commandNumber;
	}
	
	public Byte getCommandNumber() {
		return this.commandNumber;
	}
}
