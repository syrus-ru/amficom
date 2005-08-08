/*
 * $Id: IllegalDataException.java,v 1.7 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:27:25 $
 * @author $Author: arseniy $
 * @module general
 */

public class IllegalDataException extends ApplicationException {
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
