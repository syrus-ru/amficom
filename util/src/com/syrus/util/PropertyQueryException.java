/*-
 * $Id: PropertyQueryException.java,v 1.1 2006/06/08 17:06:16 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

/**
 * In the future, should extend {@link Exception}, so clients are encouraged
 * to catch and handle it.
 *
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/06/08 17:06:16 $
 * @module util
 */
public final class PropertyQueryException extends RuntimeException {
	private static final long serialVersionUID = 5749011725651803616L;

	public PropertyQueryException() {
		// super();
	}

	/**
	 * @param message
	 */
	public PropertyQueryException(final String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PropertyQueryException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public PropertyQueryException(final Throwable cause) {
		super(cause);
	}
}
