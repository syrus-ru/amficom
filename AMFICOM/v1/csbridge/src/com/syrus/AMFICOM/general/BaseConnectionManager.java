/*
 * $Id: BaseConnectionManager.java,v 1.4 2005/05/03 18:07:00 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * The sum of all extended interfaces
 * @version $Revision: 1.4 $, $Date: 2005/05/03 18:07:00 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public interface BaseConnectionManager extends LoginServerConnectionManager, EventServerConnectionManager, IGSConnectionManager {
	String KEY_SERVANT_CHECK_TIMEOUT = "ServantCheckTimeout";
	int SERVANT_CHECK_TIMEOUT = 10;		//min

	/**
	 * All known implementations are subclasses of VerifiedConnectionManager,
	 * thus automatically implement this method.
	 * @return
	 */
	CORBAServer getCORBAServer();
}
