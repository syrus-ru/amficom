/*
 * VersionCollisionException.java
 * Created on 30.06.2004 12:55:57
 * 
 */
package com.syrus.AMFICOM.general;


/**
 * @author Vladimir Dolzhenko
 */
public class VersionCollisionException extends Exception {
	static final long serialVersionUID = -3847337178607890353L;

	public VersionCollisionException() {
		super();
	}

	/**
	 * @param message
	 */
	public VersionCollisionException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VersionCollisionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public VersionCollisionException(Throwable cause) {
		super(cause);
	}

}
