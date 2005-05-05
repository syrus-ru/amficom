/*
 * $Id: ClientServantManager.java,v 1.1.1.1 2005/05/05 08:56:37 cvsadmin Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2005/05/05 08:56:37 $
 * @author $Author: cvsadmin $
 * @module generalclient_v1
 */
interface ClientServantManager extends BaseConnectionManager {
	String KEY_SERVER_HOST_NAME = "ServerHostName";

	String SERVER_HOST_NAME = "localhost";

	// Just the flag for all client servant managers. No methods.
}
