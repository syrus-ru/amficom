/*
* $Id: SessionContext.java,v 1.1 2005/02/11 09:17:36 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.1 $, $Date: 2005/02/11 09:17:36 $
 * @author $Author: bob $
 * @module general_v1
 */
public final class SessionContext {

	private static AccessIdentity accessIdentity;
	
	private SessionContext() {
		// singleton
	}
	
	public static void init(AccessIdentity accessIdentity) {
		SessionContext.accessIdentity = accessIdentity;
	}
	
	public static AccessIdentity getAccessIdentity() {
		return SessionContext.accessIdentity;
	}
}
