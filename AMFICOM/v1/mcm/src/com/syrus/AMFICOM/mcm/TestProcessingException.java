/*
 * $Id: TestProcessingException.java,v 1.1 2004/07/28 16:02:01 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.1 $, $Date: 2004/07/28 16:02:01 $
 * @author $Author: arseniy $
 * @module mcm_v1
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
