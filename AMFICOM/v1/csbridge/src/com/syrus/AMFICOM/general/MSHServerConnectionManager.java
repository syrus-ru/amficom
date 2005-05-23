/*
 * $Id: MSHServerConnectionManager.java,v 1.1 2005/05/23 07:51:43 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.mshserver.corba.MSHServer;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/23 07:51:43 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public interface MSHServerConnectionManager {
	MSHServer getMSHServerReference() throws CommunicationException;
}
