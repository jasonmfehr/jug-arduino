package com.jfehr.jug.arduino.mediator;

public enum RemoteBoardCommandEnum {

	ECHO(Byte.valueOf((byte)0), Integer.valueOf(2), Integer.valueOf(2)),
	SET_LED(Byte.valueOf((byte)1), Integer.valueOf(2), Integer.valueOf(0)),
	GET_TEMP(Byte.valueOf((byte)2), Integer.valueOf(0), Integer.valueOf(1)),
	SET_COLOR_LED(Byte.valueOf((byte)3), Integer.valueOf(3), Integer.valueOf(0));
	
	private final Byte commandNumber;
	private final Integer numberOfDataBytes;
	private final Integer numberOfResponseBytes;
	
	private RemoteBoardCommandEnum(Byte commandNumber, Integer numberOfDataBytes, Integer numberOfResponseBytes) {
		this.commandNumber = commandNumber;
		this.numberOfDataBytes = numberOfDataBytes;
		this.numberOfResponseBytes = numberOfResponseBytes;
	}
	
	public Byte getCommandNumber() {
		return this.commandNumber;
	}
	
	public Integer getNumberOfDataBytes() {
		return this.numberOfDataBytes;
	}

	public Integer getNumberOfResponseBytes() {
		return numberOfResponseBytes;
	}
	
}
