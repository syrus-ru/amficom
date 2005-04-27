/*
 * $Id: EventServerConnectionManager.java,v 1.1 2005/04/27 13:39:40 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.eventserver.corba.EventServer;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/27 13:39:40 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public interface EventServerConnectionManager {
	EventServer getEventServerReference() throws CommunicationException;
}
