/*-
 * $Id: EventProcessingException.java,v 1.1 2005/10/18 16:19:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/18 16:19:43 $
 * @module leserver
 */
final class EventProcessingException extends Exception {
	private static final long serialVersionUID = 5663391392400492553L;

	EventProcessingException() {
		// empty
	}

	/**
	 * @param message
	 * @param cause
	 */
	EventProcessingException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	EventProcessingException(final String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	EventProcessingException(final Throwable cause) {
		super(cause);
	}
}
