/*
 * $Id: LoginException.java,v 1.6 2005/10/20 14:56:02 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.6 $, $Date: 2005/10/20 14:56:02 $
 * @author $Author: bob $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public class LoginException extends ApplicationException {
	private static final long serialVersionUID = 251948020334103957L;

	private final boolean alreadyLoggedIn;

	/**
	 * @param message
	 */
	public LoginException(final String message, final boolean logging) {
		super(message);
		this.alreadyLoggedIn = logging;
	}
	
	/**
	 * @param message
	 */
	public LoginException(final String message) {
		this(message, false); 
	}

	/**
	 * @param cause
	 */
	public LoginException(final Throwable cause) {
		super(cause);
		this.alreadyLoggedIn = false;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public LoginException(String message, Throwable cause) {
		super(message, cause);
		this.alreadyLoggedIn = false;
	}
	
	public final boolean isAlreadyLoggedIn() {
		return this.alreadyLoggedIn;
	}

}
