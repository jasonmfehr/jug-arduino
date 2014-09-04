package com.jfehr.jug.iot.simulator;

import com.jfehr.jug.iot.led.LEDStatus;
import com.jfehr.jug.iot.mediator.RemoteBoardMediator;

public class ClientTester {

	public static void main(String[] args) {
		RemoteBoardMediator am = new RemoteBoardMediator();
		LEDStatus ledStatus = new LEDStatus();
		
		ledStatus.setLedNumber(1);
		ledStatus.setLedOn(Boolean.FALSE);
		ledStatus.setRemoteBoardIP("127.0.0.1");
		ledStatus.setRemoteBoardPort(51420);
		
		//am.setLed(ledStatus);
	}
}
