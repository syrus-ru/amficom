/*
 * $Id: MCMImpl.java,v 1.12 2006/01/23 16:18:37 arseniy Exp $
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
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.mcm.corba.MCMOperations;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2006/01/23 16:18:37 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class MCMImpl extends ServerCore implements MCMOperations {

	MCMImpl(final MCMServantManager mcmServantManager) {
		super(mcmServantManager, mcmServantManager.getCORBAServer().getOrb());
	}


	public void startTests(final IdlIdentifier[] testIdsT, final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		assert testIdsT != null && idlSessionKey != null : ErrorMessages.NON_NULL_EXPECTED;
		final int length = testIdsT.length;
		assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		super.validateLogin(new SessionKey(idlSessionKey));

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

	public void stopTests(final IdlIdentifier[] testIdsT, final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		assert testIdsT != null && idlSessionKey != null : ErrorMessages.NON_NULL_EXPECTED;
		final int length = testIdsT.length;
		assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		try {
			super.validateLogin(new SessionKey(idlSessionKey));

			final Set<Identifier> ids = Identifier.fromTransferables(testIdsT);
			Log.debugMessage("Request to stop " + testIdsT.length + " test(s): " + ids, Log.DEBUGLEVEL07);
			MeasurementControlModule.stopTests(ids);
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable throwable) {
			Log.errorMessage(throwable);
		}
	}}
