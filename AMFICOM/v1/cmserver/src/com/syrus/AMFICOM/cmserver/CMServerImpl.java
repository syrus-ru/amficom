/*
 * $Id: CMServerImpl.java,v 1.112 2005/06/25 18:05:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;


import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.112 $, $Date: 2005/06/25 18:05:56 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public final class CMServerImpl extends CMMeasurementTransmit {
	private static final long serialVersionUID = 3760563104903672628L;

	CMServerImpl(final ORB orb) {
		super(orb);
	}

	/**
	 * @param sessionKey
	 * @param userId
	 * @param domainId
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.general.ServerCore#validateAccess(IdlSessionKey, IdlIdentifierHolder, IdlIdentifierHolder)
	 */
	protected void validateAccess(final IdlSessionKey sessionKey,
			final IdlIdentifierHolder userId,
			final IdlIdentifierHolder domainId) throws AMFICOMRemoteException {
		try {
			CMServerSessionEnvironment.getInstance().getCMServerServantManager().getLoginServerReference().validateAccess(sessionKey,
					userId,
					domainId);
		}
		catch (final CommunicationException ce) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (AMFICOMRemoteException are) {
			//-Pass AMFICOMRemoteException upward -- do not catch it by 'throw Throwable' below
			throw are;
		}
		catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_PARTIALLY, t.getMessage());
		}
	}
}
