package com.syrus.AMFICOM.general;

public class CreateObjectException extends Exception {
	private static final long serialVersionUID = -1884812292291066856L;

	public CreateObjectException(String message) {
		super(message);
	}

	public CreateObjectException(String message, Exception e) {
		super(message, e);
	}
}
