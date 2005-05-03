/*
 * $Id: SessionKeyGenerator.java,v 1.1 2005/05/03 13:43:03 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SessionKey;


/**
 * @version $Revision: 1.1 $, $Date: 2005/05/03 13:43:03 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
public final class SessionKeyGenerator {
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
