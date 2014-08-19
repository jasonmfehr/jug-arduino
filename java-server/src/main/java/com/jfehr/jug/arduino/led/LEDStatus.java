package com.jfehr.jug.arduino.led;

public class LEDStatus {

	private Integer ledNumber;
	private Boolean ledOn;
	
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
	
}
