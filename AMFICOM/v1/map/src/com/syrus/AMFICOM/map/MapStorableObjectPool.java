/*
 * $Id: MapStorableObjectPool.java,v 1.28 2005/06/17 11:01:12 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

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
 * @version $Revision: 1.28 $, $Date: 2005/06/17 11:01:12 $
 * @author $Author: bass $
 * @module map_v1
 */
public final class MapStorableObjectPool extends StorableObjectPool {
	/**
	 * Number of entities.
	 */
	private static final int OBJECT_POOL_MAP_SIZE = 9;

	private static final int SITE_NODE_OBJECT_POOL_SIZE = 10;
	private static final int TOPOLOGICAL_NODE_OBJECT_POOL_SIZE = 10;
	private static final int NODE_LINK_OBJECT_POOL_SIZE = 10;
	private static final int MARK_OBJECT_POOL_SIZE = 10;
	private static final int PHYSICAL_LINK_OBJECT_POOL_SIZE = 10;
	private static final int COLLECTOR_OBJECT_POOL_SIZE = 10;
	private static final int MAP_OBJECT_POOL_SIZE = 10;

	private static final int SITE_NODE_TYPE_OBJECT_POOL_SIZE = 10;
	private static final int PHYSICAL_LINK_TYPE_OBJECT_POOL_SIZE = 10;

	private static MapObjectLoader mapObjectLoader;
	
	private static MapStorableObjectPool instance;


	private MapStorableObjectPool(final Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.MAP_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.SITENODE_TYPE_CODE, new SiteNodeTypeFactory());
		registerFactory(ObjectEntities.PHYSICALLINK_TYPE_CODE, new PhysicalLinkTypeFactory());
		registerFactory(ObjectEntities.SITENODE_CODE, new SiteNodeFactory());
		registerFactory(ObjectEntities.TOPOLOGICALNODE_CODE, new TopologicalNodeFactory());
		registerFactory(ObjectEntities.NODELINK_CODE, new NodeLinkFactory());
		registerFactory(ObjectEntities.MARK_CODE, new MarkFactory());
		registerFactory(ObjectEntities.PHYSICALLINK_CODE, new PhysicalLinkFactory());
		registerFactory(ObjectEntities.COLLECTOR_CODE, new CollectorFactory());
		registerFactory(ObjectEntities.MAP_CODE, new MapFactory());
	}


	/**
	 * Init with default pool class and default pool sizes
	 * @param mapObjectLoader1
	 */
	public static void init(final MapObjectLoader mapObjectLoader1) {
		init(mapObjectLoader1, LRUMap.class);
	}

	/**
	 * Init with default pool class and given pool sizes
	 * @param mapObjectLoader1
	 * @param size
	 */
	public static void init(final MapObjectLoader mapObjectLoader1, final int size) {
		init(mapObjectLoader1, LRUMap.class, size);
	}

	/**
	 * Init with given pool class and default pool sizes
	 * @param mapObjectLoader1
	 * @param cacheClass
	 */
	public static void init(final MapObjectLoader mapObjectLoader1, final Class cacheClass) {
		assert mapObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (instance == null)
			instance = new MapStorableObjectPool(cacheClass);

		mapObjectLoader = mapObjectLoader1;

		instance.addObjectPool(ObjectEntities.SITENODE_TYPE_CODE, SITE_NODE_TYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PHYSICALLINK_TYPE_CODE, PHYSICAL_LINK_TYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SITENODE_CODE, SITE_NODE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TOPOLOGICALNODE_CODE, TOPOLOGICAL_NODE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.NODELINK_CODE, NODE_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MARK_CODE, MARK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PHYSICALLINK_CODE, PHYSICAL_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.COLLECTOR_CODE, COLLECTOR_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MAP_CODE, MAP_OBJECT_POOL_SIZE);
	}

	/**
	 * Init with given pool class and given pool sizes
	 * @param mapObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final MapObjectLoader mapObjectLoader1, final Class cacheClass, final int size) {
		assert mapObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (size > 0) {
			if (instance == null)
				instance = new MapStorableObjectPool(cacheClass);

			mapObjectLoader = mapObjectLoader1;

			instance.addObjectPool(ObjectEntities.SITENODE_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.PHYSICALLINK_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.SITENODE_CODE, size);
			instance.addObjectPool(ObjectEntities.TOPOLOGICALNODE_CODE, size);
			instance.addObjectPool(ObjectEntities.NODELINK_CODE, size);
			instance.addObjectPool(ObjectEntities.MARK_CODE, size);
			instance.addObjectPool(ObjectEntities.PHYSICALLINK_CODE, size);
			instance.addObjectPool(ObjectEntities.COLLECTOR_CODE, size);
			instance.addObjectPool(ObjectEntities.MAP_CODE, size);
		}
		else {
			init(mapObjectLoader1, cacheClass);
		}
	}


	protected Set refreshStorableObjects(final Set storableObjects) throws ApplicationException {
		return mapObjectLoader.refresh(storableObjects);
	}

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.SITENODE_TYPE_CODE:
				return mapObjectLoader.loadSiteNodeTypes(ids);
			case ObjectEntities.PHYSICALLINK_TYPE_CODE:
				return mapObjectLoader.loadPhysicalLinkTypes(ids);
			case ObjectEntities.SITENODE_CODE:
				return mapObjectLoader.loadSiteNodes(ids);
			case ObjectEntities.TOPOLOGICALNODE_CODE:
				return mapObjectLoader.loadTopologicalNodes(ids);
			case ObjectEntities.NODELINK_CODE:
				return mapObjectLoader.loadNodeLinks(ids);
			case ObjectEntities.MARK_CODE:
				return mapObjectLoader.loadMarks(ids);
			case ObjectEntities.PHYSICALLINK_CODE:
				return mapObjectLoader.loadPhysicalLinks(ids);
			case ObjectEntities.COLLECTOR_CODE:
				return mapObjectLoader.loadCollectors(ids);
			case ObjectEntities.MAP_CODE:
				return mapObjectLoader.loadMaps(ids);
			default:
				Log.errorMessage("MapStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(final StorableObjectCondition storableObjectCondition, final Set ids)
			throws ApplicationException {
		final short entityCode = storableObjectCondition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.SITENODE_TYPE_CODE:
				return mapObjectLoader.loadSiteNodeTypesButIds(storableObjectCondition, ids);
			case ObjectEntities.PHYSICALLINK_TYPE_CODE:
				return mapObjectLoader.loadPhysicalLinkTypesButIds(storableObjectCondition, ids);
			case ObjectEntities.SITENODE_CODE:
				return mapObjectLoader.loadSiteNodesButIds(storableObjectCondition, ids);
			case ObjectEntities.TOPOLOGICALNODE_CODE:
				return mapObjectLoader.loadTopologicalNodesButIds(storableObjectCondition, ids);
			case ObjectEntities.NODELINK_CODE:
				return mapObjectLoader.loadNodeLinksButIds(storableObjectCondition, ids);
			case ObjectEntities.MARK_CODE:
				return mapObjectLoader.loadMarksButIds(storableObjectCondition, ids);
			case ObjectEntities.PHYSICALLINK_CODE:
				return mapObjectLoader.loadPhysicalLinksButIds(storableObjectCondition, ids);
			case ObjectEntities.COLLECTOR_CODE:
				return mapObjectLoader.loadCollectorsButIds(storableObjectCondition, ids);
			case ObjectEntities.MAP_CODE:
				return mapObjectLoader.loadMapsButIds(storableObjectCondition, ids);
			default:
				Log.errorMessage("MapStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.SITENODE_TYPE_CODE:
				mapObjectLoader.saveSiteNodeTypes(storableObjects, force);
				break;
			case ObjectEntities.PHYSICALLINK_TYPE_CODE:
				mapObjectLoader.savePhysicalLinkTypes(storableObjects, force);
				break;
			case ObjectEntities.SITENODE_CODE:
				mapObjectLoader.saveSiteNodes(storableObjects, force);
				break;
			case ObjectEntities.TOPOLOGICALNODE_CODE:
				mapObjectLoader.saveTopologicalNodes(storableObjects, force);
				break;
			case ObjectEntities.NODELINK_CODE:
				mapObjectLoader.saveNodeLinks(storableObjects, force);
				break;
			case ObjectEntities.MARK_CODE:
				mapObjectLoader.saveMarks(storableObjects, force);
				break;
			case ObjectEntities.PHYSICALLINK_CODE:
				mapObjectLoader.savePhysicalLinks(storableObjects, force);
				break;
			case ObjectEntities.COLLECTOR_CODE:
				mapObjectLoader.saveCollectors(storableObjects, force);
				break;
			case ObjectEntities.MAP_CODE:
				mapObjectLoader.saveMaps(storableObjects, force);
				break;
			default:
				Log.errorMessage("MapStorableObjectPool.saveStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	protected void deleteStorableObjects(final Set identifiables) {
		mapObjectLoader.delete(identifiables);
	}

}
