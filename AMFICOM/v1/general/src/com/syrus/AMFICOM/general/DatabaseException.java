/*
 * $Id: DatabaseException.java,v 1.1 2004/08/06 13:43:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/06 13:43:43 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class DatabaseException extends Exception {
	private static final long serialVersionUID = -8411928564386538614L;

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(String message, Throwable e) {
		super(message, e);
	}
	
	public DatabaseException(Throwable cause) {
		super(cause);
	}
}
