/*
 * $Id: DatabaseException.java,v 1.4 2005/09/14 18:51:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2005/09/14 18:51:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
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
