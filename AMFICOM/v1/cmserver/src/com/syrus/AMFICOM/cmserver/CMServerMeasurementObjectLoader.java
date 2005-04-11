/*
 * $Id: CMServerMeasurementObjectLoader.java,v 1.39 2005/04/11 14:48:58 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.39 $, $Date: 2005/04/11 14:48:58 $
 * @author $Author: bob $
 * @module cmserver_v1
 */
public final class CMServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {
	
	private Map  lastRefesh; 
	
	/**
	 * refresh timeout
	 */
	private long refreshTimeout;
	
	public CMServerMeasurementObjectLoader(long refreshTimeout) {
		this.refreshTimeout = refreshTimeout;
		this.lastRefesh = new HashMap();
	}

	public Set refresh(Set storableObjects) throws DatabaseException, CommunicationException {
		/* refresh no often than one in refreshTimeout ms */
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		StorableObject firstStorableObject = (StorableObject) storableObjects.iterator().next();
		Short entityCode = new Short(firstStorableObject.getId().getMajor());
		
		Date lastRefreshDate = (Date) this.lastRefesh.get(entityCode);
		
		if (lastRefreshDate != null && System.currentTimeMillis() - lastRefreshDate.getTime() < this.refreshTimeout)
			return Collections.EMPTY_SET;

		/* put current date*/
		this.lastRefesh.put(entityCode, new Date()); 

		try {
			StorableObjectDatabase database = MeasurementDatabaseContext.getDatabase(entityCode.shortValue());
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
			Log.debugMessage("CMServerMeasurementObjectLoader.loadMeasurement | Measurement '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			Measurement measurement = null;

			MServer mServerRef = ClientMeasurementServer.getVerifiedMServerReference();
			try {
				Measurement_Transferable transferable = mServerRef.transmitMeasurement((Identifier_Transferable) id.getTransferable());
				measurement = new Measurement(transferable);
				Log.debugMessage("CMServerMeasurementObjectLoader.loadMeasurement | Measurement '" + id
						+ "' loaded from MeasurementServer", Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("Measurement '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve measurement '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (measurement != null) {
				try {
					MeasurementDatabaseContext.getMeasurementDatabase().insert(measurement);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return measurement;
			}
			throw new ObjectNotFoundException("Measurement '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public Analysis loadAnalysis(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Analysis(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("CMServerMeasurementObjectLoader.loadAnalysis | Analysis '" + id + "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			Analysis analysis = null;

			MServer mServerRef = ClientMeasurementServer.getVerifiedMServerReference();
			try {
				Analysis_Transferable transferable = mServerRef.transmitAnalysis((Identifier_Transferable) id.getTransferable());
				analysis = new Analysis(transferable);
				Log.debugMessage("CMServerMeasurementObjectLoader.loadAnalysis | Analysis '" + id + "' loaded from MeasurementServer",
						Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("Analysis '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve analysis '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (analysis != null) {
				try {
					MeasurementDatabaseContext.getAnalysisDatabase().insert(analysis);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return analysis;
			}
			throw new ObjectNotFoundException("Analysis '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}

	public Evaluation loadEvaluation(Identifier id)
			throws RetrieveObjectException, CommunicationException, ObjectNotFoundException, CreateObjectException {
		try {
			return new Evaluation(id);
		}
		catch (ObjectNotFoundException e) {
			Log.debugMessage("CMServerMeasurementObjectLoader.loadEvaluation | Evaluation '" + id
					+ "' not found in database; trying to load from Measurement Server", Log.DEBUGLEVEL08);
			Evaluation evaluation = null;

			MServer mServerRef = ClientMeasurementServer.getVerifiedMServerReference();
			try {
				Evaluation_Transferable transferable = mServerRef.transmitEvaluation((Identifier_Transferable) id.getTransferable());
				evaluation = new Evaluation(transferable);
				Log.debugMessage("CMServerMeasurementObjectLoader.loadEvaluation | Evaluation '" + id + "' loaded from MeasurementServer",
						Log.DEBUGLEVEL08);
			}
			catch (AMFICOMRemoteException are) {
				if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
					throw new ObjectNotFoundException("Evaluation '" + id + "' not found on Measurement Server -- " + are.message);
				throw new RetrieveObjectException("Cannot retrieve evaluation '" + id + "' from Measurement Server -- " + are.message);
			}
			catch (Throwable throwable) {
				Log.errorException(throwable);
			}

			if (evaluation != null) {
				try {
					MeasurementDatabaseContext.getEvaluationDatabase().insert(evaluation);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				return evaluation;
			}
			throw new ObjectNotFoundException("Evaluation '" + id + "' not found on Measurement Server");
		}	//catch (ObjectNotFoundException e)
	}




	public Set loadMeasurements(Set ids) throws RetrieveObjectException {
		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = ClientMeasurementServer.getVerifiedMServerReference();
			Measurement_Transferable[] transferables = mServerRef.transmitMeasurements(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new Measurement(transferables[i]));
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
			Log.errorMessage("CMServerMeasurementObjectLoader.loadMeasurements | Cannot load objects from MeasurementServer");
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

	public Set loadAnalyses(Set ids) throws RetrieveObjectException {
		AnalysisDatabase database = MeasurementDatabaseContext.getAnalysisDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = ClientMeasurementServer.getVerifiedMServerReference();
			Analysis_Transferable[] transferables = mServerRef.transmitAnalyses(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new Analysis(transferables[i]));
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
			Log.errorMessage("CMServerMeasurementObjectLoader.loadAnalyses | Cannot load objects from MeasurementServer");
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

	public Set loadEvaluations(Set ids) throws RetrieveObjectException {
		EvaluationDatabase database = MeasurementDatabaseContext.getEvaluationDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = null;
		try {
			loadIdsT = super.createLoadIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = ClientMeasurementServer.getVerifiedMServerReference();
			Evaluation_Transferable[] transferables = mServerRef.transmitEvaluations(loadIdsT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new Evaluation(transferables[i]));
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
			Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluations | Cannot load objects from MeasurementServer");
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





	public Set loadMeasurementsButIds(StorableObjectCondition condition, Set ids) throws RetrieveObjectException {
		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
		Set objects = super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
		Identifier_Transferable[] loadButIdsT = null;
		try {
			loadButIdsT = super.createLoadButIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = ClientMeasurementServer.getVerifiedMServerReference();
			Measurement_Transferable[] transferables = mServerRef.transmitMeasurementsButIdsByCondition(loadButIdsT, conditionT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new Measurement(transferables[i]));
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
			Log.errorMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | Cannot load objects from MeasurementServer");
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

	public Set loadAnalysesButIds(StorableObjectCondition condition, Set ids) throws RetrieveObjectException {
		AnalysisDatabase database = MeasurementDatabaseContext.getAnalysisDatabase();
		Set objects = super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
		Identifier_Transferable[] loadButIdsT = null;
		try {
			loadButIdsT = super.createLoadButIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = ClientMeasurementServer.getVerifiedMServerReference();
			Analysis_Transferable[] transferables = mServerRef.transmitAnalysesButIdsByCondition(loadButIdsT, conditionT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new Analysis(transferables[i]));
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
			Log.errorMessage("CMServerMeasurementObjectLoader.loadAnalysesButIds | Cannot load objects from MeasurementServer");
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

	public Set loadEvaluationsButIds(StorableObjectCondition condition, Set ids) throws RetrieveObjectException {
		EvaluationDatabase database = MeasurementDatabaseContext.getEvaluationDatabase();
		Set objects = super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
		Identifier_Transferable[] loadButIdsT = null;
		try {
			loadButIdsT = super.createLoadButIdsTransferable(ids, objects);
		}
		catch (IllegalDataException ide) {
			// Never
			Log.errorException(ide);
		}
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = ClientMeasurementServer.getVerifiedMServerReference();
			Evaluation_Transferable[] transferables = mServerRef.transmitEvaluationsButIdsByCondition(loadButIdsT, conditionT);
			for (int i = 0; i < transferables.length; i++) {
				try {
					loadedObjects.add(new Evaluation(transferables[i]));
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
			Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluationsButIds | Cannot load objects from MeasurementServer");
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

}
