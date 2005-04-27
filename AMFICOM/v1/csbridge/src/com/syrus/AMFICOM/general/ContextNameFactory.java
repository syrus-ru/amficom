/*
 * $Id: ContextNameFactory.java,v 1.1 2005/04/27 13:38:54 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/27 13:38:54 $
 * @author $Author: arseniy $
 * @module general_v1
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
		catch (UnknownHostException uhe) {
			throw new CommunicationException("Cannot get host '" + hostName + "'", uhe);
		}
	}

	public static String generateContextName() throws CommunicationException {
		try {
			return InetAddress.getLocalHost().getCanonicalHostName().replaceAll("\\.", "_");
		}
		catch (UnknownHostException uhe) {
			throw new CommunicationException("Cannot get local host", uhe);
		}
	}
}
