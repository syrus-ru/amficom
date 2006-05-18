/*
 * $Id: IllegalObjectEntityException.java,v 1.9 2005/09/14 18:51:55 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/14 18:51:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */

public class IllegalObjectEntityException extends ApplicationException {
	private static final long serialVersionUID = -6641367464918789409L;

	public static final int OTHER_CODE = 0;
	public static final int NULL_ENTITY_CODE = 1;
	public static final int UNKNOWN_ENTITY_CODE = 2;
	public static final int ENTITY_NOT_REGISTERED_CODE = 3;
	
	private int code;

	public IllegalObjectEntityException(String message, int code) {
		super(message);
		this.code = code;
	}

	public IllegalObjectEntityException(String message, int code, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
