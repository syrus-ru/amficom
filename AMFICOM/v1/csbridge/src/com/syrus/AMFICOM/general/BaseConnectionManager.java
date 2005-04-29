/*
 * $Id: BaseConnectionManager.java,v 1.2 2005/04/29 17:35:25 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/29 17:35:25 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public interface BaseConnectionManager extends LoginServerConnectionManager, EventServerConnectionManager, IGSConnectionManager {
	// Just the sum of the extended interfaces
}
