/*
 * $Id: SchemeStorableObjectPool.java,v 1.26 2005/06/17 11:01:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Set;

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
 * @author $Author: bass $
 * @version $Revision: 1.26 $, $Date: 2005/06/17 11:01:17 $
 * @module scheme_v1
 */
public final class SchemeStorableObjectPool extends StorableObjectPool {
	/**
	 * Number of entities.
	 */
	private static final int OBJECT_POOL_MAP_SIZE = 15;


	private static final int SCHEME_PROTO_GROUP_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_PROTO_ELEMENT_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_ELEMENT_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_OPTIMIZE_INFO_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_OPTIMIZE_INFO_SWITCH_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_OPTIMIZE_INFO_RTU_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_MONITORING_SOLUTION_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_DEVICE_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_PORT_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_CABLE_PORT_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_LINK_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_CABLE_LINK_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_CABLE_THREAD_OBJECT_POOL_SIZE = 10;

	private static final int CABLE_CHANNELING_ITEM_OBJECT_POOL_SIZE = 10;

	private static final int SCHEME_PATH_OBJECT_POOL_SIZE = 10;

	private static final int PATH_ELEMENT_OBJECT_POOL_SIZE = 10;


	private static SchemeObjectLoader schemeObjectLoader;


	private static SchemeStorableObjectPool instance;


	private SchemeStorableObjectPool(final Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.SCHEME_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.SCHEMEPROTOGROUP_CODE, new SchemeProtoGroupFactory());
		registerFactory(ObjectEntities.SCHEMEPROTOELEMENT_CODE, new SchemeProtoElementFactory());
		registerFactory(ObjectEntities.SCHEME_CODE, new SchemeFactory());
		registerFactory(ObjectEntities.SCHEMEELEMENT_CODE, new SchemeElementFactory());
		registerFactory(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE, new SchemeOptimizeInfoFactory());
		registerFactory(ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE, new SchemeOptimizeInfoSwitchFactory());
		registerFactory(ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE, new SchemeOptimizeInfoRtuFactory());
		registerFactory(ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE, new SchemeMonitoringSolutionFactory());
		registerFactory(ObjectEntities.SCHEMEDEVICE_CODE, new SchemeDeviceFactory());
		registerFactory(ObjectEntities.SCHEMEPORT_CODE, new SchemePortFactory());
		registerFactory(ObjectEntities.SCHEMECABLEPORT_CODE, new SchemeCablePortFactory());
		registerFactory(ObjectEntities.SCHEMELINK_CODE, new SchemeLinkFactory());
		registerFactory(ObjectEntities.SCHEMECABLELINK_CODE, new SchemeCableLinkFactory());
		registerFactory(ObjectEntities.SCHEMECABLETHREAD_CODE, new SchemeCableThreadFactory());
		registerFactory(ObjectEntities.CABLECHANNELINGITEM_CODE, new CableChannelingItemFactory());
		registerFactory(ObjectEntities.SCHEMEPATH_CODE, new SchemePathFactory());
		registerFactory(ObjectEntities.PATHELEMENT_CODE, new PathElementFactory());
	}


	/**
	 * Init with default pool class and default pool sizes
	 * @param schemeObjectLoader1
	 */
	public static void init(final SchemeObjectLoader schemeObjectLoader1) {
		init(schemeObjectLoader1, LRUMap.class);
	}

	/**
	 * Init with default pool class and given pool sizes
	 * @param schemeObjectLoader1
	 * @param size
	 */
	public static void init(final SchemeObjectLoader schemeObjectLoader1, final int size) {
		init(schemeObjectLoader1, LRUMap.class, size);
	}

	/**
	 * Init with given pool class and default pool sizes
	 * @param schemeObjectLoader1
	 * @param cacheClass
	 */
	public static void init(final SchemeObjectLoader schemeObjectLoader1, final Class cacheClass) {
		assert schemeObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (instance == null)
			instance = new SchemeStorableObjectPool(cacheClass);

		schemeObjectLoader = schemeObjectLoader1;

		instance.addObjectPool(ObjectEntities.SCHEMEPROTOGROUP_CODE, SCHEME_PROTO_GROUP_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMEPROTOELEMENT_CODE, SCHEME_PROTO_ELEMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_CODE, SCHEME_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMEELEMENT_CODE, SCHEME_ELEMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE, SCHEME_OPTIMIZE_INFO_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE, SCHEME_OPTIMIZE_INFO_SWITCH_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE, SCHEME_OPTIMIZE_INFO_RTU_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE, SCHEME_MONITORING_SOLUTION_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMEDEVICE_CODE, SCHEME_DEVICE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMEPORT_CODE, SCHEME_PORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMECABLEPORT_CODE, SCHEME_CABLE_PORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMELINK_CODE, SCHEME_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMECABLELINK_CODE, SCHEME_CABLE_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMECABLETHREAD_CODE, SCHEME_CABLE_THREAD_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CABLECHANNELINGITEM_CODE, CABLE_CHANNELING_ITEM_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEMEPATH_CODE, SCHEME_PATH_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PATHELEMENT_CODE, PATH_ELEMENT_OBJECT_POOL_SIZE);
	}

	/**
	 * Init with given pool class and given pool sizes
	 * @param schemeObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final SchemeObjectLoader schemeObjectLoader1, final Class cacheClass, final int size) {
		assert schemeObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (size > 0) {
			if (instance == null)
				instance = new SchemeStorableObjectPool(cacheClass);

			schemeObjectLoader = schemeObjectLoader1;

			instance.addObjectPool(ObjectEntities.SCHEMEPROTOGROUP_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMEPROTOELEMENT_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEME_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMEELEMENT_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMEDEVICE_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMEPORT_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMECABLEPORT_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMELINK_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMECABLELINK_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMECABLETHREAD_CODE, size);
			instance.addObjectPool(ObjectEntities.CABLECHANNELINGITEM_CODE, size);
			instance.addObjectPool(ObjectEntities.SCHEMEPATH_CODE, size);
			instance.addObjectPool(ObjectEntities.PATHELEMENT_CODE, size);
		}
		else {
			init(schemeObjectLoader1, cacheClass);
		}
	}


	/**
	 * @param storableObjects
	 * @throws ApplicationException
	 */
	protected Set refreshStorableObjects(final Set storableObjects) throws ApplicationException {
		return schemeObjectLoader.refresh(storableObjects);
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see StorableObjectPool#loadStorableObjects(Set)
	 */
	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.SCHEMEPROTOGROUP_CODE:
				return schemeObjectLoader.loadSchemeProtoGroups(ids);
			case ObjectEntities.SCHEMEPROTOELEMENT_CODE:
				return schemeObjectLoader.loadSchemeProtoElements(ids);
			case ObjectEntities.SCHEME_CODE:
				return schemeObjectLoader.loadSchemes(ids);
			case ObjectEntities.SCHEMEELEMENT_CODE:
				return schemeObjectLoader.loadSchemeElements(ids);
			case ObjectEntities.SCHEMEOPTIMIZEINFO_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfos(ids);
			case ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfoSwitches(ids);
			case ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfoRtus(ids);
			case ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE:
				return schemeObjectLoader.loadSchemeMonitoringSolutions(ids);
			case ObjectEntities.SCHEMEDEVICE_CODE:
				return schemeObjectLoader.loadSchemeDevices(ids);
			case ObjectEntities.SCHEMEPORT_CODE:
				return schemeObjectLoader.loadSchemePorts(ids);
			case ObjectEntities.SCHEMECABLEPORT_CODE:
				return schemeObjectLoader.loadSchemeCablePorts(ids);
			case ObjectEntities.SCHEMELINK_CODE:
				return schemeObjectLoader.loadSchemeLinks(ids);
			case ObjectEntities.SCHEMECABLELINK_CODE:
				return schemeObjectLoader.loadSchemeCableLinks(ids);
			case ObjectEntities.SCHEMECABLETHREAD_CODE:
				return schemeObjectLoader.loadSchemeCableThreads(ids);
			case ObjectEntities.CABLECHANNELINGITEM_CODE:
				return schemeObjectLoader.loadCableChannelingItems(ids);
			case ObjectEntities.SCHEMEPATH_CODE:
				return schemeObjectLoader.loadSchemePaths(ids);
			case ObjectEntities.PATHELEMENT_CODE:
				return schemeObjectLoader.loadPathElements(ids);
			default:
				Log.errorMessage("SchemeStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 */
	protected Set loadStorableObjectsButIds(final StorableObjectCondition storableObjectCondition, final Set ids)
			throws ApplicationException {
		final short entityCode = storableObjectCondition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.SCHEMEPROTOGROUP_CODE:
				return schemeObjectLoader.loadSchemeProtoGroupsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMEPROTOELEMENT_CODE:
				return schemeObjectLoader.loadSchemeProtoElementsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_CODE:
				return schemeObjectLoader.loadSchemesButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMEELEMENT_CODE:
				return schemeObjectLoader.loadSchemeElementsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMEOPTIMIZEINFO_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfosButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfoSwitchesButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfoRtusButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE:
				return schemeObjectLoader.loadSchemeMonitoringSolutionsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMEDEVICE_CODE:
				return schemeObjectLoader.loadSchemeDevicesButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMEPORT_CODE:
				return schemeObjectLoader.loadSchemePortsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMECABLEPORT_CODE:
				return schemeObjectLoader.loadSchemeCablePortsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMELINK_CODE:
				return schemeObjectLoader.loadSchemeLinksButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMECABLELINK_CODE:
				return schemeObjectLoader.loadSchemeCableLinksButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMECABLETHREAD_CODE:
				return schemeObjectLoader.loadSchemeCableThreadsButIds(storableObjectCondition, ids);
			case ObjectEntities.CABLECHANNELINGITEM_CODE:
				return schemeObjectLoader.loadCableChannelingItemsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEMEPATH_CODE:
				return schemeObjectLoader.loadSchemePathsButIds(storableObjectCondition, ids);
			case ObjectEntities.PATHELEMENT_CODE:
				return schemeObjectLoader.loadPathElementsButIds(storableObjectCondition, ids);
			default:
				Log.errorMessage("SchemeStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	/**
	 * @param storableObjects
	 * @param force
	 * @throws ApplicationException
	 * @see StorableObjectPool#saveStorableObjects(Set, boolean)
	 */
	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.SCHEMEPROTOGROUP_CODE:
				schemeObjectLoader.saveSchemeProtoGroups(storableObjects, force);
				break;
			case ObjectEntities.SCHEMEPROTOELEMENT_CODE:
				schemeObjectLoader.saveSchemeProtoElements(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_CODE:
				schemeObjectLoader.saveSchemes(storableObjects, force);
				break;
			case ObjectEntities.SCHEMEELEMENT_CODE:
				schemeObjectLoader.saveSchemeElements(storableObjects, force);
				break;
			case ObjectEntities.SCHEMEOPTIMIZEINFO_CODE:
				schemeObjectLoader.saveSchemeOptimizeInfos(storableObjects, force);
				break;
			case ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE:
				schemeObjectLoader.saveSchemeOptimizeInfoSwitches(storableObjects, force);
				break;
			case ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE:
				schemeObjectLoader.saveSchemeOptimizeInfoRtus(storableObjects, force);
				break;
			case ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE:
				schemeObjectLoader.saveSchemeMonitoringSolutions(storableObjects, force);
				break;
			case ObjectEntities.SCHEMEDEVICE_CODE:
				schemeObjectLoader.saveSchemeDevices(storableObjects, force);
				break;
			case ObjectEntities.SCHEMEPORT_CODE:
				schemeObjectLoader.saveSchemePorts(storableObjects, force);
				break;
			case ObjectEntities.SCHEMECABLEPORT_CODE:
				schemeObjectLoader.saveSchemeCablePorts(storableObjects, force);
				break;
			case ObjectEntities.SCHEMELINK_CODE:
				schemeObjectLoader.saveSchemeLinks(storableObjects, force);
				break;
			case ObjectEntities.SCHEMECABLELINK_CODE:
				schemeObjectLoader.saveSchemeCableLinks(storableObjects, force);
				break;
			case ObjectEntities.SCHEMECABLETHREAD_CODE:
				schemeObjectLoader.saveSchemeCableThreads(storableObjects, force);
				break;
			case ObjectEntities.CABLECHANNELINGITEM_CODE:
				schemeObjectLoader.saveCableChannelingItems(storableObjects, force);
				break;
			case ObjectEntities.SCHEMEPATH_CODE:
				schemeObjectLoader.saveSchemePaths(storableObjects, force);
				break;
			case ObjectEntities.PATHELEMENT_CODE:
				schemeObjectLoader.savePathElements(storableObjects, force);
				break;
			default:
				Log.errorMessage("SchemeStorableObjectPool.saveStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	/**
	 * @param identifiables
	 * @see StorableObjectPool#deleteStorableObjects(Set)
	 */
	protected void deleteStorableObjects(final Set identifiables) {
		schemeObjectLoader.delete(identifiables);
	}
}
