/*
 * $Id: LoginException.java,v 1.4 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:27:25 $
 * @author $Author: arseniy $
 * @module general
 */
public class LoginException extends ApplicationException {
	private static final long serialVersionUID = 251948020334103957L;

	/**
	 * @param message
	 */
	public LoginException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public LoginException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public LoginException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
