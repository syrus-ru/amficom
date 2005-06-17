/*-
 * $Id: MServerImplementation.java,v 1.62 2005/06/17 20:45:54 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.62 $, $Date: 2005/06/17 20:45:54 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */
public class MServerImplementation extends MServerMeasurementTransmit {

	private static final long serialVersionUID = 395371850379497709L;

	protected void validateAccess(final SessionKey_Transferable sessionKeyT,
			final IdlIdentifierHolder userId,
			final IdlIdentifierHolder domainId) throws AMFICOMRemoteException {
		try {
			MServerSessionEnvironment.getInstance().getMServerServantManager().getLoginServerReference().validateAccess(sessionKeyT,
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
		catch (final Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION,
					CompletionStatus.COMPLETED_PARTIALLY,
					throwable.getMessage());
		}
	}

	public void deleteTests(IdlIdentifier[] ids, SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IdlIdentifierHolder userId = new IdlIdentifierHolder();
		final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
		this.validateAccess(sessionKeyT, userId, domainId);

		Set testIds = Identifier.fromTransferables(ids);
		StorableObjectPool.delete(testIds);
		try {
			StorableObjectPool.flush(ObjectEntities.TEST_CODE, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_DELETE, CompletionStatus.COMPLETED_NO, "Cannot delete '"
					+ ObjectEntities.codeToString(ObjectEntities.TEST_CODE) + "'");
		}
	}

}
