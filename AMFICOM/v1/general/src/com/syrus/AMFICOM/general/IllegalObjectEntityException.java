/*
 * $Id: IllegalObjectEntityException.java,v 1.4 2004/08/06 13:43:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/06 13:43:43 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class IllegalObjectEntityException extends Exception {
	private static final long serialVersionUID = -6641367464918789409L;

	public static final int OTHER_CODE = 0;
	public static final int NULL_ENTITY_CODE = 1;
	public static final int ENTITY_NOT_REGISTERED_CODE = 2;
	
	private int code;

	public IllegalObjectEntityException(String message, int code) {
		super(message);
		this.code = code;
	}

	public IllegalObjectEntityException(String message, int code, Exception e) {
		super(message, e);
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
