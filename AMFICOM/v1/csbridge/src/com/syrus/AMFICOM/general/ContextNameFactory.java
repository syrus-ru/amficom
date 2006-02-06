/*
 * $Id: ContextNameFactory.java,v 1.7 2005/11/16 10:20:45 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @version $Revision: 1.7 $, $Date: 2005/11/16 10:20:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public final class ContextNameFactory {

	public ContextNameFactory() {
		// singleton
		assert false;
	}

	public static String generateContextName(final String hostName) throws CommunicationException {
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
