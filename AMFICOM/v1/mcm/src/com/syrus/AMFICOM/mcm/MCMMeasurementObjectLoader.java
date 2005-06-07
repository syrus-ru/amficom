/*
 * $Id: MCMMeasurementObjectLoader.java,v 1.59 2005/06/07 16:40:04 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.mcm;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.MeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.59 $, $Date: 2005/06/07 16:40:04 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMMeasurementObjectLoader extends MCMObjectLoader implements MeasurementObjectLoader {

	public MCMMeasurementObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager);
	}



	/* Load multiple objects*/

	public java.util.Set loadMeasurementTypes(final java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementTypes(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadAnalysisTypes(final java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitAnalysisTypes(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadEvaluationTypes(final java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitEvaluationTypes(idsT, sessionKey);
			}
		});
	}



	public java.util.Set loadMeasurements(final java.util.Set ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjects(ids);
	}

	public java.util.Set loadAnalyses(final java.util.Set ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjects(ids);
	}

	public java.util.Set loadEvaluations(final java.util.Set ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjects(ids);
	}

	public java.util.Set loadMeasurementSetups(final java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTSETUP_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementSetups(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadResults(final java.util.Set ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjects(ids);
	}

	public java.util.Set loadSets(final java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SET_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitSets(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadTests(final java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.TEST_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitTests(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadCronTemporalPatterns(final java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitCronTemporalPatterns(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadIntervalsTemporalPatterns(final java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitIntervalsTemporalPatterns(idsT, sessionKey);
			}
		});
	}

	public java.util.Set loadPeriodicalTemporalPatterns(final java.util.Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitPeriodicalTemporalPatterns(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public java.util.Set loadMeasurementTypesButIds(final StorableObjectCondition condition, final java.util.Set ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitMeasurementTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadAnalysisTypesButIds(final StorableObjectCondition condition, final java.util.Set ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.ANALYSISTYPE_ENTITY_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitAnalysisTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadEvaluationTypesButIds(final StorableObjectCondition condition, final java.util.Set ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitEvaluationTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}



	public java.util.Set loadMeasurementsButIds(final StorableObjectCondition condition, final java.util.Set ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadAnalysesButIds(final StorableObjectCondition condition, final java.util.Set ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadEvaluationsButIds(final StorableObjectCondition condition, final java.util.Set ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadMeasurementSetupsButIds(final StorableObjectCondition condition, final java.util.Set ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENTSETUP_ENTITY_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitMeasurementSetupsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadResultsButIds(final StorableObjectCondition condition, final java.util.Set ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public java.util.Set loadSetsButIds(final StorableObjectCondition condition, final java.util.Set ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SET_ENTITY_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitSetsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadTestsButIds(final StorableObjectCondition condition, final java.util.Set ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.TEST_ENTITY_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitTestsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadCronTemporalPatternsButIds(final StorableObjectCondition condition, final java.util.Set ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitCronTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadIntervalsTemporalPatternsButIds(final StorableObjectCondition condition, final java.util.Set ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitIntervalsTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public java.util.Set loadPeriodicalTemporalPatternsButIds(final StorableObjectCondition condition, final java.util.Set ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitPeriodicalTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}



	/*	Save multiple objects*/

	public void saveMeasurements(final java.util.Set storableObjects, final boolean force) throws ApplicationException {
		DatabaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveAnalyses(final java.util.Set storableObjects, final boolean force) throws ApplicationException {
		DatabaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveEvaluations(final java.util.Set storableObjects, final boolean force) throws ApplicationException {
		DatabaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveResults(final java.util.Set storableObjects, final boolean force) throws ApplicationException {
		DatabaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveTests(final java.util.Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.TEST_ENTITY_CODE, storableObjects, force, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(final CommonServer server,
					final IDLEntity[] transferables,
					final SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).receiveTests((Test_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	/*
	 * MCM do not need in all below methods
	 */	


	public java.util.Set loadModelingTypes(final java.util.Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public java.util.Set loadModelings(final java.util.Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}




	
	public java.util.Set loadModelingTypesButIds(final StorableObjectCondition condition, final java.util.Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public java.util.Set loadModelingsButIds(final StorableObjectCondition condition, final java.util.Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}



	public void saveMeasurementTypes(final java.util.Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveAnalysisTypes(final java.util.Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveEvaluationTypes(final java.util.Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveModelingTypes(final java.util.Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveMeasurementSetups(final java.util.Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveModelings(final java.util.Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveSets(final java.util.Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveCronTemporalPatterns(final java.util.Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void saveIntervalsTemporalPatterns(final java.util.Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void savePeriodicalTemporalPatterns(final java.util.Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
}
