/*
 * $Id: MServerMeasurementObjectLoader.java,v 1.21 2005/04/05 10:54:38 arseniy Exp $
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
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
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
 * @version $Revision: 1.21 $, $Date: 2005/04/05 10:54:38 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public final class MServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {
	protected static Identifier preferredMCMId;
	protected static Object lock;
	
	static {
		lock = new Object();
	}




	public Measurement loadMeasurement(Identifier id)
			throws RetrieveObjectException, CommunicationException, CreateObjectException, ObjectNotFoundException {
		try {
			return new Measurement(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurement | Measurement '" + id
					+ "' not found in database; trying to load from MCM", Log.DEBUGLEVEL08);
			Measurement measurement = null;
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef;

			if (preferredMCMId != null) {
				Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurement | Trying to load from MCM '" + preferredMCMId + "'",
						Log.DEBUGLEVEL09);

				try {
					mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(preferredMCMId);
					measurement = new Measurement(mcmRef.transmitMeasurement((Identifier_Transferable)id.getTransferable()));
				}
				catch (IllegalDataException ide) {
					Log.errorException(ide);
					Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurement | Cannot load measurement '" + id
							+ "' from MCM '" + preferredMCMId + "'", Log.DEBUGLEVEL09);
				}
				catch (CommunicationException ce) {
					Log.errorException(ce);
					Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurement | Cannot load measurement '" + id
							+ "' from MCM '" + preferredMCMId + "'", Log.DEBUGLEVEL09);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() != ErrorCode._ERROR_NOT_FOUND)
						throw new RetrieveObjectException("Retrieve measurement '" + id + "' from MCM '" + preferredMCMId + "' -- " + are.message);
					Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurement | Measurement '" + id
							+ "' not found on MCM '" + preferredMCMId + "' -- " + are.message, Log.DEBUGLEVEL09);
				}
				catch (Throwable throwable) {
					Log.errorException(throwable);
				}
			}

			if (measurement == null) {
				Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurement | Searching measurement '" + id + "' on all MCMs",
						Log.DEBUGLEVEL09);

				Set mcmIds = MeasurementServer.mcmConnectionManager.getMCMIds();
				Identifier mcmId1;
				synchronized (mcmIds) {
					for (Iterator it = mcmIds.iterator(); it.hasNext();) {
						mcmId1 = (Identifier) it.next();
						try {
							mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId1);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
							continue;
						}

						try {
							Measurement_Transferable transferable = mcmRef.transmitMeasurement((Identifier_Transferable) id.getTransferable());
							measurement = new Measurement(transferable);
						}
						catch (AMFICOMRemoteException are) {
							if (are.error_code.value() != ErrorCode._ERROR_NOT_FOUND)
								throw new RetrieveObjectException("Retrieve measurement '" + id + "' from MCM '" + mcmId1 + "' -- " + are.message);
							Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurement | Measurement '" + id
									+ "' not found on MCM '" + mcmId1 + "' -- " + are.message, Log.DEBUGLEVEL09);
						}
						catch (Throwable throwable) {
							Log.errorException(throwable);
						}

					}	//for (Iterator it = mcmIds.iterator(); it.hasNext();)
				}	//synchronized (mcmIds)

			}	//if (measurement == null)

			if (measurement != null) {
				try {
				MeasurementDatabaseContext.getMeasurementDatabase().insert(measurement);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return measurement;
			}
			throw new ObjectNotFoundException("Measurement '" + id + "' not found on any MCM");

		}	//catch (ObjectNotFoundException e)
	}

	public Analysis loadAnalysis(Identifier id)
			throws RetrieveObjectException, CommunicationException, CreateObjectException, ObjectNotFoundException {
		try {
			return new Analysis(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadAnalysis | Analysis '" + id
					+ "' not found in database; trying to load from MCM", Log.DEBUGLEVEL08);
			Analysis analysis = null;
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef;

			if (preferredMCMId != null) {
				Log.debugMessage("MServerMeasurementObjectLoader.loadAnalysis | Trying to load from MCM '" + preferredMCMId + "'",
						Log.DEBUGLEVEL09);

				try {
					mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(preferredMCMId);
					Analysis_Transferable transferable = mcmRef.transmitAnalysis((Identifier_Transferable) id.getTransferable());
					analysis = new Analysis(transferable);
				}
				catch (IllegalDataException ide) {
					Log.errorException(ide);
					Log.debugMessage("MServerMeasurementObjectLoader.loadAnalysis | Cannot load analysis '" + id
							+ "' from MCM '" + preferredMCMId + "'", Log.DEBUGLEVEL09);
				}
				catch (CommunicationException ce) {
					Log.errorException(ce);
					Log.debugMessage("MServerMeasurementObjectLoader.loadAnalysis | Cannot load analysis '" + id
							+ "' from MCM '" + preferredMCMId + "'", Log.DEBUGLEVEL09);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() != ErrorCode._ERROR_NOT_FOUND)
						throw new RetrieveObjectException("Retrieve analysis '" + id + "' from MCM '" + preferredMCMId + "' -- " + are.message);
					Log.debugMessage("MServerMeasurementObjectLoader.loadAnalysis | Analysis '" + id
							+ "' not found on MCM '" + preferredMCMId + "' -- " + are.message, Log.DEBUGLEVEL09);
				}
				catch (Throwable throwable) {
					Log.errorException(throwable);
				}
			}

			if (analysis == null) {
				Log.debugMessage("MServerMeasurementObjectLoader.loadAnalysis | Searching analysis '" + id + "' on all MCMs",
						Log.DEBUGLEVEL09);

				Set mcmIds = MeasurementServer.mcmConnectionManager.getMCMIds();
				Identifier mcmId1;
				synchronized (mcmIds) {
					for (Iterator it = mcmIds.iterator(); it.hasNext();) {
						mcmId1 = (Identifier) it.next();
						try {
							mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId1);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
							continue;
						}

						try {
							Analysis_Transferable transferable = mcmRef.transmitAnalysis((Identifier_Transferable) id.getTransferable());
							analysis = new Analysis(transferable);
						}
						catch (AMFICOMRemoteException are) {
							if (are.error_code.value() != ErrorCode._ERROR_NOT_FOUND)
								throw new RetrieveObjectException("Retrieve analysis '" + id + "' from MCM '" + mcmId1 + "' -- " + are.message);
							Log.debugMessage("MServerMeasurementObjectLoader.loadAnalysis | Analysis '" + id
									+ "' not found on MCM '" + mcmId1 + "' -- " + are.message, Log.DEBUGLEVEL09);
						}
						catch (Throwable throwable) {
							Log.errorException(throwable);
						}

					}	//for (Iterator it = mcmIds.iterator(); it.hasNext();)
				}	//synchronized (mcmIds)

			}	//if (analysis == null)

			if (analysis != null) {
				try {
					MeasurementDatabaseContext.getAnalysisDatabase().insert(analysis);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return analysis;
			}
			throw new ObjectNotFoundException("Analysis '" + id + "' not found on any MCM");

		}	//catch (ObjectNotFoundException e)
	}

	public Evaluation loadEvaluation(Identifier id)
			throws RetrieveObjectException, CommunicationException, CreateObjectException, ObjectNotFoundException {
		try {
			return new Evaluation(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluation | Evaluation '" + id
					+ "' not found in database; trying to load from MCM", Log.DEBUGLEVEL08);
			Evaluation evaluation = null;
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef;

			if (preferredMCMId != null) {
				Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluation | Trying to load from MCM '" + preferredMCMId + "'",
						Log.DEBUGLEVEL09);

				try {
					mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(preferredMCMId);
					Evaluation_Transferable transferable = mcmRef.transmitEvaluation((Identifier_Transferable) id.getTransferable());
					evaluation = new Evaluation(transferable);
				}
				catch (IllegalDataException ide) {
					Log.errorException(ide);
					Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluation | Cannot load evaluation '" + id
							+ "' from MCM '" + preferredMCMId + "'", Log.DEBUGLEVEL09);
				}
				catch (CommunicationException ce) {
					Log.errorException(ce);
					Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluation | Cannot load evaluation '" + id
							+ "' from MCM '" + preferredMCMId + "'", Log.DEBUGLEVEL09);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() != ErrorCode._ERROR_NOT_FOUND)
						throw new RetrieveObjectException("Retrieve evaluation '" + id + "' from MCM '" + preferredMCMId + "' -- " + are.message);
					Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluation | Evaluation '" + id
							+ "' not found on MCM '" + preferredMCMId + "' -- " + are.message, Log.DEBUGLEVEL09);
				}
				catch (Throwable throwable) {
					Log.errorException(throwable);
				}
			}

			if (evaluation == null) {
				Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluation | Searching evaluation '" + id + "' on all MCMs",
						Log.DEBUGLEVEL09);

				Set mcmIds = MeasurementServer.mcmConnectionManager.getMCMIds();
				Identifier mcmId1;
				synchronized (mcmIds) {
					for (Iterator it = mcmIds.iterator(); it.hasNext();) {
						mcmId1 = (Identifier) it.next();
						try {
							mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId1);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
							continue;
						}

						try {
							Evaluation_Transferable transferable = mcmRef.transmitEvaluation((Identifier_Transferable) id.getTransferable());
							evaluation = new Evaluation(transferable);
						}
						catch (AMFICOMRemoteException are) {
							if (are.error_code.value() != ErrorCode._ERROR_NOT_FOUND)
								throw new RetrieveObjectException("Retrieve evaluation '" + id + "' from MCM '" + mcmId1 + "' -- " + are.message);
							Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluation | Evaluation '" + id
									+ "' not found on MCM '" + mcmId1 + "' -- " + are.message, Log.DEBUGLEVEL09);
						}
						catch (Throwable throwable) {
							Log.errorException(throwable);
						}

					}	//for (Iterator it = mcmIds.iterator(); it.hasNext();)
				}	//synchronized (mcmIds)

			}	//if (evaluation == null)

			if (evaluation != null) {
				try {
					MeasurementDatabaseContext.getEvaluationDatabase().insert(evaluation);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return evaluation;
			}
			throw new ObjectNotFoundException("Evaluation '" + id + "' not found on any MCM");

		}	//catch (ObjectNotFoundException e)
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
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = Identifier.createTransferables(loadIds);
		}
		catch (IllegalDataException ide) {
			//Never
			Log.errorException(ide);
		}

		if (preferredMCMId != null) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurements | Trying to load from MCM '" + preferredMCMId + "'",
					Log.DEBUGLEVEL09);
			if (getMeasurementsFromMCM(preferredMCMId, loadIdsT, loadIds, loadedObjects)) {
				try {
					loadIdsT = Identifier.createTransferables(loadIds);
				}
				catch (IllegalDataException ide) {
					// Never
					Log.errorException(ide);
				}
			}
		}	//if (preferredMCMId != null)

		if (!loadIds.isEmpty()) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadMeasurements | Searching on all MCMs", Log.DEBUGLEVEL09);

			Set mcmIds = MeasurementServer.mcmConnectionManager.getMCMIds();
			Identifier mcmId1;
			synchronized (mcmIds) {
				for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();) {
					mcmId1 = (Identifier) it.next();
					if (getMeasurementsFromMCM(mcmId1, loadIdsT, loadIds, loadedObjects)) {
						try {
							loadIdsT = Identifier.createTransferables(loadIds);
						}
						catch (IllegalDataException ide) {
							// Never
							Log.errorException(ide);
						}
					}
				}
			}	//synchronized (mcmIds)

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
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = Identifier.createTransferables(loadIds);
		}
		catch (IllegalDataException ide) {
			//Never
			Log.errorException(ide);
		}

		if (preferredMCMId != null) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadAnalyses | Trying to load from MCM '" + preferredMCMId + "'",
					Log.DEBUGLEVEL09);
			if (getAnalysesFromMCM(preferredMCMId, loadIdsT, loadIds, loadedObjects)) {
				try {
					loadIdsT = Identifier.createTransferables(loadIds);
				}
				catch (IllegalDataException ide) {
					// Never
					Log.errorException(ide);
				}
			}
		}	//if (preferredMCMId != null)

		if (!loadIds.isEmpty()) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadAnalyses | Searching on all MCMs", Log.DEBUGLEVEL09);

			Set mcmIds = MeasurementServer.mcmConnectionManager.getMCMIds();
			Identifier mcmId1;
			synchronized (mcmIds) {
				for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();) {
					mcmId1 = (Identifier) it.next();
					if (getAnalysesFromMCM(mcmId1, loadIdsT, loadIds, loadedObjects)) {
						try {
							loadIdsT = Identifier.createTransferables(loadIds);
						}
						catch (IllegalDataException ide) {
							// Never
							Log.errorException(ide);
						}
					}
				}
			}	//synchronized (mcmIds)

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
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = Identifier.createTransferables(loadIds);
		}
		catch (IllegalDataException ide) {
			//Never
			Log.errorException(ide);
		}

		if (preferredMCMId != null) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluations | Trying to load from MCM '" + preferredMCMId + "'",
					Log.DEBUGLEVEL09);
			if (getEvaluationsFromMCM(preferredMCMId, loadIdsT, loadIds, loadedObjects)) {
				try {
					loadIdsT = Identifier.createTransferables(loadIds);
				}
				catch (IllegalDataException ide) {
					// Never
					Log.errorException(ide);
				}
			}
		}	//if (preferredMCMId != null)

		if (!loadIds.isEmpty()) {
			Log.debugMessage("MServerMeasurementObjectLoader.loadEvaluations | Searching on all MCMs", Log.DEBUGLEVEL09);

			Set mcmIds = MeasurementServer.mcmConnectionManager.getMCMIds();
			Identifier mcmId1;
			synchronized (mcmIds) {
				for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();) {
					mcmId1 = (Identifier) it.next();
					if (getEvaluationsFromMCM(mcmId1, loadIdsT, loadIds, loadedObjects)) {
						try {
							loadIdsT = Identifier.createTransferables(loadIds);
						}
						catch (IllegalDataException ide) {
							// Never
							Log.errorException(ide);
						}
					}
				}
			}	//synchronized (mcmIds)

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
			mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId1);
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
			mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId1);
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
			mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId1);
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
		Identifier_Transferable[] loadButIdsT = null;
		try {
			loadButIdsT = Identifier.createTransferables(loadButIds);
		}
		catch (IllegalDataException ide) {
			//Never
			Log.errorException(ide);
		}
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set mcmIds = MeasurementServer.mcmConnectionManager.getMCMIds();
		Identifier mcmId1;
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		Set mcmLoadedObjects;
		synchronized (mcmIds) {
			for (Iterator it = mcmIds.iterator(); it.hasNext();) {
				mcmId1 = (Identifier) it.next();
				try {
					mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId1);
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

			}	//for (Iterator it = mcmIds.iterator(); it.hasNext();)
		}	//synchronized (mcmIds)

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
		Identifier_Transferable[] loadButIdsT = null;
		try {
			loadButIdsT = Identifier.createTransferables(loadButIds);
		}
		catch (IllegalDataException ide) {
			//Never
			Log.errorException(ide);
		}
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set mcmIds = MeasurementServer.mcmConnectionManager.getMCMIds();
		Identifier mcmId1;
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		Set mcmLoadedObjects;
		synchronized (mcmIds) {
			for (Iterator it = mcmIds.iterator(); it.hasNext();) {
				mcmId1 = (Identifier) it.next();
				try {
					mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId1);
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

			}	//for (Iterator it = mcmIds.iterator(); it.hasNext();)
		}	//synchronized (mcmIds)

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
		Identifier_Transferable[] loadButIdsT = null;
		try {
			loadButIdsT = Identifier.createTransferables(loadButIds);
		}
		catch (IllegalDataException ide) {
			//Never
			Log.errorException(ide);
		}
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set mcmIds = MeasurementServer.mcmConnectionManager.getMCMIds();
		Identifier mcmId1;
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		Set mcmLoadedObjects;
		synchronized (mcmIds) {
			for (Iterator it = mcmIds.iterator(); it.hasNext();) {
				mcmId1 = (Identifier) it.next();
				try {
					mcmRef = MeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId1);
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

			}	//for (Iterator it = mcmIds.iterator(); it.hasNext();)
		}	//synchronized (mcmIds)

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
