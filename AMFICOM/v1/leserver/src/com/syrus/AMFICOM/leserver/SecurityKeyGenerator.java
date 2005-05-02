/*
 * $Id: SecurityKeyGenerator.java,v 1.1 2005/05/02 19:02:29 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.SecurityKey;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/02 19:02:29 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class SecurityKeyGenerator {
	//TODO Implement generation
	private static final int LENGTH = 128;

	protected static SecurityKey generateSecurityKey(Identifier userId, String password) {
		String str = userId.toString() + password;
		return new SecurityKey(str.substring(0, LENGTH));
	}
}
