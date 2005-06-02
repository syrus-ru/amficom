/*
 * $Id: MCMMeasurementObjectLoader.java,v 1.53 2005/06/02 14:44:03 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.53 $, $Date: 2005/06/02 14:44:03 $
 * @author $Author: bass $
 * @module mcm_v1
 */
final class MCMMeasurementObjectLoader extends MCMObjectLoader implements MeasurementObjectLoader {

	public MCMMeasurementObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager, new DatabaseMeasurementObjectLoader());
	}

	/* Load multiple objects*/

	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementTypes(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.ANALYSISTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitAnalysisTypes(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitEvaluationTypes(idsT, sessionKey);
			}
		});
	}



	public java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException {
		return super.databaseObjectLoader.loadStorableObjects(ids);
	}

	public java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException {
		return super.databaseObjectLoader.loadStorableObjects(ids);
	}

	public java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException {
		return super.databaseObjectLoader.loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementSetups(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadResults(java.util.Set ids) throws ApplicationException {
		return super.databaseObjectLoader.loadStorableObjects(ids);
	}

	public java.util.Set loadSets(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SET_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitSets(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitCronTemporalPatterns(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadIntervalsTemporalPatterns(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitIntervalsTemporalPatterns(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadPeriodicalTemporalPatterns(java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitPeriodicalTemporalPatterns(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitMeasurementTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.ANALYSISTYPE_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitAnalysisTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.EVALUATIONTYPE_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitEvaluationTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}



	public java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.databaseObjectLoader.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadAnalysesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.databaseObjectLoader.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.databaseObjectLoader.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitMeasurementSetupsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.SET_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitSetsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.databaseObjectLoader.loadStorableObjectsButIds(condition, ids);
	}

	public java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitCronTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadIntervalsTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitIntervalsTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitPeriodicalTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}



	/*	Save multiple objects*/

	public void saveMeasurements(java.util.Set storableObjects, boolean force) throws ApplicationException {
		super.databaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveAnalyses(java.util.Set storableObjects, boolean force) throws ApplicationException {
		super.databaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveEvaluations(java.util.Set storableObjects, boolean force) throws ApplicationException {
		super.databaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveResults(java.util.Set storableObjects, boolean force) throws ApplicationException {
		super.databaseObjectLoader.saveStorableObjects(storableObjects, force);
	}
	
	public void saveTests(final java.util.Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(storableObjects, ObjectEntities.TEST_ENTITY_CODE, force, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((MServer) server).receiveTests((Test_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	/*
	 * MCM do not need in all below methods
	 * */	


	public java.util.Set loadModelingTypes(java.util.Set ids) {
		throw new UnsupportedOperationException("MCM doesn't need in modeling type, ids: " + ids);
	}

	public java.util.Set loadTests(java.util.Set ids) {
		throw new UnsupportedOperationException("MCM doesn't need in modeling type, ids: " + ids);
	}

	public java.util.Set loadModelings(java.util.Set ids) {
		throw new UnsupportedOperationException("MCM doesn't need in modeling, ids: " + ids);
	}




	
	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) {
		throw new UnsupportedOperationException("MCM doesn't need in modeling type, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) {
		throw new UnsupportedOperationException("MCM doesn't need in modeling, ids: " + ids + ", condition: " + condition);
	}

	public java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) {
		throw new UnsupportedOperationException("MCM doesn't need in modeling, ids: " + ids + ", condition: " + condition);
	}



	public void saveMeasurementTypes(java.util.Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveAnalysisTypes(java.util.Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveEvaluationTypes(java.util.Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveModelingTypes(java.util.Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveMeasurementSetups(java.util.Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveModelings(java.util.Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveSets(java.util.Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCronTemporalPatterns(java.util.Set storableObjects,boolean force){
		throw new UnsupportedOperationException("May be not need this? " + storableObjects + ", " + force);
	}
	
	public void saveIntervalsTemporalPatterns(java.util.Set storableObjects,boolean force){
		throw new UnsupportedOperationException("May be not need this? " + storableObjects + ", " + force);
	}
	
	public void savePeriodicalTemporalPatterns(java.util.Set storableObjects,boolean force){
		throw new UnsupportedOperationException("May be not need this? " + storableObjects + ", " + force);
	}
}
