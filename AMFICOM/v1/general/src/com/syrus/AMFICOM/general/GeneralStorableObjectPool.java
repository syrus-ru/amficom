/*-
 * $Id: GeneralStorableObjectPool.java,v 1.36 2005/06/21 12:43:47 bass Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Set;

import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.36 $, $Date: 2005/06/21 12:43:47 $
 * @author $Author: bass $
 * @module general_v1
 */

public final class GeneralStorableObjectPool extends StorableObjectPool {

	private static final int OBJECT_POOL_MAP_SIZE = 3; /* Number of entities */

	private static final int PARAMETERTYPE_OBJECT_POOL_SIZE = 9;
	private static final int CHARACTERISTICTYPE_OBJECT_POOL_SIZE = 9;

	private static final int CHARACTERISTIC_OBJECT_POOL_SIZE = 4;

	private static GeneralObjectLoader gObjectLoader;
	private static GeneralStorableObjectPool instance;

	private GeneralStorableObjectPool(final Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.GENERAL_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.PARAMETER_TYPE_CODE, new ParameterTypeFactory());
		registerFactory(ObjectEntities.CHARACTERISTIC_TYPE_CODE, new CharacteristicTypeFactory());

		registerFactory(ObjectEntities.CHARACTERISTIC_CODE, new CharacteristicFactory());
	}


	/**
	 * Init with default pool class and default pool sizes
	 * @param gObjectLoader1
	 */
	public static void init(final GeneralObjectLoader gObjectLoader1) {
		init(gObjectLoader1, LRUMap.class);
	}

	/**
	 * Init with default pool class and given pool sizes
	 * @param gObjectLoader1
	 * @param size
	 */
	public static void init(final GeneralObjectLoader gObjectLoader1, final int size) {
		init(gObjectLoader1, LRUMap.class, size);
	}

	/**
	 * Init with given pool class and default pool sizes
	 * @param gObjectLoader1
	 * @param cacheClass
	 */
	public static void init(final GeneralObjectLoader gObjectLoader1, final Class cacheClass) {
		assert gObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (instance == null)
			instance = new GeneralStorableObjectPool(cacheClass);

		gObjectLoader = gObjectLoader1;

		instance.addObjectPool(ObjectEntities.PARAMETER_TYPE_CODE, PARAMETERTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CHARACTERISTIC_TYPE_CODE, CHARACTERISTICTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.CHARACTERISTIC_CODE, CHARACTERISTIC_OBJECT_POOL_SIZE);
	}

	/**
	 * Init with given pool class and given pool sizes
	 * @param gObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final GeneralObjectLoader gObjectLoader1, final Class cacheClass, final int size) {
		assert gObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (size > 0) {
			if (instance == null)
				instance = new GeneralStorableObjectPool(cacheClass);

			gObjectLoader = gObjectLoader1;

			instance.addObjectPool(ObjectEntities.PARAMETER_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.CHARACTERISTIC_TYPE_CODE, size);

			instance.addObjectPool(ObjectEntities.CHARACTERISTIC_CODE, size);
		} else {
			init(gObjectLoader1, cacheClass);
		}
	}


	@Override
	protected Set refreshStorableObjects(final Set storableObjects) throws ApplicationException {
		return gObjectLoader.refresh(storableObjects);
	}

	@Override
	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.PARAMETER_TYPE_CODE:
				return gObjectLoader.loadParameterTypes(ids);
			case ObjectEntities.CHARACTERISTIC_TYPE_CODE:
				return gObjectLoader.loadCharacteristicTypes(ids);
			case ObjectEntities.CHARACTERISTIC_CODE:
				return gObjectLoader.loadCharacteristics(ids);
			default:
				Log.errorMessage("GeneralStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	@Override
	protected Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		final short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.PARAMETER_TYPE_CODE:
				return gObjectLoader.loadParameterTypesButIds(condition, ids);
			case ObjectEntities.CHARACTERISTIC_TYPE_CODE:
				return gObjectLoader.loadCharacteristicTypesButIds(condition, ids);
			case ObjectEntities.CHARACTERISTIC_CODE:
				return gObjectLoader.loadCharacteristicsButIds(condition, ids);
			default:
				Log.errorMessage("GeneralStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	@Override
	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.PARAMETER_TYPE_CODE:
				gObjectLoader.saveParameterTypes(storableObjects, force);
				break;
			case ObjectEntities.CHARACTERISTIC_TYPE_CODE:
				gObjectLoader.saveCharacteristicTypes(storableObjects, force);
				break;
			case ObjectEntities.CHARACTERISTIC_CODE:
				gObjectLoader.saveCharacteristics(storableObjects, force);
				break;
			default:
				Log.errorMessage("GeneralStorableObjectPool.saveStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	@Override
	protected void deleteStorableObjects(final Set identifiables) {
		gObjectLoader.delete(identifiables);
	}

}
