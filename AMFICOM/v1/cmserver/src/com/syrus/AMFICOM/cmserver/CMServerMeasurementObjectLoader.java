/*
 * $Id: CMServerMeasurementObjectLoader.java,v 1.5 2004/12/20 14:04:45 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.LinkedIdsCondition;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.util.Log;
/**
 * @version $Revision: 1.5 $, $Date: 2004/12/20 14:04:45 $
 * @author $Author: bob $
 * @module module_name
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
    
    public CMServerMeasurementObjectLoader(long refreshTimeout){
    	this.refreshTimeout = refreshTimeout;
    }
  
    public Set refresh(Set storableObjects) throws DatabaseException, CommunicationException {
    		/* refresh no often than one in refreshTimeout ms */
    		if (System.currentTimeMillis() - this.lastRefresh < this.refreshTimeout )
    			return Collections.EMPTY_SET;
    		
    		this.lastRefresh = System.currentTimeMillis();    		

			if (storableObjects.isEmpty())
				return Collections.EMPTY_SET;

			short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

			StorableObjectDatabase database = null;
			try {
				switch (entityCode) {
					/**
					 * there is no reason to refresh other measurement entities due to they couldn't change out of CMServer
					 */
					case ObjectEntities.ANALYSIS_ENTITY_CODE:
						database = MeasurementDatabaseContext.getAnalysisDatabase();
						break;
					case ObjectEntities.EVALUATION_ENTITY_CODE:
						database = MeasurementDatabaseContext.getEvaluationDatabase();
						break;
					case ObjectEntities.MEASUREMENT_ENTITY_CODE:
						database = MeasurementDatabaseContext.getMeasurementDatabase();
						break;
					case ObjectEntities.TEST_ENTITY_CODE:
						database = MeasurementDatabaseContext.getTestDatabase();
						break;
					case ObjectEntities.RESULT_ENTITY_CODE:
						database = MeasurementDatabaseContext.getResultDatabase();
						break;
					default:
						Log.errorMessage("CMServerMeasurementObjectLoader.refresh | Unknown entity: " + entityCode);                
				}
				if (database != null)
					return database.refresh(storableObjects);

	      return Collections.EMPTY_SET;
			}
			catch (DatabaseException e) {
				Log.errorMessage("CMServerMeasurementObjectLoader.refresh | DatabaseException: " + e.getMessage());
				throw new DatabaseException("CMServerMeasurementObjectLoader.refresh | DatabaseException: " + e.getMessage());
			}
		
		}
    
	public Measurement loadMeasurement(Identifier id) throws RetrieveObjectException, CommunicationException {
		Measurement measurement = null;
		try {
			measurement = new Measurement(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Measurement '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {
				try {
					measurement = new Measurement(mcmRef.transmitMeasurement((Identifier_Transferable)id.getTransferable()));
					measurement.insert();
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Measurement '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve measurement '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			else {
				Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
				ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			}
		}
		return measurement;
	}

	public Analysis loadAnalysis(Identifier id) throws RetrieveObjectException, CommunicationException {
		Analysis analysis = null;
		try {
			analysis = new Analysis(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Analysis '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {
				try {
					analysis = new Analysis(mcmRef.transmitAnalysis((Identifier_Transferable)id.getTransferable()));
					analysis.insert();
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Analysis '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve analysis '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			else {
				Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
				ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			}
		}
		return analysis;
	}

	public Evaluation loadEvaluation(Identifier id) throws RetrieveObjectException, CommunicationException {
		Evaluation evaluation = null;
		try {
			evaluation = new Evaluation(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Evaluation '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {
				try {
					evaluation = new Evaluation(mcmRef.transmitEvaluation((Identifier_Transferable)id.getTransferable()));
					evaluation.insert();
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Evaluation '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve evaluation '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			else {
				Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
				ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			}
		}
		return evaluation;
	}

	public List loadAnalyses(List ids) throws DatabaseException, CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase)MeasurementDatabaseContext.getAnalysisDatabase();
		List list;
		List copyOfList;
		Analysis analysis;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			List insertToDB = new LinkedList();
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Analysis '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);				
				if (mcmRef != null) {
					try {
						analysis = new Analysis(mcmRef.transmitAnalysis((Identifier_Transferable)id.getTransferable()));
						list.add(analysis);
						insertToDB.add(analysis);
					}
					catch (org.omg.CORBA.SystemException se) {
						Log.errorException(se);
						ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
						throw new CommunicationException("System exception -- " + se.getMessage(), se);
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
							Log.errorMessage("Analysis '" + id + "' not found on server database");
						else
							Log.errorMessage("Cannot retrieve analysis '" + id + "' from server database -- " + are.message);
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}
				else {
					Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
				}
			}
			AnalysisDatabase analysisDatabase = (AnalysisDatabase) MeasurementDatabaseContext.getAnalysisDatabase();
			analysisDatabase.insert(insertToDB);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadEvaluations(List ids) throws DatabaseException, CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
		List list;
		List copyOfList;
		Evaluation evaluation;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			List insertToDB = new LinkedList();
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Evaluation '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
				if (mcmRef != null) {
					try {
						evaluation = new Evaluation(mcmRef.transmitEvaluation((Identifier_Transferable)id.getTransferable()));
						list.add(evaluation);
						insertToDB.add(evaluation);
					}
					catch (org.omg.CORBA.SystemException se) {
						Log.errorException(se);
						ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
						throw new CommunicationException("System exception -- " + se.getMessage(), se);
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
							Log.errorMessage("Evaluation '" + id + "' not found on server database");
						else
							Log.errorMessage("Cannot retrieve evaluation '" + id + "' from server database -- " + are.message);
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}
				else {
					Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
					ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
				}
			}
			EvaluationDatabase evaluationDatabase = (EvaluationDatabase) MeasurementDatabaseContext.getEvaluationDatabase();
			evaluationDatabase.insert(insertToDB);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

  public List loadMeasurements(List ids) throws DatabaseException, CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
		List list;
		List copyOfList;
		Measurement measurement;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			List insertToDB = new LinkedList();
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Measurement '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
				if (mcmRef != null) {
					try {
						measurement = new Measurement(mcmRef.transmitMeasurement((Identifier_Transferable)id.getTransferable()));
						list.add(measurement);
						insertToDB.add(measurement);
					}
					catch (org.omg.CORBA.SystemException se) {
						Log.errorException(se);
						ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
						throw new CommunicationException("System exception -- " + se.getMessage(), se);
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
							Log.errorMessage("Measurement '" + id + "' not found on server database");
						else
							Log.errorMessage("Cannot retrieve measurement '" + id + "' from server database -- " + are.message);
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}
				else {
						Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
						ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
				}
			}
			MeasurementDatabase measurementDatabase = (MeasurementDatabase) MeasurementDatabaseContext.getMeasurementDatabase();
			measurementDatabase.insert(insertToDB);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}	

	public List loadMeasurementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
		List list;
		List ids2 = new ArrayList(ids);
		Measurement_Transferable[] measurementTransferables;
														
		try {
			list = database.retrieveByCondition(ids2, condition);
			for (Iterator it = list.iterator(); it.hasNext();) {
				ids2.add( ((Measurement)it.next()).getId() );
			}
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids2.size()]; 
			int i = 0;
			for (Iterator it = ids2.iterator(); it.hasNext(); i++) {
				identifierTransferables[i] = (Identifier_Transferable)( (Identifier) it.next() ).getTransferable();
			}
			
			LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition) condition;
			LinkedIdsCondition_Transferable linkedIdsConditionTransferable = (LinkedIdsCondition_Transferable)linkedIdsCondition.getTransferable();
			List tests;
			if (linkedIdsCondition.getTestIds() != null)
				tests = MeasurementStorableObjectPool.getStorableObjects(linkedIdsCondition.getTestIds(), true);
			else tests = Collections.singletonList(MeasurementStorableObjectPool.getStorableObject(linkedIdsCondition.getIdentifier(), true));
			for (Iterator it = tests.iterator(); it.hasNext();) {
				Test test = (Test) it.next();				
				MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(test.getMonitoredElement().getMeasurementPortId(), true);
				Identifier kisId = measurementPort.getKISId();
				LinkedIdsCondition linkedIdsCondition2 = LinkedIdsCondition.getInstance();
				linkedIdsCondition2.setEntityCode(ObjectEntities.MCM_ENTITY_CODE);
				linkedIdsCondition2.setIdentifier(kisId);
				List mcms = ConfigurationStorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition2, true);
				for (Iterator mcmIter = mcms.iterator(); mcmIter.hasNext();) {
					MCM mcm = (MCM) mcmIter.next();
					mcmId  = mcm.getId();
					com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
					measurementTransferables = mcmRef.transmitMeasurementsButIds(  linkedIdsConditionTransferable , identifierTransferables);
					list.add(measurementTransferables);
				}
				
			}
			return list;
		}
		catch (org.omg.CORBA.SystemException se) {
			Log.errorException(se);
			ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			throw new CommunicationException("System exception -- " + se.getMessage(), se);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + e.getMessage());
		}
		catch (AMFICOMRemoteException ae) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + ae.getMessage());
			throw new CommunicationException("CMServerMeasurementObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new CommunicationException("CMServerMeasurementObjectLoader.loadMeasurementsButIds | " + throwable);
		}
		
	}

	public List loadAnalysesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase)MeasurementDatabaseContext.getAnalysisDatabase();
		List list;
		List ids2 = new ArrayList(ids);
		Analysis_Transferable[] analysesTransferables;

		try {
			list = database.retrieveByCondition(ids2, condition);
			for (Iterator it = list.iterator(); it.hasNext();) {
					ids2.add( ((Analysis)it.next()).getId() );
			}
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids2.size()];
			int i = 0;
			for (Iterator it = ids2.iterator(); it.hasNext(); i++) {
					identifierTransferables[i] = (Identifier_Transferable)( (Identifier) it.next() ).getTransferable();
			}
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
										 
			analysesTransferables = mcmRef.transmitAnalysesButIds((LinkedIdsCondition_Transferable)condition.getTransferable(), identifierTransferables);
			list.add(analysesTransferables);
			return list;
		}
		catch (org.omg.CORBA.SystemException se) {
			Log.errorException(se);
			ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			throw new CommunicationException("System exception -- " + se.getMessage(), se);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + e.getMessage());
		}
		catch (AMFICOMRemoteException ae) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + ae.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + ae.getMessage());
		}
	}

	public List loadEvaluationsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
		List list;
		List ids2 = new ArrayList(ids);
		Evaluation_Transferable[] evaluationTransferables;
		
		try {
			list = database.retrieveByCondition(ids2, condition);
			for (Iterator it = list.iterator(); it.hasNext();) {
				ids2.add( ((Analysis)it.next()).getId() );
			}
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids2.size()];
			int i = 0;
			for (Iterator it = ids2.iterator(); it.hasNext(); i++) {
				identifierTransferables[i] = (Identifier_Transferable)( (Identifier) it.next() ).getTransferable();
			}
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
													 
			evaluationTransferables = mcmRef.transmitEvaluationsButIds((LinkedIdsCondition_Transferable)((LinkedIdsCondition) condition).getTransferable(), identifierTransferables);
			list.add(evaluationTransferables);
			return list;               
		}
		catch (org.omg.CORBA.SystemException se) {
			Log.errorException(se);
			ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			throw new CommunicationException("System exception -- " + se.getMessage(), se);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + e.getMessage());
		}
		catch (AMFICOMRemoteException ae) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + ae.getMessage());
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + ae.getMessage());
		}
	}	

    
}
