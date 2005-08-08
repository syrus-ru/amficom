/*
 * $Id: EventServerConnectionManager.java,v 1.4 2005/08/08 11:38:11 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.leserver.corba.EventServer;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:38:11 $
 * @author $Author: arseniy $
 * @module csbridge
 */
public interface EventServerConnectionManager {
	EventServer getEventServerReference() throws CommunicationException;
}
