/*
 * $Id: MServerMeasurementReceive.java,v 1.15 2005/06/17 11:01:06 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.HashSet;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerCore;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServerOperations;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.15 $, $Date: 2005/06/17 11:01:06 $
 * @author $Author: bass $
 * @module mserver_v1
 */
abstract class MServerMeasurementReceive extends ServerCore implements MServerOperations {

	private static final long serialVersionUID = 8337247295980850931L;

	public void receiveResults(Result_Transferable[] resultsT, Identifier_Transferable mcmIdT, SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		try {
			final Identifier_TransferableHolder userIdH = new Identifier_TransferableHolder();
			final Identifier_TransferableHolder domainIdH = new Identifier_TransferableHolder();
			this.validateAccess(sessionKeyT, userIdH, domainIdH);

			Identifier mcmId = new Identifier(mcmIdT);
			Log.debugMessage("Received " + resultsT.length + " results from MCM '" + mcmId + "'", Log.DEBUGLEVEL03);
			synchronized (CORBAMServerObjectLoader.lock) {
				CORBAMServerObjectLoader.preferredMCMId = mcmId;
				Result result;
				java.util.Set results = new HashSet(resultsT.length);
				for (int i = 0; i < resultsT.length; i++) {
					try {
						result = new Result(resultsT[i]);
						results.add(result);
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
						throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
					}
				}
				CORBAMServerObjectLoader.preferredMCMId = null;

				ResultDatabase resultDatabase = (ResultDatabase) DatabaseContext.getDatabase(ObjectEntities.RESULT_CODE);
				try {
					resultDatabase.insert(results);
				}
				catch (CreateObjectException e) {
					Log.errorException(e);
					throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
				}
				catch (IllegalDataException e) {
					Log.errorException(e);
					throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
				}
			}
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveTests(Test_Transferable[] testsT, boolean force, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		Log.debugMessage("MServerImplementation.receiveTests | Received " + testsT.length + " tests", Log.DEBUGLEVEL07);
		return super.receiveStorableObjects(ObjectEntities.TEST_CODE, testsT, force, sessionKeyT);
	}
}
