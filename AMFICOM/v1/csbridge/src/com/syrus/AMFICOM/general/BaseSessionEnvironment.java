/*-
 * $Id: BaseSessionEnvironment.java,v 1.29 2005/10/30 14:48:40 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.29 $, $Date: 2005/10/30 14:48:40 $
 * @author $Author: bass $
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
				Log.errorMessage(ae);
			}
		}

	}

	protected LogoutShutdownHook logoutShutdownHook;

	public BaseSessionEnvironment(final BaseConnectionManager baseConnectionManager,
			final PoolContext poolContext,
			final LoginRestorer loginRestorer) {
		this.init0(baseConnectionManager, poolContext, loginRestorer);
		IdentifierPool.init(this.baseConnectionManager);
	}

	public BaseSessionEnvironment(final BaseConnectionManager baseConnectionManager,
			final PoolContext poolContext,
			final LoginRestorer loginRestorer,
			final CORBAActionProcessor identifierPoolCORBAActionProcessor) {
		this.init0(baseConnectionManager, poolContext, loginRestorer);
		IdentifierPool.init(this.baseConnectionManager, identifierPoolCORBAActionProcessor);
	}

	private void init0(final BaseConnectionManager baseConnectionManager1,
			final PoolContext poolContext1,
			final LoginRestorer loginRestorer) {
		this.baseConnectionManager = baseConnectionManager1;
		this.poolContext = poolContext1;

		this.poolContext.init();
		LoginManager.init(this.baseConnectionManager, loginRestorer);

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

	/**
	 * Overridden in ClientSessionEnvironment
	 * @param login
	 * @param password
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public void login(final String login, final String password, final Identifier domainId)
			throws CommunicationException,
				LoginException {
		LoginManager.login(login, password, domainId);
		try {
			StorableObjectPool.deserialize(this.poolContext.getLRUSaver());
		} catch (final ApplicationException ae) {
			LoginManager.logout();
			StorableObjectPool.clean();
			if (ae instanceof CommunicationException) {
				throw (CommunicationException) ae;
			}
			throw new LoginException(I18N.getString("Error.Text.DeserializationFailed"), ae);
		}
		IdentifierPool.deserialize();

		this.baseConnectionManager.getCORBAServer().addShutdownHook(this.logoutShutdownHook);

		this.sessionEstablishDate = new Date(System.currentTimeMillis());
		this.sessionEstablished = true;
	}

	/**
	 * Overridden in ClientSessionEnvironment
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public void logout() throws CommunicationException, LoginException {
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
