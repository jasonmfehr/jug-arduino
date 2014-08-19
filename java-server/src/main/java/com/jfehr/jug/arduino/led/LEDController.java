package com.jfehr.jug.arduino.led;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/led")
public class LEDController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LEDController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public void doGet() {
		LOGGER.debug("GET method executed");
	}
	
	@RequestMapping(value="set-led", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public void setLedValue(@RequestBody LEDStatus ledStatus) {
		LOGGER.debug("setting led {} to value {}", ledStatus.getLedNumber(), ledStatus.getLedOn());
	}
}
