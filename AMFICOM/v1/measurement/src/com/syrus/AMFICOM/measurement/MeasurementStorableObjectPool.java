/*
 * $Id: MeasurementStorableObjectPool.java,v 1.1 2004/08/06 12:05:56 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObjectType;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.util.LRUMap;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/06 12:05:56 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementStorableObjectPool {
	private static final int OBJECT_POOL_MAP_SIZE = 14;	/*	Number of entities*/

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
	private static final int PARAMETERTYPE_OBJECT_POOL_SIZE = 9;
	private static final int MEASUREMENTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int ANALYSISTYPE_OBJECT_POOL_SIZE = 1;
	private static final int EVALUATIONTYPE_OBJECT_POOL_SIZE = 1;

	private static Map objectPoolMap; /*	Map <String objectEntity, LRUMap objectPool>	*/

	private MeasurementStorableObjectPool() {
	}

	public static void init() {
		objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		addObjectPool(ObjectEntities.SET_ENTITY, SET_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.SETPARAMETER_ENTITY, SETPARAMETER_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MS_ENTITY, MS_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENT_ENTITY, MEASUREMENT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.ANALYSIS_ENTITY, ANALYSIS_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EVALUATION_ENTITY, EVALUATION_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.TEST_ENTITY, TEST_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.RESULT_ENTITY, RESULT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.RESULTPARAMETER_ENTITY, RESULTPARAMETER_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.TEMPORALPATTERN_ENTITY, TEMPORALPATTERN_OBJECT_POOL_SIZE);

		addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY, PARAMETERTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENTTYPE_ENTITY, MEASUREMENTTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.ANALYSISTYPE_ENTITY, ANALYSISTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EVALUATIONTYPE_ENTITY, EVALUATIONTYPE_OBJECT_POOL_SIZE);
	}

	public static void addObjectPool(String objectEntity, int poolSize) {
		LRUMap objectPool = new LRUMap(poolSize);
		objectPoolMap.put(objectEntity, objectPool);
	}

	public static StorableObject getStorableObject(Identifier objectId) throws IllegalObjectEntityException {
		LRUMap objectPool = (LRUMap)objectPoolMap.get(objectId.getObjectEntity());
		if (objectPool != null) {
			return (StorableObject)objectPool.get(objectId);
		}
		throw new IllegalObjectEntityException("Illegal object entity: '" + objectId.getObjectEntity() + "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		Identifier objectId = storableObject.getId();
		LRUMap objectPool = (LRUMap)objectPoolMap.get(objectId.getObjectEntity());
		if (objectPool != null) {
			return (StorableObject)objectPool.put(objectId, storableObject);
		}
		throw new IllegalObjectEntityException("Illegal object entity: '" + objectId.getObjectEntity() + "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
}
