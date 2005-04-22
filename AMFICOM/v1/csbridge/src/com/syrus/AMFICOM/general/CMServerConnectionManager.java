/*
 * $Id: CMServerConnectionManager.java,v 1.2 2005/04/22 17:18:02 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.cmserver.corba.CMServer;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/22 17:18:02 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public interface CMServerConnectionManager {
	CMServer getCMServerReference() throws CommunicationException;
}
