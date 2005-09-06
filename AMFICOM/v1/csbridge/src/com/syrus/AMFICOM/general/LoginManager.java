/*
 * $Id: LoginManager.java,v 1.20 2005/09/06 19:35:39 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.20 $, $Date: 2005/09/06 19:35:39 $
 * @author $Author: arseniy $
 * @module csbridge
 */
public final class LoginManager {
	private static LoginServerConnectionManager loginServerConnectionManager;
	private static LoginRestorer loginRestorer;
	private static SessionKey sessionKey;
	private static IdlSessionKey sessionKeyT;
	private static Identifier userId;
	private static Identifier domainId;

	public LoginManager() {
		// singleton
		assert false;
	}

	static {
		sessionKeyT = new IdlSessionKey("");
		sessionKey = new SessionKey(sessionKeyT);
	}

	public static void init(final LoginServerConnectionManager loginServerConnectionManager1, LoginRestorer loginRestorer1) {
		loginServerConnectionManager = loginServerConnectionManager1;
		loginRestorer = loginRestorer1;
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static void login(final String login, final String password) throws CommunicationException, LoginException {
		final LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			IdlIdentifierHolder userIdHolder = new IdlIdentifierHolder();
			sessionKeyT = loginServer.login(login, password, userIdHolder);
			sessionKey = new SessionKey(sessionKeyT);
			userId = new Identifier(userIdHolder.value);
		}
		catch (AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case IdlErrorCode._ERROR_ILLEGAL_LOGIN:
					throw new LoginException("Illegal login");
				case IdlErrorCode._ERROR_ILLEGAL_PASSWORD:
					throw new LoginException("Illegal password");
				case IdlErrorCode._ERROR_ALREADY_LOGGED:
					throw new LoginException("Already logged");
				default:
					throw new LoginException("Cannot login -- " + are.message);
			}
		}
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static void logout() throws CommunicationException, LoginException {
		final LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			loginServer.logout(sessionKey.getTransferable());
		}
		catch (AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case IdlErrorCode._ERROR_NOT_LOGGED_IN:
					throw new LoginException("Not logged in");
				default:
					throw new LoginException("Cannot logout -- " + are.message);
			}
		}
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static Set<Domain> getAvailableDomains() throws CommunicationException, LoginException {
		final LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			final IdlDomain[] domainsT = loginServer.transmitAvailableDomains(sessionKey.getTransferable());
			final Set<Domain> domains = new HashSet<Domain>(domainsT.length);
			for (int i = 0; i < domainsT.length; i++) {
				try {
					domains.add(new Domain(domainsT[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return domains;
		}
		catch (AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case IdlErrorCode._ERROR_NO_DOMAINS_AVAILABLE:
					throw new LoginException("No domains available");
				default:
					throw new LoginException("Cannot get available domains -- " + are.message);
			}
		}
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static void selectDomain(Identifier domainId1) throws CommunicationException {
		final LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			loginServer.selectDomain(sessionKey.getTransferable(), domainId1.getTransferable());
			domainId = domainId1;
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage(are.message);
		}
	}

	public static SessionKey getSessionKey() {
		return sessionKey;
	}

	public static IdlSessionKey getSessionKeyTransferable() {
		return sessionKeyT;
	}

	public static Identifier getUserId() {
		return userId;
	}

	public static Identifier getDomainId() {
		return domainId;
	}


	/**
	 *
	 * @return true, only when LoginRestorer.restoreLogin() returned true,
	 * i.e., when user or application decided to restore login
	 * @throws LoginException
	 * @throws CommunicationException
	 */
	public static boolean restoreLogin() throws LoginException, CommunicationException {
		if (loginRestorer != null && loginRestorer.restoreLogin()) {
			login(loginRestorer.getLogin(), loginRestorer.getPassword());
			return true;
		}
		return false;
	}

}
