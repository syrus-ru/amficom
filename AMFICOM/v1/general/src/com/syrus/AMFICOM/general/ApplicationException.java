/*
 * $Id: ApplicationException.java,v 1.1 2004/08/23 20:47:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/23 20:47:57 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class ApplicationException extends Exception {
	private static final long serialVersionUID = 3078046645963979037L;

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}
}
