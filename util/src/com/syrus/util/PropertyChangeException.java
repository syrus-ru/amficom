/*-
 * $Id: PropertyChangeException.java,v 1.1 2005/09/20 07:49:21 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
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
 * @version $Revision: 1.1 $, $Date: 2005/09/20 07:49:21 $
 * @module util
 */
public final class PropertyChangeException extends RuntimeException {
	private static final long serialVersionUID = 7309338174137940431L;

	public PropertyChangeException() {
		// super();
	}

	/**
	 * @param message
	 */
	public PropertyChangeException(final String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public PropertyChangeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public PropertyChangeException(final Throwable cause) {
		super(cause);
	}
}
