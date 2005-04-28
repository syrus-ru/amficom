/*
 * $Id: LoginServerServantManager.java,v 1.1 2005/04/28 07:35:33 cvsadmin Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.loginserver;

import java.util.Collections;

import com.syrus.AMFICOM.eventserver.corba.EventServer;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.EventServerConnectionManager;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/28 07:35:33 $
 * @author $Author: cvsadmin $
 * @module loginserver_v1
 */
public final class LoginServerServantManager extends RunnableVerifiedConnectionManager implements EventServerConnectionManager {
	private static final String KEY_SERVANT_NAME_EVENTSERVER = "EventServerServantName";
	private static final String KEY_SERVANT_CHECK_TIMEOUT = "ServantCheckTimeout";

	private static final String SERVANT_NAME_EVENTSERVER = "EventServer";
	public static final int SERVANT_CHECK_TIMEOUT = 10;		//min

	private String eventServerServantName;

	public LoginServerServantManager(CORBAServer corbaServer, String eventServerServantName, long timeout) {
		super(corbaServer, Collections.singleton(eventServerServantName), timeout);

		this.eventServerServantName = eventServerServantName;

		assert timeout >= 10 * 60 * 1000 : "Too low timeout"; //not less then 10 min
	}

	public EventServer getEventServerReference() throws CommunicationException {
		try {
			return (EventServer) super.getVerifiableReference(this.eventServerServantName);
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

	public static LoginServerServantManager createAndStart(String serverHostName) throws CommunicationException {
		String contextName = ContextNameFactory.generateContextName(serverHostName);
		CORBAServer corbaServer = new CORBAServer(contextName);

		String eventServerServantName = ApplicationProperties.getString(KEY_SERVANT_NAME_EVENTSERVER, SERVANT_NAME_EVENTSERVER);

		long timeout = ApplicationProperties.getInt(KEY_SERVANT_CHECK_TIMEOUT, SERVANT_CHECK_TIMEOUT) * 60 * 1000;

		LoginServerServantManager loginServerServantManager =  new LoginServerServantManager(corbaServer, eventServerServantName, timeout);
		(new Thread(loginServerServantManager)).start();
		return loginServerServantManager;
	}

}
