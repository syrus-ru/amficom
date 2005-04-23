/*
* $Id: SessionContext.java,v 1.5 2005/04/23 13:31:41 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AccessIdentity_Transferable;


/**
 * @version $Revision: 1.5 $, $Date: 2005/04/23 13:31:41 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class SessionContext {

	private static AccessIdentity accessIdentity;
	private static String serverHostName;

	private static AccessIdentity_Transferable accessIdentityT;
	
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

	public static AccessIdentity_Transferable getAccessIdentityTransferable() {
		if (accessIdentityT == null)
			accessIdentityT = (AccessIdentity_Transferable) accessIdentity.getTransferable();
		return accessIdentityT;
	}

	public static String getServerHostName() {
		return serverHostName;
	}
}
