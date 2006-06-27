/*-
 * $Id: CORBAObjectLoader.java,v 1.70.2.1 2006/06/27 15:53:39 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.systemserver.corba.IdVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.AMFICOM.systemserver.corba.StorableObjectServer;

/**
 * @version $Revision: 1.70.2.1 $, $Date: 2006/06/27 15:53:39 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public class CORBAObjectLoader implements ObjectLoader {
	private final StorableObjectServerConnectionManager storableObjectServerConnectionManager;
	private final CORBAActionProcessor corbaActionProcessor;

	private static abstract class LoadCORBAAction<T extends Identifiable> implements CORBAAction {
		Set<T> loadedObjects;

		Set<T> getLoadedObjects() {
			return this.loadedObjects;
		}
	}

	private abstract class RemoveVersionCORBAAction implements CORBAAction {
		Map<Identifier, StorableObjectVersion> versionsMap;

		Map<Identifier, StorableObjectVersion> getVersionsMap() {
			return this.versionsMap;
		}
	}

	/**
	 * Use DefaultCORBAActionProcessor with given loginRestorer as
	 * CORBAActionProcessor
	 * 
	 * @param serverConnectionManager
	 */
	public CORBAObjectLoader(final StorableObjectServerConnectionManager storableObjectServerConnectionManager) {
		this(storableObjectServerConnectionManager, new DefaultCORBAActionProcessor());
	}

	/**
	 * Use given CORBAActionProcessor
	 * 
	 * @param serverConnectionManager
	 * @param corbaActionProcessor
	 */
	public CORBAObjectLoader(final StorableObjectServerConnectionManager storableObjectServerConnectionManager,
			final CORBAActionProcessor corbaActionProcessor) {
		assert storableObjectServerConnectionManager != null : NON_NULL_EXPECTED;
		assert corbaActionProcessor != null : NON_NULL_EXPECTED;

		this.storableObjectServerConnectionManager = storableObjectServerConnectionManager;
		this.corbaActionProcessor = corbaActionProcessor;
	}


	/**
	 * Overridden in:
	 * MCMObjectLoader
	 */
	public <T extends StorableObject> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null : NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}
		assert StorableObject.hasSingleTypeEntities(ids) : OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);

		final LoadCORBAAction<T> action = new LoadCORBAAction<T>() {
			public void perform() throws AMFICOMRemoteException, ApplicationException {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKey().getIdlTransferable();
				final StorableObjectServer server = CORBAObjectLoader.this.storableObjectServerConnectionManager.getStorableObjectServerReference();
				final IdlStorableObject[] transferables = server.transmitStorableObjects(idsT, sessionKeyT);
				this.loadedObjects = StorableObject.fromTransferables(transferables);
			}
		};

		this.corbaActionProcessor.performAction(action);

		final Set<T> loadedObjects = action.getLoadedObjects();
		if (loadedObjects != null) {
			return loadedObjects;
		}
		return Collections.emptySet();
	}

	/**
	 * Overridden in:
	 * MCMObjectLoader
	 */
	public <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null : NON_NULL_EXPECTED;
		assert condition != null : NON_NULL_EXPECTED;
		assert ids.isEmpty() || condition.getEntityCode().shortValue() == StorableObject.getEntityCodeOfIdentifiables(ids) : ILLEGAL_ENTITY_CODE;

		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		final IdlStorableObjectCondition conditionT = condition.getIdlTransferable();

		final LoadCORBAAction<T> action = new LoadCORBAAction<T>() {
			public void perform() throws AMFICOMRemoteException, ApplicationException {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKey().getIdlTransferable();
				final StorableObjectServer server = CORBAObjectLoader.this.storableObjectServerConnectionManager.getStorableObjectServerReference();
				final IdlStorableObject[] transferables = server.transmitStorableObjectsButIdsByCondition(idsT, conditionT, sessionKeyT);
				this.loadedObjects = StorableObject.fromTransferables(transferables);
			}
		};
		
		this.corbaActionProcessor.performAction(action);

		final Set<T> loadedObjects = action.getLoadedObjects();
		if (loadedObjects != null) {
			return loadedObjects;
		}
		return Collections.emptySet();
	}

	public Set<Identifier> loadIdentifiersButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition)
			throws ApplicationException {
		assert ids != null : NON_NULL_EXPECTED;
		assert condition != null : NON_NULL_EXPECTED;
		assert ids.isEmpty() || condition.getEntityCode().shortValue() == StorableObject.getEntityCodeOfIdentifiables(ids) : ILLEGAL_ENTITY_CODE;

		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		final IdlStorableObjectCondition conditionT = condition.getIdlTransferable();

		final LoadCORBAAction<Identifier> action = new LoadCORBAAction<Identifier>() {
			public void perform() throws AMFICOMRemoteException, ApplicationException {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKey().getIdlTransferable();
				final StorableObjectServer server = CORBAObjectLoader.this.storableObjectServerConnectionManager.getStorableObjectServerReference();
				final IdlIdentifier[] transferables = server.transmitIdentifiersButIdsByCondition(idsT, conditionT, sessionKeyT);
				this.loadedObjects = Identifier.fromTransferables(transferables);
			}
		};

		this.corbaActionProcessor.performAction(action);
		final Set<Identifier> loadedIdentifiers = action.getLoadedObjects();
		if (loadedIdentifiers != null) {
			return loadedIdentifiers;
		}
		return Collections.emptySet();
	}

	/**
	 * Overridden in: MCMObjectLoader
	 */
	public Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null : ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptyMap();
		}

		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);	

		final RemoveVersionCORBAAction action = new RemoveVersionCORBAAction() {
			public void perform() throws AMFICOMRemoteException, ApplicationException {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKey().getIdlTransferable();
				final StorableObjectServer server = CORBAObjectLoader.this.storableObjectServerConnectionManager.getStorableObjectServerReference();				
				final IdVersion[] idVersions = server.transmitRemoteVersions(idsT, sessionKeyT);
				this.versionsMap = new HashMap<Identifier, StorableObjectVersion>(idVersions.length);
				for (int i = 0; i < idVersions.length; i++) {
					this.versionsMap.put(Identifier.valueOf(idVersions[i].id), StorableObjectVersion.valueOf(idVersions[i].version));
				}
			}
		};

		this.corbaActionProcessor.performAction(action);

		return action.getVersionsMap();
	}

	/**
	 * Overridden in:
	 * MCMObjectLoader
	 */
	public void saveStorableObjects(final Set<StorableObject> storableObjects) throws ApplicationException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		if (storableObjects.isEmpty()) {
			return;
		}

		final CORBAAction action = new CORBAAction() {
			public void perform() throws AMFICOMRemoteException, ApplicationException {
				final ORB orb = CORBAObjectLoader.this.storableObjectServerConnectionManager.getCORBAServer().getOrb();
				final IdlStorableObject[] transferables = StorableObject.createTransferables(storableObjects, orb);
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKey().getIdlTransferable();
				final StorableObjectServer server = CORBAObjectLoader.this.storableObjectServerConnectionManager.getStorableObjectServerReference();
				server.receiveStorableObjects(transferables, sessionKeyT);
			}
		};
		this.corbaActionProcessor.performAction(action);
	}

	public final void delete(final Set<? extends Identifiable> identifiables) throws ApplicationException {
		assert identifiables != null : ErrorMessages.NON_NULL_EXPECTED;
		if (identifiables.isEmpty()) {
			return;
		}

		this.corbaActionProcessor.performAction(new CORBAAction() {
			public void perform() throws AMFICOMRemoteException, ApplicationException {
				final IdlIdentifier[] idsT = Identifier.createTransferables(identifiables);
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKey().getIdlTransferable();
				final StorableObjectServer server = CORBAObjectLoader.this.storableObjectServerConnectionManager.getStorableObjectServerReference();
				server.delete(idsT, sessionKeyT);
			}
		});
	}
}
