/*-
 * $Id: IdlConversionException.java,v 1.2 2006/03/14 10:47:54 bass Exp $
 *
 * Copyright ? 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.transport.idl;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/03/14 10:47:54 $
 * @module util
 */
public final class IdlConversionException extends Exception {
	private static final long serialVersionUID = -942043151840837309L;

	/**
	 * @param cause
	 */
	public IdlConversionException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 */
	public IdlConversionException(final String message) {
		super(message);
	}
}
