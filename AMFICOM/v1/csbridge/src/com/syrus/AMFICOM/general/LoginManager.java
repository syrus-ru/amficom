/*-
 * $Id: LoginManager.java,v 1.47 2006/03/13 08:42:13 arseniy Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.security.SessionKey;

/**
 * @version $Revision: 1.47 $, $Date: 2006/03/13 08:42:13 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class LoginManager {
	/**
	 * Основные действия по установлению сессии.
	 * Поведение реализовано в классах, расширяюющих интерфейс.
	 * Существующие реализации: CORBALoginPerformer, DatabaseLoginPerformer, XMLLoginPerformer.
	 */
	private static LoginPerformer loginPerformer;

	/**
	 * Восстановление утерянной сессии.
	 */
	private static LoginRestorer loginRestorer;

	private LoginManager() {
		assert false;
	}

	public static void init(final LoginPerformer loginPerformer1, final LoginRestorer loginRestorer1) {
		loginPerformer = loginPerformer1;
		loginRestorer = loginRestorer1;
	}

	public static void setLoginPerformer(final LoginPerformer loginPerformer1) {
		loginPerformer = loginPerformer1;
	}

	public static void setLoginRestorer(final LoginRestorer loginRestorer1) {
		loginRestorer = loginRestorer1;
	}

	public static Set<Domain> getAvailableDomains() throws CommunicationException, LoginException {
		return loginPerformer.getAvailableDomains();
	}

	public static void login(final String login, final String password, final Identifier loginDomainId)
			throws CommunicationException,
				LoginException {
		loginPerformer.login(login, password, loginDomainId);
	}

	public static void logout() throws CommunicationException, LoginException {
		loginPerformer.logout();
	}

	public static boolean isLoggedIn() {
		return loginPerformer.isLoggedIn();
	}

	/**
	 * @return true, only when LoginRestorer.restoreLogin() returned true,
	 * i.e., when user or application decided to restore login
	 * @throws LoginException
	 * @throws CommunicationException
	 */
	public static boolean restoreLogin() throws CommunicationException, LoginException {
		if (loginRestorer != null && loginRestorer.restoreLogin()) {
			loginPerformer.restoreLogin(loginRestorer.getLogin(), loginRestorer.getPassword());
			return true;
		}
		return false;
	}

	/**
	 * Set my own password
	 * @param password
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public static void setPassword(final String password) throws CommunicationException, LoginException {
		loginPerformer.setPassword(password);
	}

	/**
	 * set password to user
	 * @param systemUserId
	 * @param password
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public static void setPassword(final Identifier systemUserId, final String password)
			throws CommunicationException,
				LoginException {
		loginPerformer.setPassword(systemUserId, password);
	}

	public static SessionKey getSessionKey() {
		return loginPerformer.getSessionKey();
	}

	public static Identifier getUserId() {
		return loginPerformer.getUserId();
	}

	public static Identifier getDomainId() {
		return loginPerformer.getDomainId();
	}
}
