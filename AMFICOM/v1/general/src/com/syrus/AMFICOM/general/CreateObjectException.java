package com.syrus.AMFICOM.general;

public class CreateObjectException extends Exception {

	public CreateObjectException(String message) {
		super(message);
	}

	public CreateObjectException(String message, Exception e) {
		super(message, e);
	}
}
