package com.syrus.AMFICOM.general;

public class IdentifierGenerationException extends Exception {
	private static final long serialVersionUID = 391276674502280360L;

	public IdentifierGenerationException(String message) {
		super(message);
	}

	public IdentifierGenerationException(String message, Exception e) {
		super(message, e);
	}
}
