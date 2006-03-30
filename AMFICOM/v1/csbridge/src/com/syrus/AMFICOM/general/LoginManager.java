/*-
 * $Id: LoginManager.java,v 1.48 2006/03/30 08:12:57 arseniy Exp $
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
 * @version $Revision: 1.48 $, $Date: 2006/03/30 08:12:57 $
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

	public static synchronized void init(final LoginPerformer loginPerformer1, final LoginRestorer loginRestorer1) {
		loginPerformer = loginPerformer1;
		loginRestorer = loginRestorer1;
	}

	public static synchronized void setLoginPerformer(final LoginPerformer loginPerformer1) {
		loginPerformer = loginPerformer1;
	}

	public static synchronized void setLoginRestorer(final LoginRestorer loginRestorer1) {
		loginRestorer = loginRestorer1;
	}

	public static synchronized Set<Domain> getAvailableDomains() throws CommunicationException, LoginException {
		return loginPerformer.getAvailableDomains();
	}

	public static synchronized void login(final String login, final String password, final Identifier loginDomainId)
			throws CommunicationException,
				LoginException {
		loginPerformer.login(login, password, loginDomainId);
	}

	public static synchronized void logout() throws CommunicationException, LoginException {
		loginPerformer.logout();
	}

	public static synchronized boolean isLoggedIn() {
		return loginPerformer.isLoggedIn();
	}

	/**
	 * @return <code>true</code>, только если
	 *         {@link LoginRestorer#restoreLogin()} вернул <code>true</code>,
	 *         т. е., когда пользователь или приложение решили восстановить
	 *         сессию.
	 * @throws LoginException
	 * @throws CommunicationException
	 */
	public static synchronized boolean restoreLogin() throws CommunicationException, LoginException {
		if (loginRestorer != null && loginRestorer.restoreLogin()) {
			loginPerformer.restoreLogin(loginRestorer.getLogin(), loginRestorer.getPassword());
			return true;
		}
		return false;
	}

	/**
	 * Изменить свой собственный пароль.
	 * 
	 * @param password
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public static synchronized void setPassword(final String password) throws CommunicationException, LoginException {
		loginPerformer.setPassword(password);
	}

	/**
	 * Изменить пароль пользователю.
	 * 
	 * @param systemUserId
	 * @param password
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public static synchronized void setPassword(final Identifier systemUserId, final String password)
			throws CommunicationException,
				LoginException {
		loginPerformer.setPassword(systemUserId, password);
	}

	public static synchronized SessionKey getSessionKey() {
		return loginPerformer.getSessionKey();
	}

	public static synchronized Identifier getUserId() {
		return loginPerformer.getUserId();
	}

	public static synchronized Identifier getDomainId() {
		return loginPerformer.getDomainId();
	}
}
