/*
 * $Id: AnalysisException.java,v 1.3 2004/07/21 08:26:05 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.3 $, $Date: 2004/07/21 08:26:05 $
 * @author $Author: arseniy $
 * @module 
 */

public class AnalysisException extends Exception {

	public AnalysisException(String message) {
		super(message);
	}

	public AnalysisException(String message, Throwable cause) {
		super(message, cause);
	}
}
