/*
 * $Id: EvaluationException.java,v 1.3 2004/07/21 08:26:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.3 $, $Date: 2004/07/21 08:26:06 $
 * @author $Author: arseniy $
 * @module 
 */

public class EvaluationException extends Exception {

	public EvaluationException(String message) {
		super(message);
	}

	public EvaluationException(String message, Throwable cause) {
		super(message, cause);
	}
}
