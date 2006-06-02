/*-
 * $Id: AbstractSessionEnvironment.java,v 1.4 2006/06/02 15:25:15 arseniy Exp $
 *
 * Copyright © 2004-2006 Syrus Systems.
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
	 * Наибольшее количество попыток входа в систему.
	 */
	private static final int MAX_LOGIN_ATTEMPTS = 5;

	/**
	 * Промежуток времени между двумя последовательными попытками входа в
	 * систему. В миллисекундах.
	 */
	private static final long SLEEP_BETWEEN_LOGIN_ATTEMPTS = 2 * 1000;

	/**
	 * Время, отводимое на попытки войти в систему. Должно быть в миллисекундах.
	 */
	private static final long LOGIN_TIMEOUT = MAX_LOGIN_ATTEMPTS * SLEEP_BETWEEN_LOGIN_ATTEMPTS;

	/**
	 * Промежуток времени между последовательными попытками отправить
	 * подтверждающий запрос не сервер. Нужен только для случая, когда связь с
	 * сервером временно недоступна; если связь есть, то сервер сам говорит,
	 * через какое время нужно слать запрос (см.
	 * {@link com.syrus.AMFICOM.leserver.corba.LoginServerOperations#validateLogin(com.syrus.AMFICOM.security.corba.IdlSessionKey, LongHolder)}).
	 */
	private static final long LOGIN_VALIDATION_PERIOD_COMM_ERROR = 5 * 60 * 1000;

	/**
	 * Управляющий соединениями с серверами.
	 * <p>
	 * Создаётся один раз при создании обекта и в дальнейшем не меняется.
	 */
	private final T connectionManager;

	/**
	 * Контекст объектных пулов.
	 * <p>
	 * Создаётся один раз при создании обекта и в дальнейшем не меняется.
	 */
	private final PoolContext poolContext;

	/**
	 * Периодически отсылает на сервер запросы, подтверждающие активность
	 * текущей пользовательской сессии.
	 * <p>
	 * Создаётся один раз при создании обекта и в дальнейшем не меняется.
	 * 
	 * @see LoginValidator
	 */
	final LoginValidator loginValidator;

	/**
	 * Время установления текущей пользовательской сессии. При открытии сессии
	 * выставляется в текущее время; при закрытии - в {@code null}.
	 * Одновременно является признаком того, что сессия установлена (или
	 * закрыта).
	 * 
	 * @see #isSessionEstablished()
	 */
	private Date sessionEstablishDate;

	/**
	 * Создаёт новое окружение пользовательской сессии. Для инициализации пула
	 * идентификаторов {@link IdentifierPool} используется действие по
	 * умолчанию.
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
	 * Создаёт новое окружение пользовательской сессии. Для инициализации пула
	 * идентификаторов {@link IdentifierPool} используется действие
	 * {@code identifierPoolCORBAActionProcessor}.
	 * <p>
	 * Производятся следующие действия:
	 * <ul>
	 * <li> поле {@link #connectionManager} выставляется в
	 * {@code connectionManager};
	 * <li> поле {@link #sessionEstablishDate} выставляется в {@code null};
	 * <li> создаётся контекст объектных пулов {@link #poolContext};
	 * <li> создаётся и запускается поток, подтверждающий сессию,
	 * {@link #loginValidator};
	 * <li> инициализируется {@link LoginManager};
	 * <li> инициализируется {@link IdentifierPool};
	 * <li> добавляется "зацеп на выключение" ("shutdown hook"), позволяющий при
	 * разрыве соединения с сервером остановить {@link #loginValidator};
	 * <li> добавляется "зацеп на выключение", позволяющий при разрыве
	 * соединения с сервером закрыть текущую сессию.
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
	 * Проверить, установлена ли сессия, т. е., вошёл ли пользователь в систему.
	 * 
	 * @return Если сессия установлена - {@code true}, иначе - {@code false}.
	 */
	public final synchronized boolean isSessionEstablished() {
		return this.sessionEstablishDate != null;
	}

	/**
	 * Войти в систему и, тем самым, установить пользовательскую сессию.
	 * 
	 * @param login
	 *        Имя учётной записи пользователя.
	 * @param password
	 *        Пароль пользователя.
	 * @param domainId
	 *        Идентификатор домена, в который желает зайти пользователь.
	 * @throws CommunicationException
	 *         По одной из следующих причин:
	 *         <ul>
	 *         <li> невозможно разрешить сервер в контексте именований CORBA в
	 *         течение заданного времени ({@link #LOGIN_TIMEOUT});
	 *         <li> невозможно десериализовать закэшированный на локальном диске
	 *         пул из-за ошибки при синхронизации локальных объектов с сервером.
	 *         </ul>
	 * @throws LoginException
	 *         По одной из следующих причин:
	 *         <ul>
	 *         <li> неправильное имя учётной записи {@code login};
	 *         <li> неправильный пароль {@code pasdword};
	 *         <li> нет прав на вход в домен {@code domainId};
	 *         <li> ошибка на сервере.
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
	 * Закрыть пользовательскую сессию.
	 * 
	 * @see {@link #logout(boolean)}.
	 * @throws CommunicationException
	 * @throws LoginException
	 *         Предпринята попытка закрыть уже закрытую сессию. В этом случае
	 *         пользовательское окружение остаётся в прежнем состоянии закрытой
	 *         сессии.
	 */
	public final synchronized void logout() throws CommunicationException, LoginException {
		this.logout(true, true);
	}

	/**
	 * Провести "локальное закрытие сессии", при котором не производится никаких
	 * удалённых вызовов на сервер. Если сессия уже закрыта, то не делать
	 * ничего. Обёртка для вызова метода {@link #logout(boolean, boolean)} с
	 * {@code errorIfSessionNotEstablished == performRemoteLogout == false}.
	 * Нужен для случая, когда сервер в одностороннем порядке закрыл сессию
	 * пользователя.
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
	 * Внутренний метод, реализующий процедуру закрытия пользовательской сессии.
	 * 
	 * @param errorIfSessionNotEstablished
	 *        Если {@code true}, то в случае, когда пользовательская сессия и
	 *        так закрыта, возникнет исключение {@link LoginException}. Если
	 *        {@code false}, то метод просто вернётся.
	 * @param performRemoteLogout
	 *        Если {@code true}, то выполняется полноценная процедура закрытия
	 *        сессии, с запросом о выходе на сервер.
	 *        <p>
	 *        Если {@code false}, то выполняется "локальное закрытие сессии",
	 *        при котором не производится никаких удалённых вызовов на сервер, а
	 *        всё пользовательское окружение переводится в состояние,
	 *        соответствующее закрытой сессии. Такое поведение нужно для случая,
	 *        когда сервер по собственному почину закрыл сессию пользователя. В
	 *        этом случае {@link CommunicationException} не может быть
	 *        возбуждено.
	 * @throws CommunicationException
	 *         Возможно только в случае, когда
	 *         {@code performRemoteLogout == true}. Означает, что вызов
	 *         {@link LoginManager#logout()} не удался. В этом случае
	 *         пользовательское окружение всё равно переходит в состояние
	 *         закрытой сессии.
	 * @throws LoginException
	 *         Предпринята попытка закрыть и без того закрытую сессию. В этом
	 *         случае пользовательское окружение остаётся в прежнем состоянии
	 *         закрытой сессии.
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
	 * Класс, выполняющий периодические обращения к серверу с целью сохранения
	 * пользовательской сессии.
	 * <p>
	 * Этот поток пробуждается ({@link Object#notify()}) после того, как
	 * пользователь
	 * {@link AbstractSessionEnvironment#login(String, String, Identifier) вошёл в систему}.
	 * В течение всего времени, пока пользовательская сессия открыта, он
	 * периодически запрашивает сервер вызовом
	 * {@link com.syrus.AMFICOM.leserver.corba.LoginServerOperations#validateLogin(com.syrus.AMFICOM.security.corba.IdlSessionKey, LongHolder)},
	 * тем самым не позволяя серверу отключить сессию данного пользователя.
	 * Когда пользователь закрывает сессию
	 * {@link AbstractSessionEnvironment#logout()}, этот поток засыпает в
	 * результате вызова {@link Object#wait()}.
	 */
	private class LoginValidator extends Thread {
		public LoginValidator() {
			super("LoginValidator");
		}

		@Override
		public void run() {
			while (!interrupted()) {
				if (AbstractSessionEnvironment.this.isSessionEstablished()) {
					/* Если сессия открыта - работать. */
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
							 * Если на сервере существует открытая для нас
							 * сессия, то выполняется этот случай.
							 */
							try {
								sleep(loginValidationTimeout.value);
							} catch (InterruptedException e) {
								return;
							}
						} else {
							/* Такого вообще не должно быть. */
							assert false;
						}
					} catch (final AMFICOMRemoteException are) {
						switch (are.errorCode.value()) {
							case _ERROR_NOT_LOGGED_IN:
								/*
								 * Сервер отключил нашу сессию. Остаётся только
								 * локально перейти в состояние "сессия не
								 * открыта" и дожидаться пробуждения.
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
								 * Других кодов ошибки быть не должно.
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
					/* Если сессия закрыта - спать. */
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
