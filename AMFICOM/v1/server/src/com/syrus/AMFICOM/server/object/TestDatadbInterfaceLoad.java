/*
 * $Id: TestDatadbInterfaceLoad.java,v 1.1.2.1 2004/08/23 11:43:51 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.object;

import com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_Transferable;
import com.syrus.AMFICOM.CORBA.General.AMFICOMRemoteException;
import com.syrus.AMFICOM.CORBA.Resource.*;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2004/08/23 11:43:51 $
 * @author $Author: bass $
 * @module server_v1
 */
final class TestDatadbInterfaceLoad {
	private TestDatadbInterfaceLoad() {
	}

	static void getAlarmedTests(AccessIdentity_Transferable accessIdentity, ResourceDescriptorSeq_TransferableHolder resourceDescriptorSeq) throws AMFICOMRemoteException {
		AMFICOMdbGeneral.checkUserPrivileges(accessIdentity);
		resourceDescriptorSeq.value = new ResourceDescriptor_Transferable[0];
	}
}
