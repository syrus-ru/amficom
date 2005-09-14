/*
 * $Id: BaseConnectionManager.java,v 1.7 2005/09/14 18:21:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * The sum of all extended interfaces
 * @version $Revision: 1.7 $, $Date: 2005/09/14 18:21:31 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
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
