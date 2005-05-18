/*
 * $Id: LoginRestorer.java,v 1.3 2005/05/18 12:52:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/18 12:52:58 $
 * @author $Author: bass $
 * @module csbridge_v1
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
