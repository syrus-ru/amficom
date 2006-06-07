/*
 * $Id: MCMImpl.java,v 1.12.2.2 2006/06/07 10:28:35 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ServerCore;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.mcm.corba.MCMOperations;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12.2.2 $, $Date: 2006/06/07 10:28:35 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class MCMImpl extends ServerCore implements MCMOperations {

	MCMImpl(final MCMServantManager mcmServantManager) {
		super(mcmServantManager, mcmServantManager.getCORBAServer().getOrb());
	}

	public void startTests(final IdlIdentifier[] testIdlIds, final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		assert testIdlIds != null && idlSessionKey != null : ErrorMessages.NON_NULL_EXPECTED;
		final int length = testIdlIds.length;
		assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		super.validateLogin(SessionKey.valueOf(idlSessionKey));

		final Set<Identifier> testIds = Identifier.fromTransferables(testIdlIds);
		Log.debugMessage("Request to start " + testIdlIds.length + " test(s): " + Identifier.toString(testIds), Log.DEBUGLEVEL07);
		final MeasurementControlModule measurementControlModule = MeasurementControlModule.getInstance();
		measurementControlModule.addIdsToSynchronizer(testIds);
		measurementControlModule.addNewTestIds(testIds);
	}

	public void stopTests(final IdlIdentifier[] testIdlIds, final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		assert testIdlIds != null && idlSessionKey != null : ErrorMessages.NON_NULL_EXPECTED;
		final int length = testIdlIds.length;
		assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		super.validateLogin(SessionKey.valueOf(idlSessionKey));

		final Set<Identifier> testIds = Identifier.fromTransferables(testIdlIds);
		Log.debugMessage("Request to stop " + testIdlIds.length + " test(s): " + Identifier.toString(testIds), Log.DEBUGLEVEL07);
		final MeasurementControlModule measurementControlModule = MeasurementControlModule.getInstance();
		measurementControlModule.addIdsToSynchronizer(testIds);
		measurementControlModule.addStoppingTestIds(testIds);
	}
}
