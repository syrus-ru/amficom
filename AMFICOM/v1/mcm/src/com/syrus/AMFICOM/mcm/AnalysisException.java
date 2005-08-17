/*
 * $Id: AnalysisException.java,v 1.7 2005/08/17 11:48:45 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/17 11:48:45 $
 * @author $Author: arseniy $
 * @module mcm
 */

final class AnalysisException extends TestProcessingException {
	private static final long serialVersionUID = 8478968071078374003L;

	public AnalysisException(String message) {
		super(message);
	}

	public AnalysisException(String message, Throwable cause) {
		super(message, cause);
	}
}
