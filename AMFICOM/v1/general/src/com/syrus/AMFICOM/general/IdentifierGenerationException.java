/*
 * $Id: IdentifierGenerationException.java,v 1.5 2004/08/23 20:47:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.5 $, $Date: 2004/08/23 20:47:57 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class IdentifierGenerationException extends ApplicationException {
	private static final long serialVersionUID = 391276674502280360L;

	public IdentifierGenerationException(String message) {
		super(message);
	}

	public IdentifierGenerationException(String message, Exception e) {
		super(message, e);
	}
}
