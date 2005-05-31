/*
 * $Id: UpdateObjectException.java,v 1.4 2005/05/31 10:30:38 arseniy Exp $
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
