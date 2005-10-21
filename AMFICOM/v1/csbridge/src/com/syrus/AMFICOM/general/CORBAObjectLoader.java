/*-
 * $Id: CORBAObjectLoader.java,v 1.60 2005/10/21 12:03:12 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.60 $, $Date: 2005/10/21 12:03:12 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public class CORBAObjectLoader implements ObjectLoader {
	ServerConnectionManager serverConnectionManager;
	private CORBAActionProcessor corbaActionProcessor;

	private abstract class LoadCORBAAction<T extends StorableObject> implements CORBAAction {
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
	 * Use DefaultCORBAActionProcessor with given loginRestorer as CORBAActionProcessor
	 * @param serverConnectionManager
	 * @param loginRestorer
	 */
	public CORBAObjectLoader(final ServerConnectionManager serverConnectionManager) {
		this(serverConnectionManager, new DefaultCORBAActionProcessor());
	}

	/**
	 * Use given CORBAActionProcessor
	 * @param serverConnectionManager
	 * @param corbaActionProcessor
	 */
	public CORBAObjectLoader(final ServerConnectionManager serverConnectionManager, final CORBAActionProcessor corbaActionProcessor) {
		assert serverConnectionManager != null : NON_NULL_EXPECTED;
		assert corbaActionProcessor != null : NON_NULL_EXPECTED;

		this.serverConnectionManager = serverConnectionManager;
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

		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);

		final LoadCORBAAction<T> action = new LoadCORBAAction<T>() {
			public void perform() throws AMFICOMRemoteException, ApplicationException {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final CommonServer server = CORBAObjectLoader.this.serverConnectionManager.getServerReference();
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

		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		final IdlStorableObjectCondition conditionT = condition.getTransferable();

		final LoadCORBAAction<T> action = new LoadCORBAAction<T>() {
			public void perform() throws AMFICOMRemoteException, ApplicationException {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final CommonServer server = CORBAObjectLoader.this.serverConnectionManager.getServerReference();
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
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final CommonServer server = CORBAObjectLoader.this.serverConnectionManager.getServerReference();				
				final IdVersion[] idVersions = server.transmitRemoteVersions(idsT, sessionKeyT);
				this.versionsMap = new HashMap<Identifier, StorableObjectVersion>(idVersions.length);
				for (int i = 0; i < idVersions.length; i++) {
					this.versionsMap.put(new Identifier(idVersions[i].id), new StorableObjectVersion(idVersions[i].version));
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
				final ORB orb = CORBAObjectLoader.this.serverConnectionManager.getCORBAServer().getOrb();
				final IdlStorableObject[] transferables = StorableObject.createTransferables(storableObjects, orb);
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final CommonServer server = CORBAObjectLoader.this.serverConnectionManager.getServerReference();
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
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final CommonServer server = CORBAObjectLoader.this.serverConnectionManager.getServerReference();
				server.delete(idsT, sessionKeyT);
			}
		});
	}
}
