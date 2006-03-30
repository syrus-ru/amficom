/*-
 * $Id: LEServerLoginPerformer.java,v 1.1 2006/03/30 08:45:30 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.METHOD_NOT_NEEDED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;
import static com.syrus.AMFICOM.security.SessionKey.VOID_SESSION_KEY;

import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginPerformer;
import com.syrus.AMFICOM.security.SessionKey;

/**
 * @version $Revision: 1.1 $, $Date: 2006/03/30 08:45:30 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LEServerLoginPerformer implements LoginPerformer {
	private final Identifier systemUserId;

	LEServerLoginPerformer(final Identifier systemUserId) {
		assert systemUserId != null : NON_NULL_EXPECTED;
		assert systemUserId.getMajor() == SYSTEMUSER_CODE : ILLEGAL_ENTITY_CODE;

		this.systemUserId = systemUserId;
	}

	public Set<Domain> getAvailableDomains() throws CommunicationException, LoginException {
		throw new UnsupportedOperationException(METHOD_NOT_NEEDED);
	}

	public void login(final String login, final String password, final Identifier loginDomainId)
			throws CommunicationException,
				LoginException {
		throw new UnsupportedOperationException(METHOD_NOT_NEEDED);
	}

	public void logout() throws CommunicationException, LoginException {
		throw new UnsupportedOperationException(METHOD_NOT_NEEDED);
	}

	public boolean isLoggedIn() {
		return false;
	}

	public void restoreLogin(final String login, final String password) throws CommunicationException, LoginException {
		throw new UnsupportedOperationException(METHOD_NOT_NEEDED);
	}

	public SessionKey getSessionKey() {
		return VOID_SESSION_KEY;
	}

	public Identifier getUserId() {
		return this.systemUserId;
	}

	public Identifier getDomainId() {
		return VOID_IDENTIFIER;
	}

	public void setPassword(final String password) throws CommunicationException, LoginException {
		throw new UnsupportedOperationException(METHOD_NOT_NEEDED);
	}

	public void setPassword(final Identifier systemUserId, final String password) throws CommunicationException, LoginException {
		throw new UnsupportedOperationException(METHOD_NOT_NEEDED);
	}

}
