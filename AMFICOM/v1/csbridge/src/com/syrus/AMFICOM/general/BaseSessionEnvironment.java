/*-
 * $Id: BaseSessionEnvironment.java,v 1.8 2005/05/19 15:10:08 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.8 $, $Date: 2005/05/19 15:10:08 $
 * @author $Author: max $
 * @module csbridge_v1
 */
public abstract class BaseSessionEnvironment {
	protected BaseConnectionManager baseConnectionManager;
	private PoolContext poolContext;

	private class LogoutShutdownHook extends Thread {

		public void run() {
			try {
				BaseSessionEnvironment.this.logout0();
			}
			catch (final ApplicationException ae) {
				Log.errorException(ae);
			}
		}

	}

	private LogoutShutdownHook logoutShutdownHook;

	public BaseSessionEnvironment(final BaseConnectionManager baseConnectionManager,
			final PoolContext poolContext,
			final LoginRestorer loginRestorer) {
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
		IdentifierPool.deserialize();
		this.poolContext.deserialize();

		this.baseConnectionManager.getCORBAServer().addShutdownHook(this.logoutShutdownHook);
	}

	public void logout() throws CommunicationException, LoginException {
		this.baseConnectionManager.getCORBAServer().removeShutdownHook(this.logoutShutdownHook);

		this.logout0();
	}

	void logout0() throws CommunicationException, LoginException {
		IdentifierPool.serialize();
		this.poolContext.serialize();
		LoginManager.logout();
	}
}
