/*
 * $Id: AnalysisException.java,v 1.6 2005/08/08 11:46:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/08 11:46:55 $
 * @author $Author: arseniy $
 * @module mcm
 */

public class AnalysisException extends TestProcessingException {
	private static final long serialVersionUID = 8478968071078374003L;

	public AnalysisException(String message) {
		super(message);
	}

	public AnalysisException(String message, Throwable cause) {
		super(message, cause);
	}
}
