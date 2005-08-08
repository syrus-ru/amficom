/*-
 * $Id: ServerConnectionManager.java,v 1.6 2005/08/08 11:38:11 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.CommonServer;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/08 11:38:11 $
 * @author $Author: arseniy $
 * @module csbridge
 */
public interface ServerConnectionManager {
	CommonServer getServerReference() throws CommunicationException;

	CORBAServer getCORBAServer();
}
