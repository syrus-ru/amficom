/*
 * $Id: RetrieveObjectException.java,v 1.6 2005/09/14 18:51:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/14 18:51:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */

public class RetrieveObjectException extends DatabaseException {
	private static final long serialVersionUID = -8211577872518507838L;

	public RetrieveObjectException(String message) {
		super(message);
	}

	public RetrieveObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public RetrieveObjectException(Throwable cause) {
		super(cause);
	}
}
