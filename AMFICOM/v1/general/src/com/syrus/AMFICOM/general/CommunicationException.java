package com.syrus.AMFICOM.general;

public class CommunicationException extends Exception {
	private static final long serialVersionUID = 2340932236057527353L;

	public CommunicationException(String message) {
		super(message);
	}

	public CommunicationException(String message, Exception e) {
		super(message, e);
	}
}
