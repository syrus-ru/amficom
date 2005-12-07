/*-
 * $Id: XmlConversionException.java,v 1.2 2005/12/07 16:38:25 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/12/07 16:38:25 $
 * @module util
 */
public final class XmlConversionException extends Exception {
	private static final long serialVersionUID = -2268016851772412692L;

	/**
	 * @param cause
	 */
	public XmlConversionException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 */
	public XmlConversionException(final String message) {
		super(message);
	}
}
