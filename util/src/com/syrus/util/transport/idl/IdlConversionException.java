/*-
 * $Id: IdlConversionException.java,v 1.1 2006/03/13 16:28:56 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.transport.idl;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/03/13 16:28:56 $
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
}
