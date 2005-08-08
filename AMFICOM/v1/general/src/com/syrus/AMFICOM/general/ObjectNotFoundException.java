/*
 * $Id: ObjectNotFoundException.java,v 1.5 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:27:25 $
 * @author $Author: arseniy $
 * @module general
 */

public class ObjectNotFoundException extends DatabaseException {
	private static final long serialVersionUID = 8931150523740838625L;
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
