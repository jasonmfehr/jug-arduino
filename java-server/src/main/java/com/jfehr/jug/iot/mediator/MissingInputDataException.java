package com.jfehr.jug.iot.mediator;

import java.util.List;

public class MissingInputDataException extends RuntimeException {

	private static final long serialVersionUID = 3115698945772408202L;

	public MissingInputDataException(RemoteBoardCommandEnum command, List<Byte> actualData) {
		super("expected [" + command.getNumberOfDataBytes() + "] input data bytes but got [" + actualData.size() + "] bytes");
	}

}
