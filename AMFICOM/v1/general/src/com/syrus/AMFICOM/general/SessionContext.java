/*
* $Id: SessionContext.java,v 1.3 2005/04/01 09:29:22 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.3 $, $Date: 2005/04/01 09:29:22 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class SessionContext {

	private static AccessIdentity accessIdentity;
	private static String serverHostName;
	
	private SessionContext() {
		// singleton
	}
	
	public static void init(final AccessIdentity accessIdentity1, final String serverHostName1) {
		accessIdentity = accessIdentity1;
		serverHostName = serverHostName1;
	}
	
	public static AccessIdentity getAccessIdentity() {
		return SessionContext.accessIdentity;
	}

	public static String getServerHostName() {
		return serverHostName;
	}
}
