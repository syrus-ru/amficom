/*
 * $Id: UpdateObjectException.java,v 1.3 2004/08/06 13:43:44 arseniy Exp $
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

public class UpdateObjectException extends DatabaseException {
	private static final long serialVersionUID = -1403100175614787038L;

	public UpdateObjectException(String message) {
		super(message);
	}

	public UpdateObjectException(String message, Throwable e) {
		super(message, e);
	}

	public UpdateObjectException(Throwable e) {
		super(e);
	}
}
