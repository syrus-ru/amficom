/*
 * $Id: UserLogin.java,v 1.11 2005/11/28 12:31:38 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.11 $, $Date: 2005/11/28 12:31:38 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
public final class UserLogin {
	private SessionKey sessionKey;
	private Identifier userId;
	private Identifier domainId;
	private String userIOR;
	private Date loginDate;
	private Date lastActivityDate;

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
		this.loginDate = loginDate;
		this.lastActivityDate = lastActivityDate;
	}

	public static UserLogin createInstance(final Identifier userId,
			final Identifier domainId,
			final String userIOR) {
		final SessionKey sessionKey = SessionKeyGenerator.generateSessionKey(userId);
		final Date date = new Date(System.currentTimeMillis());
		return new UserLogin(sessionKey, userId, domainId, userIOR, date, date);
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

	public String getUserIOR() {
		return this.userIOR;
	}

	public Date getLoginDate() {
		return this.loginDate;
	}

	public Date getLastActivityDate() {
		return this.lastActivityDate;
	}

	public void updateLastActivityDate() {
		this.lastActivityDate = new Date(System.currentTimeMillis());
	}

}
