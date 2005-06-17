/*
 * $Id: MeasurementStorableObjectPool.java,v 1.101 2005/06/17 11:01:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.101 $, $Date: 2005/06/17 11:01:00 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class MeasurementStorableObjectPool extends StorableObjectPool {

	private static final int OBJECT_POOL_MAP_SIZE = 14; /* Number of entities */

	private static final int MEASUREMENTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int ANALYSISTYPE_OBJECT_POOL_SIZE = 1;
	private static final int EVALUATIONTYPE_OBJECT_POOL_SIZE = 1;
	private static final int MODELINGTYPE_OBJECT_POOL_SIZE = 1;

	private static final int MEASUREMENT_OBJECT_POOL_SIZE = 4;
	private static final int ANALYSIS_OBJECT_POOL_SIZE = 4;
	private static final int EVALUATION_OBJECT_POOL_SIZE = 4;
	private static final int MODELING_OBJECT_POOL_SIZE = 4;
	private static final int MS_OBJECT_POOL_SIZE = 4;
	private static final int RESULT_OBJECT_POOL_SIZE = 4;
	private static final int SET_OBJECT_POOL_SIZE = 4;
	private static final int TEST_OBJECT_POOL_SIZE = 2;
	private static final int CRONTEMPORALPATTERN_OBJECT_POOL_SIZE = 2;
	private static final int INTERVALSTEMPORALPATTERN_OBJECT_POOL_SIZE = 2;
	private static final int PERIODICTEMPORALPATTERN_OBJECT_POOL_SIZE = 2;

	private static MeasurementObjectLoader mObjectLoader;
	private static MeasurementStorableObjectPool instance;


	private MeasurementStorableObjectPool(Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.MEASUREMENT_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.MEASUREMENT_TYPE_CODE, new MeasurementTypeFactory());
		registerFactory(ObjectEntities.ANALYSIS_TYPE_CODE, new AnalysisTypeFactory());
		registerFactory(ObjectEntities.EVALUATION_TYPE_CODE, new EvaluationTypeFactory());
		registerFactory(ObjectEntities.MODELING_TYPE_CODE, new ModelingTypeFactory());

		registerFactory(ObjectEntities.MEASUREMENT_CODE, new MeasurementFactory());
		registerFactory(ObjectEntities.ANALYSIS_CODE, new AnalysisFactory());
		registerFactory(ObjectEntities.EVALUATION_CODE, new EvaluationFactory());
		registerFactory(ObjectEntities.MODELING_CODE, new ModelingFactory());
		registerFactory(ObjectEntities.MEASUREMENTSETUP_CODE, new MeasurementSetupFactory());
		registerFactory(ObjectEntities.RESULT_CODE, new ResultFactory());
		registerFactory(ObjectEntities.PARAMETERSET_CODE, new ParameterSetFactory());
		registerFactory(ObjectEntities.TEST_CODE, new TestFactory());
		registerFactory(ObjectEntities.CRONTEMPORALPATTERN_CODE, new CronTemporalPatternFactory());
		registerFactory(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE, new IntervalsTemporalPatternFactory());
		registerFactory(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, new PeriodicalTemporalPatternFactory());
	}


	/**
	 * Init with default pool class and default pool sizes
	 * @param mObjectLoader1
	 */
	public static void init(final MeasurementObjectLoader mObjectLoader1) {
		init(mObjectLoader1, LRUMap.class);
	}

	/**
	 * Init with default pool class and given pool sizes
	 * @param mObjectLoader1
	 * @param size
	 */
	public static void init(final MeasurementObjectLoader mObjectLoader1, final int size) {
		init(mObjectLoader1, LRUMap.class, size);
	}

	/**
	 * Init with given pool class and default pool sizes
	 * @param mObjectLoader1
	 * @param cacheClass
	 */
	public static void init(final MeasurementObjectLoader mObjectLoader1, final Class cacheClass) {
		assert mObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (instance == null)
			instance = new MeasurementStorableObjectPool(cacheClass);

		mObjectLoader = mObjectLoader1;

		instance.addObjectPool(ObjectEntities.MEASUREMENT_TYPE_CODE, MEASUREMENTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.ANALYSIS_TYPE_CODE, ANALYSISTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EVALUATION_TYPE_CODE, EVALUATIONTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MODELING_TYPE_CODE, MODELINGTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.MEASUREMENT_CODE, MEASUREMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.ANALYSIS_CODE, ANALYSIS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EVALUATION_CODE, EVALUATION_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MODELING_CODE, MODELING_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MEASUREMENTSETUP_CODE, MS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.RESULT_CODE, RESULT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PARAMETERSET_CODE, SET_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TEST_CODE, TEST_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CRONTEMPORALPATTERN_CODE, CRONTEMPORALPATTERN_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE, INTERVALSTEMPORALPATTERN_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, PERIODICTEMPORALPATTERN_OBJECT_POOL_SIZE);
	}

	/**
	 * Init with given pool class and given pool sizes
	 * @param mObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final MeasurementObjectLoader mObjectLoader1, final Class cacheClass, final int size) {
		assert mObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (size > 0) {
			if (instance == null)
				instance = new MeasurementStorableObjectPool(cacheClass);

			mObjectLoader = mObjectLoader1;

			instance.addObjectPool(ObjectEntities.MEASUREMENT_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.ANALYSIS_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.EVALUATION_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.MODELING_TYPE_CODE, size);

			instance.addObjectPool(ObjectEntities.MEASUREMENT_CODE, size);
			instance.addObjectPool(ObjectEntities.ANALYSIS_CODE, size);
			instance.addObjectPool(ObjectEntities.EVALUATION_CODE, size);
			instance.addObjectPool(ObjectEntities.MODELING_CODE, size);
			instance.addObjectPool(ObjectEntities.MEASUREMENTSETUP_CODE, size);
			instance.addObjectPool(ObjectEntities.RESULT_CODE, size);
			instance.addObjectPool(ObjectEntities.PARAMETERSET_CODE, size);
			instance.addObjectPool(ObjectEntities.TEST_CODE, size);
			instance.addObjectPool(ObjectEntities.CRONTEMPORALPATTERN_CODE, size);
			instance.addObjectPool(ObjectEntities.INTERVALSTEMPORALPATTERN_CODE, size);
			instance.addObjectPool(ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, size);
		}
		else {
			init(mObjectLoader1, cacheClass);
		}
	}


	protected java.util.Set refreshStorableObjects(final java.util.Set storableObjects) throws ApplicationException {
		return mObjectLoader.refresh(storableObjects);
	}

	protected java.util.Set loadStorableObjects(final java.util.Set ids) throws ApplicationException {
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.MEASUREMENT_TYPE_CODE:
				return mObjectLoader.loadMeasurementTypes(ids);
			case ObjectEntities.ANALYSIS_TYPE_CODE:
				return mObjectLoader.loadAnalysisTypes(ids);
			case ObjectEntities.EVALUATION_TYPE_CODE:
				return mObjectLoader.loadEvaluationTypes(ids);
			case ObjectEntities.MODELING_TYPE_CODE:
				return mObjectLoader.loadModelingTypes(ids);

			case ObjectEntities.MEASUREMENT_CODE:
				return mObjectLoader.loadMeasurements(ids);
			case ObjectEntities.ANALYSIS_CODE:
				return mObjectLoader.loadAnalyses(ids);
			case ObjectEntities.EVALUATION_CODE:
				return mObjectLoader.loadEvaluations(ids);
			case ObjectEntities.MODELING_CODE:
				return mObjectLoader.loadModelings(ids);
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				return mObjectLoader.loadMeasurementSetups(ids);
			case ObjectEntities.RESULT_CODE:
				return mObjectLoader.loadResults(ids);
			case ObjectEntities.PARAMETERSET_CODE:
				return mObjectLoader.loadParameterSets(ids);
			case ObjectEntities.TEST_CODE:
				return mObjectLoader.loadTests(ids);
			case ObjectEntities.CRONTEMPORALPATTERN_CODE:
				return mObjectLoader.loadCronTemporalPatterns(ids);
			case ObjectEntities.INTERVALSTEMPORALPATTERN_CODE:
				return mObjectLoader.loadIntervalsTemporalPatterns(ids);
			case ObjectEntities.PERIODICALTEMPORALPATTERN_CODE:
				return mObjectLoader.loadPeriodicalTemporalPatterns(ids);
			default:
				Log.errorMessage("MeasurementStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected java.util.Set loadStorableObjectsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		final short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.MEASUREMENT_TYPE_CODE:
				return mObjectLoader.loadMeasurementTypesButIds(condition, ids);
			case ObjectEntities.ANALYSIS_TYPE_CODE:
				return mObjectLoader.loadAnalysisTypesButIds(condition, ids);
			case ObjectEntities.EVALUATION_TYPE_CODE:
				return mObjectLoader.loadEvaluationTypesButIds(condition, ids);
			case ObjectEntities.MODELING_TYPE_CODE:
				return mObjectLoader.loadModelingTypesButIds(condition, ids);

			case ObjectEntities.MEASUREMENT_CODE:
				return mObjectLoader.loadMeasurementsButIds(condition, ids);
			case ObjectEntities.ANALYSIS_CODE:
				return mObjectLoader.loadAnalysesButIds(condition, ids);
			case ObjectEntities.EVALUATION_CODE:
				return mObjectLoader.loadEvaluationsButIds(condition, ids);
			case ObjectEntities.MODELING_CODE:
				return mObjectLoader.loadModelingsButIds(condition, ids);
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				return mObjectLoader.loadMeasurementSetupsButIds(condition, ids);
			case ObjectEntities.RESULT_CODE:
				return mObjectLoader.loadResultsButIds(condition, ids);
			case ObjectEntities.PARAMETERSET_CODE:
				return mObjectLoader.loadParameterSetsButIds(condition, ids);
			case ObjectEntities.TEST_CODE:
				return mObjectLoader.loadTestsButIds(condition, ids);
			case ObjectEntities.CRONTEMPORALPATTERN_CODE:
				return mObjectLoader.loadCronTemporalPatternsButIds(condition, ids);
			case ObjectEntities.INTERVALSTEMPORALPATTERN_CODE:
				return mObjectLoader.loadIntervalsTemporalPatternsButIds(condition, ids);
			case ObjectEntities.PERIODICALTEMPORALPATTERN_CODE:
				return mObjectLoader.loadPeriodicalTemporalPatternsButIds(condition, ids);
			default:
				Log.errorMessage("MeasurementStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected void saveStorableObjects(final java.util.Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.MEASUREMENT_TYPE_CODE:
				mObjectLoader.saveMeasurementTypes(storableObjects, force);
				break;
			case ObjectEntities.ANALYSIS_TYPE_CODE:
				mObjectLoader.saveAnalysisTypes(storableObjects, force);
				break;
			case ObjectEntities.EVALUATION_TYPE_CODE:
				mObjectLoader.saveEvaluationTypes(storableObjects, force);
				break;
			case ObjectEntities.MODELING_TYPE_CODE:
				mObjectLoader.saveModelingTypes(storableObjects, force);
				break;

			case ObjectEntities.MEASUREMENT_CODE:
				mObjectLoader.saveMeasurements(storableObjects, force);
				break;
			case ObjectEntities.ANALYSIS_CODE:
				mObjectLoader.saveAnalyses(storableObjects, force);
				break;
			case ObjectEntities.EVALUATION_CODE:
				mObjectLoader.saveEvaluations(storableObjects, force);
				break;
			case ObjectEntities.MODELING_CODE:
				mObjectLoader.saveModelings(storableObjects, force);
				break;
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				mObjectLoader.saveMeasurementSetups(storableObjects, force);
				break;
			case ObjectEntities.RESULT_CODE:
				mObjectLoader.saveResults(storableObjects, force);
				break;
			case ObjectEntities.PARAMETERSET_CODE:
				mObjectLoader.saveParameterSets(storableObjects, force);
				break;
			case ObjectEntities.TEST_CODE:
				mObjectLoader.saveTests(storableObjects, force);
				break;
			case ObjectEntities.CRONTEMPORALPATTERN_CODE:
				mObjectLoader.saveCronTemporalPatterns(storableObjects, force);
				break;
			case ObjectEntities.INTERVALSTEMPORALPATTERN_CODE:
				mObjectLoader.saveIntervalsTemporalPatterns(storableObjects, force);
				break;
			case ObjectEntities.PERIODICALTEMPORALPATTERN_CODE:
				mObjectLoader.savePeriodicalTemporalPatterns(storableObjects, force);
				break;				
			default:
				Log.errorMessage("MeasurementStorableObjectPool.saveStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	protected void deleteStorableObjects(final java.util.Set identifiables) {
		mObjectLoader.delete(identifiables);
	}

}
