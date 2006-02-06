/*-
 * $Id: XmlConversionException.java,v 1.1 2005/12/07 17:15:29 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.transport.xml;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/12/07 17:15:29 $
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
