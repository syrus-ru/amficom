/*
 * $Id: ApplicationException.java,v 1.4 2006/04/06 08:10:27 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2006/04/06 08:10:27 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */

public class ApplicationException extends Exception {
	private static final long serialVersionUID = 3078046645963979037L;

	public ApplicationException(final String message) {
		super(message);
	}

	public ApplicationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(final Throwable cause) {
		super(cause);
	}
}
