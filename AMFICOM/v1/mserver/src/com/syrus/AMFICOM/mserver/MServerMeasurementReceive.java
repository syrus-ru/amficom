/*
 * $Id: MServerMeasurementReceive.java,v 1.10 2005/05/26 12:01:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.HashSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServerPOA;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/05/26 12:01:59 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */
abstract class MServerMeasurementReceive extends MServerPOA {

	private static final long serialVersionUID = 8337247295980850931L;

	public void receiveResults(Result_Transferable[] resultsT, Identifier_Transferable mcmIdT) throws AMFICOMRemoteException {
		try {
			Identifier mcmId = new Identifier(mcmIdT);
			Log.debugMessage("Received " + resultsT.length + " results from MCM '" + mcmId + "'", Log.DEBUGLEVEL03);
			synchronized (MServerMeasurementObjectLoader.lock) {
				MServerMeasurementObjectLoader.preferredMCMId = mcmId;
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
				ResultDatabase resultDatabase = (ResultDatabase) DatabaseContext.getDatabase(ObjectEntities.RESULT_ENTITY_CODE);
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

		Identifier modifierId = this.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(testsT.length);
		for (int i = 0; i < testsT.length; i++) {
			try {
				objects.add(StorableObjectPool.fromTransferable(ObjectEntities.TEST_ENTITY_CODE, testsT[i]));
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		TestDatabase testDatabase = (TestDatabase) DatabaseContext.getDatabase(ObjectEntities.TEST_ENTITY_CODE);
		try {
			testDatabase.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	/*	Acces validation*/
	/**
	 * TODO Meaningful implementation*/
	private Identifier validateAccess(SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		try {
			LoginServer loginServer = MServerSessionEnvironment.getInstance().getMServerServantManager().getLoginServerReference();

			Identifier_TransferableHolder userIdTHolder = new Identifier_TransferableHolder();
			Identifier_TransferableHolder domainIdTHolder = new Identifier_TransferableHolder();
			loginServer.validateAccess(sessionKeyT, userIdTHolder, domainIdTHolder);
			return new Identifier(userIdTHolder.value);
		}
		catch (CommunicationException ce) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (AMFICOMRemoteException are) {
			throw are;
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}
}
