/*
 * $Id: ContextNameFactory.java,v 1.4 2005/08/08 11:38:11 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.general;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:38:11 $
 * @author $Author: arseniy $
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
