/*
 * $Id: MCMImpl.java,v 1.10 2005/12/17 12:12:46 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.LinkedList;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ServerCore;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.mcm.corba.MCMOperations;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/12/17 12:12:46 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class MCMImpl extends ServerCore implements MCMOperations {
	private static final long serialVersionUID = -6484515008721159857L;

	MCMImpl(final MCMServantManager mcmServantManager) {
		super(mcmServantManager, mcmServantManager.getCORBAServer().getOrb());
	}


	public void startTests(final IdlIdentifier[] testIdsT, final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		assert testIdsT != null && sessionKeyT != null : ErrorMessages.NON_NULL_EXPECTED;
		final int length = testIdsT.length;
		assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		final IdlIdentifierHolder userId = new IdlIdentifierHolder();
		final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
		super.validateAccess(sessionKeyT, userId, domainId);

		Log.debugMessage("Request to start " + testIdsT.length + " test(s)", Log.DEBUGLEVEL07);
		final Set<Identifier> testIds = Identifier.fromTransferables(testIdsT);
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjects(testIds, true);
			MeasurementControlModule.addTests(new LinkedList<Test>(tests));
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_SAVE, IdlCompletionStatus.COMPLETED_NO, ae.getMessage());
		}
	}

	public void stopTests(final IdlIdentifier[] testIdsT, final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		assert testIdsT != null && sessionKeyT != null : ErrorMessages.NON_NULL_EXPECTED;
		final int length = testIdsT.length;
		assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		try {
			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			super.validateAccess(sessionKeyT, userId, domainId);

			final Set<Identifier> ids = Identifier.fromTransferables(testIdsT);
			Log.debugMessage("Request to stop " + testIdsT.length + " test(s): " + ids, Log.DEBUGLEVEL07);
			MeasurementControlModule.stopTests(ids);
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable throwable) {
			Log.errorMessage(throwable);
		}
	}}
