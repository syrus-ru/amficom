/*
 * $Id: LoginManager.java,v 1.25 2005/10/20 14:17:20 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKeyHolder;

/**
 * @version $Revision: 1.25 $, $Date: 2005/10/20 14:17:20 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class LoginManager {
	private static final IdlSessionKey EMPTY_SESSION_KEY_T = new IdlSessionKey("");

	private static LoginServerConnectionManager loginServerConnectionManager;
	private static SessionKey sessionKey;
	private static IdlSessionKey sessionKeyT;
	private static Identifier userId;
	private static Identifier domainId;

	public LoginManager() {
		// singleton
		assert false;
	}

	static {
		resetSessionKey();
	}

	public static void init(final LoginServerConnectionManager loginServerConnectionManager1) {
		loginServerConnectionManager = loginServerConnectionManager1;
	}


	/*
	 * @todo Write meaningful processing of all possible error codes
	 */
	public static Set<Domain> getAvailableDomains() throws CommunicationException, LoginException {
		final LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			final IdlDomain[] domainsT = loginServer.transmitAvailableDomains();
			return StorableObjectPool.fromTransferables(domainsT, true);
		} catch (AMFICOMRemoteException are) {
			throw new LoginException("Cannot get available domains -- " + are.message);
		} catch (ApplicationException ae) {
			throw new LoginException("Cannot get available domains -- " + ae.getMessage(), ae);
		}
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static void login(final String login, final String password, final Identifier loginDomainId)
			throws CommunicationException,
				LoginException {
		assert login != null : NON_NULL_EXPECTED;
		assert password != null : NON_NULL_EXPECTED;
		assert loginDomainId != null : NON_NULL_EXPECTED;

		if (sessionKeyT != EMPTY_SESSION_KEY_T) {
			throw new LoginException(I18N.getString("Error.AlreadyLogged"));
		}

		final LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			final String localHostName = InetAddress.getLocalHost().getHostName();
			final IdlSessionKeyHolder idlSessionKeyHolder = new IdlSessionKeyHolder();
			final IdlIdentifierHolder userIdHolder = new IdlIdentifierHolder();
			loginServer.login(login, password, loginDomainId.getTransferable(), localHostName, idlSessionKeyHolder, userIdHolder);

			sessionKeyT = idlSessionKeyHolder.value;
			sessionKey = new SessionKey(sessionKeyT);
			userId = new Identifier(userIdHolder.value);
			domainId = loginDomainId;
		} catch (final AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case IdlErrorCode._ERROR_ILLEGAL_LOGIN:
					throw new LoginException(I18N.getString("Error.IllegalLogin"));
				case IdlErrorCode._ERROR_ILLEGAL_PASSWORD:
					throw new LoginException(I18N.getString("Error.IllegalPassword"));
				case IdlErrorCode._ERROR_ACCESS_VALIDATION:
					throw new LoginException(I18N.getString("Error.AccessValidation"));
				case IdlErrorCode._ERROR_ALREADY_LOGGED:
					throw new LoginException(I18N.getString("Error.AlreadyLogged"));
				default:
					throw new LoginException(I18N.getString("Error.CannotLogin") + " -- " + are.message);
			}
		} catch (final SystemException se) {
			throw new LoginException(I18N.getString("Error.CannotLogin") + " -- " + se.getMessage());
		} catch (UnknownHostException uhe) {
			throw new LoginException(I18N.getString("Error.CannotLogin") + " -- " + uhe.getMessage());
		}
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static void logout() throws CommunicationException, LoginException {
		if (sessionKeyT == EMPTY_SESSION_KEY_T) {
			throw new LoginException(I18N.getString("Error.AlreadyLogged"));
		}

		final LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			loginServer.logout(sessionKeyT);
			resetSessionKey();
		} catch (AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case IdlErrorCode._ERROR_NOT_LOGGED_IN:
					throw new LoginException("Not logged in");
				default:
					throw new LoginException("Cannot logout -- " + are.message);
			}
		}
	}

	private static void resetSessionKey() {
		sessionKeyT = EMPTY_SESSION_KEY_T;
		sessionKey = new SessionKey(EMPTY_SESSION_KEY_T);
	}

	public static SessionKey getSessionKey() {
		return sessionKey;
	}

	public static IdlSessionKey getSessionKeyTransferable() {
		return sessionKeyT;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public static void setUserId(final Identifier userId1) {
		userId = userId1;
	}
	
	public static Identifier getUserId() {
		return userId;
	}

	public static Identifier getDomainId() {
		return domainId;
	}

}
