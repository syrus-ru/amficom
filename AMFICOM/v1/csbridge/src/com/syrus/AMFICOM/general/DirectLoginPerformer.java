/*-
 * $Id: DirectLoginPerformer.java,v 1.1 2006/03/13 08:37:58 arseniy Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.administration.SystemUserWrapper.COLUMN_LOGIN;
import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NOT_IMPLEMENTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.security.SessionKey.VOID_SESSION_KEY;

import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.ShadowDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2006/03/13 08:37:58 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class DirectLoginPerformer implements LoginPerformer {
	private final SessionKey sessionKey;
	private Identifier userId;
	private Identifier domainId;

	public DirectLoginPerformer() {
		this.sessionKey = VOID_SESSION_KEY;

		this.reset();
		this.domainId = VOID_IDENTIFIER;
	}

	public Set<Domain> getAvailableDomains() throws CommunicationException, LoginException {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	/**
	 * This method do not check password.
	 * If it cannot find system user for the login specified,
	 * it will throw an exception. 
	 * If it cannot find domain for the identifier specified,
	 * it will try to work with domainId == VOID_IDENTIFIER.
	 */
	public void login(final String login, final String password, final Identifier loginDomainId) throws CommunicationException, LoginException {
		assert login != null : NON_NULL_EXPECTED;
		assert password != null : NON_NULL_EXPECTED;
		assert loginDomainId != null : NON_NULL_EXPECTED;
		assert (loginDomainId.isVoid() || loginDomainId.getMajor() == DOMAIN_CODE) : ILLEGAL_ENTITY_CODE;

		Domain domain = null;
		SystemUser systemUser = null;

		try {
			if (!loginDomainId.isVoid()) {
				domain = StorableObjectPool.getStorableObject(loginDomainId, true);
			} else {
				final Set<Domain> domains = StorableObjectPool.getStorableObjectsByCondition(new EquivalentCondition(DOMAIN_CODE),
						true);
				if (!domains.isEmpty()) {
					domain = domains.iterator().next();
				} else {
					Log.errorMessage("No available domains, will try to work with domainId == VOID_IDENTIFIER");
				}
			}
		} catch (ApplicationException ae) {
			if (ae instanceof LoginException) {
				throw (LoginException) ae;
			}
			throw new LoginException(ae);
		}

		final TypicalCondition condition = new TypicalCondition(login,
				OPERATION_EQUALS,
				SYSTEMUSER_CODE,
				COLUMN_LOGIN);
		try {
			final Set<SystemUser> systemUsers = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if (systemUsers.isEmpty()) {
				throw new LoginException("User not found -- '" + login + "'");
			}
			systemUser = systemUsers.iterator().next();
		} catch (ApplicationException ae) {
			if (ae instanceof LoginException) {
				throw (LoginException) ae;
			}
			throw new LoginException(ae);
		}

		if (systemUser == null) {
			throw new LoginException("Cannot find user");
		}
		this.userId = systemUser.getId();
		if (domain != null) {
			this.domainId = domain.getId();
		} else {
			this.domainId = VOID_IDENTIFIER;
		}
	}

	public void logout() throws CommunicationException, LoginException {
		this.reset();
	}

	public boolean isLoggedIn() {
		return (this.userId != VOID_IDENTIFIER);
	}

	public void restoreLogin(final String login, final String password) throws CommunicationException, LoginException {
		throw new UnsupportedOperationException(NOT_IMPLEMENTED);
	}

	public SessionKey getSessionKey() {
		return this.sessionKey;
	}

	public Identifier getUserId() {
		return this.userId;
	}

	public Identifier getDomainId() {
		return this.domainId;
	}

	public void setPassword(final String password) throws CommunicationException, LoginException {
		final ShadowDatabase shadowDatabase = new ShadowDatabase();
		try {
			shadowDatabase.updateOrInsert(this.userId, password);
		} catch (UpdateObjectException uoe) {
			throw new LoginException(uoe);
		}
	}

	public void setPassword(final Identifier systemUserId, final String password) throws CommunicationException, LoginException {
		final ShadowDatabase shadowDatabase = new ShadowDatabase();
		try {
			shadowDatabase.updateOrInsert(systemUserId, password);
		} catch (UpdateObjectException uoe) {
			throw new LoginException(uoe);
		}
	}

	private void reset() {
		this.userId = VOID_IDENTIFIER;
	}

}
