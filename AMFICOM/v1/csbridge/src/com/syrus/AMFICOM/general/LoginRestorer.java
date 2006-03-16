/*
 * $Id: LoginRestorer.java,v 1.7 2006/03/16 11:30:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.7 $, $Date: 2006/03/16 11:30:21 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public interface LoginRestorer {

	/**
	 * This method may define the results of subsequent call of getLogin and getPassword
	 * For example, when user submits in special window his login and password to restore
	 * timed out session.
	 * @return true, if user or application decided to restore login;
	 * false, if user or application decided to cancel login restore.
	 */
	boolean restoreLogin();

	String getLogin();

	String getPassword();
}
