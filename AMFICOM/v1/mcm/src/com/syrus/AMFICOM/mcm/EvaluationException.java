/*
 * $Id: EvaluationException.java,v 1.4 2004/07/21 18:43:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.4 $, $Date: 2004/07/21 18:43:32 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class EvaluationException extends Exception {
	private static final long serialVersionUID = -1273438955991381215L;

	public EvaluationException(String message) {
		super(message);
	}

	public EvaluationException(String message, Throwable cause) {
		super(message, cause);
	}
}
