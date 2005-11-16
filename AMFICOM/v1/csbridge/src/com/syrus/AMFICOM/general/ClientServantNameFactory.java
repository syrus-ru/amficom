/*-
 * $Id: ClientServantNameFactory.java,v 1.1 2005/11/16 10:20:27 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.syrus.AMFICOM.security.SessionKey;

/**
 * @version $Revision: 1.1 $, $Date: 2005/11/16 10:20:27 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class ClientServantNameFactory {

	public static String generateServantName(final SessionKey sessionKey, final String clientHostName)
			throws CommunicationException {
		try {
			return sessionKey.stringValue()
					+ Identifier.SEPARATOR
					+ InetAddress.getByName(clientHostName).getCanonicalHostName().replaceAll("\\.", "_");
		} catch (UnknownHostException uhe) {
			throw new CommunicationException(I18N.getString("Error.CannotResolveHost") + ": '" + clientHostName + "'", uhe);
		}
	}

	public static String generateServantName(final SessionKey sessionKey) throws CommunicationException {
		try {
			return sessionKey.stringValue()
					+ Identifier.SEPARATOR
					+ InetAddress.getLocalHost().getCanonicalHostName().replaceAll("\\.", "_");
		} catch (UnknownHostException uhe) {
			// Когда эта случиццо эта будит точна полный пиздец
			throw new CommunicationException(I18N.getString("Error.CannotResolveHost") + ": 'localhost'", uhe);
		}
	}

}
