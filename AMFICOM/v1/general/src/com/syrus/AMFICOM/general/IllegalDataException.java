/*
 * $Id: IllegalDataException.java,v 1.5 2004/08/06 13:43:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.5 $, $Date: 2004/08/06 13:43:43 $
 * @author $Author: arseniy $
 * @module general_v1
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
