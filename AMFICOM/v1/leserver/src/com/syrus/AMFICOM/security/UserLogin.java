/*
 * $Id: UserLogin.java,v 1.9 2005/10/20 14:09:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.9 $, $Date: 2005/10/20 14:09:56 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
public final class UserLogin {
	private SessionKey sessionKey;
	private Identifier userId;
	private Identifier domainId;
	private String userHostName;
	private Date loginDate;
	private Date lastActivityDate;

	protected UserLogin(final SessionKey sessionKey,
			final Identifier userId,
			final Identifier domainId,
			final String userHostName,
			final Date loginDate,
			final Date lastActivityDate) {
		this.sessionKey = sessionKey;
		this.userId = userId;
		this.domainId = domainId;
		this.userHostName = userHostName;
		this.loginDate = loginDate;
		this.lastActivityDate = lastActivityDate;
	}

	public static UserLogin createInstance(final Identifier userId, final Identifier domainId, final String userHostName) {
		final Date date = new Date(System.currentTimeMillis());
		return new UserLogin(SessionKeyGenerator.generateSessionKey(userId),
				userId,
				domainId,
				userHostName,
				date,
				date);
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

	public String getUserHostName() {
		return this.userHostName;
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
