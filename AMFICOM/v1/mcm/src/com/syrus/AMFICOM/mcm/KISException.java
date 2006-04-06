/*-
 * $Id: KISException.java,v 1.1.2.1 2006/04/06 08:06:13 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/04/06 08:06:13 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class KISException extends Exception {

	public KISException(final String message) {
		super(message);
	}

	public KISException(final Throwable cause) {
		super(cause);
	}

	public KISException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
