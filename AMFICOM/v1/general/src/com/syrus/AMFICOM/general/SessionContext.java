/*
* $Id: SessionContext.java,v 1.4 2005/04/08 13:00:07 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;


/**
 * @version $Revision: 1.4 $, $Date: 2005/04/08 13:00:07 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class SessionContext {

	private static AccessIdentity accessIdentity;
	private static String serverHostName;
	
	private SessionContext() {
		// singleton
		assert false;
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
