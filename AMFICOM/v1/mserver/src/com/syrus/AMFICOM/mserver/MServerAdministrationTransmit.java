/*
 * $Id: MServerAdministrationTransmit.java,v 1.9 2005/06/25 15:12:18 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.administration.corba.IdlMCM;
import com.syrus.AMFICOM.administration.corba.IdlServer;
import com.syrus.AMFICOM.administration.corba.IdlSystemUser;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.9 $, $Date: 2005/06/25 15:12:18 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */
abstract class MServerAdministrationTransmit extends MServerGeneralTransmit {

	private static final long serialVersionUID = 76258492809647849L;

	/* Transmit multiple objects*/

	public IdlSystemUser[] transmitSystemUsers(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlSystemUser[] ret = new IdlSystemUser[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlDomain[] transmitDomains(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlDomain[] ret = new IdlDomain[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlServer[] transmitServers(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlServer[] ret = new IdlServer[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlMCM[] transmitMCMs(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlMCM[] ret = new IdlMCM[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

}
