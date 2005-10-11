/*
 * $Id: AnalysisException.java,v 1.10 2005/10/11 13:36:50 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.10 $, $Date: 2005/10/11 13:36:50 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class AnalysisException extends TestProcessingException {
	private static final long serialVersionUID = 8478968071078374003L;

	public AnalysisException(final String message) {
		super(message);
	}

	public AnalysisException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public AnalysisException(final Throwable cause) {
		this(cause == null ? null : cause.toString(), cause);
	}
}
