/*
 * $Id: CMServerMeasurementObjectLoader.java,v 1.22 2005/02/15 07:47:57 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompoundCondition_Transferable;
import com.syrus.AMFICOM.general.corba.EquivalentCondition_Transferable;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.TypicalCondition_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
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
 * @version $Revision: 1.22 $, $Date: 2005/02/15 07:47:57 $
 * @author $Author: bob $
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
    
    public CMServerMeasurementObjectLoader(long refreshTimeout){
    	this.refreshTimeout = refreshTimeout;
    }
    
    private StorableObjectCondition_Transferable getConditionTransferable(StorableObjectCondition condition) {
		StorableObjectCondition_Transferable conditionTransferable = new StorableObjectCondition_Transferable();
		Object transferable = condition.getTransferable();
		if (condition instanceof LinkedIdsCondition) {
			conditionTransferable.linkedIdsCondition((LinkedIdsCondition_Transferable) transferable);
		} else if (condition instanceof CompoundCondition) {
			conditionTransferable.compoundCondition((CompoundCondition_Transferable) transferable);
		} else if (condition instanceof TypicalCondition) {
			conditionTransferable.typicalCondition((TypicalCondition_Transferable) transferable);
		} else if (condition instanceof EquivalentCondition) {
			conditionTransferable.equialentCondition((EquivalentCondition_Transferable) transferable);
		} 
		return conditionTransferable;
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
						Log.errorMessage("CMServerMeasurementObjectLoader.refresh | there is no reason to refresh " + ObjectEntities.codeToString(entityCode));                
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
			List loadedFromMCM = new LinkedList();
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Analysis '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);				
				if (mcmRef != null) {
					try {
						analysis = new Analysis(mcmRef.transmitAnalysis((Identifier_Transferable)id.getTransferable()));
						list.add(analysis);
						loadedFromMCM.add(analysis);
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
			try{
				database.update(loadedFromMCM, SessionContext.getAccessIdentity().getUserId(), StorableObjectDatabase.UPDATE_FORCE);
			}catch(VersionCollisionException vce){
				// exception isn't expected
				Log.errorMessage("CMServerMeasurementObjectLoader.loadAnalyses | exception isn't expected, but its occur: " + vce.getMessage());
			}
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
			List loadedFromMCM = new LinkedList();
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Evaluation '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
				if (mcmRef != null) {
					try {
						evaluation = new Evaluation(mcmRef.transmitEvaluation((Identifier_Transferable)id.getTransferable()));
						list.add(evaluation);
						loadedFromMCM.add(evaluation);
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
			try{
				database.update(loadedFromMCM, SessionContext.getAccessIdentity().getUserId(), StorableObjectDatabase.UPDATE_FORCE);
			}catch(VersionCollisionException vce){
				// exception isn't expected
				Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluations | exception isn't expected, but its occur: " + vce.getMessage());
			}
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
			List loadedFromMCM = new LinkedList();
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Measurement '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
				if (mcmRef != null) {
					try {
						measurement = new Measurement(mcmRef.transmitMeasurement((Identifier_Transferable)id.getTransferable()));
						list.add(measurement);
						loadedFromMCM.add(measurement);
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
			try{
				database.update(loadedFromMCM, SessionContext.getAccessIdentity().getUserId(), StorableObjectDatabase.UPDATE_FORCE);
			}catch(VersionCollisionException vce){
				// exception isn't expected
				Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluations | exception isn't expected, but its occur: " + vce.getMessage());
			}
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

			{
				List loadedFromMCM = new LinkedList();
				List measurements = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);
				List tests;
				{
					List testIds = new ArrayList(measurements.size());
					for (Iterator it = measurements.iterator(); it.hasNext();) {
						Measurement measurement = (Measurement) it.next();
						testIds.add(measurement.getTestId());
					}
					tests = MeasurementStorableObjectPool.getStorableObjects(testIds, true);
				}
				
				Log.debugMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | condition consists " + tests.size() + " test(s)", Log.DEBUGLEVEL05);
				for (Iterator it = tests.iterator(); it.hasNext();) {
					Test test = (Test) it.next();				
					MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(test.getMonitoredElement().getMeasurementPortId(), true);
					KIS kis = (KIS)ConfigurationStorableObjectPool.getStorableObject(measurementPort.getKISId(), true);
					com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(kis.getMCMId());
					measurementTransferables = mcmRef.transmitMeasurementsButIds(  this.getConditionTransferable(condition) , identifierTransferables);
					for (int j = 0; j < measurementTransferables.length; j++) {
						Measurement measurement = new Measurement(measurementTransferables[j]);
						Log.debugMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | loaded measurement : " + measurement.getId().toString(), Log.DEBUGLEVEL05);
						loadedFromMCM.add(measurement);
					}
					list.addAll(loadedFromMCM);
				}
				/* force update measurements that loaded from mcm because of client want it to use*/
				Log.debugMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | " + loadedFromMCM.size() + " measurement(s) to update", Log.DEBUGLEVEL05);
				database.update(loadedFromMCM, SessionContext.getAccessIdentity().getUserId(), StorableObjectDatabase.UPDATE_FORCE);
			} 
			Log.debugMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | " + list.size() + " measurement(s) return", Log.DEBUGLEVEL05);
			return list;
		}
		catch (org.omg.CORBA.SystemException se) {
			Log.errorException(se);
			ClientMeasurementServer.activateMCMReferenceWithId(mcmId);
			throw new CommunicationException("System exception -- " + se.getMessage(), se);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + e.getMessage());
			Log.errorException(e);
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + e.getMessage());
		}
		catch (AMFICOMRemoteException are) {
			Log.errorException(are);
			Log.errorMessage("CMServerMeasurementObjectLoader.loadMeasurementsButIds | AMFICOMRemoteException: " + are.message);
			throw new CommunicationException("CMServerMeasurementObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + are.message);
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
										 
			analysesTransferables = mcmRef.transmitAnalysesButIds(this.getConditionTransferable(condition), identifierTransferables);
			List loadedFromMCM = new LinkedList();
			for (int j = 0; j < analysesTransferables.length; j++) 
				loadedFromMCM.add(new Analysis(analysesTransferables[j]));			
			try{
				/* force update analyses that loaded from mcm because of client want it to use*/
				database.update(loadedFromMCM, SessionContext.getAccessIdentity().getUserId(), StorableObjectDatabase.UPDATE_FORCE);
			}catch(VersionCollisionException vce){
				//	 exception isn't expected
				Log.errorMessage("CMServerMeasurementObjectLoader.loadAnalysesButIds | exception isn't expected, but its occur: " + vce.getMessage()); 
			}
			list.addAll(loadedFromMCM);
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
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + are.message);
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + are.message);
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
				ids2.add( ((Evaluation)it.next()).getId() );
			}
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids2.size()];
			int i = 0;
			for (Iterator it = ids2.iterator(); it.hasNext(); i++) {
				identifierTransferables[i] = (Identifier_Transferable)( (Identifier) it.next() ).getTransferable();
			}
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)ClientMeasurementServer.mcmRefs.get(mcmId);
													 
			evaluationTransferables = mcmRef.transmitEvaluationsButIds(this.getConditionTransferable(condition), identifierTransferables);
			List loadedFromMCM = new LinkedList();
			for (int j = 0; j < evaluationTransferables.length; j++) 
				loadedFromMCM.add(new Evaluation(evaluationTransferables[j]));			
			try{
				/* force update evaluations that loaded from mcm because of client want it to use*/
				database.update(loadedFromMCM, SessionContext.getAccessIdentity().getUserId(), StorableObjectDatabase.UPDATE_FORCE);
			}catch(VersionCollisionException vce){
//				 exception isn't expected
				Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluationsButIds | exception isn't expected, but its occur: " + vce.getMessage());
			}
			list.addAll(loadedFromMCM);
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
		catch (AMFICOMRemoteException are) {
			Log.errorMessage("CMServerMeasurementObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + are.message);
			throw new DatabaseException("CMServerMeasurementObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + are.message);
		}
	}	

    
}
