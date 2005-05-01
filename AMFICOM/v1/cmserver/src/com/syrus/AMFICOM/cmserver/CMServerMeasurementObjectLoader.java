/*
 * $Id: CMServerMeasurementObjectLoader.java,v 1.41 2005/05/01 17:26:31 arseniy Exp $
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
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
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
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.41 $, $Date: 2005/05/01 17:26:31 $
 * @author $Author: arseniy $
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



	/* Load multiple objects*/

	public Set loadMeasurements(Set ids) throws RetrieveObjectException {
		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
		Set objects = super.retrieveFromDatabase(database, ids);
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = CMServerSessionEnvironment.getInstance().getCMServerServantManager().getMServerReference();
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
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = CMServerSessionEnvironment.getInstance().getCMServerServantManager().getMServerReference();
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
		Identifier_Transferable[] loadIdsT = super.createLoadIdsTransferable(ids, objects);
		if (loadIdsT.length == 0)
			return objects;

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = CMServerSessionEnvironment.getInstance().getCMServerServantManager().getMServerReference();
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
		Identifier_Transferable[] loadButIdsT = super.createLoadButIdsTransferable(ids, objects);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = CMServerSessionEnvironment.getInstance().getCMServerServantManager().getMServerReference();
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
		Identifier_Transferable[] loadButIdsT = super.createLoadButIdsTransferable(ids, objects);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = CMServerSessionEnvironment.getInstance().getCMServerServantManager().getMServerReference();
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
		Identifier_Transferable[] loadButIdsT = super.createLoadButIdsTransferable(ids, objects);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);

		Set loadedObjects = new HashSet();

		try {
			MServer mServerRef = CMServerSessionEnvironment.getInstance().getCMServerServantManager().getMServerReference();
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



	/*	Refresh*/

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

}
