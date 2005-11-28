/*-
 * $Id: LoginManager.java,v 1.35 2005/11/28 12:18:43 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.Set;

import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonUser;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKeyHolder;

/**
 * @version $Revision: 1.35 $, $Date: 2005/11/28 12:18:43 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class LoginManager {
	/*-********************************************************************
	 * Constants.                                                         *
	 **********************************************************************/

	private static final IdlSessionKey EMPTY_IDL_SESSION_KEY = new IdlSessionKey("");
	private static final SessionKey EMPTY_SESSION_KEY = new SessionKey(EMPTY_IDL_SESSION_KEY);

	/*-********************************************************************
	 * Static immutable data.                                             *
	 **********************************************************************/

	private static LoginServerConnectionManager loginServerConnectionManager;
	private static LoginRestorer loginRestorer;

	/*-********************************************************************
	 * Stateful data.                                                     *
	 **********************************************************************/

	private static SessionKey sessionKey;
	private static IdlSessionKey idlSessionKey;
	private static Identifier userId;
	private static Identifier domainId;
	private static CommonUser commonUser;

	private LoginManager() {
		assert false;
	}

	static {
		resetSessionKey();
	}

	public static void init(final LoginServerConnectionManager loginServerConnectionManager1,
			final CommonUser commonUser1,
			final LoginRestorer loginRestorer1) {
		loginServerConnectionManager = loginServerConnectionManager1;
		commonUser = commonUser1;
		loginRestorer = loginRestorer1;
	}


	/*
	 * @todo Write meaningful processing of all possible error codes
	 */
	public static Set<Domain> getAvailableDomains() throws CommunicationException, LoginException {
		final LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			final IdlDomain[] domainsT = loginServer.transmitAvailableDomains();
			final Set<Domain> availableDomains = StorableObjectPool.fromTransferables(domainsT, true);
			StorableObjectPool.putStorableObjects(availableDomains);
			return availableDomains;
		} catch (AMFICOMRemoteException are) {
			throw new LoginException("Cannot get available domains -- " + are.message);
		} catch (ApplicationException ae) {
			throw new LoginException("Cannot get available domains -- " + ae.getMessage(), ae);
		}
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static void login(final String login,
			final String password,
			final Identifier loginDomainId) throws CommunicationException, LoginException {
		assert login != null : NON_NULL_EXPECTED;
		assert password != null : NON_NULL_EXPECTED;
		assert loginDomainId != null : NON_NULL_EXPECTED;

		if (isLoggedIn()) {
			throw new LoginException(I18N.getString("Error.AlreadyLoggedIn"), true);
		}

		final LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			final IdlSessionKeyHolder idlSessionKeyHolder = new IdlSessionKeyHolder();
			final IdlIdentifierHolder userIdHolder = new IdlIdentifierHolder();
			loginServer.login(login, password, loginDomainId.getTransferable(), commonUser, idlSessionKeyHolder, userIdHolder);

			idlSessionKey = idlSessionKeyHolder.value;
			sessionKey = new SessionKey(idlSessionKey);
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
					throw new LoginException(I18N.getString("Error.AlreadyLoggedIn"));
				default:
					throw new LoginException(I18N.getString("Error.CannotLogin") + " -- " + are.message);
			}
		} catch (final SystemException se) {
			throw new LoginException(I18N.getString("Error.CannotLogin") + " -- " + se.getMessage());
		}
	}

	/*
	 * @todo Write meaningful processing of all possible error codes
	 * */
	public static void logout() throws CommunicationException, LoginException {
		if (!isLoggedIn()) {
			throw new LoginException(I18N.getString("Error.AlreadyLoggedOut"));
		}

		final LoginServer loginServer = loginServerConnectionManager.getLoginServerReference();
		try {
			loginServer.logout(idlSessionKey);
		} catch (AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case IdlErrorCode._ERROR_NOT_LOGGED_IN:
					throw new LoginException("Not logged in");
				default:
					throw new LoginException("Cannot logout -- " + are.message);
			}
		} finally {
			resetSessionKey();
		}
	}

	/**
	 * @return true, only when LoginRestorer.restoreLogin() returned true,
	 * i.e., when user or application decided to restore login
	 * @throws LoginException
	 * @throws CommunicationException
	 */
	public static boolean restoreLogin() throws CommunicationException, LoginException {
		resetSessionKey();
		if (loginRestorer != null && loginRestorer.restoreLogin()) {
			login(loginRestorer.getLogin(), loginRestorer.getPassword(), loginRestorer.getDomainId());
			return true;
		}
		return false;
	}

	private static void resetSessionKey() {
		idlSessionKey = EMPTY_IDL_SESSION_KEY;
		sessionKey = EMPTY_SESSION_KEY;
		userId = VOID_IDENTIFIER;
		domainId = VOID_IDENTIFIER;
	}

	/**
	 * @throws IllegalStateException if not logged in.
	 */
	public static SessionKey getSessionKey() {
		if (isLoggedIn()) {
			return sessionKey;
		}
		throw new IllegalStateException();
	}

	/**
	 * @throws IllegalStateException if not logged in.
	 */
	public static IdlSessionKey getIdlSessionKey() {
		if (isLoggedIn()) {
			return idlSessionKey;
		}
		throw new IllegalStateException();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public static void setUserId(final Identifier userId1) {
		if (userId1 == null) {
			throw new NullPointerException();
		} else if (userId1.getMajor() != SYSTEMUSER_CODE || userId1.isVoid()) {
			throw new IllegalArgumentException(userId1.toString());
		}

		userId = userId1;
	}

	/**
	 * @throws IllegalStateException if not logged in.
	 */
	public static Identifier getUserId() {
		if (isLoggedIn()) {
			return userId;
		}
		throw new IllegalStateException();
	}

	/**
	 * @throws IllegalStateException if not logged in.
	 */
	public static Identifier getDomainId() {
		if (isLoggedIn()) {
			return domainId;
		}
		throw new IllegalStateException();
	}

	public static boolean isLoggedIn() {
		final boolean loggedIn = (idlSessionKey != EMPTY_IDL_SESSION_KEY);
		assert loggedIn
				? userId.getMajor() == SYSTEMUSER_CODE && domainId.getMajor() == DOMAIN_CODE
				: userId.isVoid() && domainId.isVoid()
						: userId + "; " + domainId + "; logged in: " + loggedIn;
		return loggedIn;
	}
}
