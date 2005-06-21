/*-
 * $Id: CMMeasurementReceive.java,v 1.23 2005/06/21 12:44:31 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
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
 * @version $Revision: 1.23 $, $Date: 2005/06/21 12:44:31 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMMeasurementReceive extends CMConfigurationReceive {
	private static final long serialVersionUID = 2044666930827736818L;

	public final IdlStorableObject[] receiveMeasurementTypes(
			final MeasurementType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveAnalysisTypes(
			final AnalysisType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.ANALYSIS_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveEvaluationTypes(
			final EvaluationType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EVALUATION_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveModelingTypes(
			final ModelingType_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MODELING_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMeasurements(
			final Measurement_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveAnalyses(
			final Analysis_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.ANALYSIS_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveEvaluations(
			final Evaluation_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EVALUATION_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveModelings(
			final Modeling_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MODELING_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMeasurementSetups(
			final MeasurementSetup_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENTSETUP_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveResults(
			final Result_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.RESULT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveParameterSets(
			final ParameterSet_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PARAMETERSET_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveTests(
			final Test_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TEST_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCronTemporalPatterns(
			final CronTemporalPattern_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CRONTEMPORALPATTERN_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveIntervalsTemporalPatterns(
			final IntervalsTemporalPattern_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePeriodicalTemporalPatterns(
			final PeriodicalTemporalPattern_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, transferables, force, sessionKey);
	}
}
