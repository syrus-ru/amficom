/*
 * $Id: EventServerConnectionManager.java,v 1.5.2.1 2006/06/27 15:53:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.systemserver.corba.EventServer;

/**
 * @version $Revision: 1.5.2.1 $, $Date: 2006/06/27 15:53:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public interface EventServerConnectionManager {
	EventServer getEventServerReference() throws CommunicationException;
}
