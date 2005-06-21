/*-
 * $Id: CORBAMeasurementObjectLoader.java,v 1.23 2005/06/21 12:44:27 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.CronTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.IntervalsTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ModelingType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.PeriodicalTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterSet_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.23 $, $Date: 2005/06/21 12:44:27 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public final class CORBAMeasurementObjectLoader extends CORBAObjectLoader implements MeasurementObjectLoader {

	public CORBAMeasurementObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadMeasurementTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENT_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadAnalysisTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.ANALYSIS_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitAnalysisTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadEvaluationTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.EVALUATION_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEvaluationTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadModelingTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MODELING_TYPE_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitModelingTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadMeasurements(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurements(idsT, sessionKey);
			}
		});
	}

	public Set loadAnalyses(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.ANALYSIS_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitAnalyses(idsT, sessionKey);
			}
		});
	}

	public Set loadEvaluations(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.EVALUATION_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEvaluations(idsT, sessionKey);
			}
		});
	}

	public Set loadModelings(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MODELING_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitModelings(idsT, sessionKey);
			}
		});
	}

	public Set loadMeasurementSetups(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MEASUREMENTSETUP_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementSetups(idsT, sessionKey);
			}
		});
	}

	public Set loadResults(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.RESULT_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitResults(idsT, sessionKey);
			}
		});
	}

	public Set loadParameterSets(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PARAMETERSET_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitParameterSets(idsT, sessionKey);
			}
		});
	}

	public Set loadTests(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.TEST_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTests(idsT, sessionKey);
			}
		});
	}

	public Set loadCronTemporalPatterns(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.CRONTEMPORALPATTERN_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCronTemporalPatterns(idsT, sessionKey);
			}
		});
	}

	public Set loadIntervalsTemporalPatterns(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitIntervalsTemporalPatterns(idsT, sessionKey);
			}
		});
	}

	public Set loadPeriodicalTemporalPatterns(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPeriodicalTemporalPatterns(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadMeasurementTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENT_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadAnalysisTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.ANALYSIS_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitAnalysisTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadEvaluationTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.EVALUATION_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEvaluationTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadModelingTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MODELING_TYPE_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitModelingTypesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadMeasurementsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadAnalysesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.ANALYSIS_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitAnalysesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadEvaluationsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.EVALUATION_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitEvaluationsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadModelingsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MODELING_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitModelingsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadMeasurementSetupsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MEASUREMENTSETUP_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMeasurementSetupsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadResultsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.RESULT_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitResultsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadParameterSetsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PARAMETERSET_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitParameterSetsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadTestsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.TEST_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitTestsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.CRONTEMPORALPATTERN_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitCronTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadIntervalsTemporalPatternsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitIntervalsTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitPeriodicalTemporalPatternsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveMeasurementTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MEASUREMENT_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMeasurementTypes((MeasurementType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveAnalysisTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.ANALYSIS_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveAnalysisTypes((AnalysisType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveEvaluationTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.EVALUATION_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveEvaluationTypes((EvaluationType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveModelingTypes(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MODELING_TYPE_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveModelingTypes((ModelingType_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMeasurements(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MEASUREMENT_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMeasurements((Measurement_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveAnalyses(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.ANALYSIS_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveAnalyses((Analysis_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveEvaluations(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.EVALUATION_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveEvaluations((Evaluation_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveModelings(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MODELING_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveModelings((Modeling_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMeasurementSetups(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MEASUREMENTSETUP_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMeasurementSetups((MeasurementSetup_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveResults(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.RESULT_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveResults((Result_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveParameterSets(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PARAMETERSET_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveParameterSets((ParameterSet_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveTests(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.TEST_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveTests((Test_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveCronTemporalPatterns(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.CRONTEMPORALPATTERN_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveCronTemporalPatterns((CronTemporalPattern_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveIntervalsTemporalPatterns(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveIntervalsTemporalPatterns((IntervalsTemporalPattern_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void savePeriodicalTemporalPatterns(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receivePeriodicalTemporalPatterns((PeriodicalTemporalPattern_Transferable[]) transferables, force, sessionKey);
			}
		});
	}
}
