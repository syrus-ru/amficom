/*
 * $Id: MCMObjectLoader.java,v 1.16 2005/07/18 12:42:20 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.16 $, $Date: 2005/07/18 12:42:20 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
abstract class MCMObjectLoader extends CORBAObjectLoader {

	public MCMObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager);
	}

	final void loadStorableObjectsWithDependencies(final Map<Integer, Map<Short, Set<StorableObject>>> loadObjectsMap,
			final int dependencyLevel,
			final short entityCode,
			final Set<Identifier> ids) throws ApplicationException {
		final TransmitProcedure transmitProcedure = TransmitProcedureFactory.getProcedure(entityCode);
		final Set<StorableObject> loadedObjects = super.loadStorableObjects(entityCode, ids, transmitProcedure);
		if (loadedObjects.isEmpty()) {
			return;
		}

		final Integer dependencyKey = new Integer(-dependencyLevel);
		Map<Short, Set<StorableObject>> levelLoadObjectsMap = loadObjectsMap.get(dependencyKey);
		if (levelLoadObjectsMap == null) {
			levelLoadObjectsMap = new HashMap<Short, Set<StorableObject>>();
			loadObjectsMap.put(dependencyKey, levelLoadObjectsMap);
		}
		final Short entityKey1 = new Short(entityCode);
		Set<StorableObject> entityLevelLoadObjects = levelLoadObjectsMap.get(entityKey1);
		if (entityLevelLoadObjects == null) {
			entityLevelLoadObjects = new HashSet<StorableObject>();
			levelLoadObjectsMap.put(entityKey1, entityLevelLoadObjects);
		}
		entityLevelLoadObjects.addAll(loadedObjects);

		final Map<Short, Set<Identifier>> missingDependencesMap = createMissingDependenciesMap(loadedObjects);
		for (final Short entityKey : missingDependencesMap.keySet()) {
			this.loadStorableObjectsWithDependencies(loadObjectsMap,
					dependencyLevel + 1,
					entityKey.shortValue(),
					missingDependencesMap.get(entityKey));
		}

	}

	protected final Set loadStorableObjects(final short entityCode, final Set<Identifier> ids) throws ApplicationException {
		final Set<StorableObject> objects = DatabaseObjectLoader.loadStorableObjects(ids);
		final Set<Identifier> loadIds = Identifier.createSubtractionIdentifiers(ids, objects);
		if (loadIds.isEmpty()) {
			return objects;
		}

		final Map<Integer, Map<Short, Set<StorableObject>>> loadObjectsMap = new HashMap<Integer, Map<Short, Set<StorableObject>>>();
		this.loadStorableObjectsWithDependencies(loadObjectsMap, 0, entityCode, loadIds);
		if (loadObjectsMap.isEmpty()) {
			return objects;
		}

		final Set<StorableObject> loadedObjects = loadObjectsMap.get(new Integer(0)).get(new Short(entityCode));
		if (loadedObjects.isEmpty()) {
			return objects;
		}
		assert StorableObject.getEntityCodeOfIdentifiables(loadedObjects) == entityCode : ErrorMessages.ILLEGAL_ENTITY_CODE;
		objects.addAll(loadedObjects);

		insertWithDependencies(loadObjectsMap);

		return objects;
	}

	protected final Set loadStorableObjectsButIdsByCondition(final short entityCode,
			final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		final Set<StorableObject> objects = DatabaseObjectLoader.loadStorableObjectsButIdsByCondition(condition, ids);
		final Set<Identifier> loadButIds = Identifier.createSumIdentifiers(ids, objects);

		final TransmitButIdsByConditionProcedure transmitButIdsByConditionProcedure = TransmitButIdsByConditionProcedureFactory.getProcedure(entityCode);
		final Set<StorableObject> loadedObjects = super.loadStorableObjectsButIdsByCondition(entityCode,
				loadButIds,
				condition,
				transmitButIdsByConditionProcedure);
		if (loadedObjects.isEmpty()) {
			return objects;
		}
		objects.addAll(loadedObjects);

		final Map<Integer, Map<Short, Set<StorableObject>>> loadObjectsMap = new HashMap<Integer, Map<Short, Set<StorableObject>>>();
		final Map<Short, Set<StorableObject>> levelLoadObjectsMap = new HashMap<Short, Set<StorableObject>>(1);
		levelLoadObjectsMap.put(new Short(entityCode), loadedObjects);
		loadObjectsMap.put(new Integer(0), levelLoadObjectsMap);

		final Map<Short, Set<Identifier>> missingDependencesMap = createMissingDependenciesMap(loadedObjects);
		for (final Short entityKey : missingDependencesMap.keySet()) {
			this.loadStorableObjectsWithDependencies(loadObjectsMap,
					1,
					entityKey.shortValue(),
					missingDependencesMap.get(entityKey));
		}

		insertWithDependencies(loadObjectsMap);

		return objects;
	}

	private static Map<Short, Set<Identifier>> createMissingDependenciesMap(final Set<StorableObject> storableObjects) {
		final Map<Short, Set<Identifier>> missingDependencesMap = new HashMap<Short, Set<Identifier>>();
		for (final StorableObject storableObject : storableObjects) {
			final Set<Identifiable> dependencies = storableObject.getDependencies();
			for (final Identifiable dependency : dependencies) {
				final Identifier idDependency = dependency.getId();
				boolean needLoad = true;
				try {
					needLoad = !StorableObjectDatabase.isPresentInDatabase(dependency.getId());
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
				if (needLoad) {
					final Short entityKey = new Short(idDependency.getMajor());
					Set<Identifier> loadObjectIds = missingDependencesMap.get(entityKey);
					if (loadObjectIds == null) {
						loadObjectIds = new HashSet<Identifier>();
						missingDependencesMap.put(entityKey, loadObjectIds);
					}
					loadObjectIds.add(idDependency);
				}
			}
		}
		return missingDependencesMap;
	}

	private static void insertWithDependencies(final Map<Integer, Map<Short, Set<StorableObject>>> dependenciesMap) {
		for (final Integer dependencyKey : dependenciesMap.keySet()) {
			final Map<Short, Set<StorableObject>> levelLoadObjectsMap = dependenciesMap.get(dependencyKey);
			for (final Short entityKey : levelLoadObjectsMap.keySet()) {
				final Set<StorableObject> entityLevelLoadObjects = levelLoadObjectsMap.get(entityKey);
				try {
					final StorableObjectDatabase database = DatabaseContext.getDatabase(entityKey);
					database.insert(entityLevelLoadObjects);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
			}
		}
	}

	/**
	 * This method does <em>not</em> override
	 * {@link CORBAObjectLoader#saveStorableObjects(short, Set, ReceiveProcedure)}
	 * since it has an extra argument, <code>final boolean force</code>,
	 * unnecessary in super implementation.
	 */
	protected final void saveStorableObjects(final short entityCode,
			final Set<? extends StorableObject> storableObjects,
			final boolean force,
			final ReceiveProcedure receiveProcedure) throws ApplicationException {
		DatabaseObjectLoader.saveStorableObjects(storableObjects, force);

		super.saveStorableObjects(entityCode, storableObjects, receiveProcedure);
	}

	/**
	 * Currently not need to implement this method
	 * @todo Using this method load objects, changed on server relatively to MCM
	 */
	@Override
	public Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException {
		assert false : storableObjects;
		return null;
	}

	/**
	 * Currently not need to implement this method
	 */
	@Override
	public void delete(final Set<? extends Identifiable> identifiables) {
		assert false : identifiables;
	}
}
