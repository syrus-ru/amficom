/*
 * $Id: VersionCollisionException.java,v 1.3 2004/08/06 13:43:44 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/06 13:43:44 $
 * @author $Author: arseniy $
 * @module general_v1
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
