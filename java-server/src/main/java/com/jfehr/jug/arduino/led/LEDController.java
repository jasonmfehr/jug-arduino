package com.jfehr.jug.arduino.led;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jfehr.jug.arduino.mediator.IMediator;
import com.jfehr.jug.arduino.mediator.RemoteBoardCommandEnum;
import com.jfehr.jug.arduino.mediator.RemoteBoardCommandTO;

@Controller
@RequestMapping("/led")
public class LEDController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LEDController.class);
	private static final Byte LED_OFF = Byte.valueOf((byte)0);
	private static final Byte LED_ON = Byte.valueOf((byte)1);
	
	@Autowired
	private IMediator remoteBoardMediator;
	
	@RequestMapping(value="set-led", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public void setLedValue(@RequestBody LEDStatus ledStatus) {
		LOGGER.debug("setting led {} to value {} on arduino with address " + ledStatus.getArduinoIP() + ":" + ledStatus.getArduinoPort(), ledStatus.getLedNumber(), ledStatus.getLedOn());
		
		RemoteBoardCommandTO commandTO = new RemoteBoardCommandTO();
		commandTO.setRemoteIP(ledStatus.getArduinoIP());
		commandTO.setRemotePort(ledStatus.getArduinoPort());
		commandTO.setCommand(RemoteBoardCommandEnum.SET_LED);
		commandTO.getDataBytes().add(Byte.valueOf(ledStatus.getLedNumber().byteValue()));
		commandTO.getDataBytes().add(ledStatus.getLedOn() ? LED_ON : LED_OFF);
		
		remoteBoardMediator.executeCommand(commandTO);
	}
}
