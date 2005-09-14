/*
 * $Id: TestProcessingException.java,v 1.4 2005/09/14 18:13:47 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.4 $, $Date: 2005/09/14 18:13:47 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

class TestProcessingException extends Exception {
	private static final long serialVersionUID = 3482128438964994335L;

	public TestProcessingException(String message) {
		super(message);
	}
	
	public TestProcessingException(String message, Throwable cause) {
		super(message, cause);
	}
}
