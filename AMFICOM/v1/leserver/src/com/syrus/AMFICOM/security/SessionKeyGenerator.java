/*
 * $Id: SessionKeyGenerator.java,v 1.2 2005/05/18 13:29:31 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import com.syrus.AMFICOM.general.Identifier;


/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 13:29:31 $
 * @author $Author: bass $
 * @module leserver_v1
 */
final class SessionKeyGenerator {
	//TODO This is only stub. Implement generation
	private static final int LENGTH = 128;

	private SessionKeyGenerator() {
		//singleton
		assert false;
	}

	public static SessionKey generateSessionKey(Identifier userId) {
		StringBuffer stringBuffer = new StringBuffer(userId.toString() + Long.toString(System.currentTimeMillis()));
		if (stringBuffer.length() > LENGTH)
			stringBuffer.delete(LENGTH, stringBuffer.length());
		return new SessionKey(stringBuffer.toString());
	}
}
