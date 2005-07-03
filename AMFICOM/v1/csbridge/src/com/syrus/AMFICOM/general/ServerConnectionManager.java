/*-
 * $Id: ServerConnectionManager.java,v 1.5 2005/06/25 17:07:53 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.CommonServer;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/25 17:07:53 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public interface ServerConnectionManager {
	CommonServer getServerReference() throws CommunicationException;

	CORBAServer getCORBAServer();
}
