/*
 * $Id: MServerMeasurementObjectLoader.java,v 1.29 2005/05/24 13:24:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.mcm.corba.MCM;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.29 $, $Date: 2005/05/24 13:24:56 $
 * @author $Author: bass $
 * @module mserver_v1
 */

public final class MServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {
	protected static Identifier preferredMCMId;
	protected static Object lock;

	static {
		lock = new Object();
	}



	public Set loadMeasurements(Set ids) throws RetrieveObjectException {
		MeasurementDatabase database = (MeasurementDatabase) DatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_ENTITY_CODE);
		Set objects = super.retrieveFromDatabase(database, ids);

		Identifier id;
		Set loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (loadIds.isEmpty())
			return objects;

		Set loadedObjects = new HashSet();
		Identifier_Transferable[] loadIdsT = Identifier.createTransferables(loadIds);

		if (preferredMCMId != null) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurements | Trying to load from MCM '" + preferredMCMId + "'",
					Log.DEBUGLEVEL09);
			if (getMeasurementsFromMCM(preferredMCMId, loadIdsT, loadIds, loadedObjects))
				loadIdsT = Identifier.createTransferables(loadIds);
		}	//if (preferredMCMId != null)

		if (!loadIds.isEmpty()) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurements | Searching on all MCMs", Log.DEBUGLEVEL09);

			Set mcmIds = MeasurementServer.getMCMIds();
			Identifier mcmId1;
			for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();) {
				mcmId1 = (Identifier) it.next();
				if (getMeasurementsFromMCM(mcmId1, loadIdsT, loadIds, loadedObjects))
					loadIdsT = Identifier.createTransferables(loadIds);
			}

		}	// if (!loadIds.isEmpty())

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

	public Set loadAnalyses(Set ids) throws RetrieveObjectException {
		AnalysisDatabase database = (AnalysisDatabase) DatabaseContext.getDatabase(ObjectEntities.ANALYSIS_ENTITY_CODE);
		Set objects = super.retrieveFromDatabase(database, ids);

		Identifier id;
		Set loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (loadIds.isEmpty())
			return objects;

		Set loadedObjects = new HashSet();
		Identifier_Transferable[] loadIdsT = Identifier.createTransferables(loadIds);
		if (preferredMCMId != null) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadAnalyses | Trying to load from MCM '" + preferredMCMId + "'",
					Log.DEBUGLEVEL09);
			if (getAnalysesFromMCM(preferredMCMId, loadIdsT, loadIds, loadedObjects))
				loadIdsT = Identifier.createTransferables(loadIds);
		}	//if (preferredMCMId != null)

		if (!loadIds.isEmpty()) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadAnalyses | Searching on all MCMs", Log.DEBUGLEVEL09);

			Set mcmIds = MeasurementServer.getMCMIds();
			Identifier mcmId1;
			for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();) {
				mcmId1 = (Identifier) it.next();
				if (getAnalysesFromMCM(mcmId1, loadIdsT, loadIds, loadedObjects))
					loadIdsT = Identifier.createTransferables(loadIds);
			}

		}	// if (!loadIds.isEmpty())

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

	public Set loadEvaluations(Set ids) throws RetrieveObjectException {
		EvaluationDatabase database = (EvaluationDatabase) DatabaseContext.getDatabase(ObjectEntities.EVALUATION_ENTITY_CODE);
		Set objects = super.retrieveFromDatabase(database, ids);

		Identifier id;
		Set loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (loadIds.isEmpty())
			return objects;

		Set loadedObjects = new HashSet();
		Identifier_Transferable[] loadIdsT = Identifier.createTransferables(loadIds);

		if (preferredMCMId != null) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluations | Trying to load from MCM '" + preferredMCMId + "'",
					Log.DEBUGLEVEL09);
			if (getEvaluationsFromMCM(preferredMCMId, loadIdsT, loadIds, loadedObjects))
				loadIdsT = Identifier.createTransferables(loadIds);
		}	//if (preferredMCMId != null)

		if (!loadIds.isEmpty()) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluations | Searching on all MCMs", Log.DEBUGLEVEL09);

			Set mcmIds = MeasurementServer.getMCMIds();
			Identifier mcmId1;
			for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();) {
				mcmId1 = (Identifier) it.next();
				if (getEvaluationsFromMCM(mcmId1, loadIdsT, loadIds, loadedObjects))
					loadIdsT = Identifier.createTransferables(loadIds);
			}

		}	//if (!loadIds.isEmpty())

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



	private static boolean getMeasurementsFromMCM(final Identifier mcmId1,
			final Identifier_Transferable[] loadIdsT,
			Set loadIds,
			Set loadedObjects) {
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		try {
			mcmRef = MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(mcmId1);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			return false;
		}

		Set mcmLoadedObjects = null;
		try {
			Measurement_Transferable[] transferables = mcmRef.transmitMeasurements(loadIdsT);

			mcmLoadedObjects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				loadIds.remove(new Identifier(transferables[i].header.id));
				try {
					mcmLoadedObjects.add(new Measurement(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}

		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MServerMeasurementObjectLoader.loadMeasurementsFromMCM | Cannot load objects from MCM + '"
					+ mcmId1 + "' database -- " + are.message);
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurementsFromMCM | Loaded " + mcmLoadedObjects.size()
					+ " objects from MCM '" + mcmId1 + "' database", Log.DEBUGLEVEL07);
			loadedObjects.addAll(mcmLoadedObjects);
			return true;
		}
		return false;
	}

	private static boolean getAnalysesFromMCM(final Identifier mcmId1,
			final Identifier_Transferable[] loadIdsT,
			Set loadIds,
			Set loadedObjects) {
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		try {
			mcmRef = MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(mcmId1);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			return false;
		}

		Set mcmLoadedObjects = null;
		try {
			Analysis_Transferable[] transferables = mcmRef.transmitAnalyses(loadIdsT);

			mcmLoadedObjects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				loadIds.remove(new Identifier(transferables[i].header.id));
				try {
					mcmLoadedObjects.add(new Analysis(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}

		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MServerMeasurementObjectLoader.loadAnalysesFromMCM | Cannot load objects from MCM + '"
					+ mcmId1 + "' database -- " + are.message);
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadAnalysesFromMCM | Loaded " + mcmLoadedObjects.size()
					+ " objects from MCM '" + mcmId1 + "' database", Log.DEBUGLEVEL07);
			loadedObjects.addAll(mcmLoadedObjects);
			return true;
		}
		return false;
	}

	private static boolean getEvaluationsFromMCM(final Identifier mcmId1,
			final Identifier_Transferable[] loadIdsT,
			Set loadIds,
			Set loadedObjects) {
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		try {
			mcmRef = MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(mcmId1);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			return false;
		}

		Set mcmLoadedObjects = null;
		try {
			Evaluation_Transferable[] transferables = mcmRef.transmitEvaluations(loadIdsT);

			mcmLoadedObjects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				loadIds.remove(new Identifier(transferables[i].header.id));
				try {
					mcmLoadedObjects.add(new Evaluation(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}

		}
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("MServerMeasurementObjectLoader.loadEvaluationsFromMCM | Cannot load objects from MCM + '"
					+ mcmId1 + "' database -- " + are.message);
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
		}

		if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluationsFromMCM | Loaded " + mcmLoadedObjects.size()
					+ " objects from MCM '" + mcmId1 + "' database", Log.DEBUGLEVEL07);
			loadedObjects.addAll(mcmLoadedObjects);
			return true;
		}
		return false;
	}





	public Set loadMeasurementsButIds(StorableObjectCondition condition, Set ids) throws RetrieveObjectException {
		MeasurementDatabase database = (MeasurementDatabase) DatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_ENTITY_CODE);
		Set objects = super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);

		Identifier id;
		Set loadButIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadButIds.add(id);
		}

		Set loadedObjects = new HashSet();
		Identifier_Transferable[] loadButIdsT = Identifier.createTransferables(loadButIds);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set mcmIds = MeasurementServer.getMCMIds();
		Identifier mcmId1;
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		Set mcmLoadedObjects;
		for (Iterator it = mcmIds.iterator(); it.hasNext();) {
			mcmId1 = (Identifier) it.next();
			try {
				mcmRef = MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(mcmId1);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
				continue;
			}

			mcmLoadedObjects = null;
			try {
				Measurement_Transferable[] transferables = mcmRef.transmitMeasurementsButIdsByCondition(loadButIdsT, conditionT);

				mcmLoadedObjects = new HashSet(transferables.length);
				for (int i = 0; i < transferables.length; i++) {
					loadButIds.add(new Identifier(transferables[i].header.id));
					try {
						mcmLoadedObjects.add(new Measurement(transferables[i]));
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}

				loadButIdsT = Identifier.createTransferables(loadButIds);
			}
			catch (AMFICOMRemoteException are) {
				Log.errorMessage("MServerMeasurementObjectLoader.loadMeasurementsButIds | Cannot retrieve objects from MCM '"
						+ mcmId1 + "' database -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
				Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurementsButIds | Loaded " + mcmLoadedObjects.size()
						+ " objects from MCM '" + mcmId1 + "' database", Log.DEBUGLEVEL07);
				loadedObjects.addAll(mcmLoadedObjects);
			}

		} // for (Iterator it = mcmIds.iterator(); it.hasNext();)

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

	public Set loadAnalysesButIds(StorableObjectCondition condition, Set ids) throws RetrieveObjectException {
		AnalysisDatabase database = (AnalysisDatabase) DatabaseContext.getDatabase(ObjectEntities.ANALYSIS_ENTITY_CODE);
		Set objects = super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);

		Identifier id;
		Set loadButIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadButIds.add(id);
		}

		Set loadedObjects = new HashSet();
		Identifier_Transferable[] loadButIdsT = Identifier.createTransferables(loadButIds);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set mcmIds = MeasurementServer.getMCMIds();
		Identifier mcmId1;
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		Set mcmLoadedObjects;
		for (Iterator it = mcmIds.iterator(); it.hasNext();) {
			mcmId1 = (Identifier) it.next();
			try {
				mcmRef = MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(mcmId1);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
				continue;
			}

			mcmLoadedObjects = null;
			try {
				Analysis_Transferable[] transferables = mcmRef.transmitAnalysesButIdsByCondition(loadButIdsT, conditionT);

				mcmLoadedObjects = new HashSet(transferables.length);
				for (int i = 0; i < transferables.length; i++) {
					loadButIds.add(new Identifier(transferables[i].header.id));
					try {
						mcmLoadedObjects.add(new Analysis(transferables[i]));
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}

				loadButIdsT = Identifier.createTransferables(loadButIds);
			}
			catch (AMFICOMRemoteException are) {
				Log.errorMessage("MServerMeasurementObjectLoader.loadAnalysesButIds | Cannot retrieve objects from MCM '"
						+ mcmId1 + "' database -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
				Log.debugMessage("MServerMeasurementObjectLoader.loadAnalysesButIds | Loaded " + mcmLoadedObjects.size()
						+ " objects from MCM '" + mcmId1 + "' database", Log.DEBUGLEVEL07);
				loadedObjects.addAll(mcmLoadedObjects);
			}

		} // for (Iterator it = mcmIds.iterator(); it.hasNext();)

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
	public Set loadEvaluationsButIds(StorableObjectCondition condition, Set ids) throws RetrieveObjectException {
		EvaluationDatabase database = (EvaluationDatabase) DatabaseContext.getDatabase(ObjectEntities.EVALUATION_ENTITY_CODE);
		Set objects = super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);

		Identifier id;
		Set loadButIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadButIds.add(id);
		}

		Set loadedObjects = new HashSet();
		Identifier_Transferable[] loadButIdsT = Identifier.createTransferables(loadButIds);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set mcmIds = MeasurementServer.getMCMIds();
		Identifier mcmId1;
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		Set mcmLoadedObjects;
		for (Iterator it = mcmIds.iterator(); it.hasNext();) {
			mcmId1 = (Identifier) it.next();
			try {
				mcmRef = MServerSessionEnvironment.getInstance().getMServerServantManager().getVerifiedMCMReference(mcmId1);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
				continue;
			}

			mcmLoadedObjects = null;
			try {
				Evaluation_Transferable[] transferables = mcmRef.transmitEvaluationsButIdsByCondition(loadButIdsT, conditionT);

				mcmLoadedObjects = new HashSet(transferables.length);
				for (int i = 0; i < transferables.length; i++) {
					loadButIds.add(new Identifier(transferables[i].header.id));
					try {
						mcmLoadedObjects.add(new Evaluation(transferables[i]));
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}

				loadButIdsT = Identifier.createTransferables(loadButIds);
			}
			catch (AMFICOMRemoteException are) {
				Log.errorMessage("MServerMeasurementObjectLoader.loadEvaluationsButIds | Cannot retrieve objects from MCM '"
						+ mcmId1 + "' database -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
				Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluationsButIds | Loaded " + mcmLoadedObjects.size()
						+ " objects from MCM '" + mcmId1 + "' database", Log.DEBUGLEVEL07);
				loadedObjects.addAll(mcmLoadedObjects);
			}

		} // for (Iterator it = mcmIds.iterator(); it.hasNext();)

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
	
	public void delete(Set identifiables) {

		if (identifiables == null || identifiables.isEmpty()) {
			return;
		}
		
		Set nonTestIdentifiers = null;
		Set testIdentifiers = null;
		for (Iterator it = nonTestIdentifiers.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			if (id.getMajor() == ObjectEntities.TEST_ENTITY_CODE) {
				if (testIdentifiers == null) {
					testIdentifiers = new HashSet();
				}
				testIdentifiers.add(id);
			} else {
				if (nonTestIdentifiers == null) {
					nonTestIdentifiers = new HashSet();
				}
				nonTestIdentifiers.add(id);
			}			
		}
		
		if (nonTestIdentifiers != null) {
			super.delete(nonTestIdentifiers);
		}
		
		if (testIdentifiers != null) {
			MServerSessionEnvironment sessionEnvironment = MServerSessionEnvironment.getInstance();
			Set nonProcessingIdentifiers = null;
			for (Iterator iterator = testIdentifiers.iterator(); iterator.hasNext();) {
				Identifier testId = (Identifier) iterator.next();
				try {
					Test test = (Test) StorableObjectPool.getStorableObject(testId, true);
					TestStatus status = test.getStatus();
					Identifier mcmId = test.getMCMId();
					MCM verifiedMCMReference = sessionEnvironment.getMServerServantManager().getVerifiedMCMReference(mcmId);
					switch (status.value()) {
						case TestStatus._TEST_STATUS_NEW:
						case TestStatus._TEST_STATUS_SCHEDULED: 
							if (nonProcessingIdentifiers == null) {
								nonProcessingIdentifiers = new HashSet();
							}
							nonProcessingIdentifiers.add(testId);
							super.delete(Collections.singleton(testId));							
							verifiedMCMReference.abortTest((Identifier_Transferable) testId.getTransferable());
							break;
						default: 
							test.setStatus(TestStatus.TEST_STATUS_ABORTED);
							verifiedMCMReference.abortTest((Identifier_Transferable) testId.getTransferable());
							break;

					}
				} catch (ApplicationException ae) {
					Log.errorException(ae);
				} catch (AMFICOMRemoteException e) {
					Log.errorException(e);
				}				
			}
			
			if (nonProcessingIdentifiers != null) {
				super.delete(nonProcessingIdentifiers);
			}
			
		}
	
	}

}
