/*
 * $Id: MeasurementStorableObjectPool.java,v 1.5 2004/08/16 08:17:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Map;
import java.util.Hashtable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2004/08/16 08:17:58 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementStorableObjectPool {
	private static final int OBJECT_POOL_MAP_SIZE = 14;	/*	Number of entities*/

	private static final int PARAMETERTYPE_OBJECT_POOL_SIZE = 9;
	private static final int MEASUREMENTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int ANALYSISTYPE_OBJECT_POOL_SIZE = 1;
	private static final int EVALUATIONTYPE_OBJECT_POOL_SIZE = 1;

	private static final int SET_OBJECT_POOL_SIZE = 4;
	private static final int SETPARAMETER_OBJECT_POOL_SIZE = 4;
	private static final int MS_OBJECT_POOL_SIZE = 4;
	private static final int MEASUREMENT_OBJECT_POOL_SIZE = 4;
	private static final int ANALYSIS_OBJECT_POOL_SIZE = 4;
	private static final int EVALUATION_OBJECT_POOL_SIZE = 4;
	private static final int TEST_OBJECT_POOL_SIZE = 2;
	private static final int RESULT_OBJECT_POOL_SIZE = 4;
	private static final int RESULTPARAMETER_OBJECT_POOL_SIZE = 4;
	private static final int TEMPORALPATTERN_OBJECT_POOL_SIZE = 2;

	private static Map objectPoolMap; /*	Map <String objectEntity, LRUMap objectPool>	*/
	private static MeasurementObjectLoader mObjectLoader;

	private MeasurementStorableObjectPool() {
	}

	public static void init(MeasurementObjectLoader mObjectLoader1) {
		objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, PARAMETERTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, MEASUREMENTTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, ANALYSISTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, EVALUATIONTYPE_OBJECT_POOL_SIZE);

		addObjectPool(ObjectEntities.SET_ENTITY_CODE, SET_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MS_ENTITY_CODE, MS_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENT_ENTITY_CODE, MEASUREMENT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.ANALYSIS_ENTITY_CODE, ANALYSIS_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EVALUATION_ENTITY_CODE, EVALUATION_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.TEST_ENTITY_CODE, TEST_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.RESULT_ENTITY_CODE, RESULT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE, TEMPORALPATTERN_OBJECT_POOL_SIZE);

		mObjectLoader = mObjectLoader1;
	}

	private static void addObjectPool(short objectEntityCode, int poolSize) {
		LRUMap objectPool = new LRUMap(poolSize);
		objectPoolMap.put(new Short(objectEntityCode), objectPool);
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) {
		short objectEntityCode = objectId.getMajor();
		LRUMap objectPool = (LRUMap)objectPoolMap.get(new Short(objectEntityCode));
		if (objectPool != null) {
			StorableObject storableObject = (StorableObject)objectPool.get(objectId);
			if (storableObject != null)
				return storableObject;
			else {
				if (useLoader) {
					try {
						storableObject = loadStorableObject(objectId);
						if (storableObject != null)
							putStorableObject(storableObject);
					}
					catch (Exception e) {
						Log.errorException(e);
					}
				}
				return storableObject;
			}
		}
		else {
			Log.errorMessage("MeasurementStorableObjectPool.getStorableObject | Cannot find object pool for objectId: '" + objectId.toString() + "' entity code: '" + objectEntityCode + "'");
			return null;
		}
	}

	private static StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadParameterType(objectId);
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadMeasurementType(objectId);
				break;
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadAnalysisType(objectId);
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadEvaluationType(objectId);
				break;
			case ObjectEntities.SET_ENTITY_CODE:
				storableObject = mObjectLoader.loadSet(objectId);
				break;
			case ObjectEntities.MS_ENTITY_CODE:
				storableObject = mObjectLoader.loadMeasurementSetup(objectId);
				break;
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				storableObject = mObjectLoader.loadAnalysis(objectId);
				break;
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				storableObject = mObjectLoader.loadEvaluation(objectId);
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				storableObject = mObjectLoader.loadMeasurement(objectId);
				break;
			case ObjectEntities.TEST_ENTITY_CODE:
				storableObject = mObjectLoader.loadTest(objectId);
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				storableObject = mObjectLoader.loadResult(objectId);
				break;
			case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
				storableObject = mObjectLoader.loadTemporalPattern(objectId);
				break;
			default:
				Log.errorMessage("MeasurementStorableObjectPool.loadStorableObject | Unknown entity: " + objectId.getObjectEntity());
				storableObject = null;
		}
		return storableObject;
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		Identifier objectId = storableObject.getId();
		LRUMap objectPool = (LRUMap)objectPoolMap.get(new Short(objectId.getMajor()));
		if (objectPool != null) {
			return (StorableObject)objectPool.put(objectId, storableObject);
		}
		throw new IllegalObjectEntityException("MeasurementStorableObjectPool.putStorableObject | Illegal object entity: '" + objectId.getObjectEntity() + "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
}
