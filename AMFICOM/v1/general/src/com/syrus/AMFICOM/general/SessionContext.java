/*
* $Id: SessionContext.java,v 1.2 2005/03/11 17:26:28 bass Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.2 $, $Date: 2005/03/11 17:26:28 $
 * @author $Author: bass $
 * @module general_v1
 */
public final class SessionContext {

	private static AccessIdentity accessIdentity;
	
	private SessionContext() {
		// singleton
	}
	
	public static void init(final AccessIdentity accessIdentity1) {
		SessionContext.accessIdentity = accessIdentity1;
	}
	
	public static AccessIdentity getAccessIdentity() {
		return SessionContext.accessIdentity;
	}
}
