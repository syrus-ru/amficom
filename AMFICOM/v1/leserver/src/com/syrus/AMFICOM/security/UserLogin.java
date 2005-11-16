/*
 * $Id: UserLogin.java,v 1.10 2005/11/16 10:23:05 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import static com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort._USER_SORT_REGULAR;
import static com.syrus.AMFICOM.administration.corba.IdlSystemUserPackage.SystemUserSort._USER_SORT_SYSADMIN;

import java.util.Date;

import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.10 $, $Date: 2005/11/16 10:23:05 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
public class UserLogin {
	private SessionKey sessionKey;
	private Identifier userId;
	private Identifier domainId;
	private String userHostName;
	private Date loginDate;
	private Date lastActivityDate;

	UserLogin(final SessionKey sessionKey,
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

	public static UserLogin createInstance(final Identifier userId, final Identifier domainId, final String userHostName)
			throws ApplicationException {
		final SessionKey sessionKey = SessionKeyGenerator.generateSessionKey(userId);
		final Date date = new Date(System.currentTimeMillis());
		return valueOf(sessionKey, userId, domainId, userHostName, date, date);
	}

	static UserLogin valueOf(final SessionKey sessionKey,
			final Identifier userId,
			final Identifier domainId,
			final String userHostName,
			final Date loginDate,
			final Date lastActivityDate) throws ApplicationException {
		final SystemUser user = StorableObjectPool.getStorableObject(userId, true);
		final int userSort = user.getSort().value();
		if (userSort == _USER_SORT_REGULAR || userSort == _USER_SORT_SYSADMIN) {
			return new ClientUserLogin(sessionKey, userId, domainId, userHostName, loginDate, lastActivityDate);
		}
		return new UserLogin(sessionKey, userId, domainId, userHostName, loginDate, lastActivityDate);
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
