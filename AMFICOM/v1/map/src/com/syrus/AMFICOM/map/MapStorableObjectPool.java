/*
 * $Id: MapStorableObjectPool.java,v 1.10 2005/04/01 11:11:05 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2005/04/01 11:11:05 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public final class MapStorableObjectPool extends StorableObjectPool {

	private static final int				OBJECT_POOL_MAP_SIZE				= 50;		/*
																							 * Number
																							 * of
																							 * entities
																							 */

	private static final int				SITE_NODE_OBJECT_POOL_SIZE			= 200;
	private static final int				TOPOLOGICAL_NODE_OBJECT_POOL_SIZE	= 200;
	private static final int				NODE_LINK_OBJECT_POOL_SIZE			= 100;
	private static final int				MARK_OBJECT_POOL_SIZE				= 50;
	private static final int				PHYSICAL_LINK_OBJECT_POOL_SIZE		= 100;
	private static final int				COLLECTOR_OBJECT_POOL_SIZE			= 50;
	private static final int				MAP_OBJECT_POOL_SIZE				= 3;

	private static final short				SITE_NODE_TYPE_OBJECT_POOL_SIZE			= 10;
	private static final short				PHYSICAL_LINK_TYPE_OBJECT_POOL_SIZE		= 10;

	private static MapObjectLoader			mObjectLoader;
	private static MapStorableObjectPool	instance;

	private MapStorableObjectPool() {
		super(ObjectGroupEntities.MAP_GROUP_CODE);
	}

	private MapStorableObjectPool(Class cacheMapClass) {
		super(ObjectGroupEntities.MAP_GROUP_CODE, cacheMapClass);
	}

	/**
	 * 
	 * @param mObjectLoader1
	 * @param cacheClass
	 *            class must extend LRUMap
	 * @param size
	 */
	public static void init(MapObjectLoader mObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new MapStorableObjectPool(clazz);
		} catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, use default '"
					+ ((clazz == null) ? "null" : clazz.getName()) + "'");
		}
		init(mObjectLoader1, size);
	}

	public static void init(MapObjectLoader mObjectLoader1, final int size) {
		if (instance == null)
			instance = new MapStorableObjectPool();
		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(size));

		mObjectLoader = mObjectLoader1;

		instance.addObjectPool(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SITE_NODE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.NODE_LINK_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MARK_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.COLLECTOR_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MAP_ENTITY_CODE, size);

		instance.populatePools();
	}

	public static void init(MapObjectLoader mObjectLoader1) {
		if (instance == null)
			instance = new MapStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));
		mObjectLoader = mObjectLoader1;

		instance.addObjectPool(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE, SITE_NODE_TYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE, PHYSICAL_LINK_TYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SITE_NODE_ENTITY_CODE, SITE_NODE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, TOPOLOGICAL_NODE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.NODE_LINK_ENTITY_CODE, NODE_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MARK_ENTITY_CODE, MARK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, PHYSICAL_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.COLLECTOR_ENTITY_CODE, COLLECTOR_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MAP_ENTITY_CODE, MAP_OBJECT_POOL_SIZE);

		instance.populatePools();
	}

	public static void refresh() throws ApplicationException {
		instance.refreshImpl();
	}

	protected java.util.Set refreshStorableObjects(java.util.Set storableObjects) throws ApplicationException {
		return mObjectLoader.refresh(storableObjects);
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}

	public static Set getStorableObjects(Set objectIds, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static Set getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static Set getStorableObjectsByConditionButIds(	Set ids,
															StorableObjectCondition condition,
															boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws ApplicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadSiteNodeType(objectId);
				break;
			case ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadPhysicalLinkType(objectId);
				break;
			case ObjectEntities.SITE_NODE_ENTITY_CODE:
				storableObject = mObjectLoader.loadSiteNode(objectId);
				break;
			case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
				storableObject = mObjectLoader.loadTopologicalNode(objectId);
				break;
			case ObjectEntities.NODE_LINK_ENTITY_CODE:
				storableObject = mObjectLoader.loadNodeLink(objectId);
				break;
			case ObjectEntities.MARK_ENTITY_CODE:
				storableObject = mObjectLoader.loadMark(objectId);
				break;
			case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
				storableObject = mObjectLoader.loadPhysicalLink(objectId);
				break;
			case ObjectEntities.COLLECTOR_ENTITY_CODE:
				storableObject = mObjectLoader.loadCollector(objectId);
				break;
			case ObjectEntities.MAP_ENTITY_CODE:
				storableObject = mObjectLoader.loadMap(objectId);
				break;		
			default:
				Log.errorMessage("MapStorableObjectPool.loadStorableObject | Unknown entity: "
						+ ObjectEntities.codeToString(objectId.getMajor()));
				storableObject = null;
		}
		return storableObject;
	}

	protected Set loadStorableObjects(Short entityCode, Set ids) throws ApplicationException {
		Set storableObjects;
		switch (entityCode.shortValue()) {
			case ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE:
				storableObjects = mObjectLoader.loadSiteNodeTypes(ids);
				break;
			case ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE:
				storableObjects = mObjectLoader.loadPhysicalLinkTypes(ids);
				break;
			case ObjectEntities.SITE_NODE_ENTITY_CODE:
				storableObjects = mObjectLoader.loadSiteNodes(ids);
				break;
			case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
				storableObjects = mObjectLoader.loadTopologicalNodes(ids);
				break;
			case ObjectEntities.NODE_LINK_ENTITY_CODE:
				storableObjects = mObjectLoader.loadNodeLinks(ids);
				break;
			case ObjectEntities.MARK_ENTITY_CODE:
				storableObjects = mObjectLoader.loadMarks(ids);
				break;
			case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
				storableObjects = mObjectLoader.loadPhysicalLinks(ids);
				break;
			case ObjectEntities.COLLECTOR_ENTITY_CODE:
				storableObjects = mObjectLoader.loadCollectors(ids);
				break;
			case ObjectEntities.MAP_ENTITY_CODE:
				storableObjects = mObjectLoader.loadMaps(ids);
				break;
			default:
				Log.errorMessage("MapStorableObjectPool.loadStorableObjects | Unknown entityCode : " + entityCode);
				storableObjects = null;
		}
		return storableObjects;
	}

	protected Set loadStorableObjectsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		Set loadedCollection = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE:
				loadedCollection  = mObjectLoader.loadSiteNodeTypesButIds(condition, ids);
				break;
			case ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE:
				loadedCollection  = mObjectLoader.loadPhysicalLinkTypesButIds(condition, ids);
				break;
			case ObjectEntities.SITE_NODE_ENTITY_CODE:
				loadedCollection  = mObjectLoader.loadSiteNodesButIds(condition, ids);
				break;
			case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
				loadedCollection  = mObjectLoader.loadTopologicalNodesButIds(condition, ids);
				break;
			case ObjectEntities.NODE_LINK_ENTITY_CODE:
				loadedCollection  = mObjectLoader.loadNodeLinksButIds(condition, ids);
				break;
			case ObjectEntities.MARK_ENTITY_CODE:
				loadedCollection  = mObjectLoader.loadMarksButIds(condition, ids);
				break;
			case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
				loadedCollection  = mObjectLoader.loadPhysicalLinksButIds(condition, ids);
				break;
			case ObjectEntities.COLLECTOR_ENTITY_CODE:
				loadedCollection  = mObjectLoader.loadCollectorsButIds(condition, ids);
				break;
			case ObjectEntities.MAP_ENTITY_CODE:
				loadedCollection  = mObjectLoader.loadMapsButIds(condition, ids);
				break;
			default:
				Log.errorMessage("MapStorableObjectPool.loadStorableObjectsButIds | Unknown entity: "
						+ ObjectEntities.codeToString(entityCode));
				loadedCollection = null;
		}
		return loadedCollection;
	}

	protected void saveStorableObjects(short code, Set list, boolean force) throws ApplicationException {
		if (!list.isEmpty()) {
			boolean alone = (list.size() == 1);

			switch (code) {				
				case ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveSiteNodeType((SiteNodeType)list.iterator().next(), force);
					else
						mObjectLoader.saveSiteNodeTypes(list, force);
					break;
				case ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE:
					if (alone)
						mObjectLoader.savePhysicalLinkType((PhysicalLinkType) list.iterator().next(), force);
					else
						mObjectLoader.savePhysicalLinkTypes(list, force);
					break;
				case ObjectEntities.SITE_NODE_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveSiteNode((SiteNode)list.iterator().next(), force);
					else
						mObjectLoader.saveSiteNodes(list, force);
					break;
				case ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveTopologicalNode((TopologicalNode) list.iterator().next(), force);
					else
						mObjectLoader.saveTopologicalNodes(list, force);
					break;
				case ObjectEntities.NODE_LINK_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveNodeLink((NodeLink)list.iterator().next(), force);
					else
						mObjectLoader.saveNodeLinks(list, force);
					break;
				case ObjectEntities.MARK_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveMark((Mark)list.iterator().next(), force);
					else
						mObjectLoader.saveMarks(list, force);
					break;
				case ObjectEntities.PHYSICAL_LINK_ENTITY_CODE:
					if (alone)
						mObjectLoader.savePhysicalLink((PhysicalLink)list.iterator().next(), force);
					else
						mObjectLoader.savePhysicalLinks(list, force);
					break;
				case ObjectEntities.COLLECTOR_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveCollector((Collector)list.iterator().next(), force);
					else
						mObjectLoader.saveCollectors(list, force);
					break;
				case ObjectEntities.MAP_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveMap((Map)list.iterator().next(), force);
					else
						mObjectLoader.saveMaps(list, force);
					break;
				default:
					Log.errorMessage("MapStorableObjectPool.saveStorableObjects | Unknown Unknown entity : '"
							+ ObjectEntities.codeToString(code) + "'");
			}

		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static void flush(boolean force) throws ApplicationException {
		instance.flushImpl(force);
	}

	public static void cleanChangedStorableObject(Short entityCode) {
		instance.cleanChangedStorableObjectImpl(entityCode);
	}

	public static void cleanChangedStorableObjects() {
		instance.cleanChangedStorableObjectsImpl();
	}

	protected void deleteStorableObject(Identifier id) throws IllegalDataException {
	 	mObjectLoader.delete(id);
	}

	protected void deleteStorableObjects(Set ids) throws IllegalDataException {
		mObjectLoader.delete(ids);
	}

	public static void delete(Identifier id) {
		instance.deleteImpl(id);
	}

	public static void delete(Set ids) throws IllegalDataException {
		instance.deleteImpl(ids);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}
}
