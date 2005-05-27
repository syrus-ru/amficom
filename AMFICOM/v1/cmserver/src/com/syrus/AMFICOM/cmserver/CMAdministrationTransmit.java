/*
 * $Id: CMAdministrationTransmit.java,v 1.23 2005/05/27 16:24:45 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.ServerProcess_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.23 $, $Date: 2005/05/27 16:24:45 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public abstract class CMAdministrationTransmit extends CMGeneralTransmit {
	private static final long serialVersionUID = -6573358727271115828L;

	public User_Transferable[] transmitUsers(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final User_Transferable users[] = new User_Transferable[length];
		System.arraycopy(storableObjects, 0, users, 0, length);
		return users;
	}

	public User_Transferable[] transmitUsersButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final User_Transferable users[] = new User_Transferable[length];
		System.arraycopy(storableObjects, 0, users, 0, length);
		return users;
	}

	public Domain_Transferable[] transmitDomains(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Domain_Transferable domains[] = new Domain_Transferable[length];
		System.arraycopy(storableObjects, 0, domains, 0, length);
		return domains;
	}

	public Domain_Transferable[] transmitDomainsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Domain_Transferable domains[] = new Domain_Transferable[length];
		System.arraycopy(storableObjects, 0, domains, 0, length);
		return domains;
	}

	public Server_Transferable[] transmitServers(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final Server_Transferable servers[] = new Server_Transferable[length];
		System.arraycopy(storableObjects, 0, servers, 0, length);
		return servers;
	}

	public Server_Transferable[] transmitServersButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final Server_Transferable servers[] = new Server_Transferable[length];
		System.arraycopy(storableObjects, 0, servers, 0, length);
		return servers;
	}

	public MCM_Transferable[] transmitMCMs(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final MCM_Transferable mcms[] = new MCM_Transferable[length];
		System.arraycopy(storableObjects, 0, mcms, 0, length);
		return mcms;
	}

	public MCM_Transferable[] transmitMCMsButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final MCM_Transferable mcms[] = new MCM_Transferable[length];
		System.arraycopy(storableObjects, 0, mcms, 0, length);
		return mcms;
	}

	public ServerProcess_Transferable[] transmitServerProcesses(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjects(ids, sessionKey);
		final int length = storableObjects.length;
		final ServerProcess_Transferable serverProcesses[] = new ServerProcess_Transferable[length];
		System.arraycopy(storableObjects, 0, serverProcesses, 0, length);
		return serverProcesses;
	}

	public ServerProcess_Transferable[] transmitServerProcessesButIdsCondition(
			final Identifier_Transferable ids[],
			final SessionKey_Transferable sessionKey,
			final StorableObjectCondition_Transferable storableObjectCondition)
			throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(ids, sessionKey, storableObjectCondition);
		final int length = storableObjects.length;
		final ServerProcess_Transferable serverProcesses[] = new ServerProcess_Transferable[length];
		System.arraycopy(storableObjects, 0, serverProcesses, 0, length);
		return serverProcesses;
	}
}
