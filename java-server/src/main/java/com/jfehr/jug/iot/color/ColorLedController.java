package com.jfehr.jug.iot.color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jfehr.jug.iot.mediator.IMediator;
import com.jfehr.jug.iot.mediator.RemoteBoardCommandEnum;
import com.jfehr.jug.iot.mediator.RemoteBoardCommandTO;

@Controller
@RequestMapping("/colorled")
public class ColorLedController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ColorLedController.class);
	
	@Autowired
	private IMediator remoteBoardMediator;
	
	@RequestMapping(value="set-color", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public void setColor(@RequestBody ColorLedTO colorTO) {
		LOGGER.debug("setting color led to RGB [" + colorTO.getRed() + "," + colorTO.getGreen() + "," + colorTO.getBlue() + "]");
		
		RemoteBoardCommandTO commandTO = new RemoteBoardCommandTO();
		
		commandTO.setCommand(RemoteBoardCommandEnum.SET_COLOR_LED);
		commandTO.setInputDataTO(colorTO);
		
		commandTO.getDataBytes().add(Byte.valueOf(colorTO.getRed().byteValue()));
		commandTO.getDataBytes().add(Byte.valueOf(colorTO.getGreen().byteValue()));
		commandTO.getDataBytes().add(Byte.valueOf(colorTO.getBlue().byteValue()));
		
		remoteBoardMediator.executeCommand(commandTO);
	}
}
