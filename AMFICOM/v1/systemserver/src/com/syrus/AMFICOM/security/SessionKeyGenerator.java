/*
 * $Id: SessionKeyGenerator.java,v 1.1 2006/06/22 12:56:48 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.security;

import com.syrus.AMFICOM.general.Identifier;


/**
 * @version $Revision: 1.1 $, $Date: 2006/06/22 12:56:48 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
 */
final class SessionKeyGenerator {
	//TODO This is only stub. Implement generation
	private static final int LENGTH = 128;

	private SessionKeyGenerator() {
		//singleton
		assert false;
	}

	public static SessionKey generateSessionKey(final Identifier userId) {
		final StringBuffer stringBuffer = new StringBuffer(userId.toString()
				+ Identifier.SEPARATOR
				+ Long.toString(System.currentTimeMillis()));
		if (stringBuffer.length() > LENGTH) {
			stringBuffer.delete(LENGTH, stringBuffer.length());
		}
		return new SessionKey(stringBuffer.toString());
	}
}
