/*
 * $Id: DatabaseException.java,v 1.3 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/08 11:27:25 $
 * @author $Author: arseniy $
 * @module general
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
