/*
 * $Id: LoginServerConnectionManager.java,v 1.6 2005/09/14 18:21:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.leserver.corba.LoginServer;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/14 18:21:32 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public interface LoginServerConnectionManager {
	LoginServer getLoginServerReference() throws CommunicationException;
}
