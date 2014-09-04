package com.jfehr.jug.iot.mediator;

import java.util.LinkedList;
import java.util.List;

import com.jfehr.jug.iot.data.RemoteBoardInputDataTO;

public class RemoteBoardCommandTO {

	private RemoteBoardInputDataTO inputDataTO;
	private RemoteBoardCommandEnum command;
	private List<Byte> dataBytes;
	
	public RemoteBoardCommandTO() {
		dataBytes = new LinkedList<Byte>();
	}
	
	public RemoteBoardInputDataTO getInputDataTO() {
		return inputDataTO;
	}
	public void setInputDataTO(RemoteBoardInputDataTO inputDataTO) {
		this.inputDataTO = inputDataTO;
	}


	public RemoteBoardCommandEnum getCommand() {
		return command;
	}
	public void setCommand(RemoteBoardCommandEnum command) {
		this.command = command;
	}

	/**
	 * There is no setter method as this list is internally set up and thus cannot be overwritten.
	 * 
	 * @return {@link List}<{@link Byte}> containing the list of input data bytes that will be sent with this command
	 */
	public List<Byte> getDataBytes() {
		return dataBytes;
	}

}
