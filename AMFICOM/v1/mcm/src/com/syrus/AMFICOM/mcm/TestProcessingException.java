/*
 * $Id: TestProcessingException.java,v 1.2 2005/08/08 11:46:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/08 11:46:55 $
 * @author $Author: arseniy $
 * @module mcm
 */

public class TestProcessingException extends Exception {
	private static final long serialVersionUID = 3482128438964994335L;

	public TestProcessingException(String message) {
		super(message);
	}
	
	public TestProcessingException(String message, Throwable cause) {
		super(message, cause);
	}
}
