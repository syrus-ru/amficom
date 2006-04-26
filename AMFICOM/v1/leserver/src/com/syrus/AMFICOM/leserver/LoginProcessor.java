/*-
 * $Id: LoginProcessor.java,v 1.39 2006/04/26 16:35:40 bass Exp $
 *
 * Copyright © 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.AMFICOM.security.UserLoginDatabase;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.39 $, $Date: 2006/04/26 16:35:40 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LoginProcessor extends SleepButWorkThread {
	public static final String KEY_LOGIN_PROCESSOR_TICK_TIME = "LoginProcessorTickTime";
	public static final String KEY_LOGIN_PROCESSOR_MAX_FALLS = "LoginProcessorMaxFalls";
	public static final String KEY_MAX_USER_UNACTIVITY_PERIOD = "MaxUserUnactivityPeriod";
	public static final String KEY_LOGIN_VALIDATION_TIMEOUT = "LoginValidationTimeout";

	public static final int DEFAULT_LOGIN_PROCESSOR_TICK_TIME = 5;	//sec
	public static final int DEFAULT_MAX_USER_INACTIVITY_PERIOD = 1;	//minutes
	public static final int DEFAULT_LOGIN_VALIDATION_TIMEOUT = 5;	//minutes

	private static final UserLoginDatabase userLoginDatabase = new UserLoginDatabase();

	private static final Map<SessionKey, UserLogin> LOGIN_MAP =
			Collections.synchronizedMap(new HashMap<SessionKey, UserLogin>());

	private static final long MAX_USER_INACTIVITY_PERIOD =
			ApplicationProperties.getInt(KEY_MAX_USER_UNACTIVITY_PERIOD,
					DEFAULT_MAX_USER_INACTIVITY_PERIOD) * 60 * 1000;

	private static final long LOGIN_VALIDATION_TIMEOUT =
			ApplicationProperties.getInt(KEY_LOGIN_VALIDATION_TIMEOUT,
					DEFAULT_LOGIN_VALIDATION_TIMEOUT) * 60 * 1000;

	public LoginProcessor() {
		super(ApplicationProperties.getInt(KEY_LOGIN_PROCESSOR_TICK_TIME, DEFAULT_LOGIN_PROCESSOR_TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_LOGIN_PROCESSOR_MAX_FALLS, MAX_FALLS));
		super.setName("LoginProcessor");

		this.restoreState();
		printUserLogins();

		Runtime.getRuntime().addShutdownHook(new Thread("LoginProcessor -- shutdown hook") {
			@Override
			public void run() {
				LoginProcessor.this.shutdown();
			}
		});
	}

	private void restoreState() {
		try {
			final Set<UserLogin> userLogins = userLoginDatabase.retrieveAll();
			for (final UserLogin userLogin : userLogins) {
				LOGIN_MAP.put(userLogin.getSessionKey(), userLogin);
			}
		}
		catch (RetrieveObjectException roe) {
			Log.errorMessage(roe);
		}
	}

	@Override
	public void run() {
		while (!interrupted()) {

			synchronized (LOGIN_MAP) {
				for (final Iterator<UserLogin> it = LOGIN_MAP.values().iterator(); it.hasNext();) {
					final UserLogin userLogin = it.next();
					final Date lastActivityDate = userLogin.getLastActivityDate();
					if (System.currentTimeMillis() - lastActivityDate.getTime() >= MAX_USER_INACTIVITY_PERIOD + LOGIN_VALIDATION_TIMEOUT) {
						Log.debugMessage("User '" + userLogin.getUserId() + "' unactive more, than ("
								+ (MAX_USER_INACTIVITY_PERIOD / (60 * 1000)) + " + " + (LOGIN_VALIDATION_TIMEOUT / (60 * 1000))
								+ ") minutes. Deleting login", Log.DEBUGLEVEL06);
						userLoginDatabase.delete(userLogin);
						it.remove();
					}
				}
			}

			try {
				sleep(super.initialTimeToSleep);
			} catch (final InterruptedException ie) {
				return;
			}
		}
	}

	@Override
	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			default:
				Log.errorMessage("Unknown error code: " + super.fallCode);
		}
	}

	static SessionKey addUserLogin(final Identifier userId, final Identifier domainId, final String userIOR) {
		Log.debugMessage("Adding login for user '" + userId + "' to domain '" + domainId + "'", Log.DEBUGLEVEL08);
		final UserLogin userLogin = UserLogin.createInstance(userId, domainId, userIOR);
		LOGIN_MAP.put(userLogin.getSessionKey(), userLogin);
		try {
			userLoginDatabase.insert(userLogin);
		} catch (CreateObjectException coe) {
			Log.errorMessage(coe);
		}
		printUserLogins();
		return userLogin.getSessionKey();
	}

	static boolean removeUserLogin(final SessionKey sessionKey) {
		Log.debugMessage("Removing login for session key '" + sessionKey + "'", Log.DEBUGLEVEL08);
		final UserLogin userLogin = LOGIN_MAP.remove(sessionKey);
		if (userLogin == null) {
			return false;
		}

		userLoginDatabase.delete(userLogin);
		printUserLogins();
		return true;
	}


	static long getLoginValidationTimeout(final SessionKey sessionKey) {
		final UserLogin userLogin = LOGIN_MAP.get(sessionKey);
		if (userLogin == null) {
			return -1;
		}

		userLogin.updateLastActivityDate();
		try {
			userLoginDatabase.update(userLogin);
		} catch (final UpdateObjectException uoe) {
			Log.errorMessage(uoe);
		}

		return LOGIN_VALIDATION_TIMEOUT;
	}

	static boolean isUserLoginPresent(final SessionKey sessionKey) {
		return LOGIN_MAP.containsKey(sessionKey);
	}

	static void updateUserLoginLastActivityDate(final SessionKey sessionKey) {
		final UserLogin userLogin = LOGIN_MAP.get(sessionKey);
		userLogin.updateLastActivityDate();
		try {
			userLoginDatabase.update(userLogin);
		} catch (final UpdateObjectException uoe) {
			Log.errorMessage(uoe);
		}
	}

	static UserLogin getUserLogin(final SessionKey sessionKey) {
		Log.debugMessage("Getting login for session key '" + sessionKey + "'; found: " + LOGIN_MAP.containsKey(sessionKey),
				Log.DEBUGLEVEL08);
		return LOGIN_MAP.get(sessionKey);
	}

	/**
	 * Returns all user logins that correspond to the user identified by
	 * {@code userId} if he is currently logged in, or an empty {@code Set}
	 * if he is not. The {@code Set} returned is a newly created one, and
	 * does not reflect possible future logins and logouts.
	 *
	 * @param userId
	 * @return all user logins that correspond to the user identified by
	 *         {@code userId} if he is currently logged in, or an empty
	 *         {@code Set} if he is not.
	 * @see #getUserLogins()
	 */
	static Set<UserLogin> getUserLogins(final Identifier userId) {
		final Set<UserLogin> userLogins = new HashSet<UserLogin>();
		for (final UserLogin userLogin : LOGIN_MAP.values()) {
			if (userLogin.getUserId().equals(userId)) {
				userLogins.add(userLogin);
			}
		}
		return userLogins;
	}

	/**
	 * Returns user logins that correspond to <em>all</em> users currently
	 * logged in, as a newly created {@code Set} (does not reflect possible
	 * future logins and logouts).
	 *
	 * @return user logins that correspond to <em>all</em> users currently
	 *         logged in.
	 * @see #getUserLogins(Identifier)
	 */
	static Set<UserLogin> getUserLogins() {
		return new HashSet<UserLogin>(LOGIN_MAP.values());
	}

	private static void printUserLogins() {
		final StringBuffer stringBuffer = new StringBuffer("\n\t\t LoginProcessor.printUserLogins | Logged in:\n");
		int i = 0;
		synchronized (LOGIN_MAP) {
			for (final UserLogin userLogin : LOGIN_MAP.values()) {
				i++;
				final Identifier userId = userLogin.getUserId();
				SystemUser systemUser = null;
				try {
					systemUser = StorableObjectPool.getStorableObject(userId, true);
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
				}

				//	Порядковый номер
				stringBuffer.append("\n");
				stringBuffer.append(i);

				//	Ключ соединения
				stringBuffer.append(".\t Session key: '");
				stringBuffer.append(userLogin.getSessionKey());
				stringBuffer.append("'\n");

				//	Идентификатор пользователя
				stringBuffer.append("\t Id: '");
				stringBuffer.append(userId);
				stringBuffer.append("'\n");

				//	Имя пользователя
				stringBuffer.append("\t Login: '");
				stringBuffer.append((systemUser != null) ? systemUser.getLogin() : "NULL");
				stringBuffer.append("'\n");

				//	Идентификатор домена
				stringBuffer.append("\t Domain id: '");
				stringBuffer.append(userLogin.getDomainId());
				stringBuffer.append("'\n");

				//	Имя машины, с которой пользователь вошёл в систему
//				try {
//					final CommonUser commonUserRef = CommonUserHelper.narrow(corbaServer.stringToObject(userLogin.getUserIOR()));
//					final String userHostName = commonUserRef.getHostName();
//					stringBuffer.append("\t From host: '");
//					stringBuffer.append(userHostName);
//					stringBuffer.append("'\n");
//				} catch (SystemException se) {
//					Log.errorMessage("Login us illegal -- " + userLogin.getSessionKey());
//				}

				//	Время входа в систему
				stringBuffer.append("\t From date: ");
				stringBuffer.append(userLogin.getLoginDate());
				stringBuffer.append("'\n");

				//	Время последнего дейсвия пользователя в системе
				stringBuffer.append("\t Last active at: ");
				stringBuffer.append(userLogin.getLastActivityDate());
				stringBuffer.append("'\n");
			}
		}
		
		Log.debugMessage(stringBuffer.toString(), Log.DEBUGLEVEL08);
	}

	protected void shutdown() {
		this.interrupt();
	}
}
