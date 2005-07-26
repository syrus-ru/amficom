/*-
 * $Id: CORBAObjectLoader.java,v 1.44 2005/07/26 18:51:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.44 $, $Date: 2005/07/26 18:51:45 $
 * @author $Author: arseniy $
 * @module csbridge
 */
public class CORBAObjectLoader implements ObjectLoader {
	protected ServerConnectionManager serverConnectionManager;

	protected CORBAObjectLoader (final ServerConnectionManager serverConnectionManager) {
		this.serverConnectionManager = serverConnectionManager;
	}


	public final <T extends StorableObject> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final CommonServer server = this.serverConnectionManager.getServerReference();
		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		int numEfforts = 0;
		while (true) {
			try {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final IdlStorableObject[] transferables = server.transmitStorableObjects(idsT, sessionKeyT);
				return StorableObject.fromTransferables(transferables);
			}
			catch (final AMFICOMRemoteException are) {
				switch (are.errorCode.value()) {
					case IdlErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							Log.debugMessage("CORBAObjectLoader.loadStorableObjects | Login not restored", Level.INFO);
							return Collections.emptySet();
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}

	public <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition)
			throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;

		final CommonServer server = this.serverConnectionManager.getServerReference();
		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		final IdlStorableObjectCondition conditionT = condition.getTransferable();
		int numEfforts = 0;
		while (true) {
			try {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final IdlStorableObject[] transferables = server.transmitStorableObjectsButIdsByCondition(idsT, conditionT, sessionKeyT);
				return StorableObject.fromTransferables(transferables);
			}
			catch (final AMFICOMRemoteException are) {
				switch (are.errorCode.value()) {
					case IdlErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							Log.debugMessage("CORBAObjectLoader.loadStorableObjectsButIdsByCondition | Login not restored", Level.INFO);
							return Collections.emptySet();
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}

	public final Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptyMap();
		}

		final CommonServer server = this.serverConnectionManager.getServerReference();
		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		int numEfforts = 0;
		while (true) {
			try {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final IdVersion[] idVersions = server.transmitRemoteVersions(idsT, sessionKeyT);
				final Map<Identifier, StorableObjectVersion> versionsMap = new HashMap<Identifier, StorableObjectVersion>(idVersions.length);
				for (int i = 0; i < idVersions.length; i++) {
					versionsMap.put(new Identifier(idVersions[i].id), new StorableObjectVersion(idVersions[i].version));
				}
				return versionsMap;
			}
			catch (final AMFICOMRemoteException are) {
				switch (are.errorCode.value()) {
					case IdlErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							Log.debugMessage("CORBAObjectLoader.getRemoteVersions | Login not restored", Level.INFO);
							return Collections.emptyMap();
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}

	public final void saveStorableObjects(final Set<? extends StorableObject> storableObjects) throws ApplicationException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		if (storableObjects.isEmpty()) {
			return;
		}

		final CommonServer server = this.serverConnectionManager.getServerReference();
		final ORB orb = this.serverConnectionManager.getCORBAServer().getOrb();
		final IdlStorableObject[] transferables = StorableObject.createTransferables(storableObjects, orb);
		int numEfforts = 0;
		while (true) {
			try {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				server.receiveStorableObjects(transferables, sessionKeyT);
				return;
			}
			catch (final AMFICOMRemoteException are) {
				switch (are.errorCode.value()) {
					case IdlErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							throw new LoginException("Login not restored");
						}
						throw new LoginException(are.message);
					default:
						throw new UpdateObjectException(are.message);
				}
			}
		}
	}

	public Set<Identifier> getOldVersionIds(final Map<Identifier, StorableObjectVersion> versionsMap) throws ApplicationException {
		assert versionsMap != null : ErrorMessages.NON_NULL_EXPECTED;
		if (versionsMap.isEmpty()) {
			return Collections.emptySet();
		}

		final CommonServer server = this.serverConnectionManager.getServerReference();
		final IdVersion[] idVersions = new IdVersion[versionsMap.size()];
		int i = 0;
		for (final Identifier id : versionsMap.keySet()) {
			final StorableObjectVersion version = versionsMap.get(id);
			idVersions[i++] = new IdVersion(id.getTransferable(), version.longValue());
		}
		int numEfforts = 0;
		while (true) {
			try {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final IdlIdentifier[] idsT = server.transmitOldVersionIds(idVersions, sessionKeyT);
				return Identifier.fromTransferables(idsT);
			}
			catch (final AMFICOMRemoteException are) {
				switch (are.errorCode.value()) {
					case IdlErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							Log.debugMessage("CORBAObjectLoader.refresh | Login not restored", Level.INFO);
							return Collections.emptySet();
						}
						throw new LoginException(are.message);
					default:
						throw new RetrieveObjectException(are.message);
				}
			}
		}
	}

	public void delete(final Set<? extends Identifiable> identifiables) throws ApplicationException {
		assert identifiables != null : ErrorMessages.NON_NULL_EXPECTED;
		if (identifiables.isEmpty()) {
			return;
		}

		final CommonServer server = this.serverConnectionManager.getServerReference();
		final IdlIdentifier[] idsT = Identifier.createTransferables(identifiables);
		int numEfforts = 0;
		while (true) {
			try {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				server.delete(idsT, sessionKeyT);
				return;
			}
			catch (final AMFICOMRemoteException are) {
				switch (are.errorCode.value()) {
					case IdlErrorCode._ERROR_NOT_LOGGED_IN:
						if (++numEfforts == 1) {
							if (LoginManager.restoreLogin()) {
								continue;
							}
							throw new LoginException("Login not restored");
						}
						throw new LoginException(are.message);
					default:
						throw new ApplicationException(are.message);
				}
			}
		}
	}

}
