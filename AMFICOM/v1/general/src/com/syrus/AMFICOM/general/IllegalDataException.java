/*
 * IllegalDataException.java
 * Created on 28.06.2004 17:24:25
 * 
 */
package com.syrus.AMFICOM.general;


/**
 * @author Vladimir Dolzhenko
 */
public class IllegalDataException extends Exception {
	private static final long serialVersionUID = -2728886523594799410L;

	/**
	 * @param message
	 */
	public IllegalDataException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public IllegalDataException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public IllegalDataException(Throwable cause) {
		super(cause);
	}
}
