package com.jfehr.jug.iot.color;

import com.jfehr.jug.iot.data.RemoteBoardInputDataTO;

public class ColorLedTO extends RemoteBoardInputDataTO {

	private Integer red;
	private Integer green;
	private Integer blue;
	
	public Integer getRed() {
		return red;
	}
	public void setRed(Integer red) {
		this.red = red;
	}
	
	public Integer getGreen() {
		return green;
	}
	public void setGreen(Integer green) {
		this.green = green;
	}
	
	public Integer getBlue() {
		return blue;
	}
	public void setBlue(Integer blue) {
		this.blue = blue;
	}
	
}
