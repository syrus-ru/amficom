/*
 * $Id: SessionKeyGenerator.java,v 1.1 2005/05/03 19:37:01 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import com.syrus.AMFICOM.general.Identifier;


/**
 * @version $Revision: 1.1 $, $Date: 2005/05/03 19:37:01 $
 * @author $Author: arseniy $
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
