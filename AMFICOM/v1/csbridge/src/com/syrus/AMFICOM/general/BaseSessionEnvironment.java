/*-
 * $Id: BaseSessionEnvironment.java,v 1.18 2005/09/23 15:04:33 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.18 $, $Date: 2005/09/23 15:04:33 $
 * @author $Author: bob $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public abstract class BaseSessionEnvironment {
	protected BaseConnectionManager baseConnectionManager;
	protected PoolContext poolContext;
	protected Date sessionEstablishDate;
	protected boolean sessionEstablished;

	protected class LogoutShutdownHook extends Thread {

		@Override
		public void run() {
			try {
				BaseSessionEnvironment.this.logout0();
			}
			catch (final ApplicationException ae) {
				Log.errorException(ae);
			}
		}

	}

	protected LogoutShutdownHook logoutShutdownHook;

	public BaseSessionEnvironment(final BaseConnectionManager baseConnectionManager,
			final PoolContext poolContext,
			final LoginRestorer loginRestorer) {
		this.baseConnectionManager = baseConnectionManager;
		this.poolContext = poolContext;

		LoginManager.init(this.baseConnectionManager, loginRestorer);
		IdentifierPool.init(this.baseConnectionManager);
		this.poolContext.init();

		this.logoutShutdownHook = new LogoutShutdownHook();

		this.sessionEstablishDate = null;
		this.sessionEstablished = false;
	}

	public BaseConnectionManager getConnectionManager() {
		return this.baseConnectionManager;
	}
//
//	public PoolContext getPoolContext() {
//		return this.poolContext;
//	}

	public Date getSessionEstablishDate() {
		return this.sessionEstablishDate;
	}

	public boolean sessionEstablished() {
		return this.sessionEstablished;
	}

	public final void login(final String login, final String password) throws CommunicationException, LoginException {
		LoginManager.login(login, password);
		IdentifierPool.deserialize();
		try {
			StorableObjectPool.deserialize(this.poolContext.getLRUSaver());
		} catch (final ApplicationException e) {
			// ignore deserializing ?
			throw new LoginException("Cannot deserialize objects");
		}

		this.baseConnectionManager.getCORBAServer().addShutdownHook(this.logoutShutdownHook);

		this.sessionEstablishDate = new Date(System.currentTimeMillis());
		this.sessionEstablished = true;
	}

	public final void logout() throws CommunicationException, LoginException {
		this.baseConnectionManager.getCORBAServer().removeShutdownHook(this.logoutShutdownHook);

		this.logout0();

		//@todo Maybe move to logout0()
		this.sessionEstablishDate = null;
		this.sessionEstablished = false;
	}

	protected void logout0() throws CommunicationException, LoginException {
		IdentifierPool.serialize();
		StorableObjectPool.serialize(this.poolContext.getLRUSaver());
		LoginManager.logout();
	}
}
