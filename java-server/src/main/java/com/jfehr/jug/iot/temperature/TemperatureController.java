package com.jfehr.jug.iot.temperature;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jfehr.jug.iot.data.RemoteBoardInputDataTO;
import com.jfehr.jug.iot.led.LEDController;
import com.jfehr.jug.iot.mediator.IMediator;
import com.jfehr.jug.iot.mediator.RemoteBoardCommandEnum;
import com.jfehr.jug.iot.mediator.RemoteBoardCommandTO;

@Controller
@RequestMapping("/temp")
public class TemperatureController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LEDController.class);
	
	@Autowired
	private IMediator remoteBoardMediator;
	
	@RequestMapping(value="read", method=RequestMethod.GET, consumes="application/json", produces="application/json")
	public TemperatureOuputTO readTemperature(@RequestParam String remoteBoardIP, @RequestParam Integer remoteBoardPort) {
		TemperatureOuputTO output = new TemperatureOuputTO();
		RemoteBoardCommandTO command = new RemoteBoardCommandTO();
		RemoteBoardInputDataTO inputDataTO = new RemoteBoardInputDataTO();
		List<Byte> response;
		
		inputDataTO.setRemoteBoardIP(remoteBoardIP);
		inputDataTO.setRemoteBoardPort(remoteBoardPort);
		
		command.setCommand(RemoteBoardCommandEnum.GET_TEMP);
		command.setInputDataTO(inputDataTO);
		
		response = remoteBoardMediator.executeCommand(command);
				
		LOGGER.debug("response bytes length: {}", response.size());
		LOGGER.debug("response bytes: {}", Arrays.toString(response.toArray()));
		
		output.setTemperature(response.get(0));
		
		return output;
	}
}
