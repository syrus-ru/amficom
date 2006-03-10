/*-
 * $Id: MCMObjectSynchronizer.java,v 1.1.2.2 2006/03/10 16:19:49 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
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
	 * ������������ ����� ����������� ������ � ������������.
	 */
	public static final long SLEEP_TIMEOUT = 10000;

	/**
	 * ���������� ��������.
	 */
	private final CORBAObjectLoader objectLoader;

	/**
	 * ����� ��������������� ��������, ������� ��������� ����������.
	 */
	private final Set<Identifier> loadIdsQueue;

	/**
	 * ����� ���������. �������� ��������� �������. ���� - ������� �����������,
	 * ������ � �������� ������. �������� - ����� ��������, ����������� �� ����
	 * ��������. ���������� �� ������ ���� ��������� �������
	 * {@link #loadStorableObjectsWithDependencies(int, Set)}. ����� ���������
	 * ����������� ������ ����� ������ ����� ��������� �������� ��� �������, ��
	 * ��������� � ��������� ��, �����������, ��-������, �� ������ �����������,
	 * � ��-������, �� ���� ��������.
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
	 * ���������� ������� �� ������ <code>ids</code> ���������� ������ � ��
	 * �������������, �� ��� ���, ���� ����� ������������ ���� �������, ��
	 * ��������� � ��������� ��. ��������� ����� ���������
	 * {@link #loadObjectsMap}.
	 * 
	 * @param dependencyLevel
	 *        ������� �����������, �� �� ������� ����������� ������������ ������
	 *        ������. ������ ����� ����������� ����� ����� ������ ������������ �
	 *        ������� �����������, ������� �� ������� �� ��������� � ���
	 *        ���������, � ������� ��� �������ģ� ����� ����������� ������
	 *        ��������.
	 * @param ids
	 *        ����� ��������������� ��������, ������� ��������� ���������� ��
	 *        ������ ������ ������������. ��� ��������� ������ ��� ������ �����
	 *        ��������������� ��������, ������� ���� ����������. ��� �����������
	 *        ����������� ������� ��� �������������� ��������-������������,
	 *        ������� �� ���� ������� � ��������� ��. ��� ��� ��������������
	 *        ������ ������������ � ����� ��������.
	 * @throws ApplicationException
	 */
	private final void loadStorableObjectsWithDependencies(final int dependencyLevel,
			final Set<Identifier> ids) throws ApplicationException {
		/* ���������� ������� */
		final Set<StorableObject> loadedObjects = this.objectLoader.loadStorableObjects(ids);
		if (loadedObjects.isEmpty()) {
			return;
		}

		/* ���ģ� ������������ ������� � ����� ��������� */
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
		 * ��� ���� ������������ �������� ������� ����� �� ��������� � ���������
		 * �� ������������
		 */
		final Map<Short, Set<Identifier>> missingDependencesMap = this.createMissingDependenciesMap(loadedObjects);

		/*
		 * ��� ������� ���� �������� �� ����� ������������ �������� ����������
		 * loadStorableObjectsWithDependencies. ��� ���� ������� �����������
		 * ������ ����������� �� �������.
		 */
		for (final Short entityKey : missingDependencesMap.keySet()) {
			this.loadStorableObjectsWithDependencies(dependencyLevel + 1, missingDependencesMap.get(entityKey));
		}

	}

	/**
	 * ������� ����� ���� Map<Short, Set<Identifier>>, ��� ��������
	 * ����������� ������� �� �������� �������� ������
	 * <code>storableObjects</code>, �� ��������� � ��������� ��, �����������
	 * �� ����� ���������. ���� ����� �� �������, ����� ��� �������
	 * <code>storableObjects</code> ���� ����� ��������, ���� � ������������
	 * ������ ��� ������ ������.
	 * 
	 * @param storableObjects
	 *        ����� ��������, ��� ������� ������ �����������, �� ������������ �
	 *        ��������� ��.
	 * @return ����� ������������ ��������, ������������� �� ����� ��������.
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
	 * �������� � {@link StorableObjectPool} � � ��������� �� �������,
	 * ���������� � ����� ��������� {@link #loadObjectsMap}. ��� ����� ������
	 * ���� �������������� ��������� �������
	 * {@link #loadStorableObjectsWithDependencies(int, Set)}. ��������� �����
	 * ����� �����������, ����� �������� �� ����� ��������� ������ ����������� �
	 * ����� �������. ����� ������� �������������� �������� ������ ������
	 * �����������.
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
