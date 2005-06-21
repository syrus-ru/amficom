/*-
 * $Id: CMAdministrationReceive.java,v 1.17 2005/06/21 12:44:32 bass Exp $
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
import com.syrus.AMFICOM.administration.corba.SystemUser_Transferable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.17 $, $Date: 2005/06/21 12:44:32 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMAdministrationReceive extends CMGeneralReceive {
	private static final long serialVersionUID = 1181253000011968750L;

	public final IdlStorableObject[] receiveSystemUsers(
			final SystemUser_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SYSTEMUSER_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveDomains(
			final Domain_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.DOMAIN_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveServers(
			final Server_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SERVER_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMCMs(
			final MCM_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MCM_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveServerProcesses(
			final ServerProcess_Transferable transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SERVERPROCESS_CODE, transferables, force, sessionKey);
	}
}
