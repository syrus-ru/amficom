/*-
 * $Id: KISException.java,v 1.1.2.2 2006/04/06 08:08:31 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/04/06 08:08:31 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class KISException extends Exception {
	private static final long serialVersionUID = 5629070237704703388L;

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
