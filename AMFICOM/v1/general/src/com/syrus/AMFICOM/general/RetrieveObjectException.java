package com.syrus.AMFICOM.general;

public class RetrieveObjectException extends Exception {
	private static final long serialVersionUID = -8211577872518507838L;

	public RetrieveObjectException(String message) {
		super(message);
	}

	public RetrieveObjectException(String message, Exception e) {
		super(message, e);
	}
}
