/*
 * $Id: DatabaseException.java,v 1.2 2004/08/23 20:47:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/23 20:47:57 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class DatabaseException extends ApplicationException {
	private static final long serialVersionUID = -8411928564386538614L;

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DatabaseException(Throwable cause) {
		super(cause);
	}
}
