/*
 * $Id: CreateObjectException.java,v 1.5 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:27:25 $
 * @author $Author: arseniy $
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
