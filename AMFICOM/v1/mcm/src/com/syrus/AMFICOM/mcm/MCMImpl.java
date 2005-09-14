/*
 * $Id: MCMImpl.java,v 1.5 2005/09/14 18:13:47 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.mcm;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ServerCore;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.mcm.corba.MCMOperations;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/14 18:13:47 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class MCMImpl extends ServerCore implements MCMOperations {
	private static final long serialVersionUID = -6484515008721159857L;

	MCMImpl() {
		super(MCMSessionEnvironment.getInstance().getConnectionManager(),
				MCMSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb());
	}


	public void receiveTests(final IdlTest[] testsT, final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		assert testsT != null && sessionKeyT != null : ErrorMessages.NON_NULL_EXPECTED;
		final int length = testsT.length;
		assert length != 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		final IdlIdentifierHolder userId = new IdlIdentifierHolder();
		final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
		super.validateAccess(sessionKeyT, userId, domainId);

		Log.debugMessage("Received " + testsT.length + " test(s)", Log.DEBUGLEVEL07);
		final List<Test> tests = new LinkedList<Test>();
		for (int i = 0; i < testsT.length; i++) {
			try {
				tests.add(new Test(testsT[i]));
			} catch (CreateObjectException coe) {
				Log.errorException(coe);
				throw new AMFICOMRemoteException(IdlErrorCode.ERROR_SAVE, IdlCompletionStatus.COMPLETED_NO, coe.getMessage());
			}
		}

		MeasurementControlModule.addTests(tests);
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
			Log.errorException(throwable);
		}
	}}
