/*-
 * $Id: CMAdministrationReceive.java,v 1.19 2005/06/25 17:07:50 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.administration.corba.IdlMCM;
import com.syrus.AMFICOM.administration.corba.IdlServerProcess;
import com.syrus.AMFICOM.administration.corba.IdlServer;
import com.syrus.AMFICOM.administration.corba.IdlSystemUser;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.19 $, $Date: 2005/06/25 17:07:50 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public abstract class CMAdministrationReceive extends CMGeneralReceive {
	private static final long serialVersionUID = 1181253000011968750L;

	CMAdministrationReceive(final ORB orb) {
		super(orb);
	}

	public final IdlStorableObject[] receiveSystemUsers(
			final IdlSystemUser transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SYSTEMUSER_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveDomains(
			final IdlDomain transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.DOMAIN_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveServers(
			final IdlServer transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SERVER_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveMCMs(
			final IdlMCM transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.MCM_CODE, transferables, force, sessionKey);
	}

	public final IdlStorableObject[] receiveServerProcesses(
			final IdlServerProcess transferables[],
			final boolean force,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		return super.receiveStorableObjects(ObjectEntities.SERVERPROCESS_CODE, transferables, force, sessionKey);
	}
}
