/*-
 * $Id: BaseSessionEnvironment.java,v 1.39 2006/05/11 11:46:31 bass Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import com.syrus.AMFICOM.general.corba.CommonUser;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.39 $, $Date: 2006/05/11 11:46:31 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public abstract class BaseSessionEnvironment {
	/**
	 * Время, отводимое на попытки войти в систему. Должно быть в миллисекундах.
	 */
	private static final long LOGIN_TIMEOUT = 10 * 1000;

	private BaseConnectionManager baseConnectionManager;
	protected PoolContext poolContext;
	private Date sessionEstablishDate;

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
			final CommonUser commonUser,
			final LoginRestorer loginRestorer) {
		this.init0(baseConnectionManager, poolContext, commonUser, loginRestorer);
		IdentifierPool.init(this.baseConnectionManager);
	}

	public BaseSessionEnvironment(final BaseConnectionManager baseConnectionManager,
			final PoolContext poolContext,
			final CommonUser commonUser,
			final LoginRestorer loginRestorer,
			final CORBAActionProcessor identifierPoolCORBAActionProcessor) {
		this.init0(baseConnectionManager, poolContext, commonUser, loginRestorer);
		IdentifierPool.init(this.baseConnectionManager, identifierPoolCORBAActionProcessor);
	}

	private void init0(final BaseConnectionManager baseConnectionManager1,
			final PoolContext poolContext1,
			final CommonUser commonUser,
			final LoginRestorer loginRestorer) {
		this.baseConnectionManager = baseConnectionManager1;
		this.poolContext = poolContext1;

		this.poolContext.init();
		LoginManager.init(new CORBALoginPerformer(this.baseConnectionManager, commonUser), loginRestorer);

		this.logoutShutdownHook = new LogoutShutdownHook();

		this.sessionEstablishDate = null;
	}

	public BaseConnectionManager getConnectionManager() {
		return this.baseConnectionManager;
	}
//
//	public PoolContext getPoolContext() {
//		return this.poolContext;
//	}

	public final Date getSessionEstablishDate() {
		return this.isSessionEstablished()
				? (Date) this.sessionEstablishDate.clone()
				: null;
	}

	public final boolean isSessionEstablished() {
		return this.sessionEstablishDate != null;
	}

	/**
	 * @param login
	 * @param password
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public final void login(final String login, final String password, final Identifier domainId)
			throws CommunicationException,
				LoginException {
		final long loginDeadTime = System.currentTimeMillis() + LOGIN_TIMEOUT;
		boolean loggedId = false;
		while (!loggedId) {
			try {
				LoginManager.login(login, password, domainId);
				loggedId = true;
			} catch (CommunicationException ce) {
				Log.errorMessage("Cannot establish connection -- " + ce.getMessage());
				if (System.currentTimeMillis() >= loginDeadTime) {
					throw ce;
				}
				try {
					Thread.sleep(2 * 1000);
				} catch (InterruptedException ie) {
					Log.errorMessage(ie);
				}
			}
		}

		try {
			StorableObjectPool.deserialize(this.poolContext.getLRUMapSaver());
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

		this.sessionEstablishDate = new Date();
	}

	/**
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public final void logout() throws CommunicationException, LoginException {
		this.baseConnectionManager.getCORBAServer().removeShutdownHook(this.logoutShutdownHook);

		this.logout0();

		//@todo Maybe move to logout0()
		this.sessionEstablishDate = null;
	}

	protected void logout0() throws CommunicationException, LoginException {
		IdentifierPool.serialize();
		StorableObjectPool.serialize(this.poolContext.getLRUMapSaver());
		LoginManager.logout();
	}
}
