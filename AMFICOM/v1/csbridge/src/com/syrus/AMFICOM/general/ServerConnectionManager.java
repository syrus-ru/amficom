/*-
 * $Id: ServerConnectionManager.java,v 1.7 2005/09/14 18:21:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.CommonServer;

/**
 * @version $Revision: 1.7 $, $Date: 2005/09/14 18:21:32 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public interface ServerConnectionManager {
	CommonServer getServerReference() throws CommunicationException;

	CORBAServer getCORBAServer();
}
