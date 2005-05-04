/*
 * $Id: BaseSessionEnvironment.java,v 1.5 2005/05/04 12:24:10 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/04 12:24:10 $
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

	public BaseSessionEnvironment(BaseConnectionManager baseConnectionManager, PoolContext poolContext, LoginRestorer loginRestorer) {
		this.baseConnectionManager = baseConnectionManager;
		this.poolContext = poolContext;

		LoginManager.init(this.baseConnectionManager, loginRestorer);
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
		this.poolContext.serialize();
		LoginManager.logout();
	}

}
