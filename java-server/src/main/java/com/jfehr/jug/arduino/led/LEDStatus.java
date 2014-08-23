package com.jfehr.jug.arduino.led;

public class LEDStatus {

	private Integer ledNumber;
	private Boolean ledOn;
	private String arduinoIP;
	private Integer arduinoPort;
	
	public Integer getLedNumber() {
		return ledNumber;
	}
	public void setLedNumber(Integer ledNumber) {
		this.ledNumber = ledNumber;
	}
	
	public Boolean getLedOn() {
		return ledOn;
	}
	public void setLedOn(Boolean ledOn) {
		this.ledOn = ledOn;
	}
	
	public String getArduinoIP() {
		return arduinoIP;
	}
	public void setArduinoIP(String arduinoIP) {
		this.arduinoIP = arduinoIP;
	}
	
	public Integer getArduinoPort() {
		return arduinoPort;
	}
	public void setArduinoPort(Integer arduinoPort) {
		this.arduinoPort = arduinoPort;
	}
	
}
