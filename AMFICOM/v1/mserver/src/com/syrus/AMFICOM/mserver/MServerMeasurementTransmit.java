/*
 * $Id: MServerMeasurementTransmit.java,v 1.15 2005/06/23 18:45:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

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
import com.syrus.AMFICOM.measurement.corba.IdlPeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.corba.IdlParameterSet;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.15 $, $Date: 2005/06/23 18:45:10 $
 * @author $Author: bass $
 * @module mserver_v1
 */
abstract class MServerMeasurementTransmit extends MServerConfigurationTransmit {

	private static final long serialVersionUID = -3608597706693444950L;


  /* Transmit multiple objects*/

	public IdlMeasurementType[] transmitMeasurementTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlMeasurementType[] ret = new IdlMeasurementType[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlAnalysisType[] transmitAnalysisTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlAnalysisType[] ret = new IdlAnalysisType[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlEvaluationType[] transmitEvaluationTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlEvaluationType[] ret = new IdlEvaluationType[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlMeasurement[] transmitMeasurements(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlMeasurement[] ret = new IdlMeasurement[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlAnalysis[] transmitAnalyses(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlAnalysis[] ret = new IdlAnalysis[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlEvaluation[] transmitEvaluations(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlEvaluation[] ret = new IdlEvaluation[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlMeasurementSetup[] transmitMeasurementSetups(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlMeasurementSetup[] ret = new IdlMeasurementSetup[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlParameterSet[] transmitParameterSets(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlParameterSet[] ret = new IdlParameterSet[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlTest[] transmitTests(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlTest[] ret = new IdlTest[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlCronTemporalPattern[] transmitCronTemporalPatterns(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlCronTemporalPattern[] ret = new IdlCronTemporalPattern[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlIntervalsTemporalPattern[] transmitIntervalsTemporalPatterns(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlIntervalsTemporalPattern[] ret = new IdlIntervalsTemporalPattern[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlPeriodicalTemporalPattern[] transmitPeriodicalTemporalPatterns(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlPeriodicalTemporalPattern[] ret = new IdlPeriodicalTemporalPattern[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}



	/* Transmit multiple objects but ids by condition*/

	public IdlMeasurementType[] transmitMeasurementTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMeasurementType[] ret = new IdlMeasurementType[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlAnalysisType[] transmitAnalysisTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlAnalysisType[] ret = new IdlAnalysisType[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlEvaluationType[] transmitEvaluationTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlEvaluationType[] ret = new IdlEvaluationType[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}



	public IdlMeasurement[] transmitMeasurementsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMeasurement[] ret = new IdlMeasurement[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlAnalysis[] transmitAnalysesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlAnalysis[] ret = new IdlAnalysis[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlEvaluation[] transmitEvaluationsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlEvaluation[] ret = new IdlEvaluation[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlMeasurementSetup[] transmitMeasurementSetupsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMeasurementSetup[] ret = new IdlMeasurementSetup[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlParameterSet[] transmitParameterSetsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlParameterSet[] ret = new IdlParameterSet[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlTest[] transmitTestsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlTest[] ret = new IdlTest[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlCronTemporalPattern[] transmitCronTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlCronTemporalPattern[] ret = new IdlCronTemporalPattern[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlIntervalsTemporalPattern[] transmitIntervalsTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlIntervalsTemporalPattern[] ret = new IdlIntervalsTemporalPattern[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlPeriodicalTemporalPattern[] transmitPeriodicalTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlPeriodicalTemporalPattern[] ret = new IdlPeriodicalTemporalPattern[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

}
