/*
 * $Id: LEServerServantManager.java,v 1.5 2005/05/04 13:50:45 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.HashSet;

import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/04 13:50:45 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class LEServerServantManager extends RunnableVerifiedConnectionManager {

	public LEServerServantManager(CORBAServer corbaServer, long timeout) {
		super(corbaServer, new HashSet(), timeout);

		assert timeout >= 10L * 60L * 1000L : "Too low timeout"; //not less then 10 min
	}

	protected void onLoseConnection(String servantName) {
		// TODO Remove session of client
		super.onLoseConnection(servantName);
	}

	public static LEServerServantManager createAndStart(String serverHostName) throws CommunicationException {
		String contextName = ContextNameFactory.generateContextName(serverHostName);
		CORBAServer corbaServer = new CORBAServer(contextName);

		long timeout = ApplicationProperties.getInt(BaseConnectionManager.KEY_SERVANT_CHECK_TIMEOUT,
				BaseConnectionManager.SERVANT_CHECK_TIMEOUT) * 60 * 1000;

		LEServerServantManager leServerServantManager =  new LEServerServantManager(corbaServer, timeout);
		(new Thread(leServerServantManager)).start();
		return leServerServantManager;
	}

}
