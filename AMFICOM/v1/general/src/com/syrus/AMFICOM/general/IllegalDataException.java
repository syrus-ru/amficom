/*
 * $Id: IllegalDataException.java,v 1.6 2004/08/23 20:47:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2004/08/23 20:47:57 $
 * @author $Author: arseniy $
 * @module general_v1
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
