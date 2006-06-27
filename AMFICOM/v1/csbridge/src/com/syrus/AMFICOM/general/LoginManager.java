/*-
 * $Id: LoginManager.java,v 1.49.2.1 2006/06/27 15:57:10 arseniy Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;

import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.security.SessionKey;

/**
 * @version $Revision: 1.49.2.1 $, $Date: 2006/06/27 15:57:10 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class LoginManager {
	/**
	 * �������� �������� �� ������������ ������. ��������� ����������� �
	 * �������, ������������ ���������. ������������ ����������:
	 * <ul>
	 * <li> CORBALoginPerformer;
	 * <li> DatabaseLoginPerformer;
	 * <li> XMLLoginPerformer.
	 * </ul>
	 */
	private static LoginPerformer loginPerformer;

	/**
	 * �������������� ��������� ������.
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

	public static boolean isInitialized() {
		return loginPerformer != null;
	}

	public static synchronized Set<Domain> getAvailableDomains() throws CommunicationException, LoginException {
		if (!isInitialized()) {
			throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
		}

		return loginPerformer.getAvailableDomains();
	}

	public static synchronized void login(final String login, final String password, final Identifier loginDomainId)
			throws CommunicationException,
				LoginException {
		if (!isInitialized()) {
			throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
		}

		loginPerformer.login(login, password, loginDomainId);
	}

	public static synchronized void logout() throws CommunicationException, LoginException {
		if (!isInitialized()) {
			throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
		}

		loginPerformer.logout();
	}

	public static synchronized boolean isLoggedIn() {
		if (!isInitialized()) {
			throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
		}

		return loginPerformer.isLoggedIn();
	}

	/**
	 * @return <code>true</code>, ������ ����
	 *         {@link LoginRestorer#restoreLogin()} ������ <code>true</code>,
	 *         �. �., ����� ������������ ��� ���������� ������ ������������
	 *         ������.
	 * @throws LoginException
	 * @throws CommunicationException
	 */
	public static synchronized boolean restoreLogin() throws CommunicationException, LoginException {
		if (!isInitialized()) {
			throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
		}

		if (loginRestorer != null && loginRestorer.restoreLogin()) {
			loginPerformer.restoreLogin(loginRestorer.getLogin(), loginRestorer.getPassword());
			return true;
		}
		return false;
	}

	/**
	 * �������� ���� ����������� ������.
	 * 
	 * @param password
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public static synchronized void setPassword(final String password) throws CommunicationException, LoginException {
		if (!isInitialized()) {
			throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
		}

		loginPerformer.setPassword(password);
	}

	/**
	 * �������� ������ ������������.
	 * 
	 * @param systemUserId
	 * @param password
	 * @throws CommunicationException
	 * @throws LoginException
	 */
	public static synchronized void setPassword(final Identifier systemUserId, final String password)
			throws CommunicationException,
				LoginException {
		if (!isInitialized()) {
			throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
		}

		loginPerformer.setPassword(systemUserId, password);
	}

	public static synchronized SessionKey getSessionKey() {
		if (!isInitialized()) {
			throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
		}

		return loginPerformer.getSessionKey();
	}

	public static synchronized Identifier getUserId() {
		if (!isInitialized()) {
			throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
		}

		return loginPerformer.getUserId();
	}

	public static synchronized Identifier getDomainId() {
		if (!isInitialized()) {
			throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
		}

		return loginPerformer.getDomainId();
	}
}
