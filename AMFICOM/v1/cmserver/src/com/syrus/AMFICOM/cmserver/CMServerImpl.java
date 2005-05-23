/*
 * $Id: CMServerImpl.java,v 1.105 2005/05/23 09:01:04 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;


import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.105 $, $Date: 2005/05/23 09:01:04 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public class CMServerImpl extends CMMeasurementTransmit {
	private static final long serialVersionUID = 3760563104903672628L;

	/**
	 * @param sessionKey
	 * @param userId
	 * @param domainId
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.general.ServerCore#validateAccess(com.syrus.AMFICOM.security.corba.SessionKey_Transferable, com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder, com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder)
	 */
	protected void validateAccess(final SessionKey_Transferable sessionKey,
			final Identifier_TransferableHolder userId,
			final Identifier_TransferableHolder domainId)
			throws AMFICOMRemoteException {
		try {
			CMServerSessionEnvironment.getInstance()
					.getCMServerServantManager()
					.getLoginServerReference()
					.validateAccess(sessionKey, userId, domainId);
		} catch (final CommunicationException ce) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_PARTIALLY, t.getMessage());
		}
	}
}
