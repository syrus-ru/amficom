/*
 * $Id: LoginProcessor.java,v 1.23 2005/10/30 15:20:46 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.ContextNameFactory;
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
 * @version $Revision: 1.23 $, $Date: 2005/10/30 15:20:46 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LoginProcessor extends SleepButWorkThread {
	public static final String KEY_LOGIN_PROCESSOR_TICK_TIME = "LoginProcessorTickTime";
	public static final String KEY_LOGIN_PROCESSOR_MAX_FALLS = "LoginProcessorMaxFalls";
	public static final String KEY_MAX_USER_UNACTIVITY_PERIOD = "MaxUserUnactivityPeriod";

	public static final int LOGIN_PROCESSOR_TICK_TIME = 5;	//sec
	public static final int MAX_USER_UNACTIVITY_PERIOD = 1;	//minutes

	private static final UserLoginDatabase userLoginDatabase = new UserLoginDatabase();

	private static Map<SessionKey, UserLogin> loginMap;
	private long maxUserUnactivityPeriod;
	private boolean running;

	public LoginProcessor() {
		super(ApplicationProperties.getInt(KEY_LOGIN_PROCESSOR_TICK_TIME, LOGIN_PROCESSOR_TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_LOGIN_PROCESSOR_MAX_FALLS, MAX_FALLS));

		if (loginMap == null) {
			loginMap = Collections.synchronizedMap(new HashMap<SessionKey, UserLogin>());
		}

		this.maxUserUnactivityPeriod = ApplicationProperties.getInt(KEY_MAX_USER_UNACTIVITY_PERIOD, MAX_USER_UNACTIVITY_PERIOD) * 60 * 1000;
		this.running = true;

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
				loginMap.put(userLogin.getSessionKey(), userLogin);
			}
		}
		catch (RetrieveObjectException roe) {
			assert Log.errorMessage(roe);
		}
	}

	@Override
	public void run() {
		while (this.running) {

			synchronized (loginMap) {
				for (final Iterator<SessionKey> it = loginMap.keySet().iterator(); it.hasNext();) {
					final SessionKey sessionKey = it.next();
					final UserLogin userLogin = loginMap.get(sessionKey);
					final Date lastActivityDate = userLogin.getLastActivityDate();
					if (System.currentTimeMillis() - lastActivityDate.getTime() >= this.maxUserUnactivityPeriod) {
						assert Log.debugMessage("User '" + userLogin.getUserId() + "' unactive more, than "
								+ (this.maxUserUnactivityPeriod / (60 * 1000)) + " minutes. Deleting login", Log.DEBUGLEVEL06);
						userLoginDatabase.delete(userLogin);
						it.remove();

						deactivateUserServant(userLogin);
					}
				}
			}

			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				assert Log.errorMessage(ie);
			}
		}
	}

	private void deactivateUserServant(final UserLogin userLogin) {
		try {
			final SystemUser user = StorableObjectPool.getStorableObject(userLogin.getUserId(), true);
			final int userSort = user.getSort().value();
			if (userSort == SystemUserSort._USER_SORT_REGULAR || userSort == SystemUserSort._USER_SORT_SYSADMIN) {
				final String servantName = userLogin.getSessionKey().toString()
						+ Identifier.SEPARATOR
						+ ContextNameFactory.generateContextName(userLogin.getUserHostName());
				final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
				corbaServer.deactivateServant(servantName, false);
			}
		} catch (ApplicationException ae) {
			assert Log.errorMessage(ae);
		}
	}

	@Override
	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			default:
				assert Log.errorMessage("Unknown error code: " + super.fallCode);
		}
	}

	static SessionKey addUserLogin(final Identifier userId, final Identifier domainId, final String userHostName) {
		assert Log.debugMessage("Adding login for user '" + userId + "' to domain '" + domainId + "'", Log.DEBUGLEVEL08);
		final UserLogin userLogin = UserLogin.createInstance(userId, domainId, userHostName);
		loginMap.put(userLogin.getSessionKey(), userLogin);
		try {
			userLoginDatabase.insert(userLogin);
		} catch (CreateObjectException coe) {
			assert Log.errorMessage(coe);
		}
		printUserLogins();
		return userLogin.getSessionKey();
	}

	static boolean removeUserLogin(final SessionKey sessionKey) {
		assert Log.debugMessage("Removing login for session key '" + sessionKey + "'", Log.DEBUGLEVEL08);
		final UserLogin userLogin = loginMap.remove(sessionKey);
		if (userLogin == null) {
			return false;
		}

		userLoginDatabase.delete(userLogin);
		printUserLogins();
		return true;
	}

	static boolean isUserLoginPresent(final SessionKey sessionKey) {
		return loginMap.containsKey(sessionKey);
	}

	static void updateUserLoginLastActivityDate(final SessionKey sessionKey) {
		final UserLogin userLogin = loginMap.get(sessionKey);
		userLogin.updateLastActivityDate();
		try {
			userLoginDatabase.update(userLogin);
		} catch (final UpdateObjectException uoe) {
			assert Log.errorMessage(uoe);
		}
		printUserLogins();
	}

	static UserLogin getUserLogin(final SessionKey sessionKey) {
		assert Log.debugMessage("Getting login for session key '" + sessionKey + "'; found: " + loginMap.containsKey(sessionKey),
				Log.DEBUGLEVEL08);
		return loginMap.get(sessionKey);
	}

	private static void printUserLogins() {
		final StringBuffer stringBuffer = new StringBuffer("\n\t\t LoginProcessor.printUserLogins | Logged in:\n");
		int i = 0;
		synchronized (loginMap) {
			for (final SessionKey sessionKey : loginMap.keySet()) {
				i++;
				final UserLogin userLogin = loginMap.get(sessionKey);
				final Identifier userId = userLogin.getUserId();
				SystemUser systemUser = null;
				try {
					systemUser = StorableObjectPool.getStorableObject(userId, true);
				} catch (ApplicationException ae) {
					assert Log.errorMessage(ae);
				}

				stringBuffer.append("\n");
				stringBuffer.append(i);
				stringBuffer.append(".\t Session key: '");
				stringBuffer.append(sessionKey);
				stringBuffer.append("'\n");
				stringBuffer.append("\t Id: '");
				stringBuffer.append(userId);
				stringBuffer.append("'\n");
				stringBuffer.append("\t Login: '");
				stringBuffer.append((systemUser != null) ? systemUser.getLogin() : "NULL");
				stringBuffer.append("'\n");
				stringBuffer.append("\t Domain id: '");
				stringBuffer.append(userLogin.getDomainId());
				stringBuffer.append("'\n");
				stringBuffer.append("\t From host: '");
				stringBuffer.append(userLogin.getUserHostName());
				stringBuffer.append("'\n");
				stringBuffer.append("\t From date: ");
				stringBuffer.append(userLogin.getLoginDate());
				stringBuffer.append("'\n");
				stringBuffer.append("\t Last active at: ");
				stringBuffer.append(userLogin.getLastActivityDate());
				stringBuffer.append("'\n");
			}
		}
		
		assert Log.debugMessage(stringBuffer.toString(), Log.DEBUGLEVEL08);
	}

	protected void shutdown() {
		this.running = false;
	}

}
