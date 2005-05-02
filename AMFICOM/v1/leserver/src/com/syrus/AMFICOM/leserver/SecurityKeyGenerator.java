/*
 * $Id: SecurityKeyGenerator.java,v 1.2 2005/05/02 19:06:47 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.SecurityKey;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/02 19:06:47 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class SecurityKeyGenerator {
	//TODO Implement generation
	private static final int LENGTH = 128;

	private SecurityKeyGenerator() {
		//singleton
		assert false;
	}

	protected static SecurityKey generateSecurityKey(Identifier userId, String password) {
		String str = userId.toString() + password;
		return new SecurityKey(str.substring(0, LENGTH));
	}
}
