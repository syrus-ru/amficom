/*-
 * $Id: UserLogin.java,v 1.14 2006/06/02 13:41:12 arseniy Exp $
 *
 * Copyright ? 2004-2006 Syrus Systems.
 * ??????-??????????? ?????.
 * ??????: ???????.
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
 * @version $Revision: 1.14 $, $Date: 2006/06/02 13:41:12 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
public final class UserLogin implements SessionData {
	private final SessionKey sessionKey;
	private final Identifier userId;
	private final Identifier domainId;
	private final String userIOR;
	private final Date loginDate;
	private final Date lastActivityDate;

	UserLogin(final SessionKey sessionKey,
			final Identifier userId,
			final Identifier domainId,
			final String userIOR,
			final Date loginDate,
			final Date lastActivityDate) {
		this.sessionKey = sessionKey;
		this.userId = userId;
		this.domainId = domainId;
		this.userIOR = userIOR;
		this.loginDate = new Date(loginDate == null
				? System.currentTimeMillis()
				: loginDate.getTime());
		this.lastActivityDate = new Date(lastActivityDate == null
				? System.currentTimeMillis()
				: lastActivityDate.getTime());
	}

	public static UserLogin createInstance(final Identifier userId,
			final Identifier domainId,
			final String userIOR) {
		assert userId != null : NON_NULL_EXPECTED;
		assert domainId != null : NON_NULL_EXPECTED;
		assert userIOR != null : NON_NULL_EXPECTED;
		assert userId.getMajor() == SYSTEMUSER_CODE : ILLEGAL_ENTITY_CODE;
		assert domainId.getMajor() == DOMAIN_CODE : ILLEGAL_ENTITY_CODE;
		assert userIOR.length() != 0 : NON_EMPTY_EXPECTED;

		final SessionKey sessionKey = SessionKeyGenerator.generateSessionKey(userId);
		return new UserLogin(sessionKey, userId, domainId, userIOR, null, null);
	}

	/**
	 * @see SessionData#getSessionKey()
	 */
	public SessionKey getSessionKey() {
		return this.sessionKey;
	}

	public Identifier getUserId() {
		return this.userId;
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
