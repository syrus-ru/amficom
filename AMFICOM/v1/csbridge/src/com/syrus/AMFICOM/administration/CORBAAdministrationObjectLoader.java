/*-
 * $Id: CORBAAdministrationObjectLoader.java,v 1.18 2005/06/02 14:44:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.ServerProcess_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.18 $, $Date: 2005/06/02 14:44:03 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public final class CORBAAdministrationObjectLoader extends CORBAObjectLoader implements AdministrationObjectLoader {

	public CORBAAdministrationObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}

	public Set loadUsers(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.USER_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitUsers(ids1, sessionKey);
			}
		});
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.DOMAIN_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitDomains(ids1, sessionKey);
			}
		});
	}

	public Set loadServers(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SERVER_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServers(ids1, sessionKey);
			}
		});
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MCM_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMCMs(ids1, sessionKey);
			}
		});
	}

	public Set loadServerProcesses(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SERVERPROCESS_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServerProcesses(ids1, sessionKey);
			}
		});
	}

	public Set loadUsersButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.USER_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitUsersButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.DOMAIN_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitDomainsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SERVER_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServersButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.MCM_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMCMsButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids, condition, ObjectEntities.SERVERPROCESS_ENTITY_CODE, new TransmitButIdsConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable ids1[],
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable condition1)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServerProcessesButIdsCondition(ids1, sessionKey, condition1);
			}
		});
	}

	public void saveUsers(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(storableObjects, ObjectEntities.USER_ENTITY_CODE, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveUsers((User_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveDomains(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Domain_Transferable[] transferables = new Domain_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Domain_Transferable) ((Domain) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveDomains(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveServers(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		Server_Transferable[] transferables = new Server_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Server_Transferable) ((Server) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveServers(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMCMs(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MCM_Transferable) ((MCM) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMCMs(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveServerProcesses(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		ServerProcess_Transferable[] transferables = new ServerProcess_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (ServerProcess_Transferable) ((ServerProcess) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveServerProcesses(transferables, force, sessionKeyT);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new UpdateObjectException(mesg + are.message);
		}
	}
}
