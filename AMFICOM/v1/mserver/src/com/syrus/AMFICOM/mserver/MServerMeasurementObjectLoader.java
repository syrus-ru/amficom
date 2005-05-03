/*
 * $Id: MServerMeasurementObjectLoader.java,v 1.25 2005/05/03 15:28:48 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.25 $, $Date: 2005/05/03 15:28:48 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public final class MServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {
	protected static Identifier preferredMCMId;
	protected static Object lock;
	
	static {
		lock = new Object();
	}



	public Set loadMeasurements(Set ids) throws RetrieveObjectException {
		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
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
		AnalysisDatabase database = MeasurementDatabaseContext.getAnalysisDatabase();
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
		EvaluationDatabase database = MeasurementDatabaseContext.getEvaluationDatabase();
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
		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
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
		AnalysisDatabase database = MeasurementDatabaseContext.getAnalysisDatabase();
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
		EvaluationDatabase database = MeasurementDatabaseContext.getEvaluationDatabase();
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

}
