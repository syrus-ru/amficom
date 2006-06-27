/*
 * $Id: LoginServerConnectionManager.java,v 1.6.2.1 2006/06/27 15:54:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.systemserver.corba.LoginServer;

/**
 * @version $Revision: 1.6.2.1 $, $Date: 2006/06/27 15:54:13 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public interface LoginServerConnectionManager {
	LoginServer getLoginServerReference() throws CommunicationException;
}
