/*
 * $Id: AnalysisException.java,v 1.9 2005/09/21 14:57:06 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/21 14:57:06 $
 * @author $Author: arseniy $
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
}
