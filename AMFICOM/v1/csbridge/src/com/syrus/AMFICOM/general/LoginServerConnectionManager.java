/*
 * $Id: LoginServerConnectionManager.java,v 1.2 2005/04/27 13:36:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.loginserver.corba.LoginServer;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/27 13:36:21 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public interface LoginServerConnectionManager {
	LoginServer getLoginServerReference() throws CommunicationException;
}
