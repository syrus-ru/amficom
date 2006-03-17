/*
 * $Id: MCMObjectLoader.java,v 1.35.2.3 2006/03/17 12:36:39 arseniy Exp $
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
 * @version $Revision: 1.35.2.3 $, $Date: 2006/03/17 12:36:39 $
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

		final Set<T> loadedObjects = (Set<T>) loadObjectsMap.get(Integer.valueOf(0)).get(Short.valueOf(entityCode));
		if (loadedObjects.isEmpty()) {
			return objects;
		}
		assert StorableObject.getEntityCodeOfIdentifiables(loadedObjects) == entityCode : ErrorMessages.ILLEGAL_ENTITY_CODE;
		objects.addAll(loadedObjects);

		this.insertWithDependencies(loadObjectsMap);

		return objects;
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
		levelLoadObjectsMap.put(Short.valueOf(entityCode), (Set<StorableObject>) loadedObjects);
		loadObjectsMap.put(Integer.valueOf(0), levelLoadObjectsMap);

		final Map<Short, Set<Identifier>> missingDependencesMap = this.createMissingDependenciesMap((Set<StorableObject>) loadedObjects);
		for (final Short entityKey : missingDependencesMap.keySet()) {
			this.loadStorableObjectsWithDependencies(loadObjectsMap, 1, missingDependencesMap.get(entityKey));
		}

		this.insertWithDependencies(loadObjectsMap);

		return objects;
	}

	/**
	 * Подгружает объекты по списку <code>ids</code> рекурсивно вместе с их
	 * зависимостями, до тех пор, пока среди зависимостей есть объекты, не
	 * найденные в локальной БД. Первый параметр -- карта подгрузки --
	 * передаётся по ссылке всем вложенным вызовам. После окончания рекурсивной
	 * работы этого метода карта подгрузки содержит все объекты, не найденные в
	 * локальной БД, разложенные 1) по уровню зависимости, 2) по коду сущности.
	 * 
	 * @param loadObjectsMap
	 *        Карта подгрузки. По ключу хранится уровень зависимости, взятый с
	 *        обратным знаком. Величина - карта, где хранятся подгруженные
	 *        объекты, разложенные по кодам сущностей. Карта подгрузки
	 *        пополняется новыми объектами с каждым новым рекурсивным вызовом
	 *        метода.
	 * @param dependencyLevel
	 *        Уровень зависимости, он же уровень вложенности рекурсивного вызова
	 *        метода. Каждый новый рекурсивный вызов этого метода производится с
	 *        уровнем зависимости, большим на единицу по сравнению с тем
	 *        значением, с которым был произведён вызов предыдущего уровня
	 *        рекурсии.
	 * @param ids
	 *        Набор идентификаторов объектов, которые требуется подгрузить на
	 *        данном уровне зависимостей. При начальном вызове это просто набор
	 *        идентификаторов объектов, которые надо подгрузить. При последующих
	 *        рекурсивных вызовах это идентификаторы объектов-зависимостей,
	 *        которые не были найдены в локальной БД.
	 * @throws ApplicationException
	 */
	private final void loadStorableObjectsWithDependencies(final SortedMap<Integer, Map<Short, Set<StorableObject>>> loadObjectsMap,
			final int dependencyLevel,
			final Set<Identifier> ids) throws ApplicationException {
		final Set<StorableObject> loadedObjects = super.loadStorableObjects(ids);
		if (loadedObjects.isEmpty()) {
			return;
		}

		final Integer dependencyKey = Integer.valueOf(-dependencyLevel);
		Map<Short, Set<StorableObject>> levelLoadObjectsMap = loadObjectsMap.get(dependencyKey);
		if (levelLoadObjectsMap == null) {
			levelLoadObjectsMap = new HashMap<Short, Set<StorableObject>>();
			loadObjectsMap.put(dependencyKey, levelLoadObjectsMap);
		}
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		final Short entityKey1 = Short.valueOf(entityCode);
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

	/**
	 * Создаёт карту вида Map<Short, Set<Identifier>>, где хранятся
	 * зависимости каждого из объектов входного набора
	 * <code>storableObjects</code>, не найденные в локальной БД, разложенные
	 * по кодам сущностей. Этот метод не требует, чтобы все объекты
	 * <code>storableObjects</code> были одной сущности, хотя и используется
	 * только для такого случая.
	 * 
	 * @param storableObjects
	 *        Набор объектов, для которых ищутся зависимости, не существующие в
	 *        локальной БД.
	 * @return Карта зависимостей объектов, упорядоченных по ключу сущности.
	 */
	private Map<Short, Set<Identifier>> createMissingDependenciesMap(final Set<StorableObject> storableObjects) {
		final Map<Short, Set<Identifier>> missingDependencesMap = new HashMap<Short, Set<Identifier>>();
		for (final StorableObject storableObject : storableObjects) {
			final Set<Identifiable> dependencies = storableObject.getDependencies();
			for (final Identifiable dependency : dependencies) {
				final Identifier idDependency = dependency.getId();
				boolean needLoad = true;
				try {
					needLoad = !StorableObjectDatabase.isObjectPresentInDatabase(dependency.getId());
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
				}
				if (needLoad) {
					final Short entityKey = Short.valueOf(idDependency.getMajor());
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



	/**
	 * Вставить в локальную БД объекты, хранящиеся в карте зависимостей
	 * <code>dependenciesMap</code>. Эта карта устроена следующим образом.
	 * Ключ - уровень зависимости, взятый с обратным знаком. Величина - карта
	 * объектов, разложенных по коду сущности. Перебирая ключи карты
	 * зависимости, метод движется от более глубокого уровня зависимости к более
	 * мелкому. Такой порядок обеспечивается обратным знаком уровня зависимости.
	 * 
	 * @param dependenciesMap
	 *        Карта зависимостей. Устроена также, как и карта подгрузки метода
	 *        {@link #loadStorableObjectsWithDependencies(SortedMap, int, Set)}
	 *        и получается в результате вызова этого метода.
	 */
	private void insertWithDependencies(final Map<Integer, Map<Short, Set<StorableObject>>> dependenciesMap) {
		for (final Integer dependencyKey : dependenciesMap.keySet()) {
			final Map<Short, Set<StorableObject>> levelLoadObjectsMap = dependenciesMap.get(dependencyKey);
			for (final Short entityKey : levelLoadObjectsMap.keySet()) {
				final Set<StorableObject> entityLevelLoadObjects = levelLoadObjectsMap.get(entityKey);
				try {
					final StorableObjectDatabase<? extends StorableObject> database = DatabaseContext.getDatabase(entityKey);
					((StorableObjectDatabase) database).save(entityLevelLoadObjects);
				} catch (ApplicationException ae) {
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
