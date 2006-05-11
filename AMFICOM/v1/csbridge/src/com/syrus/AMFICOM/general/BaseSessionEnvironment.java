/*-
 * $Id: BaseSessionEnvironment.java,v 1.44 2006/05/11 12:23:27 bass Exp $
 *
 * Copyright © 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Date;

import com.syrus.AMFICOM.general.corba.CommonUser;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.44 $, $Date: 2006/05/11 12:23:27 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public abstract class BaseSessionEnvironment {
	/**
	 * Amount of login attempts to take before reporting an error. 
	 */
	private static final int MAX_LOGIN_ATTEMPTS = 5;

	/**
	 * Sleep timeout between subsequent login attempts, in milliseconds.
	 */
	private static final long SLEEP_BETWEEN_LOGIN_ATTEMPTS = 2 * 1000;

	/**
	 * Время, отводимое на попытки войти в систему. Должно быть в миллисекундах.
	 */
	private static final long LOGIN_TIMEOUT = MAX_LOGIN_ATTEMPTS * SLEEP_BETWEEN_LOGIN_ATTEMPTS;


	/**
	 * Immutable: initialized <em>once</em> at object creation stage.
	 */
	private BaseConnectionManager baseConnectionManager;

	/**
	 * Immutable: initialized <em>once</em> at object creation stage.
	 */
	private PoolContext poolContext;

	/**
	 * Mutable: set to the current date upon login, and to {@code null} upon
	 * logout.
	 */
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

	public final BaseConnectionManager getConnectionManager() {
		return this.baseConnectionManager;
	}

//	public final PoolContext getPoolContext() {
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
			} catch (final CommunicationException ce) {
				if (System.currentTimeMillis() >= loginDeadTime) {
					Log.errorMessage("Cannot establish connection -- " + ce.getMessage());
					throw ce;
				}
				try {
					Thread.sleep(SLEEP_BETWEEN_LOGIN_ATTEMPTS);
				} catch (final InterruptedException ie) {
					Log.errorMessage(ie);
				}
			}
		}

		try {
			StorableObjectPool.deserialize(this.poolContext.getLRUMapSaver());
		} catch (final CommunicationException ce) {
			LoginManager.logout();
			StorableObjectPool.clean();
			throw ce;
		} catch (final ApplicationException ae) {
			LoginManager.logout();
			StorableObjectPool.clean();
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

	final void logout0() throws CommunicationException, LoginException {
		IdentifierPool.serialize();
		StorableObjectPool.serialize(this.poolContext.getLRUMapSaver());
		LoginManager.logout();
	}
}
