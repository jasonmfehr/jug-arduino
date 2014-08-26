package com.jfehr.jug.arduino.mediator;

import java.util.List;

public interface IMediator {

	public List<Byte> executeCommand(RemoteBoardCommandTO commandTO);
	
}
