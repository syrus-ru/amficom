/*
 * $Id: IdentifierGenerationException.java,v 1.8 2005/09/14 18:51:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.8 $, $Date: 2005/09/14 18:51:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
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
