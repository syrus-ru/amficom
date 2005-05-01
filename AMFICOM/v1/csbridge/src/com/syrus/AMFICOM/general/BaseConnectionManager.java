/*
 * $Id: BaseConnectionManager.java,v 1.3 2005/05/01 16:52:50 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/01 16:52:50 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public interface BaseConnectionManager extends LoginServerConnectionManager, EventServerConnectionManager, IGSConnectionManager {
	String KEY_SERVANT_CHECK_TIMEOUT = "ServantCheckTimeout";
	int SERVANT_CHECK_TIMEOUT = 10;		//min

	// Just the sum of the extended interfaces
}
