/*
 * $Id: LoginManager.java,v 1.5 2005/05/03 14:26:21 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.SessionKey_Transferable;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/03 14:26:21 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public final class LoginManager {
	private static LoginServerConnectionManager loginServerConnectionManager;
	private static SessionKey sessionKey;
	private static SessionKey_Transferable sessionKeyT;
	private static Identifier userId;
	private static Identifier domainId;
	private static boolean isLoggedIn;

	public LoginManager() {
		// singleton
		assert false;
	}

	public static void init(final LoginServerConnectionManager loginServerConnectionManager1) {
		loginServerConnectionManager = loginServerConnectionManager1;
		isLoggedIn = false;
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static void login(final String login, final String password) throws CommunicationException, LoginException {
		if (isLoggedIn)
			throw new LoginException("Already logged in, logout first");//AlreadyLoggedInException -- ?

		LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			Identifier_TransferableHolder userIdHolder = new Identifier_TransferableHolder();
			sessionKeyT = loginServer.login(login, password, userIdHolder);
			sessionKey = new SessionKey(sessionKeyT);
			userId = new Identifier(userIdHolder.value);
			isLoggedIn = true;
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
		if (!isLoggedIn)
			throw new LoginException("Not logged in");

		LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			loginServer.logout((SessionKey_Transferable) sessionKey.getTransferable());
			isLoggedIn = false;
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
}
