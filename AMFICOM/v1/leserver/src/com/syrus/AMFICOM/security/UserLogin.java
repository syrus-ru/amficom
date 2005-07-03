/*
 * $Id: UserLogin.java,v 1.4 2005/06/17 20:11:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/17 20:11:59 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
public final class UserLogin {
	private SessionKey sessionKey;
	private Identifier userId;
	private Identifier domainId;
	private Date loginDate;
	private Date lastActivityDate;

	protected UserLogin(SessionKey sessionKey, Identifier userId, Identifier domainId, Date loginDate, Date lastActivityDate) {
		this.sessionKey = sessionKey;
		this.userId = userId;
		this.domainId = domainId;
		this.loginDate = loginDate;
		this.lastActivityDate = lastActivityDate;
	}

	public static UserLogin createInstance(Identifier userId) {
		Date date = new Date(System.currentTimeMillis());
		return new UserLogin(SessionKeyGenerator.generateSessionKey(userId), userId, Identifier.VOID_IDENTIFIER, date, date);
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

	public void setDomainId(Identifier domainId) {
		this.domainId = domainId;
		this.updateLastActivityDate();
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
