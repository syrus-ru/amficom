/*
 * $Id: CMServerMeasurementObjectLoader.java,v 1.36 2005/03/31 16:04:41 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
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
 * @version $Revision: 1.36 $, $Date: 2005/03/31 16:04:41 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public final class CMServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {
//	protected static Identifier mcmId;
	protected static Object lock;
	
	private long lastRefresh = 0;
	
	/**
	 * refresh timeout
	 */
	private long refreshTimeout;
	
	static {
		lock = new Object();
	}
	
	public CMServerMeasurementObjectLoader(long refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
	}

	public Set refresh(Set storableObjects) throws DatabaseException, CommunicationException {
		/* refresh no often than one in refreshTimeout ms */
		if (System.currentTimeMillis() - this.lastRefresh < this.refreshTimeout)
			return Collections.EMPTY_SET;

		this.lastRefresh = System.currentTimeMillis();

		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();
		try {
			StorableObjectDatabase database = MeasurementDatabaseContext.getDatabase(entityCode);
			if (database != null)
				return database.refresh(storableObjects);

			return Collections.EMPTY_SET;
		}
		catch (DatabaseException e) {
			Log.errorMessage("CMServerMeasurementObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.refresh | DatabaseException: " + e.getMessage());
		}

	}




	public Measurement loadMeasurement(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Measurement(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("Measurement '" + id + "' not found in database; trying to load from MCM", Log.DEBUGLEVEL08);

			Set mcmIds = ClientMeasurementServer.mcmConnectionManager.getMCMIds();
			synchronized (mcmIds) {
				Identifier mcmId;
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
				for (Iterator it = mcmIds.iterator(); it.hasNext();) {
					mcmId = (Identifier) it.next();
					try {
						mcmRef = ClientMeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId);
					}
					catch (CommunicationException ce) {
						continue;
					}

					try {
						Measurement_Transferable transferable = mcmRef.transmitMeasurement((Identifier_Transferable) id.getTransferable());
						Measurement measurement = new Measurement(transferable);

						try {
							MeasurementDatabaseContext.getMeasurementDatabase().insert(measurement);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
						}

						Log.debugMessage("Measurement '" + id + "' loaded from MCM '" + mcmId + "' database", Log.DEBUGLEVEL07);
						return measurement;						
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
							Log.debugMessage("Measurement '" + id + "' not found in MCM '" + mcmId + "' database", Log.DEBUGLEVEL08);
						else
							Log.errorMessage("Exception while loading measurement '" + id + "' from MCM '" + mcmId + "' database");
					}
					catch (Throwable throwable) {
						Log.errorException(throwable);
					}

				}	//for (Iterator it = mcmIds.iterator(); it.hasNext();)
			}	//synchronized (mcmIds)

			throw new ObjectNotFoundException("Measurement '" + id + "' not found on any MCM");
		}	//catch (ObjectNotFoundException e)
	}

	public Analysis loadAnalysis(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Analysis(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("Analysis '" + id + "' not found in database; trying to load from MCM", Log.DEBUGLEVEL08);

			Set mcmIds = ClientMeasurementServer.mcmConnectionManager.getMCMIds();
			synchronized (mcmIds) {
				Identifier mcmId;
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
				for (Iterator it = mcmIds.iterator(); it.hasNext();) {
					mcmId = (Identifier) it.next();
					try {
						mcmRef = ClientMeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId);
					}
					catch (CommunicationException ce) {
						continue;
					}

					try {
						Analysis_Transferable transferable = mcmRef.transmitAnalysis((Identifier_Transferable) id.getTransferable());
						Analysis analysis = new Analysis(transferable);

						try {
							MeasurementDatabaseContext.getAnalysisDatabase().insert(analysis);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
						}

						Log.debugMessage("Analysis '" + id + "' loaded from MCM '" + mcmId + "' database", Log.DEBUGLEVEL07);
						return analysis;						
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
							Log.debugMessage("Analysis '" + id + "' not found in MCM '" + mcmId + "' database", Log.DEBUGLEVEL08);
						else
							Log.errorMessage("Exception while loading analysis '" + id + "' from MCM '" + mcmId + "' database");
					}
					catch (Throwable throwable) {
						Log.errorException(throwable);
					}

				}	//for (Iterator it = mcmIds.iterator(); it.hasNext();)
			}	//synchronized (mcmIds)

			throw new ObjectNotFoundException("Analysis '" + id + "' not found on any MCM");
		}	//catch (ObjectNotFoundException e)
	}

	public Evaluation loadEvaluation(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Evaluation(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("Evaluation '" + id + "' not found in database; trying to load from MCM", Log.DEBUGLEVEL08);

			Set mcmIds = ClientMeasurementServer.mcmConnectionManager.getMCMIds();
			synchronized (mcmIds) {
				Identifier mcmId;
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
				for (Iterator it = mcmIds.iterator(); it.hasNext();) {
					mcmId = (Identifier) it.next();
					try {
						mcmRef = ClientMeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId);
					}
					catch (CommunicationException ce) {
						continue;
					}

					try {
						Evaluation_Transferable transferable = mcmRef.transmitEvaluation((Identifier_Transferable) id.getTransferable());
						Evaluation evaluation = new Evaluation(transferable);

						try {
							MeasurementDatabaseContext.getEvaluationDatabase().insert(evaluation);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
						}

						Log.debugMessage("Evaluation '" + id + "' loaded from MCM '" + mcmId + "' database", Log.DEBUGLEVEL07);
						return evaluation;						
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
							Log.debugMessage("Evaluation '" + id + "' not found in MCM '" + mcmId + "' database", Log.DEBUGLEVEL08);
						else
							Log.errorMessage("Exception while loading evaluation '" + id + "' from MCM '" + mcmId + "' database");
					}
					catch (Throwable throwable) {
						Log.errorException(throwable);
					}

				}	//for (Iterator it = mcmIds.iterator(); it.hasNext();)
			}	//synchronized (mcmIds)

			throw new ObjectNotFoundException("Evaluation '" + id + "' not found on any MCM");
		}	//catch (ObjectNotFoundException e)
	}




	public Collection loadAnalyses(Collection ids) throws ApplicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.getAnalysisDatabase();
		Collection objects;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			String mesg = "CMServerMeasurementObjectLoader.loadAnalyses | Cannot load objects from database: " + ide.getMessage();
			throw new RetrieveObjectException(mesg, ide);
		}

		Identifier id;
		Set loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (loadIds.isEmpty())
			return objects;

		Set loadedObjects = new HashSet();

		Set mcmIds = ClientMeasurementServer.mcmConnectionManager.getMCMIds();
		synchronized (mcmIds) {
			Identifier mcmId;
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
			Identifier_Transferable[] loadIdsT = Identifier.createTransferables(loadIds);
			Set mcmLoadedObjects;
			for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();) {
				mcmId = (Identifier) it.next();
				try {
					mcmRef = ClientMeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId);
				}
				catch (CommunicationException ce) {
					continue;
				}

				mcmLoadedObjects = null;
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

					if (!loadIds.isEmpty())
						loadIdsT = Identifier.createTransferables(loadIds);
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("CMServerMeasurementObjectLoader.loadAnalyses | Cannot load objects from MCM + '"
							+ mcmId + "' database -- " + are.message);
				}
				catch (Throwable throwable) {
					Log.errorException(throwable);
				}

				if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
					Log.debugMessage("CMServerMeasurementObjectLoader.loadAnalyses | Loaded " + mcmLoadedObjects.size()
							+ " objects from MCM '" + mcmId + "' database", Log.DEBUGLEVEL07);
					loadedObjects.addAll(mcmLoadedObjects);
				}

			}	//for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();)
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

	public Collection loadEvaluations(Collection ids) throws ApplicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.getEvaluationDatabase();
		Collection objects;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			String mesg = "CMServerMeasurementObjectLoader.loadEvaluations | Cannot load objects from database: " + ide.getMessage();
			throw new RetrieveObjectException(mesg, ide);
		}

		Identifier id;
		Set loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (loadIds.isEmpty())
			return objects;

		Set loadedObjects = new HashSet();

		Set mcmIds = ClientMeasurementServer.mcmConnectionManager.getMCMIds();
		synchronized (mcmIds) {
			Identifier mcmId;
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
			Identifier_Transferable[] loadIdsT = Identifier.createTransferables(loadIds);
			Set mcmLoadedObjects;
			for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();) {
				mcmId = (Identifier) it.next();
				try {
					mcmRef = ClientMeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId);
				}
				catch (CommunicationException ce) {
					continue;
				}

				mcmLoadedObjects = null;
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

					if (!loadIds.isEmpty())
						loadIdsT = Identifier.createTransferables(loadIds);
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluations | Cannot load objects from MCM + '"
							+ mcmId + "' database -- " + are.message);
				}
				catch (Throwable throwable) {
					Log.errorException(throwable);
				}

				if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
					Log.debugMessage("CMServerMeasurementObjectLoader.loadEvaluations | Loaded " + mcmLoadedObjects.size()
							+ " objects from MCM '" + mcmId + "' database", Log.DEBUGLEVEL07);
					loadedObjects.addAll(mcmLoadedObjects);
				}

			}	//for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();)
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

	public Collection loadMeasurements(Collection ids) throws ApplicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.getMeasurementDatabase();
		Collection objects;
		try {
			objects = database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			String mesg = "CMServerMeasurementObjectLoader.loadMeasurements | Cannot load objects from database: " + ide.getMessage();
			throw new RetrieveObjectException(mesg, ide);
		}

		Identifier id;
		Set loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (loadIds.isEmpty())
			return objects;

		Set loadedObjects = new HashSet();

		Set mcmIds = ClientMeasurementServer.mcmConnectionManager.getMCMIds();
		synchronized (mcmIds) {
			Identifier mcmId;
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
			Identifier_Transferable[] loadIdsT = Identifier.createTransferables(loadIds);
			Set mcmLoadedObjects;
			for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();) {
				mcmId = (Identifier) it.next();
				try {
					mcmRef = ClientMeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId);
				}
				catch (CommunicationException ce) {
					continue;
				}

				mcmLoadedObjects = null;
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

					if (!loadIds.isEmpty())
						loadIdsT = Identifier.createTransferables(loadIds);
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("CMServerMeasurementObjectLoader.loadMeasurements | Cannot load objects from MCM + '"
							+ mcmId + "' database -- " + are.message);
				}
				catch (Throwable throwable) {
					Log.errorException(throwable);
				}

				if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
					Log.debugMessage("CMServerMeasurementObjectLoader.loadMeasurements | Loaded " + mcmLoadedObjects.size()
							+ " objects from MCM '" + mcmId + "' database", Log.DEBUGLEVEL07);
					loadedObjects.addAll(mcmLoadedObjects);
				}

			}	//for (Iterator it = mcmIds.iterator(); it.hasNext() && !loadIds.isEmpty();)
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





	public Collection loadAnalysesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.getAnalysisDatabase();
		Collection objects;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			String mesg = "CMServerMeasurementObjectLoader.loadAnalysesButIds | Cannot load objects from database: " + ide.getMessage();
			throw new RetrieveObjectException(mesg, ide);
		}

		Identifier id;
		Collection loadButIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadButIds.add(id);
		}

		Identifier_Transferable[] loadButIdsT = Identifier.createTransferables(loadButIds);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Collection loadedObjects = new HashSet();

		Set mcmIds = ClientMeasurementServer.mcmConnectionManager.getMCMIds();
		synchronized (mcmIds) {
			Identifier mcmId;
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
			Set mcmLoadedObjects;
			for (Iterator it = mcmIds.iterator(); it.hasNext();) {
				mcmId = (Identifier) it.next();
				try {
					mcmRef = ClientMeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId);
				}
				catch (CommunicationException ce) {
					continue;
				}

				mcmLoadedObjects = null;
				try {
					Analysis_Transferable[] transferables = mcmRef.transmitAnalysesButIdsByCondition(loadButIdsT, conditionT);

					mcmLoadedObjects = new HashSet(transferables.length);
					for (int i = 0; i < transferables.length; i++) {
						try {
							mcmLoadedObjects.add(new Analysis(transferables[i]));
						}
						catch (CreateObjectException coe) {
							Log.errorException(coe);
						}
					}
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("CMServerMeasurementObjectLoader.loadAnalysesButIds | Cannot retrieve objects from MCM '"
							+ mcmId + "' database -- " + are.message);
				}
				catch (Throwable throwable) {
					Log.errorException(throwable);
				}

				if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
					Log.debugMessage("CMServerMeasurementObjectLoader.loadAnalysesButIds | Loaded " + mcmLoadedObjects.size()
							+ " objects from MCM '" + mcmId + "' database", Log.DEBUGLEVEL07);
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

	public Collection loadEvaluationsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.getEvaluationDatabase();
		Collection objects;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			String mesg = "CMServerMeasurementObjectLoader.loadEvaluationsButIds | Cannot load objects from database: " + ide.getMessage();
			throw new RetrieveObjectException(mesg, ide);
		}

		Identifier id;
		Collection loadButIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadButIds.add(id);
		}

		Identifier_Transferable[] loadButIdsT = Identifier.createTransferables(loadButIds);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Collection loadedObjects = new HashSet();

		Set mcmIds = ClientMeasurementServer.mcmConnectionManager.getMCMIds();
		synchronized (mcmIds) {
			Identifier mcmId;
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
			Set mcmLoadedObjects;
			for (Iterator it = mcmIds.iterator(); it.hasNext();) {
				mcmId = (Identifier) it.next();
				try {
					mcmRef = ClientMeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId);
				}
				catch (CommunicationException ce) {
					continue;
				}

				mcmLoadedObjects = null;
				try {
					Evaluation_Transferable[] transferables = mcmRef.transmitEvaluationsButIdsByCondition(loadButIdsT, conditionT);

					mcmLoadedObjects = new HashSet(transferables.length);
					for (int i = 0; i < transferables.length; i++) {
						try {
							mcmLoadedObjects.add(new Evaluation(transferables[i]));
						}
						catch (CreateObjectException coe) {
							Log.errorException(coe);
						}
					}
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluationsButIds | Cannot retrieve objects from MCM '"
							+ mcmId + "' database -- " + are.message);
				}
				catch (Throwable throwable) {
					Log.errorException(throwable);
				}

				if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
					Log.debugMessage("CMServerMeasurementObjectLoader.loadEvaluationsButIds | Loaded " + mcmLoadedObjects.size()
							+ " objects from MCM '" + mcmId + "' database", Log.DEBUGLEVEL07);
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

	public Collection loadMeasurementsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.getMeasurementDatabase();
		Collection objects;
		try {
			objects = database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
			String mesg = "CMServerMeasurementObjectLoader.loadMeasurementsButIds | Cannot load objects from database: " + ide.getMessage();
			throw new RetrieveObjectException(mesg, ide);
		}

		Identifier id;
		Collection loadButIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadButIds.add(id);
		}

		Identifier_Transferable[] loadButIdsT = Identifier.createTransferables(loadButIds);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Collection loadedObjects = new HashSet();

		Set mcmIds = ClientMeasurementServer.mcmConnectionManager.getMCMIds();
		synchronized (mcmIds) {
			Identifier mcmId;
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
			Set mcmLoadedObjects;
			for (Iterator it = mcmIds.iterator(); it.hasNext();) {
				mcmId = (Identifier) it.next();
				try {
					mcmRef = ClientMeasurementServer.mcmConnectionManager.getVerifiedMCMReference(mcmId);
				}
				catch (CommunicationException ce) {
					continue;
				}

				mcmLoadedObjects = null;
				try {
					Measurement_Transferable[] transferables = mcmRef.transmitMeasurementsButIdsByCondition(loadButIdsT, conditionT);

					mcmLoadedObjects = new HashSet(transferables.length);
					for (int i = 0; i < transferables.length; i++) {
						try {
							mcmLoadedObjects.add(new Measurement(transferables[i]));
						}
						catch (CreateObjectException coe) {
							Log.errorException(coe);
						}
					}
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | Cannot retrieve objects from MCM '"
							+ mcmId + "' database -- " + are.message);
				}
				catch (Throwable throwable) {
					Log.errorException(throwable);
				}

				if (mcmLoadedObjects != null && !mcmLoadedObjects.isEmpty()) {
					Log.debugMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | Loaded " + mcmLoadedObjects.size()
							+ " objects from MCM '" + mcmId + "' database", Log.DEBUGLEVEL07);
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
