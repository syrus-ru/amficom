/*
 * $Id: CMAdministrationTransmit.java,v 1.27 2005/06/16 10:46:11 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.cmserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.ServerProcess_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.SystemUser_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.27 $, $Date: 2005/06/16 10:46:11 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public abstract class CMAdministrationTransmit extends CMGeneralTransmit {

	private static final long serialVersionUID = -6573358727271115828L;


	/* Transmit multiple objects*/

	public SystemUser_Transferable[] transmitSystemUsers(final Identifier_Transferable[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final SystemUser_Transferable[] users = new SystemUser_Transferable[length];
		System.arraycopy(storableObjects, 0, users, 0, length);
		return users;
	}

	public Domain_Transferable[] transmitDomains(final Identifier_Transferable[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Domain_Transferable[] domains = new Domain_Transferable[length];
		System.arraycopy(storableObjects, 0, domains, 0, length);
		return domains;
	}

	public Server_Transferable[] transmitServers(final Identifier_Transferable[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final Server_Transferable[] servers = new Server_Transferable[length];
		System.arraycopy(storableObjects, 0, servers, 0, length);
		return servers;
	}

	public MCM_Transferable[] transmitMCMs(final Identifier_Transferable[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final MCM_Transferable[] mcms = new MCM_Transferable[length];
		System.arraycopy(storableObjects, 0, mcms, 0, length);
		return mcms;
	}

	public ServerProcess_Transferable[] transmitServerProcesses(final Identifier_Transferable[] idsT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final ServerProcess_Transferable[] serverProcesses = new ServerProcess_Transferable[length];
		System.arraycopy(storableObjects, 0, serverProcesses, 0, length);
		return serverProcesses;
	}



	/* Transmit multiple objects but ids by condition */

	public SystemUser_Transferable[] transmitSystemUsersButIdsByCondition(final Identifier_Transferable[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final SystemUser_Transferable[] users = new SystemUser_Transferable[length];
		System.arraycopy(storableObjects, 0, users, 0, length);
		return users;
	}

	public Domain_Transferable[] transmitDomainsButIdsByCondition(final Identifier_Transferable[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Domain_Transferable[] domains = new Domain_Transferable[length];
		System.arraycopy(storableObjects, 0, domains, 0, length);
		return domains;
	}

	public Server_Transferable[] transmitServersButIdsByCondition(final Identifier_Transferable[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Server_Transferable[] servers = new Server_Transferable[length];
		System.arraycopy(storableObjects, 0, servers, 0, length);
		return servers;
	}

	public MCM_Transferable[] transmitMCMsButIdsByCondition(final Identifier_Transferable[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final MCM_Transferable[] mcms = new MCM_Transferable[length];
		System.arraycopy(storableObjects, 0, mcms, 0, length);
		return mcms;
	}

	public ServerProcess_Transferable[] transmitServerProcessesButIdsByCondition(final Identifier_Transferable[] idsT,
			final StorableObjectCondition_Transferable conditionT,
			final SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final ServerProcess_Transferable[] serverProcesses = new ServerProcess_Transferable[length];
		System.arraycopy(storableObjects, 0, serverProcesses, 0, length);
		return serverProcesses;
	}
}
