/*
 * $Id: CMServerConnectionManager.java,v 1.1.1.1 2005/04/22 06:54:44 cvsadmin Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.cmserver.corba.CMServer;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2005/04/22 06:54:44 $
 * @author $Author: cvsadmin $
 * @module generalclient_v1
 */
public interface CMServerConnectionManager {
	CMServer getCMServerReference() throws CommunicationException;
}
