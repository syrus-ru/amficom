/*
 * $Id: IdentifierGenerationException.java,v 1.7 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/08 11:27:25 $
 * @author $Author: arseniy $
 * @module general
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
