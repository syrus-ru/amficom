/*-
 * $Id: CMMeasurementReceive.java,v 1.19 2005/05/25 13:01:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
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
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.19 $, $Date: 2005/05/25 13:01:03 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMMeasurementReceive extends CMConfigurationReceive {
	private static final long serialVersionUID = 2044666930827736818L;

	public final StorableObject_Transferable[] receiveMeasurementTypes(
			final MeasurementType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveAnalysisTypes(
			final AnalysisType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveEvaluationTypes(
			final EvaluationType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveModelingTypes(
			final ModelingType_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MODELINGTYPE_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveMeasurements(
			final Measurement_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENT_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveAnalyses(
			final Analysis_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.ANALYSIS_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveEvaluations(
			final Evaluation_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.EVALUATION_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveModelings(
			final Modeling_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MODELING_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveMeasurementSetups(
			final MeasurementSetup_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveResults(
			final Result_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.RESULT_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveSets(
			final Set_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SET_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveTests(
			final Test_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.TEST_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveCronTemporalPatterns(
			final CronTemporalPattern_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveIntervalsTemporalPatterns(
			final IntervalsTemporalPattern_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receivePeriodicalTemporalPatterns(
			final PeriodicalTemporalPattern_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE, transferables, force, sessionKey);
	}
}
