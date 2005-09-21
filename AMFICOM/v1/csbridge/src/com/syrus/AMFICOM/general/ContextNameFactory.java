/*
 * $Id: ContextNameFactory.java,v 1.6 2005/09/21 13:25:12 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @version $Revision: 1.6 $, $Date: 2005/09/21 13:25:12 $
 * @author $Author: bob $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class ContextNameFactory {

	public ContextNameFactory() {
		// singleton
		assert false;
	}

	public static String generateContextName(String hostName) throws CommunicationException {
		try {
			return InetAddress.getByName(hostName).getCanonicalHostName().replaceAll("\\.", "_");
		}
		catch (final UnknownHostException uhe) {
			throw new CommunicationException(I18N.getString("Error.CannotResolveHost") + ": '" + hostName + "'", uhe);
		}
	}

	public static String generateContextName() throws CommunicationException {
		try {
			return InetAddress.getLocalHost().getCanonicalHostName().replaceAll("\\.", "_");
		}
		catch (final UnknownHostException uhe) {
			// Когда эта случиццо эта будит точна полный пиздец
			throw new CommunicationException(I18N.getString("Error.CannotResolveHost") + ": 'localhost'", uhe);
		}
	}
}
