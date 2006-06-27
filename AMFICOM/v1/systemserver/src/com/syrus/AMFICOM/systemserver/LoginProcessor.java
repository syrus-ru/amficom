/*-
 * $Id: LoginProcessor.java,v 1.1.1.1 2006/06/27 08:36:49 cvsadmin Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.systemserver;

import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.AMFICOM.security.UserLoginDatabase;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;


/**
 * Обработчик пользовательских сессий на стороне сервера.
 * <p>
 * Воплощение шаблона "Одиночка". Имеет не более одного объекта в
 * пределах приложения, который создаётся методом {@link #createInstance()} и
 * ссылку на который можно получить с помощью метода {@link #getInstance()}.
 * <p>
 * Объект этого класса создаёт поток, который периодически обследует карту
 * пользовательских сессий {@link #userLoginMap} на предмет наличия в ней
 * "мёртвых", т. е. - нерабочих сессий. Такие сессии удаляются. Это позволяет
 * избежать зависания процедуры рассылки событий пользователям при разрешении
 * ссылки мёртвой CORBA. См. метод {@link #run()}. Все изменения
 * пользовательских сессий отражаются в базе данных; при перезапуске сервера из
 * БД считывается состояние каждое сессии на время выключения.
 * <p>
 * В этом классе реализованы процедуры добавления и удаления пользовательских
 * сессий (соответственно, методы
 * {@link #addUserLogin(Identifier, Identifier, String)} и {@link #removeUserLogin(SessionKey)}),
 * получения сведений о сессиях (методы {@link #getUserLogins()},
 * {@link #getUserLogins(Identifier)}) и, наконец, некоторые вспомогательные
 * методы. Кроме того, возможно оповещение слушателей, следящих за изменениями
 * пользовательских сессий ({@link #addListener(LoginProcessorListener)} и
 * {@link #removeListener(LoginProcessorListener)}).
 * 
 * @version $Revision: 1.1.1.1 $, $Date: 2006/06/27 08:36:49 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
 */
final class LoginProcessor extends Thread {
	public static final String KEY_LOGIN_PROCESSOR_TICK_TIME = "LoginProcessorTickTime";
	public static final String KEY_LOGIN_PROCESSOR_MAX_FALLS = "LoginProcessorMaxFalls";
	public static final String KEY_MAX_USER_INACTIVITY_TIME = "MaxUserInactivityTime";
	public static final String KEY_LOGIN_VALIDATION_PERIOD = "LoginValidationPeriod";

	public static final int LOGIN_PROCESSOR_TICK_TIME = 5; // sec
	public static final int MAX_USER_INACTIVITY_TIME = 10; // minutes
	public static final int LOGIN_VALIDATION_PERIOD = 2; // minutes

	/**
	 * Объект-одиночка. Создаётся методом {@link #createInstance()}. Ссылка на
	 * него - через метод {@link #getInstance()}.
	 */
	private static LoginProcessor instance;

	/**
	 * Характерное время одного цикла потока.
	 */
	private final long tickTime;

	/**
	 * Драйвер базы данных для объектов пользовательских сессий
	 * {@link UserLogin}.
	 */
	private final UserLoginDatabase userLoginDatabase;

	/**
	 * Карта пользовательских сессий. По ключу - уникальный ключ сессии
	 * {@link SessionKey}, по величине - сама пользовательская сессия
	 * {@link UserLogin}.
	 */
	private final Map<SessionKey, UserLogin> userLoginMap;

	/**
	 * Время, в течение которого контрольные вызовы от пользователя могут не
	 * поступать на сервер. По истечении этого времени сессия пользователя будет
	 * удалена как мёртвая. В миллисекундах.
	 */
	private final long maxUserInactivityTime;

	/**
	 * Период, с которым пользователи должны слать контрольные вызовы на сервер,
	 * для того, чтобы подтверждать живое состояние своих сессий. В
	 * миллисекундах.
	 */
	private final long loginValidationPeriod;

	/**
	 * Список слушателей, подписавшихся на уведомления о входах/выходах
	 * пользователей в&nbsp;систему/из&nbsp;системы.
	 * <p>
	 * Слушатель может быть добавлен с помощью метода
	 * {@link #addListener(LoginProcessorListener)}, а удалён с помощью метода
	 * {@link #removeListener(LoginProcessorListener)}. Когда создаётся новая
	 * пользовательская сессия (метод
	 * {@link #addUserLogin(Identifier, Identifier, String)}), все слушатели
	 * оповещаются методом {@link #fireUserLoggedIn(UserLogin)}.
	 * <p>
	 * Для ускорения последовательного перебора в методах
	 * {@link #fireUserLoggedIn(UserLogin)} и
	 * {@link #fireUserLoggedOut(UserLogin)} используется {@link ArrayList} и
	 * доступ к элементам по индексу {@link ArrayList#get(int)}. Это быстрее,
	 * чем перебор итератором.
	 */
	private final ArrayList<LoginProcessorListener> loginProcessorListeners;

	/**
	 * Вспомогательное поле. Используется для извлечения пользователей из БД в
	 * состоянии, когда работа с пулом невозможна по причине отсутсвия сессии в
	 * системе.
	 */
	private final SystemUserDatabase systemUserDatabase;

	/**
	 * Создать объект-одиночку данного класса.
	 */
	public static synchronized void createInstance() {
		if (instance == null) {
			instance = new LoginProcessor();
		}
	}

	/**
	 * Получить ссылку на объект-одиночку данного класса.
	 * 
	 * @return Ссылку на обработчик пользовательских сессий.
	 * @throws IllegalStateException
	 *         Если объект ещё не был создан методом {@link #createInstance()}.
	 */
	public static synchronized LoginProcessor getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Not initialized");
		}
		return instance;
	}

	/**
	 * Закрытый конструктор.
	 * <p>
	 * Делает следующее:
	 * <ul>
	 * <li> выставляет все внутренние поля в начальные значения;
	 * <li> восстанавливает состояние пользовательских сессий на время
	 * предыдущего выключения сервера;
	 * <li> добавляет "зацеп на выключение" ("shutdown hook"), позволяющий
	 * остановить поток обработчика пользовательских сессий;
	 * <li> дополнительно - добавляет слушателя, следящего за изменением числа
	 * пользовательских сессий и выводящего отладочную информацию о событиях
	 * такого рода.
	 * </ul>
	 */
	private LoginProcessor() {
		super("LoginProcessor");

		this.tickTime = ApplicationProperties.getInt(KEY_LOGIN_PROCESSOR_TICK_TIME, LOGIN_PROCESSOR_TICK_TIME) * 1000;
		this.userLoginDatabase = new UserLoginDatabase();
		this.userLoginMap = Collections.synchronizedMap(new HashMap<SessionKey, UserLogin>());
		this.maxUserInactivityTime = ApplicationProperties.getInt(KEY_MAX_USER_INACTIVITY_TIME, MAX_USER_INACTIVITY_TIME) * 60 * 1000;
		this.loginValidationPeriod = ApplicationProperties.getInt(KEY_LOGIN_VALIDATION_PERIOD, LOGIN_VALIDATION_PERIOD) * 60 * 1000;
		this.loginProcessorListeners = new ArrayList<LoginProcessorListener>();

		this.restoreUserLogins();

		Runtime.getRuntime().addShutdownHook(new Thread("LoginProcessor -- shutdown hook") {
			@Override
			public void run() {
				LoginProcessor.this.interrupt();
			}
		});

		this.addDebugOutputListener();

		final StorableObjectDatabase<SystemUser> storableObjectDatabase = DatabaseContext.getDatabase(SYSTEMUSER_CODE);
		this.systemUserDatabase = (SystemUserDatabase) storableObjectDatabase;

		this.printUserLogins();
	}

	/**
	 * Восстановить пользовательские сессии из БД, как они были сохранены при
	 * остановке сервера.
	 */
	private void restoreUserLogins() {
		try {
			final Set<UserLogin> userLogins = this.userLoginDatabase.retrieveAll();
			for (final UserLogin userLogin : userLogins) {
				this.userLoginMap.put(userLogin.getSessionKey(), userLogin);
			}
		} catch (RetrieveObjectException roe) {
			Log.errorMessage(roe);
		}
	}

	private void addDebugOutputListener() {
		this.loginProcessorListeners.add(new LoginProcessorListener() {

			public void userLoggedIn(UserLogin userLogin) {
				Log.debugMessage("Adding login for user '"
						+ userLogin.getSystemUserId() + "' to domain '" + userLogin.getDomainId() + "'", Log.DEBUGLEVEL08);
				LoginProcessor.this.printUserLogins();
			}

			public void userLoggedOut(UserLogin userLogin) {
				Log.debugMessage("Removing login for session key '" + userLogin.getSessionKey() + "'", Log.DEBUGLEVEL08);
				LoginProcessor.this.printUserLogins();
			}

		});
	}

	/**
	 * Главный цикл потока.
	 * <p>
	 * Периодически (с периодом {@link #tickTime}) пробегает карту
	 * пользовательских сессий {@link #userLoginMap}. Ищет в ней сессии,
	 * активность которых не проявлялась в течение времени
	 * {@link #maxUserInactivityTime} (с поправкой на период
	 * {@link #loginValidationPeriod}, с которым пользователь шлёт
	 * подтверждения на сервер). Каждую такую сессию рассматривает, как чуждую
	 * идеям работы в системе АМФИКОМ, и уничтожает её на месте без суда и
	 * следствия.
	 */
	@Override
	public void run() {
		while (!interrupted()) {
			synchronized (this.userLoginMap) {
				for (final Iterator<UserLogin> it = this.userLoginMap.values().iterator(); it.hasNext();) {
					final UserLogin userLogin = it.next();
					final Date lastActivityDate = userLogin.getLastActivityDate();
					if (System.currentTimeMillis() - lastActivityDate.getTime() >= this.maxUserInactivityTime
							+ this.loginValidationPeriod) {
						Log.debugMessage("User '"
								+ userLogin.getSystemUserId() + "' unactive more, than (" + (this.maxUserInactivityTime / (60 * 1000))
								+ " + " + (this.loginValidationPeriod / (60 * 1000)) + ") minutes. Deleting login",
								Log.DEBUGLEVEL06);
						this.userLoginDatabase.delete(userLogin);
						it.remove();
					}
				}
			}
			try {
				sleep(this.tickTime);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	/**
	 * Добавить новую пользовательскую сессию.
	 * <p>
	 * Создаёт новую сессию, помещает её в карту сессий {@link #userLoginMap} и
	 * сохраняет её в БД. Кроме того, уведомляет об этом событии слушателей из
	 * списка {@link #loginProcessorListeners}.
	 * 
	 * @param userId
	 *        Идентификатор пользователя.
	 * @param domainId
	 *        Идентификатор домена.
	 * @param userIOR
	 *        IOR CORBA-интерфейса данного пользователя.
	 * @return Ключ новой сессии.
	 */
	SessionKey addUserLogin(final Identifier userId, final Identifier domainId, final String userIOR) {
		final UserLogin userLogin = UserLogin.createInstance(userId, domainId, userIOR);
		final SessionKey sessionKey = userLogin.getSessionKey();

		this.userLoginMap.put(sessionKey, userLogin);

		try {
			this.userLoginDatabase.insert(userLogin);
		} catch (CreateObjectException coe) {
			Log.errorMessage(coe);
		}

		this.fireUserLoggedIn(userLogin);

		return sessionKey;
	}

	/**
	 * Удалить пользовательскую сессию.
	 * <p>
	 * Ищет пользовательскую сессию в карте {@link #userLoginMap} для данного
	 * ключа сессии {@code sessionKey}; удаляет её из карты и из БД. Кроме
	 * того, уведомляет об этом событии слушателей из списка
	 * {@link #loginProcessorListeners}.
	 * 
	 * @param sessionKey
	 *        Ключ сессии, которую надо удалить.
	 * @return {@code true}, если сессия с таким ключом найдена, в противном
	 *         случае - {@code false}.
	 */
	boolean removeUserLogin(final SessionKey sessionKey) {
		final UserLogin userLogin = this.userLoginMap.remove(sessionKey);
		if (userLogin == null) {
			return false;
		}

		this.userLoginDatabase.delete(userLogin);

		this.fireUserLoggedOut(userLogin);

		return true;
	}

	/**
	 * Получить период, через который пользователь должен прислать следующий
	 * контрольный вызов.
	 * <p>
	 * Этот метод также обновляет время последнего проявления активности данной
	 * сессии.
	 * 
	 * @param sessionKey
	 *        Ключ сессии.
	 * @return {@link #loginValidationPeriod}, если сессия с таким ключом
	 *         найдена, иначе - {@code -1}.
	 */
	long getLoginValidationPeriod(final SessionKey sessionKey) {
		final UserLogin userLogin = this.userLoginMap.get(sessionKey);
		if (userLogin == null) {
			return -1;
		}

		userLogin.updateLastActivityDate();
		try {
			this.userLoginDatabase.update(userLogin);
		} catch (final UpdateObjectException uoe) {
			Log.errorMessage(uoe);
		}

		return this.loginValidationPeriod;
	}

	/**
	 * Найти все пользовательские сессии, открытые пользователем с
	 * идентификатором {@code userId}.
	 * <p>
	 * Возвращаемый набор создаётся заново внутри метода, поэтому не отражает
	 * все последующие изменения числа сессий данного пользователя.
	 * 
	 * @param userId
	 *        Идентификатор пользователя.
	 * @return Набор сессий пользователя с идентификатором {@code userId}.
	 */
	Set<UserLogin> getUserLogins(final Identifier userId) {
		final Set<UserLogin> userLogins = new HashSet<UserLogin>();
		synchronized (this.userLoginMap) {
			for (final UserLogin userLogin : this.userLoginMap.values()) {
				if (userLogin.getSystemUserId().equals(userId)) {
					userLogins.add(userLogin);
				}
			}
		}
		return userLogins;
	}

	/**
	 * Вернуть набор всех пользовательских сессий.
	 * <p>
	 * Возвращаемый набор создаётся заново внутри метода, поэтому не отражает
	 * все последующие изменения числа сессий.
	 * 
	 * @return Набор всех сессий.
	 */
	Set<UserLogin> getUserLogins() {
		return new HashSet<UserLogin>(this.userLoginMap.values());
	}

	// По-видимому, этот метод не нужен.
	// /**
	// * Проверить, существует ли пользовательская сессия с таким ключом.
	// *
	// * @param sessionKey
	// * Ключ сессии.
	// * @return Если сессия существует - <code>true</code>, иначе -
	// * <code>false</code>.
	// */
	// boolean isUserLoginPresent(final SessionKey sessionKey) {
	// return this.userLoginMap.containsKey(sessionKey);
	// }

	/**
	 * Распечатать в красивом виде все открытые пользовательские сессии.
	 */
	void printUserLogins() {
		final StringBuffer stringBuffer = new StringBuffer("\n\t\t LoginProcessor.printUserLogins | Logged in:\n");
		int i = 0;
		synchronized (this.userLoginMap) {
			for (final UserLogin userLogin : this.userLoginMap.values()) {
				i++;
				final Identifier systemUserId = userLogin.getSystemUserId();
				SystemUser systemUser = null;
				try {
					if (LoginManager.isInitialized() && LoginManager.isLoggedIn()) {
						systemUser = StorableObjectPool.getStorableObject(systemUserId, true);
					} else {
						systemUser = this.systemUserDatabase.retrieveForId(systemUserId);
					}
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
				}

				// Порядковый номер
				stringBuffer.append("\n");
				stringBuffer.append(i);

				// Ключ соединения
				stringBuffer.append(".\t Session key: '");
				stringBuffer.append(userLogin.getSessionKey());
				stringBuffer.append("'\n");

				// Идентификатор пользователя
				stringBuffer.append("\t Id: '");
				stringBuffer.append(systemUserId);
				stringBuffer.append("'\n");

				// Имя пользователя
				stringBuffer.append("\t Login: '");
				stringBuffer.append((systemUser != null) ? systemUser.getLogin() : "NULL");
				stringBuffer.append("'\n");

				// Идентификатор домена
				stringBuffer.append("\t Domain id: '");
				stringBuffer.append(userLogin.getDomainId());
				stringBuffer.append("'\n");

				// Имя машины, с которой пользователь вошёл в систему
				// try {
				// final CommonUser commonUserRef =
				// CommonUserHelper.narrow(corbaServer.stringToObject(userLogin.getUserIOR()));
				// final String userHostName = commonUserRef.getHostName();
				// stringBuffer.append("\t From host: '");
				// stringBuffer.append(userHostName);
				// stringBuffer.append("'\n");
				// } catch (SystemException se) {
				// Log.errorMessage("Login us illegal -- " +
				// userLogin.getSessionKey());
				// }

				// Время входа в систему
				stringBuffer.append("\t From date: ");
				stringBuffer.append(userLogin.getLoginDate());
				stringBuffer.append("'\n");

				// Время последнего дейсвия пользователя в системе
				stringBuffer.append("\t Last active at: ");
				stringBuffer.append(userLogin.getLastActivityDate());
				stringBuffer.append("'\n");
			}
		}

		Log.debugMessage(stringBuffer.toString(), Log.DEBUGLEVEL08);
	}

	/**
	 * Добавить нового слушателя, получающего уведомления об изменении числа
	 * пользовательских сессий.
	 * 
	 * @param loginProcessorListener
	 *        Слушатель, которого надо добавить.
	 * @throws NullPointerException
	 *         Если слушатель - {@code null}.
	 */
	void addListener(final LoginProcessorListener loginProcessorListener) {
		if (loginProcessorListener == null) {
			throw new NullPointerException("Listener is null");
		}

		synchronized (this.loginProcessorListeners) {
			this.loginProcessorListeners.add(loginProcessorListener);
		}
	}

	/**
	 * Удалить слушателя, получающего уведомления об изменении числа
	 * пользовательских сессий.
	 * 
	 * @param loginProcessorListener
	 *        Слушатель, которого надо удалить.
	 * @throws NullPointerException
	 *         Если слушатель - {@code null}.
	 */
	void removeListener(final LoginProcessorListener loginProcessorListener) {
		if (loginProcessorListener == null) {
			throw new NullPointerException("Listener is null");
		}

		synchronized (this.loginProcessorListeners) {
			this.loginProcessorListeners.remove(loginProcessorListener);
		}
	}

	/**
	 * Уведомить всех слушателей из списка {@link #loginProcessorListeners}, о
	 * появлении новой пользовательской сессии {@code userLogin}.
	 * 
	 * @param userLogin
	 *        Новая пользовательская сессия.
	 */
	private void fireUserLoggedIn(final UserLogin userLogin) {
		if (userLogin == null) {
			return;
		}

		synchronized (this.loginProcessorListeners) {
			for (int i = 0; i < this.loginProcessorListeners.size(); i++) {
				final LoginProcessorListener loginProcessorListener = this.loginProcessorListeners.get(i);
				loginProcessorListener.userLoggedIn(userLogin);
			}
		}
	}

	/**
	 * Уведомить всех слушателей из списка {@link #loginProcessorListeners}, о
	 * закрытии пользовательской сессии {@code userLogin}.
	 * 
	 * @param userLogin
	 *        Закрытая пользовательская сессия.
	 */
	private void fireUserLoggedOut(final UserLogin userLogin) {
		if (userLogin == null) {
			return;
		}

		synchronized (this.loginProcessorListeners) {
			for (int i = 0; i < this.loginProcessorListeners.size(); i++) {
				final LoginProcessorListener loginProcessorListener = this.loginProcessorListeners.get(i);
				loginProcessorListener.userLoggedOut(userLogin);
			}
		}
	}
}
