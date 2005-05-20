/*
 * $Id: MapStorableObjectPool.java,v 1.22 2005/05/20 21:11:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.22 $, $Date: 2005/05/20 21:11:56 $
 * @author $Author: arseniy $
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


	private MapStorableObjectPool() {
		this(LRUMap.class);
	}

	private MapStorableObjectPool(final Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.MAP_GROUP_CODE, cacheMapClass);
	}

	public static void init(final MapObjectLoader mapObjectLoader1, final int size) {
		if (instance == null)
			instance = new MapStorableObjectPool();

		mapObjectLoader = mapObjectLoader1;

		instance.addObjectPool(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SITE_NODE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.NODE_LINK_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MARK_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.COLLECTOR_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MAP_ENTITY_CODE, size);
	}

	public static void init(final MapObjectLoader mapObjectLoader1) {
		if (instance == null)
			instance = new MapStorableObjectPool();

		mapObjectLoader = mapObjectLoader1;

		instance.addObjectPool(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE, SITE_NODE_TYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE, PHYSICAL_LINK_TYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SITE_NODE_ENTITY_CODE, SITE_NODE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, TOPOLOGICAL_NODE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.NODE_LINK_ENTITY_CODE, NODE_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MARK_ENTITY_CODE, MARK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, PHYSICAL_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.COLLECTOR_ENTITY_CODE, COLLECTOR_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MAP_ENTITY_CODE, MAP_OBJECT_POOL_SIZE);
	}

	/**
	 * @param mapObjectLoader1
	 * @param cacheClass
	 *          class must extend LRUMap
	 * @param size
	 */
	public static void init(final MapObjectLoader mapObjectLoader1, final Class cacheClass, final int size) {
		final String cacheClassName = cacheClass.getName();
		try {
			instance = new MapStorableObjectPool(Class.forName(cacheClassName));
		}
		catch (final ClassNotFoundException cnfe) {
			Log.errorMessage("Cache class '" + cacheClassName + "' cannot be found, using default");
			instance = new MapStorableObjectPool();
		}
		init(mapObjectLoader1, size);
	}

	public static void init(final MapObjectLoader mapObjectLoader1, final Class cacheClass) {
		final String cacheClassName = cacheClass.getName();
		try {
			instance = new MapStorableObjectPool(Class.forName(cacheClassName));
		}
		catch (final ClassNotFoundException cnfe) {
			Log.errorMessage("Cache class '" + cacheClassName + "' cannot be found, using default");
			instance = new MapStorableObjectPool();
		}
		init(mapObjectLoader1);
	}

	protected Set refreshStorableObjects(final Set storableObjects) throws ApplicationException {
		return mapObjectLoader.refresh(storableObjects);
	}

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE:
				return mapObjectLoader.loadSiteNodeTypes(ids);
			case ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE:
				return mapObjectLoader.loadPhysicalLinkTypes(ids);
			case ObjectEntities.SITE_NODE_ENTITY_CODE:
				return mapObjectLoader.loadSiteNodes(ids);
			case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
				return mapObjectLoader.loadTopologicalNodes(ids);
			case ObjectEntities.NODE_LINK_ENTITY_CODE:
				return mapObjectLoader.loadNodeLinks(ids);
			case ObjectEntities.MARK_ENTITY_CODE:
				return mapObjectLoader.loadMarks(ids);
			case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
				return mapObjectLoader.loadPhysicalLinks(ids);
			case ObjectEntities.COLLECTOR_ENTITY_CODE:
				return mapObjectLoader.loadCollectors(ids);
			case ObjectEntities.MAP_ENTITY_CODE:
				return mapObjectLoader.loadMaps(ids);
			default:
				Log.errorMessage("MapStorableObjectPool.loadStorableObjects | Unknown entity: "
						+ ObjectEntities.codeToString(entityCode)
						+ " (" + entityCode + ')');
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(
			final StorableObjectCondition storableObjectCondition,
			final Set ids)
			throws ApplicationException {
		final short entityCode = storableObjectCondition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE:
				return mapObjectLoader.loadSiteNodeTypesButIds(storableObjectCondition, ids);
			case ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE:
				return mapObjectLoader.loadPhysicalLinkTypesButIds(storableObjectCondition, ids);
			case ObjectEntities.SITE_NODE_ENTITY_CODE:
				return mapObjectLoader.loadSiteNodesButIds(storableObjectCondition, ids);
			case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
				return mapObjectLoader.loadTopologicalNodesButIds(storableObjectCondition, ids);
			case ObjectEntities.NODE_LINK_ENTITY_CODE:
				return mapObjectLoader.loadNodeLinksButIds(storableObjectCondition, ids);
			case ObjectEntities.MARK_ENTITY_CODE:
				return mapObjectLoader.loadMarksButIds(storableObjectCondition, ids);
			case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
				return mapObjectLoader.loadPhysicalLinksButIds(storableObjectCondition, ids);
			case ObjectEntities.COLLECTOR_ENTITY_CODE:
				return mapObjectLoader.loadCollectorsButIds(storableObjectCondition, ids);
			case ObjectEntities.MAP_ENTITY_CODE:
				return mapObjectLoader.loadMapsButIds(storableObjectCondition, ids);
			default:
				Log.errorMessage("MapStorableObjectPool.loadStorableObjectsButIds | Unknown entity: "
						+ ObjectEntities.codeToString(entityCode)
						+ " (" + entityCode + ')');
				return Collections.EMPTY_SET;
		}
	}

	protected void saveStorableObjects(final Set storableObjects,
			final boolean force)
			throws ApplicationException {
		if (storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final boolean singleton = storableObjects.size() == 1;
		switch (entityCode) {				
			case ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE:
				if (singleton)
					mapObjectLoader.saveSiteNodeType((SiteNodeType)storableObjects.iterator().next(), force);
				else
					mapObjectLoader.saveSiteNodeTypes(storableObjects, force);
				break;
			case ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE:
				if (singleton)
					mapObjectLoader.savePhysicalLinkType((PhysicalLinkType) storableObjects.iterator().next(), force);
				else
					mapObjectLoader.savePhysicalLinkTypes(storableObjects, force);
				break;
			case ObjectEntities.SITE_NODE_ENTITY_CODE:
				if (singleton)
					mapObjectLoader.saveSiteNode((SiteNode)storableObjects.iterator().next(), force);
				else
					mapObjectLoader.saveSiteNodes(storableObjects, force);
				break;
			case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
				if (singleton)
					mapObjectLoader.saveTopologicalNode((TopologicalNode) storableObjects.iterator().next(), force);
				else
					mapObjectLoader.saveTopologicalNodes(storableObjects, force);
				break;
			case ObjectEntities.NODE_LINK_ENTITY_CODE:
				if (singleton)
					mapObjectLoader.saveNodeLink((NodeLink)storableObjects.iterator().next(), force);
				else
					mapObjectLoader.saveNodeLinks(storableObjects, force);
				break;
			case ObjectEntities.MARK_ENTITY_CODE:
				if (singleton)
					mapObjectLoader.saveMark((Mark)storableObjects.iterator().next(), force);
				else
					mapObjectLoader.saveMarks(storableObjects, force);
				break;
			case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
				if (singleton)
					mapObjectLoader.savePhysicalLink((PhysicalLink)storableObjects.iterator().next(), force);
				else
					mapObjectLoader.savePhysicalLinks(storableObjects, force);
				break;
			case ObjectEntities.COLLECTOR_ENTITY_CODE:
				if (singleton)
					mapObjectLoader.saveCollector((Collector)storableObjects.iterator().next(), force);
				else
					mapObjectLoader.saveCollectors(storableObjects, force);
				break;
			case ObjectEntities.MAP_ENTITY_CODE:
				if (singleton)
					mapObjectLoader.saveMap((Map)storableObjects.iterator().next(), force);
				else
					mapObjectLoader.saveMaps(storableObjects, force);
				break;
			default:
				Log.errorMessage("MapStorableObjectPool.saveStorableObjects | Unknown entity: "
						+ ObjectEntities.codeToString(entityCode)
						+ " (" + entityCode + ')');
		}
	}

	protected void deleteStorableObjects(final Set identifiables) {
		mapObjectLoader.delete(identifiables);
	}

}
