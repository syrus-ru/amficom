/*-
 * $Id: AbstractSessionEnvironment.java,v 1.1 2006/05/30 11:42:20 bass Exp $
 *
 * Copyright © 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.ALREADY_LOGGED_IN;
import static com.syrus.AMFICOM.general.ErrorMessages.NOT_LOGGED_IN;
import static java.util.logging.Level.SEVERE;

import java.util.Date;

import org.omg.CORBA.LongHolder;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonUser;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2006/05/30 11:42:20 $
 * @author Tashoyan Arseniy Feliksovich
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @module csbridge
 */
public abstract class AbstractSessionEnvironment<T extends BaseConnectionManager> {
	/**
	 * Amount of login attempts to take before reporting an error. 
	 */
	private static final int MAX_LOGIN_ATTEMPTS = 5;

	/**
	 * Sleep timeout between subsequent login attempts, in milliseconds.
	 */
	private static final long SLEEP_BETWEEN_LOGIN_ATTEMPTS = 2 * 1000;

	/**
	 * Sleep timeout between subsequent login validation attempts in case
	 * server gave us no clue ({@code CommunicationException}), in
	 * milliseconds. 
	 */
	private static final long SLEEP_BETWEEN_LOGIN_VALIDATIONS = 5 * 60 * 1000;

	/**
	 * Время, отводимое на попытки войти в систему. Должно быть в миллисекундах.
	 */
	private static final long LOGIN_TIMEOUT = MAX_LOGIN_ATTEMPTS * SLEEP_BETWEEN_LOGIN_ATTEMPTS;


	/**
	 * Immutable: initialized <em>once</em> at object creation stage.
	 */
	private final T connectionManager;

	/**
	 * Immutable: initialized <em>once</em> at object creation stage.
	 */
	private final PoolContext poolContext;

	/**
	 * Immutable: initialized <em>once</em> at object creation stage.
	 */
	final Thread loginValidator;

	/**
	 * Mutable: set to the current date upon login, and to {@code null} upon
	 * logout.
	 */
	private Date sessionEstablishDate;

	public AbstractSessionEnvironment(final T connectionManager,
			final PoolContext poolContext,
			final CommonUser commonUser,
			final LoginRestorer loginRestorer) {
		this(connectionManager, poolContext, commonUser, loginRestorer, null);
	}

	public AbstractSessionEnvironment(final T connectionManager,
			final PoolContext poolContext,
			final CommonUser commonUser,
			final LoginRestorer loginRestorer,
			final CORBAActionProcessor identifierPoolCORBAActionProcessor) {
		this.connectionManager = connectionManager;
		this.sessionEstablishDate = null;

		(this.poolContext = poolContext).init();
		(this.loginValidator = this.newLoginValidator()).start();

		LoginManager.init(new CORBALoginPerformer(this.connectionManager, commonUser), loginRestorer);
		IdentifierPool.init(this.connectionManager, identifierPoolCORBAActionProcessor);

		final CORBAServer corbaServer = this.connectionManager.getCORBAServer();
		corbaServer.addShutdownHook(new Thread("LoginValidatorShutdown") {
			@Override
			public void run() {
				AbstractSessionEnvironment.this.loginValidator.interrupt();
			}
		});
		corbaServer.addShutdownHook(new Thread("LogoutShutdown") {
			@Override
			public void run() {
				try {
					AbstractSessionEnvironment.this.safeLogout(true);
				} catch (final Throwable t) {
					Log.errorMessage(t);
				}
			}
		});
	}

	public final T getConnectionManager() {
		return this.connectionManager;
	}

//	public final PoolContext getPoolContext() {
//		return this.poolContext;
//	}

	public final synchronized Date getSessionEstablishDate() {
		return this.isSessionEstablished()
				? (Date) this.sessionEstablishDate.clone()
				: null;
	}

	public final synchronized boolean isSessionEstablished() {
		return this.sessionEstablishDate != null;
	}

	/**
	 * @param login
	 * @param password
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public final synchronized void login(final String login, final String password, final Identifier domainId)
			throws CommunicationException,
				LoginException {
		if (this.isSessionEstablished()) {
			throw new LoginException(ALREADY_LOGGED_IN);
		}

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

		this.sessionEstablishDate = new Date();
		synchronized (this.loginValidator) {
			this.loginValidator.notify();
		}
	}

	/**
	 * Performs local cleanup without sending anything to the server, and
	 * pretends logout completed successfully. Useful if the server
	 * unilaterally ends the session.
	 */
	final void localLogout() {
		try {
			this.safeLogout(false);
		} catch (final CommunicationException ce) {
			/*
			 * Never.
			 */
			assert false;
		} catch (final LoginException le) {
			/*
			 * Never.
			 */
			assert false;
		}
	}

	/**
	 * @param useLoginManager
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	final synchronized void safeLogout(final boolean useLoginManager)
	throws CommunicationException, LoginException {
		if (!this.isSessionEstablished()) {
			return;
		}

		this.logout(useLoginManager);
	}

	/**
	 * @throws CommunicationException
	 * @throws LoginException if one is not logged in and tries to log out (
	 *         both local and remote checks). In this case, the
	 *         <em>logged in</em> state remains unchanged (cleared).
	 */
	public final synchronized void logout() throws CommunicationException, LoginException {
		if (!this.isSessionEstablished()) {
			throw new LoginException(NOT_LOGGED_IN);
		}
		
		this.logout(true);
	}

	/**
	 * @param useLoginManager if {@code false}, then
	 *        {@link LoginManager#logout()} is not invoked (useful when
	 *        server already logged us out). In this case,
	 *        {@link CommunicationException} is never thrown. 
	 * @throws CommunicationException if {@code useLoginManager} is
	 *         {@code true}, and {@link LoginManager#logout()} invocation
	 *         throws a {@link CommunicationException}. In this case, the
	 *         <em>logged in</em> state still gets cleared.
	 * @throws LoginException if one is not logged in and tries to log out
	 *         (remote check if {@code useLoginManager} is {@code true}).
	 *         In this case, the <em>logged in</em> state remains unchanged
	 *         (cleared).
	 */
	private void logout(final boolean useLoginManager)
	throws CommunicationException, LoginException {
		IdentifierPool.serialize();
		StorableObjectPool.serialize(this.poolContext.getLRUMapSaver());
		this.sessionEstablishDate = null;
		
		if (useLoginManager) {
			LoginManager.logout();
		}
	}

	private Thread newLoginValidator() {
		return new Thread("LoginValidator") {
			@Override
			public void run() {
				while (!interrupted()) {
					if (AbstractSessionEnvironment.this.isSessionEstablished()) {
						try {
							final LongHolder loginValidationTimeout = new LongHolder(-1);
							synchronized (AbstractSessionEnvironment.this) {
								if (AbstractSessionEnvironment.this.isSessionEstablished()) {
									AbstractSessionEnvironment.this.getConnectionManager().getLoginServerReference().validateLogin(LoginManager.getSessionKey().getIdlTransferable(), loginValidationTimeout);
								}
							}
							if (loginValidationTimeout.value != -1) {
								try {
									/*
									 * Sleep half as long
									 * as we need in the ideal case.
									 */
									sleep(loginValidationTimeout.value / 2);
								} catch (final InterruptedException ie) {
									return;
								}
							}
						} catch (final AMFICOMRemoteException are) {
							switch (are.errorCode.value()) {
							case IdlErrorCode._ERROR_NOT_LOGGED_IN:
								/*
								 * Server already logged us out.
								 * Perform local cleanup and wait
								 * until logged in again.
								 */
								AbstractSessionEnvironment.this.localLogout();
								synchronized (this) {
									try {
										this.wait();
									} catch (final InterruptedException ie) {
										return;
									}
								}
								break;
							default:
								/*
								 * Never.
								 */
								assert false;
								break;
							}
						} catch (final CommunicationException ce) {
							Log.debugMessage(ce, SEVERE);
							try {
								sleep(SLEEP_BETWEEN_LOGIN_VALIDATIONS);
							} catch (final InterruptedException ie) {
								return;
							}
						}
					} else {
						synchronized (this) {
							try {
								this.wait();
							} catch (final InterruptedException ie) {
								return;
							}
						}
					}
				}
			}
		};
	}
}
