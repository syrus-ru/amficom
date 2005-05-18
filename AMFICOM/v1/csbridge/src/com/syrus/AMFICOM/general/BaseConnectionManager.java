/*
 * $Id: BaseConnectionManager.java,v 1.5 2005/05/18 12:52:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * The sum of all extended interfaces
 * @version $Revision: 1.5 $, $Date: 2005/05/18 12:52:58 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public interface BaseConnectionManager extends LoginServerConnectionManager, EventServerConnectionManager, IGSConnectionManager {
	String KEY_SERVANT_CHECK_TIMEOUT = "ServantCheckTimeout";
	int SERVANT_CHECK_TIMEOUT = 10;		//min

	/**
	 * All known implementations are subclasses of VerifiedConnectionManager,
	 * thus automatically implement this method.
	 */
	CORBAServer getCORBAServer();
}
