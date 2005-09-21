/*
 * $Id: TestProcessingException.java,v 1.5 2005/09/21 14:57:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/21 14:57:06 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

class TestProcessingException extends Exception {
	private static final long serialVersionUID = 3482128438964994335L;

	public TestProcessingException(final String message) {
		super(message);
	}
	
	public TestProcessingException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
