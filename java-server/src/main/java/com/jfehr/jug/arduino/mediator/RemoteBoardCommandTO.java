package com.jfehr.jug.arduino.mediator;

import java.util.LinkedList;
import java.util.List;

public class RemoteBoardCommandTO {

	private String remoteIP;
	private Integer remotePort;
	private RemoteBoardCommandEnum command;
	private List<Byte> dataBytes;
	
	public RemoteBoardCommandTO() {
		dataBytes = new LinkedList<Byte>();
	}
	
	public String getRemoteIP() {
		return remoteIP;
	}
	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}
	
	public Integer getRemotePort() {
		return remotePort;
	}
	public void setRemotePort(Integer remotePort) {
		this.remotePort = remotePort;
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
