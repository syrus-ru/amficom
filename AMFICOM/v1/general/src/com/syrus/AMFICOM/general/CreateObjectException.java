/*
 * $Id: CreateObjectException.java,v 1.6 2005/09/14 18:51:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/14 18:51:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */

public class CreateObjectException extends DatabaseException {
	private static final long serialVersionUID = -1884812292291066856L;

	public CreateObjectException(String message) {
		super(message);
	}

	public CreateObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public CreateObjectException(Throwable cause) {
		super(cause);
	}
}
