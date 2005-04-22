/*
 * $Id: ClientSession.java,v 1.2 2005/04/22 17:18:28 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/22 17:18:28 $
 * @author $Author: arseniy $
 * @module csbridge_v1
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
