/*
 * $Id: ClientServantManager.java,v 1.4 2005/05/18 14:01:20 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/18 14:01:20 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
interface ClientServantManager extends BaseConnectionManager {
	String KEY_SERVER_HOST_NAME = "ServerHostName";

	String SERVER_HOST_NAME = "localhost";

	// Just the flag for all client servant managers. No methods.
}
