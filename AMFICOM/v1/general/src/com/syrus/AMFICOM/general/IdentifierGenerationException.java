package com.syrus.AMFICOM.general;

public class IdentifierGenerationException extends Exception {

	public IdentifierGenerationException(String message) {
		super(message);
	}

	public IdentifierGenerationException(String message, Exception e) {
		super(message, e);
	}
}