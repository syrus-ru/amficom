/*
 * $Id: MCMImpl.java,v 1.13 2006/06/05 13:44:33 arseniy Exp $
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
 * @version $Revision: 1.13 $, $Date: 2006/06/05 13:44:33 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class MCMImpl extends ServerCore implements MCMOperations {

	MCMImpl(final MCMServantManager mcmServantManager) {
		super(mcmServantManager, mcmServantManager.getCORBAServer().getOrb());
	}


	public void startTests(final IdlIdentifier[] idlTestIds, final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		assert idlTestIds != null && idlSessionKey != null : ErrorMessages.NON_NULL_EXPECTED;
		final int length = idlTestIds.length;
		assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		super.validateLogin(SessionKey.valueOf(idlSessionKey));

		Log.debugMessage("Request to start " + idlTestIds.length + " test(s)", Log.DEBUGLEVEL07);
		final Set<Identifier> testIds = Identifier.fromTransferables(idlTestIds);
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjects(testIds, true);
			MeasurementControlModule.addTests(new LinkedList<Test>(tests));
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_SAVE, IdlCompletionStatus.COMPLETED_NO, ae.getMessage());
		}
	}

	public void stopTests(final IdlIdentifier[] idlTestIds, final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		assert idlTestIds != null && idlSessionKey != null : ErrorMessages.NON_NULL_EXPECTED;
		final int length = idlTestIds.length;
		assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		try {
			super.validateLogin(SessionKey.valueOf(idlSessionKey));

			final Set<Identifier> ids = Identifier.fromTransferables(idlTestIds);
			Log.debugMessage("Request to stop " + idlTestIds.length + " test(s): " + ids, Log.DEBUGLEVEL07);
			MeasurementControlModule.stopTests(ids);
		} catch (AMFICOMRemoteException are) {
			throw are;
		} catch (Throwable throwable) {
			Log.errorMessage(throwable);
		}
	}}
