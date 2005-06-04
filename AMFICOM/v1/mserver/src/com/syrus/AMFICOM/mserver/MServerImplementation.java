/*-
 * $Id: MServerImplementation.java,v 1.57 2005/06/04 16:56:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.57 $, $Date: 2005/06/04 16:56:21 $
 * @author $Author: bass $
 * @module mserver_v1
 */
public class MServerImplementation extends MServerMeasurementTransmit {

	private static final long serialVersionUID = 395371850379497709L;

	public void deleteTests(Identifier_Transferable[] ids) throws AMFICOMRemoteException {
		Set testIds = Identifier.fromTransferables(ids);
		StorableObjectPool.delete(testIds);
		try {
			StorableObjectPool.flush(ObjectEntities.TEST_ENTITY_CODE, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_DELETE, CompletionStatus.COMPLETED_NO, "Cannot delete '"
					+ ObjectEntities.codeToString(ObjectEntities.TEST_ENTITY_CODE) + "'");
		}
	}
}
