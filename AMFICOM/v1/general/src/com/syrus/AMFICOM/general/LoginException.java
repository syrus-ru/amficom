/*
 * $Id: LoginException.java,v 1.2 2005/04/27 14:02:28 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/27 14:02:28 $
 * @author $Author: arseniy $
 * @module general_v1
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
