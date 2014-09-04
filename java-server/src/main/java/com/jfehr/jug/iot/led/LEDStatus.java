package com.jfehr.jug.iot.led;

import com.jfehr.jug.iot.data.RemoteBoardInputDataTO;

public class LEDStatus extends RemoteBoardInputDataTO {

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
