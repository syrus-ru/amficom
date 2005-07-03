/*
 * $Id: EventServerConnectionManager.java,v 1.3 2005/05/18 12:52:58 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.leserver.corba.EventServer;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/18 12:52:58 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public interface EventServerConnectionManager {
	EventServer getEventServerReference() throws CommunicationException;
}
