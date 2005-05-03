/*
 * $Id: MCMMeasurementObjectLoader.java,v 1.43 2005/05/03 18:30:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.HashSet;
import java.util.Iterator;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.SessionKey_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.CronTemporalPattern;
import com.syrus.AMFICOM.measurement.CronTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPattern;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.CronTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.IntervalsTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.PeriodicalTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.43 $, $Date: 2005/05/03 18:30:31 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {

	/* Load multiple objects*/

	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws RetrieveObjectException {
		MeasurementTypeDatabase database = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			MeasurementType_Transferable[] transferables = mServerRef.transmitMeasurementTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new MeasurementType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadMeasurementTypes | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws RetrieveObjectException {
		AnalysisTypeDatabase database = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			AnalysisType_Transferable[] transferables = mServerRef.transmitAnalysisTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new AnalysisType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadAnalysisTypes | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws RetrieveObjectException {
		EvaluationTypeDatabase database = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			EvaluationType_Transferable[] transferables = mServerRef.transmitEvaluationTypes(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new EvaluationType(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadEvaluationTypes | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}



	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws RetrieveObjectException {
		MeasurementSetupDatabase database = MeasurementDatabaseContext.getMeasurementSetupDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			MeasurementSetup_Transferable[] transferables = mServerRef.transmitMeasurementSetups(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new MeasurementSetup(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadMeasurementSetups | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadSets(java.util.Set ids) throws RetrieveObjectException {
		SetDatabase database = MeasurementDatabaseContext.getSetDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			Set_Transferable[] transferables = mServerRef.transmitSets(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new Set(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadSets | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws RetrieveObjectException {
		CronTemporalPatternDatabase database = MeasurementDatabaseContext.getCronTemporalPatternDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			CronTemporalPattern_Transferable[] transferables = mServerRef.transmitCronTemporalPatterns(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new CronTemporalPattern(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadCronTemporalPatterns | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadIntervalsTemporalPatterns(java.util.Set ids) throws RetrieveObjectException {
		IntervalsTemporalPatternDatabase database = MeasurementDatabaseContext.getIntervalsTemporalPatternDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			IntervalsTemporalPattern_Transferable[] transferables = mServerRef.transmitIntervalsTemporalPatterns(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new IntervalsTemporalPattern(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadIntervalsTemporalPatterns | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}

	public java.util.Set loadPeriodicalTemporalPatterns(java.util.Set ids) throws RetrieveObjectException {
		PeriodicalTemporalPatternDatabase database = MeasurementDatabaseContext.getPeriodicalTemporalPatternDatabase();
		java.util.Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		java.util.Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
			PeriodicalTemporalPattern_Transferable[] transferables = mServerRef.transmitPeriodicalTemporalPatterns(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new PeriodicalTemporalPattern(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MCMMeasurementObjectLoader.loadPeriodicalTemporalPatterns | Cannot load objects from MeasurementServer");
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (!loadedObjects.isEmpty()) {
			objects.addAll(loadedObjects);

			try {
				database.insert(loadedObjects);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}

		return objects;
	}


	public void saveTests(java.util.Set objects, boolean force) throws ApplicationException {
		super.saveTests(objects, force);

		MServer mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Test_Transferable[] transferables = new Test_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Test_Transferable) ((Test) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = mServerRef.receiveTests(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	/*
	 * MCM do not need in all below methods
	 * */	

	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling type, ids: " + ids);
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling, ids: " + ids);
	}




	
	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling type, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		throw new UnsupportedOperationException("MCM doesn't need in modeling, ids: " + ids + ", condition: " + condition);
	}


	public void saveAnalysisTypes(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveEvaluationTypes(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveMeasurementSetups(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveMeasurementTypes(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveModelings(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveSets(java.util.Set objects, boolean force) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCronTemporalPatterns(java.util.Set storableObjects,boolean force) throws ApplicationException{
		throw new UnsupportedOperationException("May be not need this? " + storableObjects + ", " + force);
	}
	
	public void saveIntervalsTemporalPatterns(java.util.Set storableObjects,boolean force) throws ApplicationException{
		throw new UnsupportedOperationException("May be not need this? " + storableObjects + ", " + force);
	}
	
	public void savePeriodicalTemporalPatterns(java.util.Set storableObjects,boolean force) throws ApplicationException{
		throw new UnsupportedOperationException("May be not need this? " + storableObjects + ", " + force);
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		throw new UnsupportedOperationException("Method not implemented, objects: " + storableObjects);
	}




	public void delete(final java.util.Set identifiables) {
		throw new UnsupportedOperationException("Method not implemented, objects: " + identifiables);
	}

}
