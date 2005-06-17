/*
 * $Id: CMMeasurementTransmit.java,v 1.37 2005/06/17 13:06:59 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
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
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.37 $, $Date: 2005/06/17 13:06:59 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMMeasurementTransmit extends CMConfigurationTransmit {

	private static final long serialVersionUID = 7239422140270778290L;


	/* Transmit multiple objects*/

	public MeasurementType_Transferable[] transmitMeasurementTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final MeasurementType_Transferable[] measurementTypes = new MeasurementType_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementTypes, 0, length);
		return measurementTypes;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final AnalysisType_Transferable[] analysisTypes = new AnalysisType_Transferable[length];
		System.arraycopy(storableObjects, 0, analysisTypes, 0, length);
		return analysisTypes;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final EvaluationType_Transferable[] evaluationTypes = new EvaluationType_Transferable[length];
		System.arraycopy(storableObjects, 0, evaluationTypes, 0, length);
		return evaluationTypes;
	}

	public ModelingType_Transferable[] transmitModelingTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final ModelingType_Transferable[] modelingTypes = new ModelingType_Transferable[length];
		System.arraycopy(storableObjects, 0, modelingTypes, 0, length);
		return modelingTypes;
	}

	public Measurement_Transferable[] transmitMeasurements(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Measurement_Transferable[] measurements = new Measurement_Transferable[length];
		System.arraycopy(storableObjects, 0, measurements, 0, length);
		return measurements;
	}

	public Analysis_Transferable[] transmitAnalyses(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Analysis_Transferable[] analyses = new Analysis_Transferable[length];
		System.arraycopy(storableObjects, 0, analyses, 0, length);
		return analyses;
	}

	public Evaluation_Transferable[] transmitEvaluations(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Evaluation_Transferable[] evaluations = new Evaluation_Transferable[length];
		System.arraycopy(storableObjects, 0, evaluations, 0, length);
		return evaluations;
	}

	public Modeling_Transferable[] transmitModelings(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Modeling_Transferable[] modelings = new Modeling_Transferable[length];
		System.arraycopy(storableObjects, 0, modelings, 0, length);
		return modelings;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetups(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final MeasurementSetup_Transferable[] measurementSetups = new MeasurementSetup_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementSetups, 0, length);
		return measurementSetups;
	}

	public Result_Transferable[] transmitResults(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Result_Transferable[] results = new Result_Transferable[length];
		System.arraycopy(storableObjects, 0, results, 0, length);
		return results;
	}

	public ParameterSet_Transferable[] transmitParameterSets(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final ParameterSet_Transferable[] sets = new ParameterSet_Transferable[length];
		System.arraycopy(storableObjects, 0, sets, 0, length);
		return sets;
	}

	public Test_Transferable[] transmitTests(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Test_Transferable[] tests = new Test_Transferable[length];
		System.arraycopy(storableObjects, 0, tests, 0, length);
		return tests;
	}

	public CronTemporalPattern_Transferable[] transmitCronTemporalPatterns(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final CronTemporalPattern_Transferable[] cronTemporalPatterns = new CronTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, cronTemporalPatterns, 0, length);
		return cronTemporalPatterns;
	}

	public IntervalsTemporalPattern_Transferable[] transmitIntervalsTemporalPatterns(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IntervalsTemporalPattern_Transferable[] intervalsTemporalPatterns = new IntervalsTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, intervalsTemporalPatterns, 0, length);
		return intervalsTemporalPatterns;
	}

	public PeriodicalTemporalPattern_Transferable[] transmitPeriodicalTemporalPatterns(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final PeriodicalTemporalPattern_Transferable[] periodicalTemporalPatterns = new PeriodicalTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, periodicalTemporalPatterns, 0, length);
		return periodicalTemporalPatterns;
	}



	/* Transmit multiple objects but idsT by condition */

	public MeasurementType_Transferable[] transmitMeasurementTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final MeasurementType_Transferable[] measurementTypes = new MeasurementType_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementTypes, 0, length);
		return measurementTypes;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final AnalysisType_Transferable[] analysisTypes = new AnalysisType_Transferable[length];
		System.arraycopy(storableObjects, 0, analysisTypes, 0, length);
		return analysisTypes;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final EvaluationType_Transferable[] evaluationTypes = new EvaluationType_Transferable[length];
		System.arraycopy(storableObjects, 0, evaluationTypes, 0, length);
		return evaluationTypes;
	}

	public ModelingType_Transferable[] transmitModelingTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final ModelingType_Transferable[] modelingTypes = new ModelingType_Transferable[length];
		System.arraycopy(storableObjects, 0, modelingTypes, 0, length);
		return modelingTypes;
	}

	public Measurement_Transferable[] transmitMeasurementsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Measurement_Transferable[] measurements = new Measurement_Transferable[length];
		System.arraycopy(storableObjects, 0, measurements, 0, length);
		return measurements;
	}

	public Analysis_Transferable[] transmitAnalysesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Analysis_Transferable[] analyses = new Analysis_Transferable[length];
		System.arraycopy(storableObjects, 0, analyses, 0, length);
		return analyses;
	}

	public Evaluation_Transferable[] transmitEvaluationsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Evaluation_Transferable[] evaluations = new Evaluation_Transferable[length];
		System.arraycopy(storableObjects, 0, evaluations, 0, length);
		return evaluations;
	}

	public Modeling_Transferable[] transmitModelingsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Modeling_Transferable[] modelings = new Modeling_Transferable[length];
		System.arraycopy(storableObjects, 0, modelings, 0, length);
		return modelings;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final MeasurementSetup_Transferable[] measurementSetups = new MeasurementSetup_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementSetups, 0, length);
		return measurementSetups;
	}

	public Result_Transferable[] transmitResultsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Result_Transferable[] results = new Result_Transferable[length];
		System.arraycopy(storableObjects, 0, results, 0, length);
		return results;
	}

	public ParameterSet_Transferable[] transmitParameterSetsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final ParameterSet_Transferable[] sets = new ParameterSet_Transferable[length];
		System.arraycopy(storableObjects, 0, sets, 0, length);
		return sets;
	}

	public Test_Transferable[] transmitTestsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Test_Transferable[] tests = new Test_Transferable[length];
		System.arraycopy(storableObjects, 0, tests, 0, length);
		return tests;
	}

	public CronTemporalPattern_Transferable[] transmitCronTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final CronTemporalPattern_Transferable[] cronTemporalPatterns = new CronTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, cronTemporalPatterns, 0, length);
		return cronTemporalPatterns;
	}

	public IntervalsTemporalPattern_Transferable[] transmitIntervalsTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IntervalsTemporalPattern_Transferable[] intervalsTemporalPatterns = new IntervalsTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, intervalsTemporalPatterns, 0, length);
		return intervalsTemporalPatterns;
	}

	public PeriodicalTemporalPattern_Transferable[] transmitPeriodicalTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final PeriodicalTemporalPattern_Transferable[] periodicalTemporalPatterns = new PeriodicalTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, periodicalTemporalPatterns, 0, length);
		return periodicalTemporalPatterns;
	}
}
