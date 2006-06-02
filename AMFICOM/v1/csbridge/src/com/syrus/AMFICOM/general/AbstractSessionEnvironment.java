/*-
 * $Id: AbstractSessionEnvironment.java,v 1.4 2006/06/02 15:25:15 arseniy Exp $
 *
 * Copyright � 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.ALREADY_LOGGED_IN;
import static com.syrus.AMFICOM.general.ErrorMessages.NOT_LOGGED_IN;
import static com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode._ERROR_NOT_LOGGED_IN;
import static java.util.logging.Level.SEVERE;

import java.util.Date;

import org.omg.CORBA.LongHolder;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonUser;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.4 $, $Date: 2006/06/02 15:25:15 $
 * @author Tashoyan Arseniy Feliksovich
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @module csbridge
 */
public abstract class AbstractSessionEnvironment<T extends BaseConnectionManager> {
	/**
	 * ���������� ���������� ������� ����� � �������.
	 */
	private static final int MAX_LOGIN_ATTEMPTS = 5;

	/**
	 * ���������� ������� ����� ����� ����������������� ��������� ����� �
	 * �������. � �������������.
	 */
	private static final long SLEEP_BETWEEN_LOGIN_ATTEMPTS = 2 * 1000;

	/**
	 * �����, ��������� �� ������� ����� � �������. ������ ���� � �������������.
	 */
	private static final long LOGIN_TIMEOUT = MAX_LOGIN_ATTEMPTS * SLEEP_BETWEEN_LOGIN_ATTEMPTS;

	/**
	 * ���������� ������� ����� ����������������� ��������� ���������
	 * �������������� ������ �� ������. ����� ������ ��� ������, ����� ����� �
	 * �������� �������� ����������; ���� ����� ����, �� ������ ��� �������,
	 * ����� ����� ����� ����� ����� ������ (��.
	 * {@link com.syrus.AMFICOM.leserver.corba.LoginServerOperations#validateLogin(com.syrus.AMFICOM.security.corba.IdlSessionKey, LongHolder)}).
	 */
	private static final long LOGIN_VALIDATION_PERIOD_COMM_ERROR = 5 * 60 * 1000;

	/**
	 * ����������� ������������ � ���������.
	 * <p>
	 * ��������� ���� ��� ��� �������� ������ � � ���������� �� ��������.
	 */
	private final T connectionManager;

	/**
	 * �������� ��������� �����.
	 * <p>
	 * ��������� ���� ��� ��� �������� ������ � � ���������� �� ��������.
	 */
	private final PoolContext poolContext;

	/**
	 * ������������ �������� �� ������ �������, �������������� ����������
	 * ������� ���������������� ������.
	 * <p>
	 * ��������� ���� ��� ��� �������� ������ � � ���������� �� ��������.
	 * 
	 * @see LoginValidator
	 */
	final LoginValidator loginValidator;

	/**
	 * ����� ������������ ������� ���������������� ������. ��� �������� ������
	 * ������������ � ������� �����; ��� �������� - � {@code null}.
	 * ������������ �������� ��������� ����, ��� ������ ����������� (���
	 * �������).
	 * 
	 * @see #isSessionEstablished()
	 */
	private Date sessionEstablishDate;

	/**
	 * ������� ����� ��������� ���������������� ������. ��� ������������� ����
	 * ��������������� {@link IdentifierPool} ������������ �������� ��
	 * ���������.
	 * 
	 * @param connectionManager
	 * @param poolContext
	 * @param commonUser
	 * @param loginRestorer
	 * @see IdentifierPool#init(IGSConnectionManager, CORBAActionProcessor)
	 * @see #AbstractSessionEnvironment(BaseConnectionManager, PoolContext,
	 *      CommonUser, LoginRestorer, CORBAActionProcessor)
	 */
	public AbstractSessionEnvironment(final T connectionManager,
			final PoolContext poolContext,
			final CommonUser commonUser,
			final LoginRestorer loginRestorer) {
		this(connectionManager, poolContext, commonUser, loginRestorer, null);
	}

	/**
	 * ������� ����� ��������� ���������������� ������. ��� ������������� ����
	 * ��������������� {@link IdentifierPool} ������������ ��������
	 * {@code identifierPoolCORBAActionProcessor}.
	 * <p>
	 * ������������ ��������� ��������:
	 * <ul>
	 * <li> ���� {@link #connectionManager} ������������ �
	 * {@code connectionManager};
	 * <li> ���� {@link #sessionEstablishDate} ������������ � {@code null};
	 * <li> ��������� �������� ��������� ����� {@link #poolContext};
	 * <li> ��������� � ����������� �����, �������������� ������,
	 * {@link #loginValidator};
	 * <li> ���������������� {@link LoginManager};
	 * <li> ���������������� {@link IdentifierPool};
	 * <li> ����������� "����� �� ����������" ("shutdown hook"), ����������� ���
	 * ������� ���������� � �������� ���������� {@link #loginValidator};
	 * <li> ����������� "����� �� ����������", ����������� ��� �������
	 * ���������� � �������� ������� ������� ������.
	 * </ul>
	 * 
	 * @param connectionManager
	 * @param poolContext
	 * @param commonUser
	 * @param loginRestorer
	 * @param identifierPoolCORBAActionProcessor
	 * @see IdentifierPool#init(IGSConnectionManager, CORBAActionProcessor)
	 * @see #login(String, String, Identifier)
	 * @see #logout()
	 * @see CORBAServer#addShutdownHook(Thread)
	 */
	public AbstractSessionEnvironment(final T connectionManager,
			final PoolContext poolContext,
			final CommonUser commonUser,
			final LoginRestorer loginRestorer,
			final CORBAActionProcessor identifierPoolCORBAActionProcessor) {
		this.connectionManager = connectionManager;
		this.sessionEstablishDate = null;

		this.poolContext = poolContext;
		this.poolContext.init();

		this.loginValidator = new LoginValidator();
		this.loginValidator.start();

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
					AbstractSessionEnvironment.this.logout(false, true);
				} catch (final Throwable t) {
					Log.errorMessage(t);
				}
			}
		});
	}

	public final T getConnectionManager() {
		return this.connectionManager;
	}

	// public final PoolContext getPoolContext() {
	// return this.poolContext;
	// }

	public final synchronized Date getSessionEstablishDate() {
		return this.isSessionEstablished() ? (Date) this.sessionEstablishDate.clone() : null;
	}

	/**
	 * ���������, ����������� �� ������, �. �., ��ۣ� �� ������������ � �������.
	 * 
	 * @return ���� ������ ����������� - {@code true}, ����� - {@code false}.
	 */
	public final synchronized boolean isSessionEstablished() {
		return this.sessionEstablishDate != null;
	}

	/**
	 * ����� � ������� �, ��� �����, ���������� ���������������� ������.
	 * 
	 * @param login
	 *        ��� �ޣ���� ������ ������������.
	 * @param password
	 *        ������ ������������.
	 * @param domainId
	 *        ������������� ������, � ������� ������ ����� ������������.
	 * @throws CommunicationException
	 *         �� ����� �� ��������� ������:
	 *         <ul>
	 *         <li> ���������� ��������� ������ � ��������� ���������� CORBA �
	 *         ������� ��������� ������� ({@link #LOGIN_TIMEOUT});
	 *         <li> ���������� ��������������� �������������� �� ��������� �����
	 *         ��� ��-�� ������ ��� ������������� ��������� �������� � ��������.
	 *         </ul>
	 * @throws LoginException
	 *         �� ����� �� ��������� ������:
	 *         <ul>
	 *         <li> ������������ ��� �ޣ���� ������ {@code login};
	 *         <li> ������������ ������ {@code pasdword};
	 *         <li> ��� ���� �� ���� � ����� {@code domainId};
	 *         <li> ������ �� �������.
	 *         </ul>
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
	 * ������� ���������������� ������.
	 * 
	 * @see {@link #logout(boolean)}.
	 * @throws CommunicationException
	 * @throws LoginException
	 *         ����������� ������� ������� ��� �������� ������. � ���� ������
	 *         ���������������� ��������� �������� � ������� ��������� ��������
	 *         ������.
	 */
	public final synchronized void logout() throws CommunicationException, LoginException {
		this.logout(true, true);
	}

	/**
	 * �������� "��������� �������� ������", ��� ������� �� ������������ �������
	 * ���̣���� ������� �� ������. ���� ������ ��� �������, �� �� ������
	 * ������. �£���� ��� ������ ������ {@link #logout(boolean, boolean)} �
	 * {@code errorIfSessionNotEstablished == performRemoteLogout == false}.
	 * ����� ��� ������, ����� ������ � ������������� ������� ������ ������
	 * ������������.
	 */
	final void localLogout() {
		try {
			this.logout(false, false);
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
	 * ���������� �����, ����������� ��������� �������� ���������������� ������.
	 * 
	 * @param errorIfSessionNotEstablished
	 *        ���� {@code true}, �� � ������, ����� ���������������� ������ �
	 *        ��� �������, ��������� ���������� {@link LoginException}. ����
	 *        {@code false}, �� ����� ������ ���Σ���.
	 * @param performRemoteLogout
	 *        ���� {@code true}, �� ����������� ����������� ��������� ��������
	 *        ������, � �������� � ������ �� ������.
	 *        <p>
	 *        ���� {@code false}, �� ����������� "��������� �������� ������",
	 *        ��� ������� �� ������������ ������� ���̣���� ������� �� ������, �
	 *        �ӣ ���������������� ��������� ����������� � ���������,
	 *        ��������������� �������� ������. ����� ��������� ����� ��� ������,
	 *        ����� ������ �� ������������ ������ ������ ������ ������������. �
	 *        ���� ������ {@link CommunicationException} �� ����� ����
	 *        ����������.
	 * @throws CommunicationException
	 *         �������� ������ � ������, �����
	 *         {@code performRemoteLogout == true}. ��������, ��� �����
	 *         {@link LoginManager#logout()} �� ������. � ���� ������
	 *         ���������������� ��������� �ӣ ����� ��������� � ���������
	 *         �������� ������.
	 * @throws LoginException
	 *         ����������� ������� ������� � ��� ���� �������� ������. � ����
	 *         ������ ���������������� ��������� �������� � ������� ���������
	 *         �������� ������.
	 */
	void logout(final boolean errorIfSessionNotEstablished, final boolean performRemoteLogout)
			throws CommunicationException,
				LoginException {
		if (!this.isSessionEstablished()) {
			if (errorIfSessionNotEstablished) {
				throw new LoginException(NOT_LOGGED_IN);
			}
			return;
		}

		IdentifierPool.serialize();
		StorableObjectPool.serialize(this.poolContext.getLRUMapSaver());
		this.sessionEstablishDate = null;

		if (performRemoteLogout) {
			LoginManager.logout();
		}
	}

	/**
	 * �����, ����������� ������������� ��������� � ������� � ����� ����������
	 * ���������������� ������.
	 * <p>
	 * ���� ����� ������������ ({@link Object#notify()}) ����� ����, ���
	 * ������������
	 * {@link AbstractSessionEnvironment#login(String, String, Identifier) ��ۣ� � �������}.
	 * � ������� ����� �������, ���� ���������������� ������ �������, ��
	 * ������������ ����������� ������ �������
	 * {@link com.syrus.AMFICOM.leserver.corba.LoginServerOperations#validateLogin(com.syrus.AMFICOM.security.corba.IdlSessionKey, LongHolder)},
	 * ��� ����� �� �������� ������� ��������� ������ ������� ������������.
	 * ����� ������������ ��������� ������
	 * {@link AbstractSessionEnvironment#logout()}, ���� ����� �������� �
	 * ���������� ������ {@link Object#wait()}.
	 */
	private class LoginValidator extends Thread {
		public LoginValidator() {
			super("LoginValidator");
		}

		@Override
		public void run() {
			while (!interrupted()) {
				if (AbstractSessionEnvironment.this.isSessionEstablished()) {
					/* ���� ������ ������� - ��������. */
					try {
						final LongHolder loginValidationTimeout = new LongHolder(-1);
						synchronized (AbstractSessionEnvironment.this) {
							if (AbstractSessionEnvironment.this.isSessionEstablished()) {
								AbstractSessionEnvironment.this.getConnectionManager().getLoginServerReference().validateLogin(LoginManager.getSessionKey().getIdlTransferable(),
										loginValidationTimeout);
							}
						}
						if (loginValidationTimeout.value != -1) {
							/*
							 * ���� �� ������� ���������� �������� ��� ���
							 * ������, �� ����������� ���� ������.
							 */
							try {
								sleep(loginValidationTimeout.value);
							} catch (InterruptedException e) {
								return;
							}
						} else {
							/* ������ ������ �� ������ ����. */
							assert false;
						}
					} catch (final AMFICOMRemoteException are) {
						switch (are.errorCode.value()) {
							case _ERROR_NOT_LOGGED_IN:
								/*
								 * ������ �������� ���� ������. �������� ������
								 * �������� ������� � ��������� "������ ��
								 * �������" � ���������� �����������.
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
								 * ������ ����� ������ ���� �� ������.
								 */
								assert false;
								break;
						}
					} catch (final CommunicationException ce) {
						Log.debugMessage(ce, SEVERE);
						try {
							sleep(LOGIN_VALIDATION_PERIOD_COMM_ERROR);
						} catch (final InterruptedException ie) {
							return;
						}
					}
				} else {
					/* ���� ������ ������� - �����. */
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
	}
}
