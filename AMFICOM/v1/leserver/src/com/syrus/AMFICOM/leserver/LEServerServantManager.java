/*
 * $Id: LEServerServantManager.java,v 1.1 2005/04/28 10:34:00 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.HashSet;

import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/28 10:34:00 $
 * @author $Author: arseniy $
 * @module loginserver_v1
 */
public final class LEServerServantManager extends RunnableVerifiedConnectionManager {
	private static final String KEY_SERVANT_CHECK_TIMEOUT = "ServantCheckTimeout";

	public static final int SERVANT_CHECK_TIMEOUT = 10;		//min

	public LEServerServantManager(CORBAServer corbaServer, long timeout) {
		super(corbaServer, new HashSet(), timeout);

		assert timeout >= 10 * 60 * 1000 : "Too low timeout"; //not less then 10 min
	}

	public static LEServerServantManager createAndStart(String serverHostName) throws CommunicationException {
		String contextName = ContextNameFactory.generateContextName(serverHostName);
		CORBAServer corbaServer = new CORBAServer(contextName);

		long timeout = ApplicationProperties.getInt(KEY_SERVANT_CHECK_TIMEOUT, SERVANT_CHECK_TIMEOUT) * 60 * 1000;

		LEServerServantManager leServerServantManager =  new LEServerServantManager(corbaServer, timeout);
		(new Thread(leServerServantManager)).start();
		return leServerServantManager;
	}

}
