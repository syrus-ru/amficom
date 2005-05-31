/*
 * $Id: CORBAAdministrationObjectLoader.java,v 1.13 2005/05/31 14:54:42 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.HashSet;
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2005/05/31 14:54:42 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public final class CORBAAdministrationObjectLoader extends CORBAObjectLoader implements AdministrationObjectLoader {
	public CORBAAdministrationObjectLoader(ServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
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



	/* Load multiple objects but ids*/

	public Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			User_Transferable[] transferables = cmServer.transmitUsersButIdsCondition(idsT, sessionKeyT, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				objects.add(new User(transferables[i]));
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			Domain_Transferable[] transferables = cmServer.transmitDomainsButIdsCondition(idsT, sessionKeyT, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Domain(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			Server_Transferable[] transferables = cmServer.transmitServersButIdsCondition(idsT, sessionKeyT, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new Server(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			MCM_Transferable[] transferables = cmServer.transmitMCMsButIdsCondition(idsT, sessionKeyT, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				try {
					objects.add(new MCM(transferables[i]));
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		try {
			ServerProcess_Transferable[] transferables = cmServer.transmitServerProcessesButIdsCondition(idsT, sessionKeyT, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				objects.add(new ServerProcess(transferables[i]));
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_LOGGED_IN)
				throw new LoginException("Not logged in");
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Save multiple objects*/

	public void saveUsers(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = (CMServer) super.serverConnectionManager.getServerReference();
		SessionKey_Transferable sessionKeyT = LoginManager.getSessionKeyTransferable();

		User_Transferable[] transferables = new User_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (User_Transferable) ((User) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveUsers(transferables, force, sessionKeyT);
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
