package com.syrus.AMFICOM.general;

public class UpdateObjectException extends Exception {
	private static final long serialVersionUID = -1403100175614787038L;

	public UpdateObjectException(String message) {
		super(message);
	}

	public UpdateObjectException(String message, Exception e) {
		super(message, e);
	}
}
