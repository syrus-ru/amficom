/*
 * $Id: AnalysisException.java,v 1.5 2004/07/28 16:02:00 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.5 $, $Date: 2004/07/28 16:02:00 $
 * @author $Author: arseniy $
 * @module mcm_v1
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
