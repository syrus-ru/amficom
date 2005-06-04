/*
 * $Id: LoginManager.java,v 1.10 2005/06/04 16:56:20 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/04 16:56:20 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public final class LoginManager {
	private static LoginServerConnectionManager loginServerConnectionManager;
	private static LoginRestorer loginRestorer;
	private static SessionKey sessionKey;
	private static SessionKey_Transferable sessionKeyT;
	private static Identifier userId;
	private static Identifier domainId;

	public LoginManager() {
		// singleton
		assert false;
	}

	public static void init(final LoginServerConnectionManager loginServerConnectionManager1, LoginRestorer loginRestorer1) {
		loginServerConnectionManager = loginServerConnectionManager1;
		loginRestorer = loginRestorer1;
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static void login(final String login, final String password) throws CommunicationException, LoginException {
		LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			Identifier_TransferableHolder userIdHolder = new Identifier_TransferableHolder();
			sessionKeyT = loginServer.login(login, password, userIdHolder);
			sessionKey = new SessionKey(sessionKeyT);
			userId = new Identifier(userIdHolder.value);
		}
		catch (AMFICOMRemoteException are) {
			switch (are.error_code.value()) {
				case ErrorCode._ERROR_ILLEGAL_LOGIN:
					throw new LoginException("Illegal login");
				case ErrorCode._ERROR_ILLEGAL_PASSWORD:
					throw new LoginException("Illegal password");
				case ErrorCode._ERROR_ALREADY_LOGGED:
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
		LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			loginServer.logout((SessionKey_Transferable) sessionKey.getTransferable());
		}
		catch (AMFICOMRemoteException are) {
			switch (are.error_code.value()) {
				case ErrorCode._ERROR_NOT_LOGGED_IN:
					throw new LoginException("Not logged in");
				default:
					throw new LoginException("Cannot logout -- " + are.message);
			}
		}
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static Set getAvailableDomains() throws CommunicationException, LoginException {
		LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			Domain_Transferable[] domainsT = loginServer.transmitAvailableDomains((SessionKey_Transferable) sessionKey.getTransferable());
			Set domains = new HashSet(domainsT.length);
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
			switch (are.error_code.value()) {
				case ErrorCode._ERROR_NO_DOMAINS_AVAILABLE:
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
		LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			loginServer.selectDomain((SessionKey_Transferable) sessionKey.getTransferable(),
					(Identifier_Transferable) domainId1.getTransferable());
			domainId = domainId1;
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage(are.message);
		}
	}

	public static SessionKey getSessionKey() {
		return sessionKey;
	}

	public static SessionKey_Transferable getSessionKeyTransferable() {
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
