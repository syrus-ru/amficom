/*-
 * $Id: ClientUserLogin.java,v 1.1 2005/11/16 10:21:14 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.security;

import java.util.Date;

import com.syrus.AMFICOM.general.ClientServantNameFactory;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.1 $, $Date: 2005/11/16 10:21:14 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
public final class ClientUserLogin extends UserLogin {
	private String servantName;

	ClientUserLogin(final SessionKey sessionKey,
			final Identifier userId,
			final Identifier domainId,
			final String userHostName,
			final Date loginDate,
			final Date lastActivityDate) throws CommunicationException {
		super(sessionKey, userId, domainId, userHostName, loginDate, lastActivityDate);

		this.servantName = ClientServantNameFactory.generateServantName(super.getSessionKey(), super.getUserHostName());		
	}

	public String getServantName() {
		return this.servantName;
	}
}
