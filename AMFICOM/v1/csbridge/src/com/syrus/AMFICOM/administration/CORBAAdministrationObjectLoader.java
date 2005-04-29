/*
 * $Id: CORBAAdministrationObjectLoader.java,v 1.5 2005/04/29 08:56:46 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.administration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.ServerProcess_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CMServerConnectionManager;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.SecurityKey;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/29 08:56:46 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public final class CORBAAdministrationObjectLoader extends CORBAObjectLoader implements AdministrationObjectLoader {

	public CORBAAdministrationObjectLoader(CMServerConnectionManager cmServerConnectionManager) {
		super(cmServerConnectionManager);
	}



	/* Load single object*/

	public User loadUser(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			User_Transferable transferable = cmServer.transmitUser((Identifier_Transferable) id.getTransferable(), securityKey);
			User storableObject = new User(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Domain loadDomain(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Domain_Transferable transferable = cmServer.transmitDomain((Identifier_Transferable) id.getTransferable(), securityKey);
			Domain storableObject = new Domain(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public Server loadServer(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Server_Transferable transferable = cmServer.transmitServer((Identifier_Transferable) id.getTransferable(), securityKey);
			Server storableObject = new Server(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public MCM loadMCM(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MCM_Transferable transferable = cmServer.transmitMCM((Identifier_Transferable) id.getTransferable(), securityKey);
			MCM storableObject = new MCM(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}

	public ServerProcess loadServerProcess(Identifier id) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			ServerProcess_Transferable transferable = cmServer.transmitServerProcess((Identifier_Transferable) id.getTransferable(), securityKey);
			ServerProcess storableObject = new ServerProcess(transferable);
			return storableObject;
		}
		catch (AMFICOMRemoteException are) {
			if (are.error_code.value() == ErrorCode._ERROR_NOT_FOUND)
				throw new ObjectNotFoundException("Object '" + id + "' not found on server");
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Load multiple objects*/

	public Set loadUsers(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			User_Transferable[] transferables = cmServer.transmitUsers(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				objects.add(new User(transferables[i]));
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Domain_Transferable[] transferables = cmServer.transmitDomains(idsT, securityKey);
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
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadServers(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Server_Transferable[] transferables = cmServer.transmitServers(idsT, securityKey);
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
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MCM_Transferable[] transferables = cmServer.transmitMCMs(idsT, securityKey);
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
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadServerProcesses(Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			ServerProcess_Transferable[] transferables = cmServer.transmitServerProcesses(idsT, securityKey);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				objects.add(new ServerProcess(transferables[i]));
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Load multiple objects but ids*/

	public Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			User_Transferable[] transferables = cmServer.transmitUsersButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				objects.add(new User(transferables[i]));
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Domain_Transferable[] transferables = cmServer.transmitDomainsButIdsCondition(idsT, securityKey, conditionT);
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
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			Server_Transferable[] transferables = cmServer.transmitServersButIdsCondition(idsT, securityKey, conditionT);
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
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			MCM_Transferable[] transferables = cmServer.transmitMCMsButIdsCondition(idsT, securityKey, conditionT);
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
			throw new RetrieveObjectException(are.message);
		}
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		Identifier_Transferable[] idsT = Identifier.createTransferables(ids);
		StorableObjectCondition_Transferable conditionT = StorableObjectConditionBuilder.getConditionTransferable(condition);
		SecurityKey securityKey = LoginManager.getSecurityKey();

		try {
			ServerProcess_Transferable[] transferables = cmServer.transmitServerProcessesButIdsCondition(idsT, securityKey, conditionT);
			Set objects = new HashSet(transferables.length);
			for (int i = 0; i < transferables.length; i++) {
				objects.add(new ServerProcess(transferables[i]));
			}
			return objects;
		}
		catch (AMFICOMRemoteException are) {
			throw new RetrieveObjectException(are.message);
		}
	}



	/* Save single object*/

	public void saveUser(User user, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		User_Transferable transferable = (User_Transferable) user.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveUser(transferable, force, securityKey);
			user.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + user.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveDomain(Domain domain, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Domain_Transferable transferable = (Domain_Transferable) domain.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveDomain(transferable, force, securityKey);
			domain.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + domain.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveServer(Server server, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Server_Transferable transferable = (Server_Transferable) server.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveServer(transferable, force, securityKey);
			server.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + server.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveMCM(MCM mcm, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		MCM_Transferable transferable = (MCM_Transferable) mcm.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveMCM(transferable, force, securityKey);
			mcm.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + mcm.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveServerProcess(ServerProcess serverProcess, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		ServerProcess_Transferable transferable = (ServerProcess_Transferable) serverProcess.getTransferable();
		try {
			StorableObject_Transferable header = cmServer.receiveServerProcess(transferable, force, securityKey);
			serverProcess.updateFromHeaderTransferable(header);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save object '" + serverProcess.getId() + "' -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	/* Save multiple objects*/

	public void saveUsers(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		User_Transferable[] transferables = new User_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (User_Transferable) ((User) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveUsers(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveDomains(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Domain_Transferable[] transferables = new Domain_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Domain_Transferable) ((Domain) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveDomains(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveServers(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		Server_Transferable[] transferables = new Server_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (Server_Transferable) ((Server) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveServers(transferables, force, securityKey);
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
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (MCM_Transferable) ((MCM) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveMCMs(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}

	public void saveServerProcesses(Set objects, boolean force) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		ServerProcess_Transferable[] transferables = new ServerProcess_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++)
			transferables[i] = (ServerProcess_Transferable) ((ServerProcess) it.next()).getTransferable();

		try {
			StorableObject_Transferable[] headers = cmServer.receiveServerProcesses(transferables, force, securityKey);
			super.updateHeaders(objects, headers);
		}
		catch (AMFICOMRemoteException are) {
			String mesg = "Cannot save objects -- ";
			if (are.error_code.value() == ErrorCode._ERROR_VERSION_COLLISION)
				throw new VersionCollisionException(mesg + are.message, 0L, 0L);
			throw new UpdateObjectException(mesg + are.message);
		}
	}



	public Set refresh(Set storableObjects) throws ApplicationException {
		CMServer cmServer = super.cmServerConnectionManager.getCMServerReference();
		SecurityKey securityKey = LoginManager.getSecurityKey();

		StorableObject_Transferable[] headersT = StorableObject.createHeadersTransferable(storableObjects);

		try {
			Identifier_Transferable[] idsT = cmServer.transmitRefreshedAdministrationObjects(headersT, securityKey);

			Set refreshedIds = Identifier.fromTransferables(idsT);
			return refreshedIds;
		}
		catch (AMFICOMRemoteException are) {
			throw new ApplicationException(are);
		}
	}

}
