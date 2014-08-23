package com.jfehr.jug.arduino.mediator;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfehr.jug.arduino.led.LEDStatus;

@Component
public class ArduinoMediator {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArduinoMediator.class);
	
	@Autowired
	private SocketFactory socketFactory;
	
	public void setLed(LEDStatus status) {
		try{
			LOGGER.debug("attempting to set led [{}] to [{}] on arduino with address [" + status.getArduinoIP() + ":" + status.getArduinoPort() + "]", status.getLedNumber(), status.getLedOn());
			Socket arduino = socketFactory.buildSocket(status.getArduinoIP(), status.getArduinoPort());
			
			arduino.getOutputStream().write(ArduinoCommandEnum.SET_LED.getCommandNumber());
			arduino.getOutputStream().write(status.getLedNumber().byteValue());
			arduino.getOutputStream().write(status.getLedOn() ? (byte)1 : (byte)0);
			arduino.getOutputStream().write((byte)-1);
			arduino.getOutputStream().write((byte)-1);
			
			arduino.close();
			LOGGER.debug("successfully set led [{}] to [{}] on arduino with address [" + status.getArduinoIP() + ":" + status.getArduinoPort() + "]", status.getLedNumber(), status.getLedOn());
		}catch(UnknownHostException e){
			LOGGER.error("exception setting led [" + status.getLedNumber() + "] to [" + status.getLedOn() + "] on arduino with address [" + status.getArduinoIP() + ":" + status.getArduinoPort() + "]", e);
		}catch(IOException e){
			LOGGER.error("exception setting led [" + status.getLedNumber() + "] to [" + status.getLedOn() + "] on arduino with address [" + status.getArduinoIP() + ":" + status.getArduinoPort() + "]", e);
		}catch(SocketInitializationException e){
			LOGGER.error("exception setting led [" + status.getLedNumber() + "] to [" + status.getLedOn() + "] on arduino with address [" + status.getArduinoIP() + ":" + status.getArduinoPort() + "]", e);
		}
	}
}
