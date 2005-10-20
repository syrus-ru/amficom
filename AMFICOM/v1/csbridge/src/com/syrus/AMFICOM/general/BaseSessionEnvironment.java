/*-
 * $Id: BaseSessionEnvironment.java,v 1.26 2005/10/20 14:17:36 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.26 $, $Date: 2005/10/20 14:17:36 $
 * @author $Author: arseniy $
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

	public BaseSessionEnvironment(final BaseConnectionManager baseConnectionManager, final PoolContext poolContext) {
		this.baseConnectionManager = baseConnectionManager;
		this.poolContext = poolContext;
		IdentifierPool.init(this.baseConnectionManager);
		this.init0();
	}

	public BaseSessionEnvironment(final BaseConnectionManager baseConnectionManager,
			final PoolContext poolContext,
			final CORBAActionProcessor identifierPoolCORBAActionProcessor) {
		this.baseConnectionManager = baseConnectionManager;
		this.poolContext = poolContext;
		IdentifierPool.init(this.baseConnectionManager, identifierPoolCORBAActionProcessor);
		this.init0();
	}

	private void init0() {
		LoginManager.init(this.baseConnectionManager);

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
		IdentifierPool.deserialize();
		try {
			StorableObjectPool.deserialize(this.poolContext.getLRUSaver());
		} catch (final CommunicationException ce) {
			StorableObjectPool.clean();
			throw ce;
		} catch (final ApplicationException e) {
			StorableObjectPool.clean();
			throw new LoginException(I18N.getString("Error.Text.DeserializationFailed"));
		}

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
