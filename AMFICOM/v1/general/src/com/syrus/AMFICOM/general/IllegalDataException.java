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

	public IllegalDataException() {
		super();
	}

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
