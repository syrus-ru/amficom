/*
 * $Id: UserLogin.java,v 1.1 2005/05/02 19:03:24 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import java.util.Date;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.SecurityKey;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/02 19:03:24 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class UserLogin {
	private SecurityKey securityKey;
	private Identifier userId;
	private Date loginDate;
	private Date lastActivityDate;

	protected UserLogin(SecurityKey securityKey, Identifier userId, Date loginDate, Date lastActivityDate) {
		this.securityKey = securityKey;
		this.userId = userId;
		this.loginDate = loginDate;
		this.lastActivityDate = lastActivityDate;
	}

	protected static UserLogin createInstance(Identifier userId, String password) {
		Date date = new Date(System.currentTimeMillis());
		return new UserLogin(SecurityKeyGenerator.generateSecurityKey(userId, password), userId, date, date);
	}

	protected SecurityKey getSecurityKey() {
		return this.securityKey;
	}

	protected Identifier getUserId() {
		return this.userId;
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
