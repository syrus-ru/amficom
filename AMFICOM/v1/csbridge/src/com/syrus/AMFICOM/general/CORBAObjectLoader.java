/*-
 * $Id: CORBAObjectLoader.java,v 1.58 2005/10/10 11:42:00 bob Exp $
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
 * @version $Revision: 1.58 $, $Date: 2005/10/10 11:42:00 $
 * @author $Author: bob $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public class CORBAObjectLoader implements ObjectLoader {
	protected ServerConnectionManager serverConnectionManager;
	
	protected static CORBAActionProcessor actionProcessor;

	
	private static abstract class AbstractLoadAction<T extends StorableObject> 
	implements CORBAAction {
		protected Set<T> sett;		
		
		public Set<T> getResult() {
			return this.sett;
		}
	}

	public CORBAObjectLoader(final ServerConnectionManager serverConnectionManager) {
		this.serverConnectionManager = serverConnectionManager;
	}

	/**
	 * Overridden in: MCMObjectLoader
	 */
	public <T extends StorableObject> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null : ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final CommonServer server = this.serverConnectionManager.getServerReference();
		return loadStorableObjects(server, ids);
	}

	/**
	 * Overridden in:
	 * MCMObjectLoader
	 */
	public <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null && condition != null : ErrorMessages.NON_NULL_EXPECTED;

		final CommonServer server = this.serverConnectionManager.getServerReference();
		return loadStorableObjectsButIdsByCondition(server, ids, condition);
	}
	
	private static synchronized CORBAActionProcessor getCORBAActionProcessor() {
		if (actionProcessor == null) {
			actionProcessor = new CORBAActionProcessor() {
				public void performAction(final CORBAAction action) throws ApplicationException {
					int numEfforts = 0;
					while (true) {
						try {
							action.performAction();
							return;
						} catch (final AMFICOMRemoteException are) {
							switch (are.errorCode.value()) {
								case IdlErrorCode._ERROR_NOT_LOGGED_IN:
									if (++numEfforts == 1) {
										if (LoginManager.restoreLogin()) {
											continue;
										}
										Log.debugMessage("CORBAObjectLoader.CORBAActionProcessor.performAction | Login not restored", Level.INFO);
										return;
									}
									throw new LoginException(are.message);
								default:
									throw new RetrieveObjectException(are.message);
							}
						}
					}
				}
			};
		}
		
		return actionProcessor;
	}
	
	public static final void setActionProcessor(final CORBAActionProcessor actionProcessor) {
		CORBAObjectLoader.actionProcessor = actionProcessor;
	}
	
	private static <T extends StorableObject> Set<T> loading(final AbstractLoadAction<T> loadAction) 
	throws ApplicationException {
		getCORBAActionProcessor().performAction(loadAction);
		
		final Set<T> result = loadAction.getResult();
		if (result != null) {
			return result;
		}
		return Collections.emptySet();		
	}
	
	public static final <T extends StorableObject> Set<T> loadStorableObjects(final CommonServer server, final Set<Identifier> ids)
			throws ApplicationException {
		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		
		return loading(new AbstractLoadAction<T>() {			
			public void performAction() 
			throws AMFICOMRemoteException {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final IdlStorableObject[] transferables = server.transmitStorableObjects(idsT, sessionKeyT);
				this.sett = StorableObject.fromTransferables(transferables);
			}
		});
	}

	public static final <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final CommonServer server,
			final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		final IdlIdentifier[] idsT = Identifier.createTransferables(ids);
		final IdlStorableObjectCondition conditionT = condition.getTransferable();
		
		return loading(new AbstractLoadAction<T>() {
			public void performAction() 
			throws AMFICOMRemoteException {
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				final IdlStorableObject[] transferables = server.transmitStorableObjectsButIdsByCondition(idsT, conditionT, sessionKeyT);
				this.sett = StorableObject.fromTransferables(transferables);
			}
		});		
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
		
		class RemoveVersionAction implements CORBAAction {
			
			private Map<Identifier, StorableObjectVersion> versionsMap;
			
			public void performAction() throws AMFICOMRemoteException, ApplicationException {
				final CommonServer server = CORBAObjectLoader.this.serverConnectionManager.getServerReference();
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();				
				final IdVersion[] idVersions = server.transmitRemoteVersions(idsT, sessionKeyT);
				this.versionsMap = new HashMap<Identifier, StorableObjectVersion>(idVersions.length);
				for (int i = 0; i < idVersions.length; i++) {
					this.versionsMap.put(new Identifier(idVersions[i].id), new StorableObjectVersion(idVersions[i].version));
				}
			}
			
			public final Map<Identifier, StorableObjectVersion> getVersionsMap() {
				return this.versionsMap;
			}
			
		}
		
		final RemoveVersionAction removeVersionAction = new RemoveVersionAction();
		
		getCORBAActionProcessor().performAction(removeVersionAction);
		
		return removeVersionAction.getVersionsMap();
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
		
		getCORBAActionProcessor().performAction(new CORBAAction() {
			public void performAction() throws AMFICOMRemoteException ,ApplicationException {
				final CommonServer server = CORBAObjectLoader.this.serverConnectionManager.getServerReference();
				final ORB orb = CORBAObjectLoader.this.serverConnectionManager.getCORBAServer().getOrb();
				final IdlStorableObject[] transferables = StorableObject.createTransferables(storableObjects, orb);
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				server.receiveStorableObjects(transferables, sessionKeyT);
			}
		});
	}

	public final void delete(final Set<? extends Identifiable> identifiables) throws ApplicationException {
		assert identifiables != null : ErrorMessages.NON_NULL_EXPECTED;
		if (identifiables.isEmpty()) {
			return;
		}
		
		getCORBAActionProcessor().performAction(new CORBAAction() {
			public void performAction() throws AMFICOMRemoteException ,ApplicationException {
				final CommonServer server = CORBAObjectLoader.this.serverConnectionManager.getServerReference();
				final IdlIdentifier[] idsT = Identifier.createTransferables(identifiables);
				final IdlSessionKey sessionKeyT = LoginManager.getSessionKeyTransferable();
				server.delete(idsT, sessionKeyT);
			}
		});
	}
}
