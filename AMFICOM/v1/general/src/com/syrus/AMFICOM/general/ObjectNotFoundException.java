/*
 * ObjectNotFoundException.java
 * Created on 28.06.2004 17:23:11
 * 
 */
package com.syrus.AMFICOM.general;


/**
 * @author Vladimir Dolzhenko
 */
public class ObjectNotFoundException extends Exception {

	public ObjectNotFoundException() {
		super();		
	}

	/**
	 * @param message
	 */
	public ObjectNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ObjectNotFoundException(Throwable cause) {
		super(cause);
	}

}
