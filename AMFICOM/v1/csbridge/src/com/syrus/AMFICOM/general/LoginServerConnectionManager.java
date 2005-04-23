/*
 * $Id: LoginServerConnectionManager.java,v 1.1 2005/04/23 17:50:19 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.LoginServer;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/23 17:50:19 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public interface LoginServerConnectionManager {
	LoginServer getLoginServerReference() throws CommunicationException;
}
