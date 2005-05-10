/*
 * $Id: SchemeStorableObjectPool.java,v 1.19 2005/05/10 19:25:04 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.19 $, $Date: 2005/05/10 19:25:04 $
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


	private SchemeStorableObjectPool() {
		this(LRUMap.class);
	}

	private SchemeStorableObjectPool(final Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.SCHEME_GROUP_CODE, cacheMapClass);
		registerPool(ObjectGroupEntities.SCHEME_GROUP_CODE, this);
	}

	public static void init(final SchemeObjectLoader schemeObjectLoader1, final int size) {
		if (instance == null)
			instance = new SchemeStorableObjectPool();

		schemeObjectLoader = schemeObjectLoader1;

		instance.addObjectPool(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_DEVICE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_PORT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_LINK_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SCHEME_PATH_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.PATH_ELEMENT_ENTITY_CODE, size);
	}

	public static void init(final SchemeObjectLoader schemeObjectLoader1) {
		if (instance == null)
			instance = new SchemeStorableObjectPool();

		schemeObjectLoader = schemeObjectLoader1;

		instance.addObjectPool(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE, SCHEME_PROTO_GROUP_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE, SCHEME_PROTO_ELEMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_ENTITY_CODE, SCHEME_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE, SCHEME_ELEMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE, SCHEME_OPTIMIZE_INFO_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE, SCHEME_MONITORING_SOLUTION_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_DEVICE_ENTITY_CODE, SCHEME_DEVICE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_PORT_ENTITY_CODE, SCHEME_PORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE, SCHEME_CABLE_PORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_LINK_ENTITY_CODE, SCHEME_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE, SCHEME_CABLE_LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE, SCHEME_CABLE_THREAD_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE, CABLE_CHANNELING_ITEM_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SCHEME_PATH_ENTITY_CODE, SCHEME_PATH_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PATH_ELEMENT_ENTITY_CODE, PATH_ELEMENT_OBJECT_POOL_SIZE);
	}

	/**
	 * @param schemeObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final SchemeObjectLoader schemeObjectLoader1, final Class cacheClass, final int size) {
		final String cacheClassName = cacheClass.getName();
		try {
			instance = new SchemeStorableObjectPool(Class.forName(cacheClassName));
		} catch (final ClassNotFoundException cnfe) {
			Log.errorMessage("Cache class '" + cacheClassName //$NON-NLS-1$
					+ "' cannot be found, using default"); //$NON-NLS-1$
			instance = new SchemeStorableObjectPool();
		}
		init(schemeObjectLoader1, size);
	}

	public static void init(final SchemeObjectLoader schemeObjectLoader1, final Class cacheClass) {
		final String cacheClassName = cacheClass.getName();
		try {
			instance = new SchemeStorableObjectPool(Class.forName(cacheClassName));
		} catch (final ClassNotFoundException cnfe) {
			Log.errorMessage("Cache class '" + cacheClassName //$NON-NLS-1$
					+ "' cannot be found, using default"); //$NON-NLS-1$
			instance = new SchemeStorableObjectPool();
		}
		init(schemeObjectLoader1);
	}

	public static void refresh() throws ApplicationException {
		instance.refreshImpl();
	}

	/**
	 * @param storableObjects
	 * @throws ApplicationException 
	 */
	protected Set refreshStorableObjects(final Set storableObjects) throws ApplicationException {
		return schemeObjectLoader.refresh(storableObjects);
	}

	public static StorableObject getStorableObject(final Identifier id, final boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectImpl(id, useLoader);
	}

	public static Set getStorableObjects(final Set ids, final boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsImpl(ids, useLoader);
	}

	public static Set getStorableObjectsByCondition(
			final StorableObjectCondition storableObjectCondition,
			final boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(storableObjectCondition, useLoader);
	}

	public static Set getStorableObjectsByConditionButIds(final Set ids,
			final StorableObjectCondition storableObjectCondition,
			final boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, storableObjectCondition, useLoader);
	}

	/**
	 * @param id
	 * @throws ApplicationException
	 */
	protected StorableObject loadStorableObject(final Identifier id) throws ApplicationException {
		switch (id.getMajor()) {
			case ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeProtoGroup(id);
			case ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeProtoElement(id);
			case ObjectEntities.SCHEME_ENTITY_CODE:
				return schemeObjectLoader.loadScheme(id);
			case ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeElement(id);
			case ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfo(id);
			case ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeMonitoringSolution(id);
			case ObjectEntities.SCHEME_DEVICE_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeDevice(id);
			case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemePort(id);
			case ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeCablePort(id);
			case ObjectEntities.SCHEME_LINK_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeLink(id);
			case ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeCableLink(id);
			case ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeCableThread(id);
			case ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE:
				return schemeObjectLoader.loadCableChannelingItem(id);
			case ObjectEntities.SCHEME_PATH_ENTITY_CODE:
				return schemeObjectLoader.loadSchemePath(id);
			case ObjectEntities.PATH_ELEMENT_ENTITY_CODE:
				return schemeObjectLoader.loadPathElement(id);
			default:
				final short entityCode = id.getMajor();
				Log.errorMessage("SchemeStorableObjectPool.loadStorableObject | Unknown entity: " //$NON-NLS-1$
						+ ObjectEntities.codeToString(entityCode)
						+ " (" + entityCode + ')'); //$NON-NLS-1$
				return null;
		}
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see StorableObjectPool#loadStorableObjects(Set)
	 */
	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeProtoGroups(ids);
			case ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeProtoElements(ids);
			case ObjectEntities.SCHEME_ENTITY_CODE:
				return schemeObjectLoader.loadSchemes(ids);
			case ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeElements(ids);
			case ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfos(ids);
			case ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeMonitoringSolutions(ids);
			case ObjectEntities.SCHEME_DEVICE_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeDevices(ids);
			case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemePorts(ids);
			case ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeCablePorts(ids);
			case ObjectEntities.SCHEME_LINK_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeLinks(ids);
			case ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeCableLinks(ids);
			case ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeCableThreads(ids);
			case ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE:
				return schemeObjectLoader.loadCableChannelingItems(ids);
			case ObjectEntities.SCHEME_PATH_ENTITY_CODE:
				return schemeObjectLoader.loadSchemePaths(ids);
			case ObjectEntities.PATH_ELEMENT_ENTITY_CODE:
				return schemeObjectLoader.loadPathElements(ids);
			default:
				Log.errorMessage("SchemeStorableObjectPool.loadStorableObjects | Unknown entity: " //$NON-NLS-1$
						+ ObjectEntities.codeToString(entityCode)
						+ " (" + entityCode + ')'); //$NON-NLS-1$
				return Collections.EMPTY_SET;
		}
	}

	/**
	 * @param storableObjectCondition
	 * @param ids
	 * @throws ApplicationException
	 */
	protected Set loadStorableObjectsButIds(
			final StorableObjectCondition storableObjectCondition,
			final Set ids)
			throws ApplicationException {
		final short entityCode = storableObjectCondition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeProtoGroupsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeProtoElementsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_ENTITY_CODE:
				return schemeObjectLoader.loadSchemesButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeElementsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeOptimizeInfosButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeMonitoringSolutionsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_DEVICE_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeDevicesButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemePortsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeCablePortsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_LINK_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeLinksButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeCableLinksButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE:
				return schemeObjectLoader.loadSchemeCableThreadsButIds(storableObjectCondition, ids);
			case ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE:
				return schemeObjectLoader.loadCableChannelingItemsButIds(storableObjectCondition, ids);
			case ObjectEntities.SCHEME_PATH_ENTITY_CODE:
				return schemeObjectLoader.loadSchemePathsButIds(storableObjectCondition, ids);
			case ObjectEntities.PATH_ELEMENT_ENTITY_CODE:
				return schemeObjectLoader.loadPathElementsButIds(storableObjectCondition, ids);
			default:
				Log.errorMessage("SchemeStorableObjectPool.loadStorableObjectsButIds | Unknown entity: " //$NON-NLS-1$
						+ ObjectEntities.codeToString(entityCode)
						+ " (" + entityCode + ')'); //$NON-NLS-1$
				return Collections.EMPTY_SET;
		}
	}

	/**
	 * @param storableObjects
	 * @param force
	 * @throws ApplicationException
	 * @see StorableObjectPool#saveStorableObjects(Set, boolean)
	 */
	protected void saveStorableObjects(final Set storableObjects,
			final boolean force)
			throws ApplicationException {
		if (storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final boolean singleton = storableObjects.size() == 1;
		switch (entityCode) {
			case ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemeProtoGroup((SchemeProtoGroup) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemeProtoGroups(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemeProtoElement((SchemeProtoElement) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemeProtoElements(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveScheme((Scheme) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemes(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemeElement((SchemeElement) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemeElements(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemeOptimizeInfo((SchemeOptimizeInfo) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemeOptimizeInfos(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemeMonitoringSolution((SchemeMonitoringSolution) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemeMonitoringSolutions(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_DEVICE_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemeDevice((SchemeDevice) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemeDevices(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemePort((SchemePort) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemePorts(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemeCablePort((SchemeCablePort) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemeCablePorts(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_LINK_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemeLink((SchemeLink) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemeLinks(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemeCableLink((SchemeCableLink) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemeCableLinks(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemeCableThread((SchemeCableThread) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemeCableThreads(storableObjects, force);
				break;
			case ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveCableChannelingItem((CableChannelingItem) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveCableChannelingItems(storableObjects, force);
				break;
			case ObjectEntities.SCHEME_PATH_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.saveSchemePath((SchemePath) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.saveSchemePaths(storableObjects, force);
				break;
			case ObjectEntities.PATH_ELEMENT_ENTITY_CODE:
				if (singleton)
					schemeObjectLoader.savePathElement((PathElement) storableObjects.iterator().next(), force);
				else
					schemeObjectLoader.savePathElements(storableObjects, force);
				break;
			default:
				Log.errorMessage("SchemeStorableObjectPool.saveStorableObjects | Unknown entity: " //$NON-NLS-1$
						+ ObjectEntities.codeToString(entityCode)
						+ " (" + entityCode + ')'); //$NON-NLS-1$
		}
	}

	public static StorableObject putStorableObject(final StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static StorableObject fromTransferable(final Identifier id, final IDLEntity transferable) throws ApplicationException {
		return instance.fromTransferableImpl(id, transferable);
	}

	public static void flush(final Identifier id, final boolean force) throws ApplicationException {
		instance.flushImpl(id, force);
	}

	public static void flush(final short entityCode, final boolean force) throws ApplicationException {		 
		instance.flushImpl(entityCode, force);
	}

	public static void flush(final Short entityCode, final boolean force) throws ApplicationException {		 
		instance.flushImpl(entityCode, force);
	}

	public static void flush(final boolean force) throws ApplicationException {		 
		instance.flushImpl(force);
	}

	public static void cleanChangedStorableObject(final Short entityCode) {
		instance.cleanChangedStorableObjectImpl(entityCode);
	}

	public static void cleanChangedStorableObjects() {
		instance.cleanChangedStorableObjectsImpl();
	}

	public static void delete(final Identifier id) {
		instance.deleteImpl(id);
	}

	public static void delete(final Set identifiables) {
		instance.deleteImpl(identifiables);
	}

	/**
	 * @param id
	 */
	protected void deleteStorableObject(final Identifier id) {
		schemeObjectLoader.delete(id);
	}

	/**
	 * @param identifiables
	 * @see StorableObjectPool#deleteStorableObjects(Set)
	 */
	protected void deleteStorableObjects(final Set identifiables) {
		schemeObjectLoader.delete(identifiables);
	}

	public static void deserializePool() {
		instance.deserializePoolImpl();
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

	public static void truncateObjectPool(final short entityCode) {
		instance.truncateObjectPoolImpl(entityCode);
	}
}
