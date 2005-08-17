/*
 * $Id: TestProcessingException.java,v 1.3 2005/08/17 11:48:45 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/17 11:48:45 $
 * @author $Author: arseniy $
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
