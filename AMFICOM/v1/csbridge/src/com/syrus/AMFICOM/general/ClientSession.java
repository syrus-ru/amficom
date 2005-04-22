/*
 * $Id: ClientSession.java,v 1.1.1.1 2005/04/22 06:54:44 cvsadmin Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2005/04/22 06:54:44 $
 * @author $Author: cvsadmin $
 * @module generalclient_v1
 */
public final class ClientSession {

	public static void init() {
		
	}

	private static void initObjectPools() {
		
	}

	public static AccessIdentifier_Transferable getAccessIdentifierTransferable() {
		return  (AccessIdentifier_Transferable) SessionContext.getAccessIdentity().getTransferable();
	}
}
