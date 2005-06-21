/*-
 * $Id: CORBAAdministrationObjectLoader.java,v 1.27 2005/06/21 14:13:37 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.IdlDomain;
import com.syrus.AMFICOM.administration.corba.IdlMCM;
import com.syrus.AMFICOM.administration.corba.IdlServerProcess;
import com.syrus.AMFICOM.administration.corba.IdlServer;
import com.syrus.AMFICOM.administration.corba.IdlSystemUser;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.27 $, $Date: 2005/06/21 14:13:37 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public final class CORBAAdministrationObjectLoader extends CORBAObjectLoader implements AdministrationObjectLoader {

	public CORBAAdministrationObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadSystemUsers(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SYSTEMUSER_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitSystemUsers(idsT, sessionKey);
			}
		});
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.DOMAIN_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitDomains(idsT, sessionKey);
			}
		});
	}

	public Set loadServers(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SERVER_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServers(idsT, sessionKey);
			}
		});
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MCM_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMCMs(idsT, sessionKey);
			}
		});
	}

	public Set loadServerProcesses(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SERVERPROCESS_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServerProcesses(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadSystemUsersButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SYSTEMUSER_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitSystemUsersButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.DOMAIN_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitDomainsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SERVER_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServersButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MCM_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMCMsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SERVERPROCESS_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final IdlIdentifier[] idsT,
					final IdlSessionKey sessionKey,
					final IdlStorableObjectCondition conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServerProcessesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveSystemUsers(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SYSTEMUSER_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveSystemUsers((IdlSystemUser[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveDomains(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.DOMAIN_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveDomains((IdlDomain[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveServers(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SERVER_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveServers((IdlServer[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMCMs(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MCM_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMCMs((IdlMCM[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveServerProcesses(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SERVERPROCESS_CODE, storableObjects, new ReceiveProcedure() {
			public IdlStorableObject[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final IdlSessionKey sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveServerProcesses((IdlServerProcess[]) transferables, force, sessionKey);
			}
		});
	}
}
