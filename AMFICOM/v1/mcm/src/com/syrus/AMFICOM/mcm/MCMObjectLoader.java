/*
 * $Id: MCMObjectLoader.java,v 1.23 2005/09/21 09:58:26 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.RESULT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.23 $, $Date: 2005/09/21 09:58:26 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class MCMObjectLoader extends CORBAObjectLoader {
	private DatabaseObjectLoader databaseObjectLoader;

	public MCMObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager);
		this.databaseObjectLoader = new DatabaseObjectLoader();
	}

	@Override
	public <T extends StorableObject> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case RESULT_CODE:
				return this.databaseObjectLoader.loadStorableObjects(ids);
			default:
				return this.loadStorableObjectsCustom(ids);
		}
	}

	@Override
	public final <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case RESULT_CODE:
				return this.databaseObjectLoader.loadStorableObjectsButIdsByCondition(ids, condition);
			default:
				return this.loadStorableObjectsButIdsByConditionCustom(ids, condition);
		}
	}

	private final <T extends StorableObject> Set<T> loadStorableObjectsCustom(final Set<Identifier> ids) throws ApplicationException {
		final Set<T> objects = this.databaseObjectLoader.loadStorableObjects(ids);
		final Set<Identifier> loadIds = Identifier.createSubtractionIdentifiers(ids, objects);
		if (loadIds.isEmpty()) {
			return objects;
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		final SortedMap<Integer, Map<Short, Set<StorableObject>>> loadObjectsMap = new TreeMap<Integer, Map<Short, Set<StorableObject>>>();
		this.loadStorableObjectsWithDependencies(loadObjectsMap, 0, loadIds);
		if (loadObjectsMap.isEmpty()) {
			return objects;
		}

		final Set<T> loadedObjects = (Set<T>) loadObjectsMap.get(new Integer(0)).get(new Short(entityCode));
		if (loadedObjects.isEmpty()) {
			return objects;
		}
		assert StorableObject.getEntityCodeOfIdentifiables(loadedObjects) == entityCode : ErrorMessages.ILLEGAL_ENTITY_CODE;
		objects.addAll(loadedObjects);

		this.insertWithDependencies(loadObjectsMap);

		return objects;
	}

	private final <T extends StorableObject> Set<T> loadStorableObjectsButIdsByConditionCustom(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		final Set<T> objects = this.databaseObjectLoader.loadStorableObjectsButIdsByCondition(ids, condition);
		final Set<Identifier> loadButIds = Identifier.createSumIdentifiers(ids, objects);

		final Set<T> loadedObjects = super.loadStorableObjectsButIdsByCondition(loadButIds, condition);
		if (loadedObjects.isEmpty()) {
			return objects;
		}
		objects.addAll(loadedObjects);

		final SortedMap<Integer, Map<Short, Set<StorableObject>>> loadObjectsMap = new TreeMap<Integer, Map<Short, Set<StorableObject>>>();
		final Map<Short, Set<StorableObject>> levelLoadObjectsMap = new HashMap<Short, Set<StorableObject>>(1);
		final short entityCode = condition.getEntityCode().shortValue();
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		levelLoadObjectsMap.put(new Short(entityCode), (Set<StorableObject>) loadedObjects);
		loadObjectsMap.put(new Integer(0), levelLoadObjectsMap);

		final Map<Short, Set<Identifier>> missingDependencesMap = this.createMissingDependenciesMap((Set<StorableObject>) loadedObjects);
		for (final Short entityKey : missingDependencesMap.keySet()) {
			this.loadStorableObjectsWithDependencies(loadObjectsMap, 1, missingDependencesMap.get(entityKey));
		}

		this.insertWithDependencies(loadObjectsMap);

		return objects;
	}

	final void loadStorableObjectsWithDependencies(final SortedMap<Integer, Map<Short, Set<StorableObject>>> loadObjectsMap,
			final int dependencyLevel,
			final Set<Identifier> ids) throws ApplicationException {
		final Set<StorableObject> loadedObjects = super.loadStorableObjects(ids);
		if (loadedObjects.isEmpty()) {
			return;
		}

		final Integer dependencyKey = new Integer(-dependencyLevel);
		Map<Short, Set<StorableObject>> levelLoadObjectsMap = loadObjectsMap.get(dependencyKey);
		if (levelLoadObjectsMap == null) {
			levelLoadObjectsMap = new HashMap<Short, Set<StorableObject>>();
			loadObjectsMap.put(dependencyKey, levelLoadObjectsMap);
		}
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		final Short entityKey1 = new Short(entityCode);
		Set<StorableObject> entityLevelLoadObjects = levelLoadObjectsMap.get(entityKey1);
		if (entityLevelLoadObjects == null) {
			entityLevelLoadObjects = new HashSet<StorableObject>();
			levelLoadObjectsMap.put(entityKey1, entityLevelLoadObjects);
		}
		entityLevelLoadObjects.addAll(loadedObjects);

		final Map<Short, Set<Identifier>> missingDependencesMap = this.createMissingDependenciesMap(loadedObjects);
		for (final Short entityKey : missingDependencesMap.keySet()) {
			this.loadStorableObjectsWithDependencies(loadObjectsMap,
					dependencyLevel + 1,
					missingDependencesMap.get(entityKey));
		}

	}

	private Map<Short, Set<Identifier>> createMissingDependenciesMap(final Set<StorableObject> storableObjects) {
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

	private void insertWithDependencies(final Map<Integer, Map<Short, Set<StorableObject>>> dependenciesMap) {
		for (final Integer dependencyKey : dependenciesMap.keySet()) {
			final Map<Short, Set<StorableObject>> levelLoadObjectsMap = dependenciesMap.get(dependencyKey);
			for (final Short entityKey : levelLoadObjectsMap.keySet()) {
				final Set<StorableObject> entityLevelLoadObjects = levelLoadObjectsMap.get(entityKey);
				try {
					final StorableObjectDatabase<StorableObject> database = DatabaseContext.getDatabase(entityKey);
					database.save(entityLevelLoadObjects);
				}
				catch (ApplicationException ae) {
					Log.errorException(ae);
				}
			}
		}
	}

	@Override
	public final void saveStorableObjects(final Set<StorableObject> storableObjects) throws ApplicationException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		if (storableObjects.isEmpty()) {
			return;
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case TEST_CODE:
				this.databaseObjectLoader.saveStorableObjects(storableObjects);
				super.saveStorableObjects(storableObjects);
				break;
			default:
				this.databaseObjectLoader.saveStorableObjects(storableObjects);
		}
	}

}
