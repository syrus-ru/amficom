/*
 * $Id: CMMeasurementTransmit.java,v 1.30 2005/05/23 09:01:04 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
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
import com.syrus.util.Log;

/**
 * @version $Revision: 1.30 $, $Date: 2005/05/23 09:01:04 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMMeasurementTransmit extends CMConfigurationTransmit {
	public MeasurementType_Transferable[] transmitMeasurementTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final MeasurementType_Transferable measurementTypes[] = new MeasurementType_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementTypes, 0, length);
		return measurementTypes;
	}

	public MeasurementType_Transferable[] transmitMeasurementTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final MeasurementType_Transferable measurementTypes[] = new MeasurementType_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementTypes, 0, length);
		return measurementTypes;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final AnalysisType_Transferable analysisTypes[] = new AnalysisType_Transferable[length];
		System.arraycopy(storableObjects, 0, analysisTypes, 0, length);
		return analysisTypes;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final AnalysisType_Transferable analysisTypes[] = new AnalysisType_Transferable[length];
		System.arraycopy(storableObjects, 0, analysisTypes, 0, length);
		return analysisTypes;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final EvaluationType_Transferable evaluationTypes[] = new EvaluationType_Transferable[length];
		System.arraycopy(storableObjects, 0, evaluationTypes, 0, length);
		return evaluationTypes;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final EvaluationType_Transferable evaluationTypes[] = new EvaluationType_Transferable[length];
		System.arraycopy(storableObjects, 0, evaluationTypes, 0, length);
		return evaluationTypes;
	}

	public ModelingType_Transferable[] transmitModelingTypes(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final ModelingType_Transferable modelingTypes[] = new ModelingType_Transferable[length];
		System.arraycopy(storableObjects, 0, modelingTypes, 0, length);
		return modelingTypes;
	}

	public ModelingType_Transferable[] transmitModelingTypesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final ModelingType_Transferable modelingTypes[] = new ModelingType_Transferable[length];
		System.arraycopy(storableObjects, 0, modelingTypes, 0, length);
		return modelingTypes;
	}

	public Measurement_Transferable[] transmitMeasurements(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Measurement_Transferable measurements[] = new Measurement_Transferable[length];
		System.arraycopy(storableObjects, 0, measurements, 0, length);
		return measurements;
	}

	public Measurement_Transferable[] transmitMeasurementsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Measurement_Transferable measurements[] = new Measurement_Transferable[length];
		System.arraycopy(storableObjects, 0, measurements, 0, length);
		return measurements;
	}

	public Analysis_Transferable[] transmitAnalyses(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Analysis_Transferable analyses[] = new Analysis_Transferable[length];
		System.arraycopy(storableObjects, 0, analyses, 0, length);
		return analyses;
	}

	public Analysis_Transferable[] transmitAnalysesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Analysis_Transferable analyses[] = new Analysis_Transferable[length];
		System.arraycopy(storableObjects, 0, analyses, 0, length);
		return analyses;
	}

	public Evaluation_Transferable[] transmitEvaluations(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Evaluation_Transferable evaluations[] = new Evaluation_Transferable[length];
		System.arraycopy(storableObjects, 0, evaluations, 0, length);
		return evaluations;
	}

	public Evaluation_Transferable[] transmitEvaluationsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Evaluation_Transferable evaluations[] = new Evaluation_Transferable[length];
		System.arraycopy(storableObjects, 0, evaluations, 0, length);
		return evaluations;
	}

	public Modeling_Transferable[] transmitModelings(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Modeling_Transferable modelings[] = new Modeling_Transferable[length];
		System.arraycopy(storableObjects, 0, modelings, 0, length);
		return modelings;
	}

	public Modeling_Transferable[] transmitModelingsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Modeling_Transferable modelings[] = new Modeling_Transferable[length];
		System.arraycopy(storableObjects, 0, modelings, 0, length);
		return modelings;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetups(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final MeasurementSetup_Transferable measurementSetups[] = new MeasurementSetup_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementSetups, 0, length);
		return measurementSetups;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final MeasurementSetup_Transferable measurementSetups[] = new MeasurementSetup_Transferable[length];
		System.arraycopy(storableObjects, 0, measurementSetups, 0, length);
		return measurementSetups;
	}

	public Result_Transferable[] transmitResults(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Result_Transferable results[] = new Result_Transferable[length];
		System.arraycopy(storableObjects, 0, results, 0, length);
		return results;
	}

	public Result_Transferable[] transmitResultsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Result_Transferable results[] = new Result_Transferable[length];
		System.arraycopy(storableObjects, 0, results, 0, length);
		return results;
	}

	public Set_Transferable[] transmitSets(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Set_Transferable sets[] = new Set_Transferable[length];
		System.arraycopy(storableObjects, 0, sets, 0, length);
		return sets;
	}

	public Set_Transferable[] transmitSetsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Set_Transferable sets[] = new Set_Transferable[length];
		System.arraycopy(storableObjects, 0, sets, 0, length);
		return sets;
	}

	public Test_Transferable[] transmitTests(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Test_Transferable tests[] = new Test_Transferable[length];
		System.arraycopy(storableObjects, 0, tests, 0, length);
		return tests;
	}

	public Test_Transferable[] transmitTestsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Test_Transferable tests[] = new Test_Transferable[length];
		System.arraycopy(storableObjects, 0, tests, 0, length);
		return tests;
	}

	public CronTemporalPattern_Transferable[] transmitCronTemporalPatterns(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final CronTemporalPattern_Transferable cronTemporalPatterns[] = new CronTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, cronTemporalPatterns, 0, length);
		return cronTemporalPatterns;
	}

	public CronTemporalPattern_Transferable[] transmitCronTemporalPatternsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final CronTemporalPattern_Transferable cronTemporalPatterns[] = new CronTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, cronTemporalPatterns, 0, length);
		return cronTemporalPatterns;
	}

	public IntervalsTemporalPattern_Transferable[] transmitIntervalsTemporalPatterns(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final IntervalsTemporalPattern_Transferable intervalsTemporalPatterns[] = new IntervalsTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, intervalsTemporalPatterns, 0, length);
		return intervalsTemporalPatterns;
	}

	public IntervalsTemporalPattern_Transferable[] transmitIntervalsTemporalPatternsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final IntervalsTemporalPattern_Transferable intervalsTemporalPatterns[] = new IntervalsTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, intervalsTemporalPatterns, 0, length);
		return intervalsTemporalPatterns;
	}

	public PeriodicalTemporalPattern_Transferable[] transmitPeriodicalTemporalPatterns(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final PeriodicalTemporalPattern_Transferable periodicalTemporalPatterns[] = new PeriodicalTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, periodicalTemporalPatterns, 0, length);
		return periodicalTemporalPatterns;
	}

	public PeriodicalTemporalPattern_Transferable[] transmitPeriodicalTemporalPatternsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final PeriodicalTemporalPattern_Transferable periodicalTemporalPatterns[] = new PeriodicalTemporalPattern_Transferable[length];
		System.arraycopy(storableObjects, 0, periodicalTemporalPatterns, 0, length);
		return periodicalTemporalPatterns;
	}

	/*	Refresh*/

	/**
	 * @deprecated
	 */
	public Identifier_Transferable[] transmitRefreshedMeasurementObjects(StorableObject_Transferable[] storableObjectsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final Identifier_TransferableHolder userId = new Identifier_TransferableHolder();
		final Identifier_TransferableHolder domainId = new Identifier_TransferableHolder();
		this.validateAccess(sessionKeyT, userId, domainId);

		Map storableObjectsTMap = new HashMap();
		for (int i = 0; i < storableObjectsT.length; i++)
			storableObjectsTMap.put(new Identifier(storableObjectsT[i].id), storableObjectsT[i]);

		try {
			StorableObjectPool.refresh();

			java.util.Set storableObjects = StorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				final StorableObject so = (StorableObject) it.next();
				final StorableObject_Transferable soT = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				// Remove objects with older versions as well as objects with the same versions.
				// Not only with older ones!
				if (!so.hasNewerVersion(soT.version))
					it.remove();
			}

			return Identifier.createTransferables(storableObjects);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, throwable.getMessage());
		}
	}
}
