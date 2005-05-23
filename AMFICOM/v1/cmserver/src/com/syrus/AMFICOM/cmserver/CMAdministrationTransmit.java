/*
 * $Id: CMAdministrationTransmit.java,v 1.21 2005/05/23 09:01:04 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.ServerProcess_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.21 $, $Date: 2005/05/23 09:01:04 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public abstract class CMAdministrationTransmit extends CMGeneralTransmit {
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

	/*	Refresh*/

	/**
	 * @deprecated
	 */
	public Identifier_Transferable[] transmitRefreshedAdministrationObjects(StorableObject_Transferable[] storableObjectsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final Identifier_TransferableHolder userId = new Identifier_TransferableHolder();
		final Identifier_TransferableHolder domainId = new Identifier_TransferableHolder();
		this.validateAccess(sessionKeyT, userId, domainId);

		Map storableObjectsTMap = new HashMap();
		for (int i = 0; i < storableObjectsT.length; i++)
			storableObjectsTMap.put(new Identifier(storableObjectsT[i].id), storableObjectsT[i]);

		try {
			StorableObjectPool.refresh();

			Set storableObjects = StorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				final StorableObject so = (StorableObject) it.next();
				final StorableObject_Transferable soT = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				// Remove objects with older versions as well as objects with the same versions.
				// Not only with older ones!
				if (!so.hasNewerVersion(soT.version))
					it.remove();
			}

			return Identifier.createTransferables(storableObjects);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, throwable.getMessage());
		}
	}

}
