/*
 * $Id: UserLogin.java,v 1.3 2005/05/03 14:36:40 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SessionKey;
import com.syrus.AMFICOM.general.SessionKeyGenerator;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/03 14:36:40 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class UserLogin {
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

	protected static UserLogin createInstance(Identifier userId) {
		Date date = new Date(System.currentTimeMillis());
		return new UserLogin(SessionKeyGenerator.generateSessionKey(userId), userId, null, date, date);
	}

	protected SessionKey getSessionKey() {
		return this.sessionKey;
	}

	protected Identifier getUserId() {
		return this.userId;
	}

	protected Identifier getDomainId() {
		return this.domainId;
	}

	protected void setDomainId(Identifier domainId) {
		this.domainId = domainId;
		this.updateLastActivityDate();
	}

	protected Date getLoginDate() {
		return this.loginDate;
	}

	protected Date getLastActivityDate() {
		return this.lastActivityDate;
	}

	protected void updateLastActivityDate() {
		this.lastActivityDate = new Date(System.currentTimeMillis());
	}

}
