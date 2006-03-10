/*-
 * $Id: MCMObjectSynchronizer.java,v 1.1.2.2 2006/03/10 16:19:49 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mcm;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1.2.2 $, $Date: 2006/03/10 16:19:49 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class MCMObjectSynchronizer extends Thread {

	/**
	 * Максимальное время бездействия потока в наносекундах.
	 */
	public static final long SLEEP_TIMEOUT = 10000;

	/**
	 * Подгрузчик объектов.
	 */
	private final CORBAObjectLoader objectLoader;

	/**
	 * Набор идентификаторов объектов, которые требуется подгрузить.
	 */
	private final Set<Identifier> loadIdsQueue;

	/**
	 * Карта подгрузки. Устроена следующим образом. Ключ - уровень зависимости,
	 * взятый с обратным знаком. Величина - карта объектов, разложенных по коду
	 * сущности. Передаётся по ссылке всем вложенным вызовам
	 * {@link #loadStorableObjectsWithDependencies(int, Set)}. После окончания
	 * рекурсивной работы этого метода карта подгрузки содержит все объекты, не
	 * найденные в локальной БД, разложенные, во-первых, по уровню зависимости,
	 * а во-вторых, по коду сущности.
	 */
	private final Map<Integer, Map<Short, Set<StorableObject>>> loadObjectsMap;

	public MCMObjectSynchronizer(final ServerConnectionManager serverConnectionManager) {
		super.setName("MCMObjectSynchronizer");

		assert serverConnectionManager != null : NON_NULL_EXPECTED;
		this.objectLoader = new CORBAObjectLoader(serverConnectionManager);

		this.loadIdsQueue = Collections.synchronizedSet(new HashSet<Identifier>());
		this.loadObjectsMap = Collections.synchronizedMap(new HashMap<Integer, Map<Short, Set<StorableObject>>>());
	}

	public synchronized void addIdentifier(final Identifier id) {
		assert id != null : NON_NULL_EXPECTED;
		this.loadIdsQueue.add(id);
		this.notifyAll();
	}

	public synchronized void addIdentifiers(final Set<Identifier> ids) {
		assert ids != null : NON_NULL_EXPECTED;
		this.loadIdsQueue.addAll(ids);
		this.notifyAll();
	}

	@Override
	public void run() {
		synchronized (this) {
			while (this.loadIdsQueue.isEmpty()) {
				try {
					this.wait(SLEEP_TIMEOUT);
				} catch (InterruptedException e) {
					Log.debugMessage(this.getName() + " -- interrupted", Log.DEBUGLEVEL07);
				}
			}
		}

		final Map<Short, Set<Identifier>> entityIdsMap;
		synchronized (this.loadIdsQueue) {
			entityIdsMap = Identifier.createEntityIdsMap(this.loadIdsQueue);
		}

		for (final Short entityKey : entityIdsMap.keySet()) {
			final Set<Identifier> entityLoadIds = entityIdsMap.get(entityKey);
			this.loadObjectsMap.clear();
			try {
				this.loadStorableObjectsWithDependencies(0, entityLoadIds);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
			this.insertWithDependencies();

			final Set<StorableObject> loadedObjects = this.loadObjectsMap.get(Integer.valueOf(0)).get(entityKey);
			if (loadedObjects == null || loadedObjects.isEmpty()) {
				continue;
			}
			assert StorableObject.getEntityCodeOfIdentifiables(loadedObjects) == entityKey.shortValue() : ILLEGAL_ENTITY_CODE;
			Identifier.subtractFromIdentifiers(this.loadIdsQueue, loadedObjects);
		}
	}

	/**
	 * Подгружает объекты по списку <code>ids</code> рекурсивно вместе с их
	 * зависимостями, до тех пор, пока среди зависимостей есть объекты, не
	 * найденные в локальной БД. Заполняет карту подгрузки
	 * {@link #loadObjectsMap}.
	 * 
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
	 *        которые не были найдены в локальной БД. Все эти идентификаторы
	 *        должны принадлежать к одной сущности.
	 * @throws ApplicationException
	 */
	private final void loadStorableObjectsWithDependencies(final int dependencyLevel,
			final Set<Identifier> ids) throws ApplicationException {
		/* Подгружаем объекты */
		final Set<StorableObject> loadedObjects = this.objectLoader.loadStorableObjects(ids);
		if (loadedObjects.isEmpty()) {
			return;
		}

		/* Кладём подгруженные объекты в карту подгрузки */
		final Integer dependencyKey = Integer.valueOf(-dependencyLevel);
		Map<Short, Set<StorableObject>> levelLoadObjectsMap = this.loadObjectsMap.get(dependencyKey);
		if (levelLoadObjectsMap == null) {
			levelLoadObjectsMap = new HashMap<Short, Set<StorableObject>>();
			this.loadObjectsMap.put(dependencyKey, levelLoadObjectsMap);
		}
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		final Short entityKey1 = Short.valueOf(entityCode);
		Set<StorableObject> entityLevelLoadObjects = levelLoadObjectsMap.get(entityKey1);
		if (entityLevelLoadObjects == null) {
			entityLevelLoadObjects = new HashSet<StorableObject>();
			levelLoadObjectsMap.put(entityKey1, entityLevelLoadObjects);
		}
		entityLevelLoadObjects.addAll(loadedObjects);

		/*
		 * Для всех подгруженных объектов создаём карту не найденных в локальной
		 * БД зависимостей
		 */
		final Map<Short, Set<Identifier>> missingDependencesMap = this.createMissingDependenciesMap(loadedObjects);

		/*
		 * Для каждого кода сущности из карты зависимостей вызываем рекурсивно
		 * loadStorableObjectsWithDependencies. При этом уровень зависимости
		 * должен увеличиться на единицу.
		 */
		for (final Short entityKey : missingDependencesMap.keySet()) {
			this.loadStorableObjectsWithDependencies(dependencyLevel + 1, missingDependencesMap.get(entityKey));
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
					needLoad = StorableObjectDatabase.isObjectPresentInDatabase(idDependency);
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
	 * Вставить в {@link StorableObjectPool} и в локальную БД объекты,
	 * хранящиеся в карте подгрузки {@link #loadObjectsMap}. Эта карта должна
	 * быть предварительно заполнена методом
	 * {@link #loadStorableObjectsWithDependencies(int, Set)}. Перебирая ключи
	 * карты зависимости, метод движется от более глубокого уровня зависимости к
	 * более мелкому. Такой порядок обеспечивается обратным знаком уровня
	 * зависимости.
	 */
	private void insertWithDependencies() {
		for (final Integer dependencyKey : this.loadObjectsMap.keySet()) {
			final Map<Short, Set<StorableObject>> levelLoadObjectsMap = this.loadObjectsMap.get(dependencyKey);
			for (final Short entityKey : levelLoadObjectsMap.keySet()) {
				final Set<StorableObject> entityLevelLoadObjects = levelLoadObjectsMap.get(entityKey);
				try {
					StorableObjectPool.putStorableObjects(entityLevelLoadObjects);
				} catch (IllegalObjectEntityException ioee) {
					Log.errorMessage(ioee);
				}
				final StorableObjectDatabase<?> storableObjectDatabase = DatabaseContext.getDatabase(entityKey);
				try {
					((StorableObjectDatabase) storableObjectDatabase).save(entityLevelLoadObjects);
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
				}
			}
		}
	}
}
