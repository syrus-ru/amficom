package com.syrus.AMFICOM.general;

public class UpdateObjectException extends Exception {
	public UpdateObjectException(String message) {
		super(message);
	}

	public UpdateObjectException(String message, Exception e) {
		super(message, e);
	}
}
