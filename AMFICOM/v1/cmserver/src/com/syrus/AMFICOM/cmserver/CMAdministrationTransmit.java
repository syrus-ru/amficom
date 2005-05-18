/*
 * $Id: CMAdministrationTransmit.java,v 1.20 2005/05/18 13:11:21 bass Exp $
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

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.ServerProcess_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.20 $, $Date: 2005/05/18 13:11:21 $
 * @author $Author: bass $
 * @module cmserver_v1
 */

public abstract class CMAdministrationTransmit extends CMGeneralTransmit {

	private static final long serialVersionUID = 5322471674445475587L;



	/* Transmit multiple objects*/

	public User_Transferable[] transmitUsers(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		User_Transferable[] transferables = new User_Transferable[objects.size()];
		int i = 0;
		User object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (User) it.next();
			transferables[i] = (User_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Domain_Transferable[] transmitDomains(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		Domain_Transferable[] transferables = new Domain_Transferable[objects.size()];
		int i = 0;
		Domain object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Domain) it.next();
			transferables[i] = (Domain_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Server_Transferable[] transmitServers(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		Server_Transferable[] transferables = new Server_Transferable[objects.size()];
		int i = 0;
		Server object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Server) it.next();
			transferables[i] = (Server_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MCM_Transferable[] transmitMCMs(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
		int i = 0;
		MCM object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MCM) it.next();
			transferables[i] = (MCM_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public ServerProcess_Transferable[] transmitServerProcesses(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjects(idsT);

		ServerProcess_Transferable[] transferables = new ServerProcess_Transferable[objects.size()];
		int i = 0;
		ServerProcess object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (ServerProcess) it.next();
			transferables[i] = (ServerProcess_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private Set getObjects(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		try {
			Set ids = Identifier.fromTransferables(idsT);
			Set objects = StorableObjectPool.getStorableObjects(ids, true);
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


	/* Transmit multiple objects but ids by condition*/

	public User_Transferable[] transmitUsersButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		User_Transferable[] transferables = new User_Transferable[objects.size()];
		int i = 0;
		User object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (User) it.next();
			transferables[i] = (User_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Domain_Transferable[] transmitDomainsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Domain_Transferable[] transferables = new Domain_Transferable[objects.size()];
		int i = 0;
		Domain object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Domain) it.next();
			transferables[i] = (Domain_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Server_Transferable[] transmitServersButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Server_Transferable[] transferables = new Server_Transferable[objects.size()];
		int i = 0;
		Server object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Server) it.next();
			transferables[i] = (Server_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MCM_Transferable[] transmitMCMsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		MCM_Transferable[] transferables = new MCM_Transferable[objects.size()];
		int i = 0;
		MCM object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MCM) it.next();
			transferables[i] = (MCM_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public ServerProcess_Transferable[] transmitServerProcessesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		ServerProcess_Transferable[] transferables = new ServerProcess_Transferable[objects.size()];
		int i = 0;
		ServerProcess object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (ServerProcess) it.next();
			transferables[i] = (ServerProcess_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private Set getObjectsButIdsCondition(Identifier_Transferable[] idsT, StorableObjectCondition_Transferable conditionT)
			throws AMFICOMRemoteException {
		try {

			StorableObjectCondition condition = null;
			try {
				condition = StorableObjectConditionBuilder.restoreCondition(conditionT);
			}
			catch (IllegalDataException ide) {
				throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
						CompletionStatus.COMPLETED_NO,
						"Cannot restore condition -- " + ide.getMessage());
			}

			try {
				Set ids = Identifier.fromTransferables(idsT);
				Set objects = StorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
				return objects;
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
			}
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	/*	Refresh*/

	public Identifier_Transferable[] transmitRefreshedAdministrationObjects(StorableObject_Transferable[] storableObjectsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

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
