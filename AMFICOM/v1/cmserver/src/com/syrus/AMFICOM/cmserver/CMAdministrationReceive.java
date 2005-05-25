/*-
 * $Id: CMAdministrationReceive.java,v 1.14 2005/05/25 13:01:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.ServerProcess_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.14 $, $Date: 2005/05/25 13:01:03 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMAdministrationReceive extends CMGeneralReceive {
	private static final long serialVersionUID = 1181253000011968750L;

	public final StorableObject_Transferable[] receiveUsers(
			final User_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.USER_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveDomains(
			final Domain_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.DOMAIN_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveServers(
			final Server_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SERVER_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveMCMs(
			final MCM_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MCM_ENTITY_CODE, transferables, force, sessionKey);
	}

	public final StorableObject_Transferable[] receiveServerProcesses(
			final ServerProcess_Transferable transferables[],
			final boolean force,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SERVERPROCESS_ENTITY_CODE, transferables, force, sessionKey);
	}
}
