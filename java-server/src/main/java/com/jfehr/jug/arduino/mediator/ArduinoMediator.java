package com.jfehr.jug.arduino.mediator;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.springframework.stereotype.Component;

import com.jfehr.jug.arduino.led.LEDStatus;

@Component
public class ArduinoMediator {

	public void setLed(LEDStatus status, String arduinoIP, Integer arduinoPort) {
		try {
			Socket arduino = new Socket(arduinoIP, arduinoPort);
			
			arduino.getOutputStream().write((byte)1);
			arduino.getOutputStream().write(status.getLedNumber().byteValue());
			arduino.getOutputStream().write(status.getLedOn() ? (byte)1 : (byte)0);
			
			arduino.close();
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
