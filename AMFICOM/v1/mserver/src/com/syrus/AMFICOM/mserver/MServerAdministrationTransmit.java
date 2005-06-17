/*
 * $Id: MServerAdministrationTransmit.java,v 1.6 2005/06/17 13:07:00 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/17 13:07:00 $
 * @author $Author: bass $
 * @module mserver_v1
 */
abstract class MServerAdministrationTransmit extends MServerGeneralTransmit {

	private static final long serialVersionUID = 76258492809647849L;

	/* Transmit multiple objects*/

	public Server_Transferable[] transmitServers(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final Server_Transferable[] ret = new Server_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public MCM_Transferable[] transmitMCMs(final IdlIdentifier[] idsT, final SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final MCM_Transferable[] ret = new MCM_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

}
