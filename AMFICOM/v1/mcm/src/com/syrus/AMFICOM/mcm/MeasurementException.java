/*
 * $Id: MeasurementException.java,v 1.2 2004/07/27 16:17:12 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.2 $, $Date: 2004/07/27 16:17:12 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class MeasurementException extends Exception {
	private static final long serialVersionUID = 5663146797166499357L;

	public static final int OTHER_CODE = 0;
	public static final int IDENTIFIER_GENERATION_FAILED_CODE = 1;
	public static final int DATABASE_CALL_FAILED_CODE = 2;
	
	private int code;

	public MeasurementException(String message, int code) {
		super(message);
		this.code = code;
	}
	
	public MeasurementException(String message, int code, Exception e) {
		super(message, e);
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
