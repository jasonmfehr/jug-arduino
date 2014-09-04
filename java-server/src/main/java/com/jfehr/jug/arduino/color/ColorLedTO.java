package com.jfehr.jug.arduino.color;

public class ColorLedTO {

	private String arduinoIP;
	private Integer arduinoPort;
	private Integer red;
	private Integer green;
	private Integer blue;
	
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
	
	public Integer getRed() {
		return red;
	}
	public void setRed(Integer red) {
		this.red = red;
	}
	
	public Integer getGreen() {
		return green;
	}
	public void setGreen(Integer green) {
		this.green = green;
	}
	
	public Integer getBlue() {
		return blue;
	}
	public void setBlue(Integer blue) {
		this.blue = blue;
	}
	
}
