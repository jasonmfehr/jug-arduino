package com.jfehr.jug.arduino.simulator;

import com.jfehr.jug.arduino.led.LEDStatus;
import com.jfehr.jug.arduino.mediator.RemoteBoardMediator;

public class ClientTester {

	public static void main(String[] args) {
		RemoteBoardMediator am = new RemoteBoardMediator();
		LEDStatus ledStatus = new LEDStatus();
		
		ledStatus.setLedNumber(1);
		ledStatus.setLedOn(Boolean.FALSE);
		ledStatus.setArduinoIP("127.0.0.1");
		ledStatus.setArduinoPort(51420);
		
		//am.setLed(ledStatus);
	}
}
