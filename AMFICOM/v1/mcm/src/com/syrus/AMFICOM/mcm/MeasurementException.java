/*
 * $Id: MeasurementException.java,v 1.8 2005/09/21 14:57:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.8 $, $Date: 2005/09/21 14:57:06 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class MeasurementException extends TestProcessingException {
	private static final long serialVersionUID = 5663146797166499357L;

	public static final int OTHER_CODE = 0;
	public static final int IDENTIFIER_GENERATION_FAILED_CODE = 1;
	public static final int COMMUNICATION_FAILED_CODE = 2;
	public static final int DATABASE_CALL_FAILED_CODE = 3;
	
	private int code;

	public MeasurementException(final String message, final int code) {
		super(message);
		this.code = code;
	}
	
	public MeasurementException(final String message, final int code, final Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
