/*
 * $Id: CMServerMeasurementObjectLoader.java,v 1.30 2005/03/23 12:43:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
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
 * @version $Revision: 1.30 $, $Date: 2005/03/23 12:43:31 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public final class CMServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {
	protected static Identifier mcmId;
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
			Log.debugMessage("Measurement '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);

			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM) ClientMeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {

				try {
					Measurement_Transferable transferable = mcmRef.transmitMeasurement((Identifier_Transferable) id.getTransferable());
					Measurement measurement = new Measurement(transferable);

					try {
						MeasurementDatabaseContext.getMeasurementDatabase().insert(measurement);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return measurement;
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Measurement '" + id + "' not found in MCM '" + mcmId + "' database");
					throw new RetrieveObjectException("Cannot retrieve measurement '" + id + "' from MCM '"
							+ mcmId + "' database -- " + are.message);
				}
			}

			String mesg = "Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it";
			Log.errorMessage(mesg);
			ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			throw new CommunicationException(mesg);
		}

	}

	public Analysis loadAnalysis(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Analysis(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("Analysis '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);

			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM) ClientMeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {

				try {
					Analysis_Transferable transferable = mcmRef.transmitAnalysis((Identifier_Transferable) id.getTransferable());
					Analysis analysis = new Analysis(transferable);

					try {
						MeasurementDatabaseContext.getAnalysisDatabase().insert(analysis);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return analysis;
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Analysis '" + id + "' not found in MCM '" + mcmId + "' database");
					throw new RetrieveObjectException("Cannot retrieve analysis '" + id + "' from MCM '"
							+ mcmId + "' database -- " + are.message);
				}
			}

			String mesg = "Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it";
			Log.errorMessage(mesg);
			ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			throw new CommunicationException(mesg);
		}

	}

	public Evaluation loadEvaluation(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Evaluation(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("Evaluation '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);

			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM) ClientMeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {

				try {
					Evaluation_Transferable transferable = mcmRef.transmitEvaluation((Identifier_Transferable) id.getTransferable());
					Evaluation evaluation = new Evaluation(transferable);

					try {
						MeasurementDatabaseContext.getEvaluationDatabase().insert(evaluation);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}

					return evaluation;
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
						throw new ObjectNotFoundException("Evaluation '" + id + "' not found in MCM '" + mcmId + "' database");
					throw new RetrieveObjectException("Cannot retrieve evaluation '" + id + "' from MCM '"
							+ mcmId + "' database -- " + are.message);
				}
			}

			String mesg = "Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it";
			Log.errorMessage(mesg);
			ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			throw new CommunicationException(mesg);
		}

	}

	public Collection loadAnalyses(Collection ids) throws RetrieveObjectException, CommunicationException {
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
		Collection loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (!loadIds.isEmpty()) {
			Identifier_Transferable[] loadIdsT = new Identifier_Transferable[loadIds.size()];
			int i = 0;
			for (Iterator it = loadIds.iterator(); it.hasNext(); i++) {
				id = (Identifier) it.next();
				loadIdsT[i] = (Identifier_Transferable) id.getTransferable();
			}

			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM) ClientMeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {
				Collection loadedObjects = null;
				try {
					Analysis_Transferable[] transferables = mcmRef.transmitAnalyses(loadIdsT);
					loadedObjects = new ArrayList(transferables.length);
					for (i = 0; i < transferables.length; i++)
						loadedObjects.add(new Analysis(transferables[i]));
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("Cannot retrieve analyses from MCM + '" + mcmId + "' database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}

				if (loadedObjects != null && !loadedObjects.isEmpty()) {
					objects.addAll(loadedObjects);

					try {
						database.insert(loadedObjects);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}
				}

			}
			else {
				Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
				ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			}
		}

		return objects;
	}

	public Collection loadEvaluations(Collection ids) throws RetrieveObjectException, CommunicationException {
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
		Collection loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (!loadIds.isEmpty()) {
			Identifier_Transferable[] loadIdsT = new Identifier_Transferable[loadIds.size()];
			int i = 0;
			for (Iterator it = loadIds.iterator(); it.hasNext(); i++) {
				id = (Identifier) it.next();
				loadIdsT[i] = (Identifier_Transferable) id.getTransferable();
			}

			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM) ClientMeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {
				Collection loadedObjects = null;
				try {
					Evaluation_Transferable[] transferables = mcmRef.transmitEvaluations(loadIdsT);
					loadedObjects = new ArrayList(transferables.length);
					for (i = 0; i < transferables.length; i++)
						loadedObjects.add(new Evaluation(transferables[i]));
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("Cannot retrieve evaluations from MCM '" + mcmId + "' database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}

				if (loadedObjects != null && !loadedObjects.isEmpty()) {
					objects.addAll(loadedObjects);

					try {
						database.insert(loadedObjects);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}
				}

			}
			else {
				Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
				ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			}
		}

		return objects;
	}

	public Collection loadMeasurements(Collection ids) throws RetrieveObjectException, CommunicationException {
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
		Collection loadIds = new HashSet(ids);
		for (Iterator it = objects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		if (!loadIds.isEmpty()) {
			Identifier_Transferable[] loadIdsT = new Identifier_Transferable[loadIds.size()];
			int i = 0;
			for (Iterator it = loadIds.iterator(); it.hasNext(); i++) {
				id = (Identifier) it.next();
				loadIdsT[i] = (Identifier_Transferable) id.getTransferable();
			}

			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM) ClientMeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {
				Collection loadedObjects = null;
				try {
					Measurement_Transferable[] transferables = mcmRef.transmitMeasurements(loadIdsT);
					loadedObjects = new ArrayList(transferables.length);
					for (i = 0; i < transferables.length; i++)
						loadedObjects.add(new Measurement(transferables[i]));
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("Cannot retrieve measurements from MCM '" + mcmId + "' database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}

				if (loadedObjects != null && !loadedObjects.isEmpty()) {
					objects.addAll(loadedObjects);

					try {
						database.insert(loadedObjects);
					}
					catch (ApplicationException ae) {
						Log.errorException(ae);
					}
				}

			}
			else {
				Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
				ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			}
		}

		return objects;
	}

	public Collection loadAnalysesButIds(StorableObjectCondition condition, Collection ids) throws RetrieveObjectException, CommunicationException {
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

		Identifier_Transferable[] loadButIdsT = new Identifier_Transferable[loadButIds.size()];
		int i = 0;
		for (Iterator it = loadButIds.iterator(); it.hasNext(); i++) {
			id = (Identifier) it.next();
			loadButIdsT[i] = (Identifier_Transferable) id.getTransferable();
		}

		com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM) ClientMeasurementServer.mcmRefs.get(mcmId);
		if (mcmRef != null) {
			StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
			Collection loadedObjects = null;
			try {
				Analysis_Transferable[] transferables = mcmRef.transmitAnalysesButIdsByCondition(loadButIdsT, conditionT);
				loadedObjects = new ArrayList(transferables.length);
				for (i = 0; i < transferables.length; i++)
					loadedObjects.add(new Analysis(transferables[i]));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				Log.errorMessage("Cannot retrieve analyses from MCM '" + mcmId + "' database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}

			if (loadedObjects != null && !loadedObjects.isEmpty()) {
				objects.addAll(loadedObjects);

				try {
					database.insert(loadedObjects);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
			}

		}
		else {
			Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
			ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
		}

		return objects;
	}

	public Collection loadEvaluationsButIds(StorableObjectCondition condition, Collection ids) throws RetrieveObjectException, CommunicationException {
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

		Identifier_Transferable[] loadButIdsT = new Identifier_Transferable[loadButIds.size()];
		int i = 0;
		for (Iterator it = loadButIds.iterator(); it.hasNext(); i++) {
			id = (Identifier) it.next();
			loadButIdsT[i] = (Identifier_Transferable) id.getTransferable();
		}

		com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM) ClientMeasurementServer.mcmRefs.get(mcmId);
		if (mcmRef != null) {
			StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
			Collection loadedObjects = null;
			try {
				Evaluation_Transferable[] transferables = mcmRef.transmitEvaluationsButIdsByCondition(loadButIdsT, conditionT);
				loadedObjects = new ArrayList(transferables.length);
				for (i = 0; i < transferables.length; i++)
					loadedObjects.add(new Evaluation(transferables[i]));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				Log.errorMessage("Cannot retrieve evaluations from MCM '" + mcmId + "' database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}

			if (loadedObjects != null && !loadedObjects.isEmpty()) {
				objects.addAll(loadedObjects);

				try {
					database.insert(loadedObjects);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
			}

		}
		else {
			Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
			ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
		}

		return objects;
	}

	public Collection loadMeasurementsButIds(StorableObjectCondition condition, Collection ids) throws RetrieveObjectException, CommunicationException {
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

		Identifier_Transferable[] loadButIdsT = new Identifier_Transferable[loadButIds.size()];
		int i = 0;
		for (Iterator it = loadButIds.iterator(); it.hasNext(); i++) {
			id = (Identifier) it.next();
			loadButIdsT[i] = (Identifier_Transferable) id.getTransferable();
		}

		com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM) ClientMeasurementServer.mcmRefs.get(mcmId);
		if (mcmRef != null) {
			StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
			Collection loadedObjects = null;
			try {
				Measurement_Transferable[] transferables = mcmRef.transmitMeasurementsButIdsByCondition(loadButIdsT, conditionT);
				loadedObjects = new ArrayList(transferables.length);
				for (i = 0; i < transferables.length; i++)
					loadedObjects.add(new Measurement(transferables[i]));
			}
			catch (org.omg.CORBA.SystemException se) {
				Log.errorException(se);
				ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
				throw new CommunicationException("System exception -- " + se.getMessage(), se);
			}
			catch (AMFICOMRemoteException are) {
				Log.errorMessage("Cannot retrieve measurements from MCM '" + mcmId + "' database -- " + are.message);
			}
			catch (CreateObjectException coe) {
				Log.errorException(coe);
			}

			if (loadedObjects != null && !loadedObjects.isEmpty()) {
				objects.addAll(loadedObjects);

				try {
					database.insert(loadedObjects);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
			}

		}
		else {
			Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
			ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
		}

		return objects;
	}	
    
}
