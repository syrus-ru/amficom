/*
 * $Id: UpdateObjectException.java,v 1.5 2005/08/08 11:27:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:27:26 $
 * @author $Author: arseniy $
 * @module general
 */

public class UpdateObjectException extends DatabaseException {
	private static final long serialVersionUID = -1403100175614787038L;

	public UpdateObjectException(String message) {
		super(message);
	}

	public UpdateObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateObjectException(Throwable cause) {
		super(cause);
	}
}
