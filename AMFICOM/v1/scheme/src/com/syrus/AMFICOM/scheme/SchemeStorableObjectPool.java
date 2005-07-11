/*
 * $Id: SchemeStorableObjectPool.java,v 1.28 2005/07/11 12:12:57 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.CABLECHANNELINGITEM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PATHELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEDEVICE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.codeToString;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.SCHEME_GROUP_CODE;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.28 $, $Date: 2005/07/11 12:12:57 $
 * @module scheme_v1
 */
public final class SchemeStorableObjectPool extends StorableObjectPool {
	/**
	 * Number of entities.
	 */
	private static final int OBJECT_POOL_MAP_SIZE = 17;


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
		super(OBJECT_POOL_MAP_SIZE, SCHEME_GROUP_CODE, cacheMapClass);

		registerFactory(SCHEMEPROTOGROUP_CODE, new SchemeProtoGroupFactory());
		registerFactory(SCHEMEPROTOELEMENT_CODE, new SchemeProtoElementFactory());
		registerFactory(SCHEME_CODE, new SchemeFactory());
		registerFactory(SCHEMEELEMENT_CODE, new SchemeElementFactory());
		registerFactory(SCHEMEOPTIMIZEINFO_CODE, new SchemeOptimizeInfoFactory());
		registerFactory(SCHEMEOPTIMIZEINFOSWITCH_CODE, new SchemeOptimizeInfoSwitchFactory());
		registerFactory(SCHEMEOPTIMIZEINFORTU_CODE, new SchemeOptimizeInfoRtuFactory());
		registerFactory(SCHEMEMONITORINGSOLUTION_CODE, new SchemeMonitoringSolutionFactory());
		registerFactory(SCHEMEDEVICE_CODE, new SchemeDeviceFactory());
		registerFactory(SCHEMEPORT_CODE, new SchemePortFactory());
		registerFactory(SCHEMECABLEPORT_CODE, new SchemeCablePortFactory());
		registerFactory(SCHEMELINK_CODE, new SchemeLinkFactory());
		registerFactory(SCHEMECABLELINK_CODE, new SchemeCableLinkFactory());
		registerFactory(SCHEMECABLETHREAD_CODE, new SchemeCableThreadFactory());
		registerFactory(CABLECHANNELINGITEM_CODE, new CableChannelingItemFactory());
		registerFactory(SCHEMEPATH_CODE, new SchemePathFactory());
		registerFactory(PATHELEMENT_CODE, new PathElementFactory());
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
		assert schemeObjectLoader1 != null : NON_NULL_EXPECTED;
		assert cacheClass != null : NON_NULL_EXPECTED;

		if (instance == null)
			instance = new SchemeStorableObjectPool(cacheClass);

		schemeObjectLoader = schemeObjectLoader1;

		instance.addObjectPool(SCHEMEPROTOGROUP_CODE, SCHEME_PROTO_GROUP_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMEPROTOELEMENT_CODE, SCHEME_PROTO_ELEMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEME_CODE, SCHEME_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMEELEMENT_CODE, SCHEME_ELEMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMEOPTIMIZEINFO_CODE, SCHEME_OPTIMIZE_INFO_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMEOPTIMIZEINFOSWITCH_CODE, SCHEME_OPTIMIZE_INFO_SWITCH_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMEOPTIMIZEINFORTU_CODE, SCHEME_OPTIMIZE_INFO_RTU_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMEMONITORINGSOLUTION_CODE, SCHEME_MONITORING_SOLUTION_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMEDEVICE_CODE, SCHEME_DEVICE_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMEPORT_CODE, SCHEME_PORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMECABLEPORT_CODE, SCHEME_CABLE_PORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMELINK_CODE, SCHEME_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMECABLELINK_CODE, SCHEME_CABLE_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMECABLETHREAD_CODE, SCHEME_CABLE_THREAD_OBJECT_POOL_SIZE);
		instance.addObjectPool(CABLECHANNELINGITEM_CODE, CABLE_CHANNELING_ITEM_OBJECT_POOL_SIZE);
		instance.addObjectPool(SCHEMEPATH_CODE, SCHEME_PATH_OBJECT_POOL_SIZE);
		instance.addObjectPool(PATHELEMENT_CODE, PATH_ELEMENT_OBJECT_POOL_SIZE);
	}

	/**
	 * Init with given pool class and given pool sizes
	 * @param schemeObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final SchemeObjectLoader schemeObjectLoader1, final Class cacheClass, final int size) {
		assert schemeObjectLoader1 != null : NON_NULL_EXPECTED;
		assert cacheClass != null : NON_NULL_EXPECTED;

		if (size > 0) {
			if (instance == null)
				instance = new SchemeStorableObjectPool(cacheClass);

			schemeObjectLoader = schemeObjectLoader1;

			instance.addObjectPool(SCHEMEPROTOGROUP_CODE, size);
			instance.addObjectPool(SCHEMEPROTOELEMENT_CODE, size);
			instance.addObjectPool(SCHEME_CODE, size);
			instance.addObjectPool(SCHEMEELEMENT_CODE, size);
			instance.addObjectPool(SCHEMEOPTIMIZEINFO_CODE, size);
			instance.addObjectPool(SCHEMEOPTIMIZEINFOSWITCH_CODE, size);
			instance.addObjectPool(SCHEMEOPTIMIZEINFORTU_CODE, size);
			instance.addObjectPool(SCHEMEMONITORINGSOLUTION_CODE, size);
			instance.addObjectPool(SCHEMEDEVICE_CODE, size);
			instance.addObjectPool(SCHEMEPORT_CODE, size);
			instance.addObjectPool(SCHEMECABLEPORT_CODE, size);
			instance.addObjectPool(SCHEMELINK_CODE, size);
			instance.addObjectPool(SCHEMECABLELINK_CODE, size);
			instance.addObjectPool(SCHEMECABLETHREAD_CODE, size);
			instance.addObjectPool(CABLECHANNELINGITEM_CODE, size);
			instance.addObjectPool(SCHEMEPATH_CODE, size);
			instance.addObjectPool(PATHELEMENT_CODE, size);
		} else {
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
			case SCHEMEPROTOGROUP_CODE:
				return schemeObjectLoader.loadSchemeProtoGroups(ids);
			case SCHEMEPROTOELEMENT_CODE:
				return schemeObjectLoader.loadSchemeProtoElements(ids);
			case SCHEME_CODE:
				return schemeObjectLoader.loadSchemes(ids);
			case SCHEMEELEMENT_CODE:
				return schemeObjectLoader.loadSchemeElements(ids);
			case SCHEMEOPTIMIZEINFO_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfos(ids);
			case SCHEMEOPTIMIZEINFOSWITCH_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfoSwitches(ids);
			case SCHEMEOPTIMIZEINFORTU_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfoRtus(ids);
			case SCHEMEMONITORINGSOLUTION_CODE:
				return schemeObjectLoader.loadSchemeMonitoringSolutions(ids);
			case SCHEMEDEVICE_CODE:
				return schemeObjectLoader.loadSchemeDevices(ids);
			case SCHEMEPORT_CODE:
				return schemeObjectLoader.loadSchemePorts(ids);
			case SCHEMECABLEPORT_CODE:
				return schemeObjectLoader.loadSchemeCablePorts(ids);
			case SCHEMELINK_CODE:
				return schemeObjectLoader.loadSchemeLinks(ids);
			case SCHEMECABLELINK_CODE:
				return schemeObjectLoader.loadSchemeCableLinks(ids);
			case SCHEMECABLETHREAD_CODE:
				return schemeObjectLoader.loadSchemeCableThreads(ids);
			case CABLECHANNELINGITEM_CODE:
				return schemeObjectLoader.loadCableChannelingItems(ids);
			case SCHEMEPATH_CODE:
				return schemeObjectLoader.loadSchemePaths(ids);
			case PATHELEMENT_CODE:
				return schemeObjectLoader.loadPathElements(ids);
			default:
				Log.errorMessage("SchemeStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ codeToString(entityCode) + "'/" + entityCode);
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
			case SCHEMEPROTOGROUP_CODE:
				return schemeObjectLoader.loadSchemeProtoGroupsButIds(storableObjectCondition, ids);
			case SCHEMEPROTOELEMENT_CODE:
				return schemeObjectLoader.loadSchemeProtoElementsButIds(storableObjectCondition, ids);
			case SCHEME_CODE:
				return schemeObjectLoader.loadSchemesButIds(storableObjectCondition, ids);
			case SCHEMEELEMENT_CODE:
				return schemeObjectLoader.loadSchemeElementsButIds(storableObjectCondition, ids);
			case SCHEMEOPTIMIZEINFO_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfosButIds(storableObjectCondition, ids);
			case SCHEMEOPTIMIZEINFOSWITCH_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfoSwitchesButIds(storableObjectCondition, ids);
			case SCHEMEOPTIMIZEINFORTU_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfoRtusButIds(storableObjectCondition, ids);
			case SCHEMEMONITORINGSOLUTION_CODE:
				return schemeObjectLoader.loadSchemeMonitoringSolutionsButIds(storableObjectCondition, ids);
			case SCHEMEDEVICE_CODE:
				return schemeObjectLoader.loadSchemeDevicesButIds(storableObjectCondition, ids);
			case SCHEMEPORT_CODE:
				return schemeObjectLoader.loadSchemePortsButIds(storableObjectCondition, ids);
			case SCHEMECABLEPORT_CODE:
				return schemeObjectLoader.loadSchemeCablePortsButIds(storableObjectCondition, ids);
			case SCHEMELINK_CODE:
				return schemeObjectLoader.loadSchemeLinksButIds(storableObjectCondition, ids);
			case SCHEMECABLELINK_CODE:
				return schemeObjectLoader.loadSchemeCableLinksButIds(storableObjectCondition, ids);
			case SCHEMECABLETHREAD_CODE:
				return schemeObjectLoader.loadSchemeCableThreadsButIds(storableObjectCondition, ids);
			case CABLECHANNELINGITEM_CODE:
				return schemeObjectLoader.loadCableChannelingItemsButIds(storableObjectCondition, ids);
			case SCHEMEPATH_CODE:
				return schemeObjectLoader.loadSchemePathsButIds(storableObjectCondition, ids);
			case PATHELEMENT_CODE:
				return schemeObjectLoader.loadPathElementsButIds(storableObjectCondition, ids);
			default:
				Log.errorMessage("SchemeStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '"
						+ codeToString(entityCode) + "'/" + entityCode);
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
			case SCHEMEPROTOGROUP_CODE:
				schemeObjectLoader.saveSchemeProtoGroups(storableObjects, force);
				break;
			case SCHEMEPROTOELEMENT_CODE:
				schemeObjectLoader.saveSchemeProtoElements(storableObjects, force);
				break;
			case SCHEME_CODE:
				schemeObjectLoader.saveSchemes(storableObjects, force);
				break;
			case SCHEMEELEMENT_CODE:
				schemeObjectLoader.saveSchemeElements(storableObjects, force);
				break;
			case SCHEMEOPTIMIZEINFO_CODE:
				schemeObjectLoader.saveSchemeOptimizeInfos(storableObjects, force);
				break;
			case SCHEMEOPTIMIZEINFOSWITCH_CODE:
				schemeObjectLoader.saveSchemeOptimizeInfoSwitches(storableObjects, force);
				break;
			case SCHEMEOPTIMIZEINFORTU_CODE:
				schemeObjectLoader.saveSchemeOptimizeInfoRtus(storableObjects, force);
				break;
			case SCHEMEMONITORINGSOLUTION_CODE:
				schemeObjectLoader.saveSchemeMonitoringSolutions(storableObjects, force);
				break;
			case SCHEMEDEVICE_CODE:
				schemeObjectLoader.saveSchemeDevices(storableObjects, force);
				break;
			case SCHEMEPORT_CODE:
				schemeObjectLoader.saveSchemePorts(storableObjects, force);
				break;
			case SCHEMECABLEPORT_CODE:
				schemeObjectLoader.saveSchemeCablePorts(storableObjects, force);
				break;
			case SCHEMELINK_CODE:
				schemeObjectLoader.saveSchemeLinks(storableObjects, force);
				break;
			case SCHEMECABLELINK_CODE:
				schemeObjectLoader.saveSchemeCableLinks(storableObjects, force);
				break;
			case SCHEMECABLETHREAD_CODE:
				schemeObjectLoader.saveSchemeCableThreads(storableObjects, force);
				break;
			case CABLECHANNELINGITEM_CODE:
				schemeObjectLoader.saveCableChannelingItems(storableObjects, force);
				break;
			case SCHEMEPATH_CODE:
				schemeObjectLoader.saveSchemePaths(storableObjects, force);
				break;
			case PATHELEMENT_CODE:
				schemeObjectLoader.savePathElements(storableObjects, force);
				break;
			default:
				Log.errorMessage("SchemeStorableObjectPool.saveStorableObjects | Unknown entity: '"
						+ codeToString(entityCode) + "'/" + entityCode);
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
