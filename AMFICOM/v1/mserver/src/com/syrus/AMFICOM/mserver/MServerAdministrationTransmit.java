/*
 * $Id: MServerAdministrationTransmit.java,v 1.2 2005/05/18 13:25:44 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.Iterator;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/18 13:25:44 $
 * @author $Author: bass $
 * @module mserver_v1
 */
abstract class MServerAdministrationTransmit extends MServerGeneralTransmit {

	private static final long serialVersionUID = 76258492809647849L;

	/* Transmit multiple objects*/

	public Server_Transferable[] transmitServers(Identifier_Transferable[] idsT)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getAdministrationObjects(idsT);

		Server_Transferable[] transferables = new Server_Transferable[objects.size()];
		int i = 0;
		Server object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Server) it.next();
			transferables[i] = (Server_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MCM_Transferable[] transmitMCMs(Identifier_Transferable[] idsT)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getAdministrationObjects(idsT);

		MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
		int i = 0;
		MCM object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MCM) it.next();
			transferables[i] = (MCM_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getAdministrationObjects(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		try {
			java.util.Set ids = Identifier.fromTransferables(idsT);
			java.util.Set objects = StorableObjectPool.getStorableObjects(ids, true);
			return objects;
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

}
