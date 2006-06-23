/*-
 * $Id: UserLogin.java,v 1.1 2006/06/23 12:43:00 cvsadmin Exp $
 *
 * Copyright © 2004-2006 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.security;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;

/**
 * Immutable.
 *
 * @version $Revision: 1.1 $, $Date: 2006/06/23 12:43:00 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
 */
public final class UserLogin implements SessionData {
	private final SessionKey sessionKey;
	private final Identifier systemUserId;
	private final Identifier domainId;
	private final String userIOR;
	private final Date loginDate;
	private final Date lastActivityDate;

	UserLogin(final SessionKey sessionKey,
			final Identifier systemUserId,
			final Identifier domainId,
			final String userIOR,
			final Date loginDate,
			final Date lastActivityDate) {
		this.sessionKey = sessionKey;
		this.systemUserId = systemUserId;
		this.domainId = domainId;
		this.userIOR = userIOR;
		this.loginDate = new Date(loginDate == null
				? System.currentTimeMillis()
				: loginDate.getTime());
		this.lastActivityDate = new Date(lastActivityDate == null
				? System.currentTimeMillis()
				: lastActivityDate.getTime());
	}

	public static UserLogin createInstance(final Identifier systemUserId,
			final Identifier domainId,
			final String userIOR) {
		assert systemUserId != null : NON_NULL_EXPECTED;
		assert domainId != null : NON_NULL_EXPECTED;
		assert userIOR != null : NON_NULL_EXPECTED;
		assert systemUserId.getMajor() == SYSTEMUSER_CODE : ILLEGAL_ENTITY_CODE;
		assert domainId.getMajor() == DOMAIN_CODE : ILLEGAL_ENTITY_CODE;
		assert userIOR.length() != 0 : NON_EMPTY_EXPECTED;

		final SessionKey sessionKey = SessionKeyGenerator.generateSessionKey(systemUserId);
		return new UserLogin(sessionKey, systemUserId, domainId, userIOR, null, null);
	}

	/**
	 * @see SessionData#getSessionKey()
	 */
	public SessionKey getSessionKey() {
		return this.sessionKey;
	}

	public Identifier getSystemUserId() {
		return this.systemUserId;
	}

	public Identifier getDomainId() {
		return this.domainId;
	}

	public String getUserIOR() {
		return this.userIOR;
	}

	public Date getLoginDate() {
		return (Date) this.loginDate.clone();
	}

	public Date getLastActivityDate() {
		return (Date) this.lastActivityDate.clone();
	}

	public void updateLastActivityDate() {
		this.lastActivityDate.setTime(System.currentTimeMillis());
	}

	/*-********************************************************************
	 * #eqauls() and #hashCode()                                          *
	 **********************************************************************/

	/**
	 * @param that
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(final Object that) {
		return this.sessionKey.equals(that);
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.sessionKey.hashCode();
	}
}
