/*-
 * $Id: LoginProcessor.java,v 1.1.1.1 2006/06/27 08:36:49 cvsadmin Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
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
 * ���������� ���������������� ������ �� ������� �������.
 * <p>
 * ���������� ������� "��������". ����� �� ����� ������ ������� �
 * �������� ����������, ������� ��������� ������� {@link #createInstance()} �
 * ������ �� ������� ����� �������� � ������� ������ {@link #getInstance()}.
 * <p>
 * ������ ����� ������ ������� �����, ������� ������������ ��������� �����
 * ���������������� ������ {@link #userLoginMap} �� ������� ������� � ���
 * "ͣ�����", �. �. - ��������� ������. ����� ������ ���������. ��� ���������
 * �������� ��������� ��������� �������� ������� ������������� ��� ����������
 * ������ ͣ����� CORBA. ��. ����� {@link #run()}. ��� ���������
 * ���������������� ������ ���������� � ���� ������; ��� ����������� ������� ��
 * �� ����������� ��������� ������ ������ �� ����� ����������.
 * <p>
 * � ���� ������ ����������� ��������� ���������� � �������� ����������������
 * ������ (��������������, ������
 * {@link #addUserLogin(Identifier, Identifier, String)} � {@link #removeUserLogin(SessionKey)}),
 * ��������� �������� � ������� (������ {@link #getUserLogins()},
 * {@link #getUserLogins(Identifier)}) �, �������, ��������� ���������������
 * ������. ����� ����, �������� ���������� ����������, �������� �� �����������
 * ���������������� ������ ({@link #addListener(LoginProcessorListener)} �
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
	 * ������-��������. ��������� ������� {@link #createInstance()}. ������ ��
	 * ���� - ����� ����� {@link #getInstance()}.
	 */
	private static LoginProcessor instance;

	/**
	 * ����������� ����� ������ ����� ������.
	 */
	private final long tickTime;

	/**
	 * ������� ���� ������ ��� �������� ���������������� ������
	 * {@link UserLogin}.
	 */
	private final UserLoginDatabase userLoginDatabase;

	/**
	 * ����� ���������������� ������. �� ����� - ���������� ���� ������
	 * {@link SessionKey}, �� �������� - ���� ���������������� ������
	 * {@link UserLogin}.
	 */
	private final Map<SessionKey, UserLogin> userLoginMap;

	/**
	 * �����, � ������� �������� ����������� ������ �� ������������ ����� ��
	 * ��������� �� ������. �� ��������� ����� ������� ������ ������������ �����
	 * ������� ��� ͣ�����. � �������������.
	 */
	private final long maxUserInactivityTime;

	/**
	 * ������, � ������� ������������ ������ ����� ����������� ������ �� ������,
	 * ��� ����, ����� ������������ ����� ��������� ����� ������. �
	 * �������������.
	 */
	private final long loginValidationPeriod;

	/**
	 * ������ ����������, ������������� �� ����������� � ������/�������
	 * ������������� �&nbsp;�������/��&nbsp;�������.
	 * <p>
	 * ��������� ����� ���� �������� � ������� ������
	 * {@link #addListener(LoginProcessorListener)}, � ���̣� � ������� ������
	 * {@link #removeListener(LoginProcessorListener)}. ����� ��������� �����
	 * ���������������� ������ (�����
	 * {@link #addUserLogin(Identifier, Identifier, String)}), ��� ���������
	 * ����������� ������� {@link #fireUserLoggedIn(UserLogin)}.
	 * <p>
	 * ��� ��������� ����������������� �������� � �������
	 * {@link #fireUserLoggedIn(UserLogin)} �
	 * {@link #fireUserLoggedOut(UserLogin)} ������������ {@link ArrayList} �
	 * ������ � ��������� �� ������� {@link ArrayList#get(int)}. ��� �������,
	 * ��� ������� ����������.
	 */
	private final ArrayList<LoginProcessorListener> loginProcessorListeners;

	/**
	 * ��������������� ����. ������������ ��� ���������� ������������� �� �� �
	 * ���������, ����� ������ � ����� ���������� �� ������� ��������� ������ �
	 * �������.
	 */
	private final SystemUserDatabase systemUserDatabase;

	/**
	 * ������� ������-�������� ������� ������.
	 */
	public static synchronized void createInstance() {
		if (instance == null) {
			instance = new LoginProcessor();
		}
	}

	/**
	 * �������� ������ �� ������-�������� ������� ������.
	 * 
	 * @return ������ �� ���������� ���������������� ������.
	 * @throws IllegalStateException
	 *         ���� ������ �ݣ �� ��� ������ ������� {@link #createInstance()}.
	 */
	public static synchronized LoginProcessor getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Not initialized");
		}
		return instance;
	}

	/**
	 * �������� �����������.
	 * <p>
	 * ������ ���������:
	 * <ul>
	 * <li> ���������� ��� ���������� ���� � ��������� ��������;
	 * <li> ��������������� ��������� ���������������� ������ �� �����
	 * ����������� ���������� �������;
	 * <li> ��������� "����� �� ����������" ("shutdown hook"), �����������
	 * ���������� ����� ����������� ���������������� ������;
	 * <li> ������������� - ��������� ���������, ��������� �� ���������� �����
	 * ���������������� ������ � ���������� ���������� ���������� � ��������
	 * ������ ����.
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
	 * ������������ ���������������� ������ �� ��, ��� ��� ���� ��������� ���
	 * ��������� �������.
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
	 * ������� ���� ������.
	 * <p>
	 * ������������ (� �������� {@link #tickTime}) ��������� �����
	 * ���������������� ������ {@link #userLoginMap}. ���� � ��� ������,
	 * ���������� ������� �� ����������� � ������� �������
	 * {@link #maxUserInactivityTime} (� ��������� �� ������
	 * {@link #loginValidationPeriod}, � ������� ������������ �̣�
	 * ������������� �� ������). ������ ����� ������ �������������, ��� ������
	 * ����� ������ � ������� �������, � ���������� ţ �� ����� ��� ���� �
	 * ���������.
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
	 * �������� ����� ���������������� ������.
	 * <p>
	 * ������� ����� ������, �������� ţ � ����� ������ {@link #userLoginMap} �
	 * ��������� ţ � ��. ����� ����, ���������� �� ���� ������� ���������� ��
	 * ������ {@link #loginProcessorListeners}.
	 * 
	 * @param userId
	 *        ������������� ������������.
	 * @param domainId
	 *        ������������� ������.
	 * @param userIOR
	 *        IOR CORBA-���������� ������� ������������.
	 * @return ���� ����� ������.
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
	 * ������� ���������������� ������.
	 * <p>
	 * ���� ���������������� ������ � ����� {@link #userLoginMap} ��� �������
	 * ����� ������ {@code sessionKey}; ������� ţ �� ����� � �� ��. �����
	 * ����, ���������� �� ���� ������� ���������� �� ������
	 * {@link #loginProcessorListeners}.
	 * 
	 * @param sessionKey
	 *        ���� ������, ������� ���� �������.
	 * @return {@code true}, ���� ������ � ����� ������ �������, � ���������
	 *         ������ - {@code false}.
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
	 * �������� ������, ����� ������� ������������ ������ �������� ���������
	 * ����������� �����.
	 * <p>
	 * ���� ����� ����� ��������� ����� ���������� ���������� ���������� ������
	 * ������.
	 * 
	 * @param sessionKey
	 *        ���� ������.
	 * @return {@link #loginValidationPeriod}, ���� ������ � ����� ������
	 *         �������, ����� - {@code -1}.
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
	 * ����� ��� ���������������� ������, �������� ������������� �
	 * ��������������� {@code userId}.
	 * <p>
	 * ������������ ����� ��������� ������ ������ ������, ������� �� ��������
	 * ��� ����������� ��������� ����� ������ ������� ������������.
	 * 
	 * @param userId
	 *        ������������� ������������.
	 * @return ����� ������ ������������ � ��������������� {@code userId}.
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
	 * ������� ����� ���� ���������������� ������.
	 * <p>
	 * ������������ ����� ��������� ������ ������ ������, ������� �� ��������
	 * ��� ����������� ��������� ����� ������.
	 * 
	 * @return ����� ���� ������.
	 */
	Set<UserLogin> getUserLogins() {
		return new HashSet<UserLogin>(this.userLoginMap.values());
	}

	// ��-��������, ���� ����� �� �����.
	// /**
	// * ���������, ���������� �� ���������������� ������ � ����� ������.
	// *
	// * @param sessionKey
	// * ���� ������.
	// * @return ���� ������ ���������� - <code>true</code>, ����� -
	// * <code>false</code>.
	// */
	// boolean isUserLoginPresent(final SessionKey sessionKey) {
	// return this.userLoginMap.containsKey(sessionKey);
	// }

	/**
	 * ����������� � �������� ���� ��� �������� ���������������� ������.
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

				// ���������� �����
				stringBuffer.append("\n");
				stringBuffer.append(i);

				// ���� ����������
				stringBuffer.append(".\t Session key: '");
				stringBuffer.append(userLogin.getSessionKey());
				stringBuffer.append("'\n");

				// ������������� ������������
				stringBuffer.append("\t Id: '");
				stringBuffer.append(systemUserId);
				stringBuffer.append("'\n");

				// ��� ������������
				stringBuffer.append("\t Login: '");
				stringBuffer.append((systemUser != null) ? systemUser.getLogin() : "NULL");
				stringBuffer.append("'\n");

				// ������������� ������
				stringBuffer.append("\t Domain id: '");
				stringBuffer.append(userLogin.getDomainId());
				stringBuffer.append("'\n");

				// ��� ������, � ������� ������������ ��ۣ� � �������
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

				// ����� ����� � �������
				stringBuffer.append("\t From date: ");
				stringBuffer.append(userLogin.getLoginDate());
				stringBuffer.append("'\n");

				// ����� ���������� ������� ������������ � �������
				stringBuffer.append("\t Last active at: ");
				stringBuffer.append(userLogin.getLastActivityDate());
				stringBuffer.append("'\n");
			}
		}

		Log.debugMessage(stringBuffer.toString(), Log.DEBUGLEVEL08);
	}

	/**
	 * �������� ������ ���������, ����������� ����������� �� ��������� �����
	 * ���������������� ������.
	 * 
	 * @param loginProcessorListener
	 *        ���������, �������� ���� ��������.
	 * @throws NullPointerException
	 *         ���� ��������� - {@code null}.
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
	 * ������� ���������, ����������� ����������� �� ��������� �����
	 * ���������������� ������.
	 * 
	 * @param loginProcessorListener
	 *        ���������, �������� ���� �������.
	 * @throws NullPointerException
	 *         ���� ��������� - {@code null}.
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
	 * ��������� ���� ���������� �� ������ {@link #loginProcessorListeners}, �
	 * ��������� ����� ���������������� ������ {@code userLogin}.
	 * 
	 * @param userLogin
	 *        ����� ���������������� ������.
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
	 * ��������� ���� ���������� �� ������ {@link #loginProcessorListeners}, �
	 * �������� ���������������� ������ {@code userLogin}.
	 * 
	 * @param userLogin
	 *        �������� ���������������� ������.
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
