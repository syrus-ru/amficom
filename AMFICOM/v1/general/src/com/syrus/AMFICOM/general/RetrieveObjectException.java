package com.syrus.AMFICOM.general;

public class RetrieveObjectException extends Exception {

	public RetrieveObjectException(String message) {
		super(message);
	}

	public RetrieveObjectException(String message, Exception e) {
		super(message, e);
	}
}
