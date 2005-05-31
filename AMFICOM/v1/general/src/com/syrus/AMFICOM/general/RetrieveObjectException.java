/*
 * $Id: RetrieveObjectException.java,v 1.4 2005/05/31 10:30:38 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/31 10:30:38 $
 * @author $Author: arseniy $
 * @module general_v1
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
