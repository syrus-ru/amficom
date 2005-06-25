/*
 * $Id: CMAdministrationTransmit.java,v 1.31 2005/06/25 17:07:50 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.administration.corba.IdlMCM;
import com.syrus.AMFICOM.administration.corba.IdlServerProcess;
import com.syrus.AMFICOM.administration.corba.IdlServer;
import com.syrus.AMFICOM.administration.corba.IdlSystemUser;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.31 $, $Date: 2005/06/25 17:07:50 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public abstract class CMAdministrationTransmit extends CMGeneralTransmit {

	private static final long serialVersionUID = -6573358727271115828L;

	CMAdministrationTransmit(final ORB orb) {
		super(orb);
	}

	/* Transmit multiple objects*/

	public IdlSystemUser[] transmitSystemUsers(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlSystemUser[] users = new IdlSystemUser[length];
		System.arraycopy(storableObjects, 0, users, 0, length);
		return users;
	}

	public IdlDomain[] transmitDomains(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlDomain[] domains = new IdlDomain[length];
		System.arraycopy(storableObjects, 0, domains, 0, length);
		return domains;
	}

	public IdlServer[] transmitServers(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlServer[] servers = new IdlServer[length];
		System.arraycopy(storableObjects, 0, servers, 0, length);
		return servers;
	}

	public IdlMCM[] transmitMCMs(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlMCM[] mcms = new IdlMCM[length];
		System.arraycopy(storableObjects, 0, mcms, 0, length);
		return mcms;
	}

	public IdlServerProcess[] transmitServerProcesses(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjects.length;
		final IdlServerProcess[] serverProcesses = new IdlServerProcess[length];
		System.arraycopy(storableObjects, 0, serverProcesses, 0, length);
		return serverProcesses;
	}



	/* Transmit multiple objects but ids by condition */

	public IdlSystemUser[] transmitSystemUsersButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlSystemUser[] users = new IdlSystemUser[length];
		System.arraycopy(storableObjects, 0, users, 0, length);
		return users;
	}

	public IdlDomain[] transmitDomainsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlDomain[] domains = new IdlDomain[length];
		System.arraycopy(storableObjects, 0, domains, 0, length);
		return domains;
	}

	public IdlServer[] transmitServersButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlServer[] servers = new IdlServer[length];
		System.arraycopy(storableObjects, 0, servers, 0, length);
		return servers;
	}

	public IdlMCM[] transmitMCMsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMCM[] mcms = new IdlMCM[length];
		System.arraycopy(storableObjects, 0, mcms, 0, length);
		return mcms;
	}

	public IdlServerProcess[] transmitServerProcessesButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjects = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlServerProcess[] serverProcesses = new IdlServerProcess[length];
		System.arraycopy(storableObjects, 0, serverProcesses, 0, length);
		return serverProcesses;
	}
}
