/*
 * $Id: CMAdministrationTransmit.java,v 1.29 2005/06/21 12:44:32 bass Exp $
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
import com.syrus.AMFICOM.administration.corba.SystemUser_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.29 $, $Date: 2005/06/21 12:44:32 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public abstract class CMAdministrationTransmit extends CMGeneralTransmit {

	private static final long serialVersionUID = -6573358727271115828L;


	/* Transmit multiple objects*/

	public SystemUser_Transferable[] transmitSystemUsers(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final SystemUser_Transferable[] users = new SystemUser_Transferable[length];
		System.arraycopy(storableObjects, 0, users, 0, length);
		return users;
	}

	public Domain_Transferable[] transmitDomains(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Domain_Transferable[] domains = new Domain_Transferable[length];
		System.arraycopy(storableObjects, 0, domains, 0, length);
		return domains;
	}

	public Server_Transferable[] transmitServers(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Server_Transferable[] servers = new Server_Transferable[length];
		System.arraycopy(storableObjects, 0, servers, 0, length);
		return servers;
	}

	public MCM_Transferable[] transmitMCMs(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final MCM_Transferable[] mcms = new MCM_Transferable[length];
		System.arraycopy(storableObjects, 0, mcms, 0, length);
		return mcms;
	}

	public ServerProcess_Transferable[] transmitServerProcesses(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final ServerProcess_Transferable[] serverProcesses = new ServerProcess_Transferable[length];
		System.arraycopy(storableObjects, 0, serverProcesses, 0, length);
		return serverProcesses;
	}



	/* Transmit multiple objects but ids by condition */

	public SystemUser_Transferable[] transmitSystemUsersButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final SystemUser_Transferable[] users = new SystemUser_Transferable[length];
		System.arraycopy(storableObjects, 0, users, 0, length);
		return users;
	}

	public Domain_Transferable[] transmitDomainsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Domain_Transferable[] domains = new Domain_Transferable[length];
		System.arraycopy(storableObjects, 0, domains, 0, length);
		return domains;
	}

	public Server_Transferable[] transmitServersButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Server_Transferable[] servers = new Server_Transferable[length];
		System.arraycopy(storableObjects, 0, servers, 0, length);
		return servers;
	}

	public MCM_Transferable[] transmitMCMsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final MCM_Transferable[] mcms = new MCM_Transferable[length];
		System.arraycopy(storableObjects, 0, mcms, 0, length);
		return mcms;
	}

	public ServerProcess_Transferable[] transmitServerProcessesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final ServerProcess_Transferable[] serverProcesses = new ServerProcess_Transferable[length];
		System.arraycopy(storableObjects, 0, serverProcesses, 0, length);
		return serverProcesses;
	}
}
