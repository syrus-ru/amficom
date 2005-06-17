/*
 * $Id: MServerMeasurementTransmit.java,v 1.13 2005/06/17 13:07:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

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
import com.syrus.AMFICOM.measurement.corba.PeriodicalTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterSet_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.13 $, $Date: 2005/06/17 13:07:00 $
 * @author $Author: bass $
 * @module mserver_v1
 */
abstract class MServerMeasurementTransmit extends MServerConfigurationTransmit {

	private static final long serialVersionUID = -3608597706693444950L;


  /* Transmit multiple objects*/

	public MeasurementType_Transferable[] transmitMeasurementTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final MeasurementType_Transferable[] ret = new MeasurementType_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final AnalysisType_Transferable[] ret = new AnalysisType_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypes(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final EvaluationType_Transferable[] ret = new EvaluationType_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public Measurement_Transferable[] transmitMeasurements(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final Measurement_Transferable[] ret = new Measurement_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public Analysis_Transferable[] transmitAnalyses(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final Analysis_Transferable[] ret = new Analysis_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public Evaluation_Transferable[] transmitEvaluations(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final Evaluation_Transferable[] ret = new Evaluation_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetups(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final MeasurementSetup_Transferable[] ret = new MeasurementSetup_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public ParameterSet_Transferable[] transmitParameterSets(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final ParameterSet_Transferable[] ret = new ParameterSet_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public Test_Transferable[] transmitTests(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final Test_Transferable[] ret = new Test_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public CronTemporalPattern_Transferable[] transmitCronTemporalPatterns(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final CronTemporalPattern_Transferable[] ret = new CronTemporalPattern_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IntervalsTemporalPattern_Transferable[] transmitIntervalsTemporalPatterns(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IntervalsTemporalPattern_Transferable[] ret = new IntervalsTemporalPattern_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public PeriodicalTemporalPattern_Transferable[] transmitPeriodicalTemporalPatterns(final IdlIdentifier[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final PeriodicalTemporalPattern_Transferable[] ret = new PeriodicalTemporalPattern_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}



	/* Transmit multiple objects but ids by condition*/

	public MeasurementType_Transferable[] transmitMeasurementTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final MeasurementType_Transferable[] ret = new MeasurementType_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final AnalysisType_Transferable[] ret = new AnalysisType_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final EvaluationType_Transferable[] ret = new EvaluationType_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}



	public Measurement_Transferable[] transmitMeasurementsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Measurement_Transferable[] ret = new Measurement_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public Analysis_Transferable[] transmitAnalysesButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Analysis_Transferable[] ret = new Analysis_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public Evaluation_Transferable[] transmitEvaluationsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Evaluation_Transferable[] ret = new Evaluation_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final MeasurementSetup_Transferable[] ret = new MeasurementSetup_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public ParameterSet_Transferable[] transmitParameterSetsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final ParameterSet_Transferable[] ret = new ParameterSet_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public Test_Transferable[] transmitTestsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Test_Transferable[] ret = new Test_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public CronTemporalPattern_Transferable[] transmitCronTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final CronTemporalPattern_Transferable[] ret = new CronTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IntervalsTemporalPattern_Transferable[] transmitIntervalsTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IntervalsTemporalPattern_Transferable[] ret = new IntervalsTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public PeriodicalTemporalPattern_Transferable[] transmitPeriodicalTemporalPatternsButIdsByCondition(final IdlIdentifier[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final PeriodicalTemporalPattern_Transferable[] ret = new PeriodicalTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

}
