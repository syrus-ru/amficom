/*
 * $Id: ObjectNotFoundException.java,v 1.6 2005/09/14 18:51:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/14 18:51:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
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
