/*
 * $Id: TestDatadbInterfaceLoad.java,v 1.1.2.2 2004/09/09 11:35:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.object;

import com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_Transferable;
import com.syrus.AMFICOM.CORBA.General.AMFICOMRemoteException;
import com.syrus.AMFICOM.CORBA.Resource.*;
import java.sql.*;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2004/09/09 11:35:21 $
 * @author $Author: bass $
 * @module server_v1
 */
final class TestDatadbInterfaceLoad {
	private TestDatadbInterfaceLoad() {
	}

	static void getAlarmedTests(final Connection conn, AccessIdentity_Transferable accessIdentity, ResourceDescriptorSeq_TransferableHolder resourceDescriptorSeq) throws SQLException, AMFICOMRemoteException {
		AMFICOMdbGeneral.checkUserPrivileges(conn, accessIdentity);
		resourceDescriptorSeq.value = new ResourceDescriptor_Transferable[0];
	}
}
