/*-
 * $Id: MServerImplementation.java,v 1.58 2005/06/06 14:43:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.58 $, $Date: 2005/06/06 14:43:06 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */
public class MServerImplementation extends MServerMeasurementTransmit {

	private static final long serialVersionUID = 395371850379497709L;

	protected void validateAccess(final SessionKey_Transferable sessionKeyT,
			final Identifier_TransferableHolder userId,
			final Identifier_TransferableHolder domainId) throws AMFICOMRemoteException {
		try {
			MServerSessionEnvironment.getInstance().getMServerServantManager().getLoginServerReference().validateAccess(sessionKeyT,
					userId,
					domainId);
		}
		catch (final CommunicationException ce) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_PARTIALLY, t.getMessage());
		}
	}

	public void deleteTests(Identifier_Transferable[] ids, SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final Identifier_TransferableHolder userId = new Identifier_TransferableHolder();
		final Identifier_TransferableHolder domainId = new Identifier_TransferableHolder();
		this.validateAccess(sessionKeyT, userId, domainId);

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
