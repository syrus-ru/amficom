/*
 * $Id: RetrieveObjectException.java,v 1.3 2004/08/06 13:43:44 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/06 13:43:44 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class RetrieveObjectException extends DatabaseException {
	private static final long serialVersionUID = -8211577872518507838L;

	public RetrieveObjectException(String message) {
		super(message);
	}

	public RetrieveObjectException(String message, Throwable e) {
		super(message, e);
	}

	public RetrieveObjectException(Throwable e) {
		super(e);
	}
}
