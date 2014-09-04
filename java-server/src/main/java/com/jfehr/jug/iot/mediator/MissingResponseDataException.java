package com.jfehr.jug.iot.mediator;

import java.util.List;

public class MissingResponseDataException extends RuntimeException {

	private static final long serialVersionUID = -8262981184763319330L;

	public MissingResponseDataException(RemoteBoardCommandEnum command, List<Byte> actualData) {
		super("expected [" + command.getNumberOfResponseBytes() + "] response bytes but got [" + actualData.size() + "] bytes");
	}
}
