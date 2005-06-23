/*
 * $Id: MCMMeasurementObjectLoader.java,v 1.66 2005/06/23 18:45:06 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.66 $, $Date: 2005/06/23 18:45:06 $
 * @author $Author: bass $
 * @module mcm_v1
 */
final class MCMMeasurementObjectLoader extends MCMObjectLoader implements MeasurementObjectLoader {

	public MCMMeasurementObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager);
	}



	/* Load multiple objects*/

	public Set loadMeasurementTypes(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENT_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadAnalysisTypes(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.ANALYSIS_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitAnalysisTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadEvaluationTypes(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.EVALUATION_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitEvaluationTypes(idsT, sessionKey);
			}
		});
	}



	public Set loadMeasurements(final Set<Identifier> ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjects(ids);
	}

	public Set loadAnalyses(final Set<Identifier> ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjects(ids);
	}

	public Set loadEvaluations(final Set<Identifier> ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjects(ids);
	}

	public Set loadMeasurementSetups(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTSETUP_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMeasurementSetups(idsT, sessionKey);
			}
		});
	}

	public Set loadResults(final Set<Identifier> ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjects(ids);
	}

	public Set loadParameterSets(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PARAMETERSET_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitParameterSets(idsT, sessionKey);
			}
		});
	}

	/**
	 * NOTE: New tests come to MCM only by CORBA call 'receiveTests'. 
	 */
	public Set loadTests(final Set<Identifier> ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjects(ids);
	}

	public Set loadCronTemporalPatterns(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CRONTEMPORALPATTERN_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitCronTemporalPatterns(idsT, sessionKey);
			}
		});
	}

	public Set loadIntervalsTemporalPatterns(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitIntervalsTemporalPatterns(idsT, sessionKey);
			}
		});
	}

	public Set loadPeriodicalTemporalPatterns(final Set<Identifier> ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitPeriodicalTemporalPatterns(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadMeasurementTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENT_TYPE_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							IdlIdentifier[] idsT,
							IdlSessionKey sessionKey,
							IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitMeasurementTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public Set loadAnalysisTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.ANALYSIS_TYPE_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							IdlIdentifier[] idsT,
							IdlSessionKey sessionKey,
							IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitAnalysisTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public Set loadEvaluationTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.EVALUATION_TYPE_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							IdlIdentifier[] idsT,
							IdlSessionKey sessionKey,
							IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitEvaluationTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}



	public Set loadMeasurementsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadAnalysesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadEvaluationsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadMeasurementSetupsButIds(final StorableObjectCondition condition, final Set<Identifier> ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENTSETUP_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							IdlIdentifier[] idsT,
							IdlSessionKey sessionKey,
							IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitMeasurementSetupsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public Set loadResultsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadParameterSetsButIds(final StorableObjectCondition condition, final Set<Identifier> ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PARAMETERSET_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							IdlIdentifier[] idsT,
							IdlSessionKey sessionKey,
							IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitParameterSetsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	/**
	 * NOTE: New tests come to MCM only by CORBA call 'receiveTests'. 
	 */
	public Set loadTestsButIds(final StorableObjectCondition condition, final Set<Identifier> ids)
			throws ApplicationException {
		return DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadCronTemporalPatternsButIds(final StorableObjectCondition condition, final Set<Identifier> ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CRONTEMPORALPATTERN_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							IdlIdentifier[] idsT,
							IdlSessionKey sessionKey,
							IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitCronTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public Set loadIntervalsTemporalPatternsButIds(final StorableObjectCondition condition, final Set<Identifier> ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							IdlIdentifier[] idsT,
							IdlSessionKey sessionKey,
							IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitIntervalsTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public Set loadPeriodicalTemporalPatternsButIds(final StorableObjectCondition condition, final Set<Identifier> ids)
			throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE,
				ids,
				condition,
				new TransmitButIdsByConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							IdlIdentifier[] idsT,
							IdlSessionKey sessionKey,
							IdlStorableObjectCondition conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitPeriodicalTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}



	/*	Save multiple objects*/

	public void saveMeasurements(final Set<Measurement> storableObjects, final boolean force) throws ApplicationException {
		DatabaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveAnalyses(final Set<Analysis> storableObjects, final boolean force) throws ApplicationException {
		DatabaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveEvaluations(final Set<Evaluation> storableObjects, final boolean force) throws ApplicationException {
		DatabaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveResults(final Set<Result> storableObjects, final boolean force) throws ApplicationException {
		DatabaseObjectLoader.saveStorableObjects(storableObjects, force);
	}

	public void saveTests(final Set<Test> storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.TEST_CODE, storableObjects, force, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(final CommonServer server,
					final IDLEntity[] transferables,
					final IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).receiveTests((IdlTest[]) transferables, force, sessionKey);
			}
		});
	}

	/*
	 * MCM do not need in all below methods
	 */	


	public Set loadModelingTypes(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}

	public Set loadModelings(final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids;
		return null;
	}




	
	public Set loadModelingTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadModelingsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}



	public void saveMeasurementTypes(final Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveAnalysisTypes(final Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveEvaluationTypes(final Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveModelingTypes(final Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveMeasurementSetups(final Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveModelings(final Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveParameterSets(final Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveCronTemporalPatterns(final Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void saveIntervalsTemporalPatterns(final Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
	
	public void savePeriodicalTemporalPatterns(final Set objects, final boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
}
