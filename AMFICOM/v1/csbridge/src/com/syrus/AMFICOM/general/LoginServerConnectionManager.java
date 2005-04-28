/*
 * $Id: LoginServerConnectionManager.java,v 1.3 2005/04/28 12:48:24 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.leserver.corba.LoginServer;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/28 12:48:24 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public interface LoginServerConnectionManager {
	LoginServer getLoginServerReference() throws CommunicationException;
}
