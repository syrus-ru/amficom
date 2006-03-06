/*-
 * $Id: CORBALoginPerformer.java,v 1.4 2006/03/06 14:58:47 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.security.SessionKey.VOID_SESSION_KEY;

import java.util.Set;

import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonUser;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKeyHolder;

/**
 * @version $Revision: 1.4 $, $Date: 2006/03/06 14:58:47 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class CORBALoginPerformer implements LoginPerformer {
	private LoginServerConnectionManager loginServerConnectionManager;
	private CommonUser commonUser;

	private SessionKey sessionKey;
	private Identifier userId;
	private Identifier domainId;

	public CORBALoginPerformer(final LoginServerConnectionManager loginServerConnectionManager, final CommonUser commonUser) {
		this.loginServerConnectionManager = loginServerConnectionManager;
		this.commonUser = commonUser;

		this.reset();
		this.domainId = VOID_IDENTIFIER;
	}

	public Set<Domain> getAvailableDomains() throws CommunicationException, LoginException {
		final LoginServer loginServer = this.loginServerConnectionManager.getLoginServerReference();
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

	public void login(final String login, final String password, final Identifier loginDomainId)
			throws CommunicationException,
				LoginException {
		assert login != null : NON_NULL_EXPECTED;
		assert password != null : NON_NULL_EXPECTED;
		assert loginDomainId != null : NON_NULL_EXPECTED;
		assert loginDomainId.getMajor() == DOMAIN_CODE : ILLEGAL_ENTITY_CODE;

		if (this.isLoggedIn()) {
			throw new LoginException(I18N.getString("Error.AlreadyLoggedIn"), true);
		}

		final LoginServer loginServer = this.loginServerConnectionManager.getLoginServerReference();
		try {
			final IdlSessionKeyHolder idlSessionKeyHolder = new IdlSessionKeyHolder();
			final IdlIdentifierHolder userIdHolder = new IdlIdentifierHolder();
			loginServer.login(login,
					password,
					loginDomainId.getIdlTransferable(),
					this.commonUser,
					idlSessionKeyHolder,
					userIdHolder);

			this.sessionKey = new SessionKey(idlSessionKeyHolder.value);
			this.userId = Identifier.valueOf(userIdHolder.value);
			this.domainId = loginDomainId;
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

	public void logout() throws CommunicationException, LoginException {
		if (!this.isLoggedIn()) {
			throw new LoginException(I18N.getString("Error.AlreadyLoggedOut"));
		}

		final LoginServer loginServer = this.loginServerConnectionManager.getLoginServerReference();
		try {
			loginServer.logout(this.sessionKey.getIdlTransferable());
		} catch (AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case IdlErrorCode._ERROR_NOT_LOGGED_IN:
					throw new LoginException("Not logged in");
				default:
					throw new LoginException("Cannot logout -- " + are.message);
			}
		} finally {
			this.reset();
		}
	}

	public boolean isLoggedIn() {
		final boolean loggedIn = (this.sessionKey != VOID_SESSION_KEY);
		assert loggedIn
				? this.userId.getMajor() == SYSTEMUSER_CODE && this.domainId.getMajor() == DOMAIN_CODE
				: this.userId.isVoid()
						: this.userId + "; " + this.domainId + "; logged in: " + loggedIn;
		return loggedIn;
	}

	public void restoreLogin(final String login, final String password) throws CommunicationException, LoginException {
		this.reset();
		this.login(login, password, this.domainId);
	}

	/**
	 * @throws IllegalStateException if not logged in.
	 */
	public SessionKey getSessionKey() {
		if (this.isLoggedIn()) {
			return this.sessionKey;
		}
		throw new IllegalStateException();
	}

	/**
	 * @throws IllegalStateException if not logged in.
	 */
	public Identifier getUserId() {
		if (this.isLoggedIn()) {
			return this.userId;
		}
		throw new IllegalStateException();
	}

	/**
	 * @throws IllegalStateException if not logged in.
	 */
	public Identifier getDomainId() {
		if (this.isLoggedIn()) {
			return this.domainId;
		}
		throw new IllegalStateException();
	}

	public void setPassword(final String password) throws CommunicationException, LoginException {
		try {
			this.loginServerConnectionManager.getLoginServerReference().setPassword(this.sessionKey.getIdlTransferable(), this.userId.getIdlTransferable(), password);
		} catch (AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case IdlErrorCode._ERROR_NOT_LOGGED_IN:
					throw new LoginException("Not logged in");
				default:
					throw new LoginException("Cannot set password -- " + are.message);
			}
		}
	}

	/**
	 * @throws IllegalArgumentException if systemUserId is not system user id
	 */
	public void setPassword(final Identifier systemUserId, final String password) throws CommunicationException, LoginException {
		if (systemUserId.getMajor() != ObjectEntities.SYSTEMUSER_CODE) {
			throw new IllegalArgumentException("System user identifier expected.");
		}

		final IdlIdentifier userIdTransferable = systemUserId.getIdlTransferable();
		try {
			this.loginServerConnectionManager.getLoginServerReference().setPassword(this.sessionKey.getIdlTransferable(), userIdTransferable, password);
		} catch (AMFICOMRemoteException are) {
			switch (are.errorCode.value()) {
				case IdlErrorCode._ERROR_NOT_LOGGED_IN:
					throw new LoginException("Not logged in");
				default:
					throw new LoginException("Cannot set password -- " + are.message);
			}
		}
	}

	private void reset() {
		this.sessionKey = VOID_SESSION_KEY;
		this.userId = VOID_IDENTIFIER;
	}

}
