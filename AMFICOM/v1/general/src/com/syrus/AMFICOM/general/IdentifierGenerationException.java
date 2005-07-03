/*
 * $Id: IdentifierGenerationException.java,v 1.6 2005/05/31 10:30:38 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/31 10:30:38 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class IdentifierGenerationException extends ApplicationException {
	private static final long serialVersionUID = 391276674502280360L;

	public IdentifierGenerationException(String message) {
		super(message);
	}

	public IdentifierGenerationException(String message, Throwable cause) {
		super(message, cause);
	}
}
