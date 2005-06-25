/*
 * $Id: CMMeasurementTransmit.java,v 1.40 2005/06/25 17:07:49 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysisType;
import com.syrus.AMFICOM.measurement.corba.IdlAnalysis;
import com.syrus.AMFICOM.measurement.corba.IdlCronTemporalPattern;
import com.syrus.AMFICOM.measurement.corba.IdlEvaluationType;
import com.syrus.AMFICOM.measurement.corba.IdlEvaluation;
import com.syrus.AMFICOM.measurement.corba.IdlIntervalsTemporalPattern;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementSetup;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementType;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurement;
import com.syrus.AMFICOM.measurement.corba.IdlModelingType;
import com.syrus.AMFICOM.measurement.corba.IdlModeling;
import com.syrus.AMFICOM.measurement.corba.IdlPeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.corba.IdlResult;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSet;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.40 $, $Date: 2005/06/25 17:07:49 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMMeasurementTransmit extends CMConfigurationTransmit {

	private static final long serialVersionUID = 7239422140270778290L;

	CMMeasurementTransmit(final ORB orb) {
		super(orb);
	}

	/* Transmit multiple objects*/

	public IdlMeasurementType[] transmitMeasurementTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlMeasurementType[] measurementTypes = new IdlMeasurementType[length];
		System.arraycopy(storableObjects, 0, measurementTypes, 0, length);
		return measurementTypes;
	}

	public IdlAnalysisType[] transmitAnalysisTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlAnalysisType[] analysisTypes = new IdlAnalysisType[length];
		System.arraycopy(storableObjects, 0, analysisTypes, 0, length);
		return analysisTypes;
	}

	public IdlEvaluationType[] transmitEvaluationTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlEvaluationType[] evaluationTypes = new IdlEvaluationType[length];
		System.arraycopy(storableObjects, 0, evaluationTypes, 0, length);
		return evaluationTypes;
	}

	public IdlModelingType[] transmitModelingTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlModelingType[] modelingTypes = new IdlModelingType[length];
		System.arraycopy(storableObjects, 0, modelingTypes, 0, length);
		return modelingTypes;
	}

	public IdlMeasurement[] transmitMeasurements(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlMeasurement[] measurements = new IdlMeasurement[length];
		System.arraycopy(storableObjects, 0, measurements, 0, length);
		return measurements;
	}

	public IdlAnalysis[] transmitAnalyses(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlAnalysis[] analyses = new IdlAnalysis[length];
		System.arraycopy(storableObjects, 0, analyses, 0, length);
		return analyses;
	}

	public IdlEvaluation[] transmitEvaluations(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlEvaluation[] evaluations = new IdlEvaluation[length];
		System.arraycopy(storableObjects, 0, evaluations, 0, length);
		return evaluations;
	}

	public IdlModeling[] transmitModelings(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlModeling[] modelings = new IdlModeling[length];
		System.arraycopy(storableObjects, 0, modelings, 0, length);
		return modelings;
	}

	public IdlMeasurementSetup[] transmitMeasurementSetups(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlMeasurementSetup[] measurementSetups = new IdlMeasurementSetup[length];
		System.arraycopy(storableObjects, 0, measurementSetups, 0, length);
		return measurementSetups;
	}

	public IdlResult[] transmitResults(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlResult[] results = new IdlResult[length];
		System.arraycopy(storableObjects, 0, results, 0, length);
		return results;
	}

	public IdlParameterSet[] transmitParameterSets(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlParameterSet[] sets = new IdlParameterSet[length];
		System.arraycopy(storableObjects, 0, sets, 0, length);
		return sets;
	}

	public IdlTest[] transmitTests(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlTest[] tests = new IdlTest[length];
		System.arraycopy(storableObjects, 0, tests, 0, length);
		return tests;
	}

	public IdlCronTemporalPattern[] transmitCronTemporalPatterns(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlCronTemporalPattern[] cronTemporalPatterns = new IdlCronTemporalPattern[length];
		System.arraycopy(storableObjects, 0, cronTemporalPatterns, 0, length);
		return cronTemporalPatterns;
	}

	public IdlIntervalsTemporalPattern[] transmitIntervalsTemporalPatterns(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlIntervalsTemporalPattern[] intervalsTemporalPatterns = new IdlIntervalsTemporalPattern[length];
		System.arraycopy(storableObjects, 0, intervalsTemporalPatterns, 0, length);
		return intervalsTemporalPatterns;
	}

	public IdlPeriodicalTemporalPattern[] transmitPeriodicalTemporalPatterns(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlPeriodicalTemporalPattern[] periodicalTemporalPatterns = new IdlPeriodicalTemporalPattern[length];
		System.arraycopy(storableObjects, 0, periodicalTemporalPatterns, 0, length);
		return periodicalTemporalPatterns;
	}



	/* Transmit multiple objects but idsT by condition */

	public IdlMeasurementType[] transmitMeasurementTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMeasurementType[] measurementTypes = new IdlMeasurementType[length];
		System.arraycopy(storableObjects, 0, measurementTypes, 0, length);
		return measurementTypes;
	}

	public IdlAnalysisType[] transmitAnalysisTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlAnalysisType[] analysisTypes = new IdlAnalysisType[length];
		System.arraycopy(storableObjects, 0, analysisTypes, 0, length);
		return analysisTypes;
	}

	public IdlEvaluationType[] transmitEvaluationTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlEvaluationType[] evaluationTypes = new IdlEvaluationType[length];
		System.arraycopy(storableObjects, 0, evaluationTypes, 0, length);
		return evaluationTypes;
	}

	public IdlModelingType[] transmitModelingTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlModelingType[] modelingTypes = new IdlModelingType[length];
		System.arraycopy(storableObjects, 0, modelingTypes, 0, length);
		return modelingTypes;
	}

	public IdlMeasurement[] transmitMeasurementsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMeasurement[] measurements = new IdlMeasurement[length];
		System.arraycopy(storableObjects, 0, measurements, 0, length);
		return measurements;
	}

	public IdlAnalysis[] transmitAnalysesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlAnalysis[] analyses = new IdlAnalysis[length];
		System.arraycopy(storableObjects, 0, analyses, 0, length);
		return analyses;
	}

	public IdlEvaluation[] transmitEvaluationsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlEvaluation[] evaluations = new IdlEvaluation[length];
		System.arraycopy(storableObjects, 0, evaluations, 0, length);
		return evaluations;
	}

	public IdlModeling[] transmitModelingsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlModeling[] modelings = new IdlModeling[length];
		System.arraycopy(storableObjects, 0, modelings, 0, length);
		return modelings;
	}

	public IdlMeasurementSetup[] transmitMeasurementSetupsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMeasurementSetup[] measurementSetups = new IdlMeasurementSetup[length];
		System.arraycopy(storableObjects, 0, measurementSetups, 0, length);
		return measurementSetups;
	}

	public IdlResult[] transmitResultsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlResult[] results = new IdlResult[length];
		System.arraycopy(storableObjects, 0, results, 0, length);
		return results;
	}

	public IdlParameterSet[] transmitParameterSetsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlParameterSet[] sets = new IdlParameterSet[length];
		System.arraycopy(storableObjects, 0, sets, 0, length);
		return sets;
	}

	public IdlTest[] transmitTestsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlTest[] tests = new IdlTest[length];
		System.arraycopy(storableObjects, 0, tests, 0, length);
		return tests;
	}

	public IdlCronTemporalPattern[] transmitCronTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlCronTemporalPattern[] cronTemporalPatterns = new IdlCronTemporalPattern[length];
		System.arraycopy(storableObjects, 0, cronTemporalPatterns, 0, length);
		return cronTemporalPatterns;
	}

	public IdlIntervalsTemporalPattern[] transmitIntervalsTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlIntervalsTemporalPattern[] intervalsTemporalPatterns = new IdlIntervalsTemporalPattern[length];
		System.arraycopy(storableObjects, 0, intervalsTemporalPatterns, 0, length);
		return intervalsTemporalPatterns;
	}

	public IdlPeriodicalTemporalPattern[] transmitPeriodicalTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlPeriodicalTemporalPattern[] periodicalTemporalPatterns = new IdlPeriodicalTemporalPattern[length];
		System.arraycopy(storableObjects, 0, periodicalTemporalPatterns, 0, length);
		return periodicalTemporalPatterns;
	}
}
