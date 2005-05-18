/*
 * $Id: LoginProcessor.java,v 1.7 2005/05/18 13:29:31 bass Exp $
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

import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.UserLogin;
import com.syrus.AMFICOM.security.UserLoginDatabase;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2005/05/18 13:29:31 $
 * @author $Author: bass $
 * @module leserver_v1
 */
final class LoginProcessor extends SleepButWorkThread {
	public static final String KEY_LOGIN_PROCESSOR_TICK_TIME = "LoginProcessorTickTime";
	public static final String KEY_LOGIN_PROCESSOR_MAX_FALLS = "LoginProcessorMaxFalls";
	public static final String KEY_MAX_USER_UNACTIVITY_PERIOD = "MaxUserUnactivityPeriod";

	public static final int LOGIN_PROCESSOR_TICK_TIME = 5;	//sec
	public static final int MAX_USER_UNACTIVITY_PERIOD = 1;	//hour

	private static Map loginMap;		//Map <SessionKey sessionKey, UserLogin userLogin>
	private long maxUserUnactivityPeriod;
	private UserLoginDatabase userLoginDatabase;
	private boolean running;

	public LoginProcessor() {
		super(ApplicationProperties.getInt(KEY_LOGIN_PROCESSOR_TICK_TIME, LOGIN_PROCESSOR_TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_LOGIN_PROCESSOR_MAX_FALLS, MAX_FALLS));

		if (loginMap == null)
			loginMap = Collections.synchronizedMap(new HashMap());

		this.maxUserUnactivityPeriod = ApplicationProperties.getInt(KEY_MAX_USER_UNACTIVITY_PERIOD, MAX_USER_UNACTIVITY_PERIOD) * 60 * 60 * 1000;
		this.userLoginDatabase = new UserLoginDatabase();
		this.running = true;

		this.restoreState();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				LoginProcessor.this.shutdown();
			}
		});
	}

	private void restoreState() {
		try {
			final Set userLogins = this.userLoginDatabase.retrieveAll();
			for (Iterator it = userLogins.iterator(); it.hasNext();) {
				final UserLogin userLogin = (UserLogin) it.next();
				loginMap.put(userLogin.getSessionKey(), userLogin);
			}
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
	}

	public void run() {
		while (this.running) {

			synchronized (loginMap) {
				for (Iterator it = loginMap.keySet().iterator(); it.hasNext();) {
					final SessionKey sessionKey = (SessionKey) it.next();
					final UserLogin userLogin = (UserLogin) loginMap.get(sessionKey);
					final Date lastActivityDate = userLogin.getLastActivityDate();
					if (System.currentTimeMillis() - lastActivityDate.getTime() >= this.maxUserUnactivityPeriod) {
						Log.debugMessage("User '" + userLogin.getUserId() + "' unactive more, than "
								+ (this.maxUserUnactivityPeriod / (60 * 60 * 1000)) + " hours. Deleting login", Log.DEBUGLEVEL06);
						this.userLoginDatabase.delete(userLogin);
						it.remove();
					}
				}
			}

			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}

	protected static void addUserLogin(final UserLogin userLogin) {
		Log.debugMessage("LoginProcessor.addUserLogin | Adding login for user '" + userLogin.getUserId() + "'", Log.DEBUGLEVEL08);
		loginMap.put(userLogin.getSessionKey(), userLogin);
	}

	protected static UserLogin getUserLogin(final SessionKey sessionKey) {
		return (UserLogin) loginMap.get(sessionKey);
	}

	protected static UserLogin removeUserLogin(final SessionKey sessionKey) {
		return (UserLogin) loginMap.remove(sessionKey);
	}

	protected void shutdown() {
		this.running = false;
	}

}
