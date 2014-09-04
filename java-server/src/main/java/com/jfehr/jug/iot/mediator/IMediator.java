package com.jfehr.jug.iot.mediator;

import java.util.List;

public interface IMediator {

	public List<Byte> executeCommand(RemoteBoardCommandTO commandTO);
	
}
