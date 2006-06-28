/*
 * $Id: BaseConnectionManager.java,v 1.8 2006/06/09 15:54:24 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * The sum of all extended interfaces
 * @version $Revision: 1.8 $, $Date: 2006/06/09 15:54:24 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public interface BaseConnectionManager extends LoginServerConnectionManager, EventServerConnectionManager, IGSConnectionManager {
	/**
	 * All known implementations are subclasses of VerifiedConnectionManager,
	 * thus automatically implement this method.
	 */
	CORBAServer getCORBAServer();
}
