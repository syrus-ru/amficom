/*
 * $Id: MCMObjectLoader.java,v 1.35.2.1 2006/02/28 15:32:09 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTRESULTPARAMETER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSISRESULTPARAMETER_CODE;
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
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.35.2.1 $, $Date: 2006/02/28 15:32:09 $
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
	public <T extends StorableObject<T>> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case MEASUREMENTRESULTPARAMETER_CODE:
			case ANALYSISRESULTPARAMETER_CODE:
				return this.databaseObjectLoader.loadStorableObjects(ids);
			default:
				return this.loadStorableObjectsCustom(ids);
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

		final Set<T> loadedObjects = (Set<T>) loadObjectsMap.get(Integer.valueOf(0)).get(new Short(entityCode));
		if (loadedObjects.isEmpty()) {
			return objects;
		}
		assert StorableObject.getEntityCodeOfIdentifiables(loadedObjects) == entityCode : ErrorMessages.ILLEGAL_ENTITY_CODE;
		objects.addAll(loadedObjects);

		this.insertWithDependencies(loadObjectsMap);

		return objects;
	}



	@Override
	public final <T extends StorableObject<T>> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case MEASUREMENTRESULTPARAMETER_CODE:
			case ANALYSISRESULTPARAMETER_CODE:
				return this.databaseObjectLoader.loadStorableObjectsButIdsByCondition(ids, condition);
			default:
				return this.loadStorableObjectsButIdsByConditionCustom(ids, condition);
		}
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
		loadObjectsMap.put(Integer.valueOf(0), levelLoadObjectsMap);

		final Map<Short, Set<Identifier>> missingDependencesMap = this.createMissingDependenciesMap((Set<StorableObject>) loadedObjects);
		for (final Short entityKey : missingDependencesMap.keySet()) {
			this.loadStorableObjectsWithDependencies(loadObjectsMap, 1, missingDependencesMap.get(entityKey));
		}

		this.insertWithDependencies(loadObjectsMap);

		return objects;
	}

	private final void loadStorableObjectsWithDependencies(final SortedMap<Integer, Map<Short, Set<StorableObject>>> loadObjectsMap,
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
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
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
					final StorableObjectDatabase<? extends StorableObject<?>> database = DatabaseContext.getDatabase(entityKey);
					((StorableObjectDatabase) database).save(entityLevelLoadObjects);
				}
				catch (ApplicationException ae) {
					Log.errorMessage(ae);
				}
			}
		}
	}



	@Override
	public final Set<Identifier> loadIdentifiersButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case MEASUREMENTRESULTPARAMETER_CODE:
			case ANALYSISRESULTPARAMETER_CODE:
				return this.databaseObjectLoader.loadIdentifiersButIdsByCondition(ids, condition);
			default:
				return this.loadIdentifiersButIdsByConditionCustom(ids, condition);
		}
	}

	private final Set<Identifier> loadIdentifiersButIdsByConditionCustom(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		final Set<Identifier> identifiers = this.databaseObjectLoader.loadIdentifiersButIdsByCondition(ids, condition);
		final Set<Identifier> loadButIds = Identifier.createSumIdentifiers(ids, identifiers);

		final Set<Identifier> loadedIdentifiers = super.loadIdentifiersButIdsByCondition(loadButIds, condition);
		identifiers.addAll(loadedIdentifiers);
		
		return identifiers;
	}



	@Override
	public final Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptyMap();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		switch (entityCode) {
			case MEASUREMENT_CODE:
			case ANALYSIS_CODE:
			case MEASUREMENTRESULTPARAMETER_CODE:
			case ANALYSISRESULTPARAMETER_CODE:
				return this.databaseObjectLoader.getRemoteVersions(ids);
			default:
				return super.getRemoteVersions(ids);
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
