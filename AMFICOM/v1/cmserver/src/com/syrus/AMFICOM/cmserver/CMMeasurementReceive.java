/*-
 * $Id: CMMeasurementReceive.java,v 1.24 2005/06/23 18:45:06 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
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
 * @version $Revision: 1.24 $, $Date: 2005/06/23 18:45:06 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMMeasurementReceive extends CMConfigurationReceive {
	private static final long serialVersionUID = 2044666930827736818L;

	public final IdlStorableObject[] receiveMeasurementTypes(
			final IdlMeasurementType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENT_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveAnalysisTypes(
			final IdlAnalysisType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.ANALYSIS_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveEvaluationTypes(
			final IdlEvaluationType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EVALUATION_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveModelingTypes(
			final IdlModelingType transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MODELING_TYPE_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMeasurements(
			final IdlMeasurement transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveAnalyses(
			final IdlAnalysis transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.ANALYSIS_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveEvaluations(
			final IdlEvaluation transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EVALUATION_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveModelings(
			final IdlModeling transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MODELING_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMeasurementSetups(
			final IdlMeasurementSetup transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENTSETUP_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveResults(
			final IdlResult transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.RESULT_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveParameterSets(
			final IdlParameterSet transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PARAMETERSET_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveTests(
			final IdlTest transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TEST_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveCronTemporalPatterns(
			final IdlCronTemporalPattern transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CRONTEMPORALPATTERN_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveIntervalsTemporalPatterns(
			final IdlIntervalsTemporalPattern transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receivePeriodicalTemporalPatterns(
			final IdlPeriodicalTemporalPattern transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, transferables, force, sessionKey);
	}
}
