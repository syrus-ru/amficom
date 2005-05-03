/*
 * $Id: BaseSessionEnvironment.java,v 1.3 2005/05/03 18:15:17 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/03 18:15:17 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class BaseSessionEnvironment {
	protected BaseConnectionManager baseConnectionManager;
	private PoolContext poolContext;

	private class LogoutShutdownHook extends Thread {

		public void run() {
			try {
				BaseSessionEnvironment.this.logout0();
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

	}

	private LogoutShutdownHook logoutShutdownHook;

	public BaseSessionEnvironment(BaseConnectionManager baseConnectionManager, PoolContext poolContext) {
		this.baseConnectionManager = baseConnectionManager;
		this.poolContext = poolContext;

		LoginManager.init(this.baseConnectionManager);
		IdentifierPool.init(this.baseConnectionManager);
		this.poolContext.init();

		this.logoutShutdownHook = new LogoutShutdownHook();
	}

	public BaseConnectionManager getConnectionManager() {
		return this.baseConnectionManager;
	}
//
//	public PoolContext getPoolContext() {
//		return this.poolContext;
//	}

	public void login(String login, String password) throws CommunicationException, LoginException {
		LoginManager.login(login, password);
		this.poolContext.deserialize();

		this.baseConnectionManager.getCORBAServer().addShutdownHook(this.logoutShutdownHook);
	}

	public void logout() throws CommunicationException, LoginException {
		this.baseConnectionManager.getCORBAServer().removeShutdownHook(this.logoutShutdownHook);

		this.logout0();
	}

	void logout0() throws CommunicationException, LoginException {
		LoginManager.logout();
		this.poolContext.serialize();
	}

}
