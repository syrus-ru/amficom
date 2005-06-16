/*-
 * $Id: CORBAAdministrationObjectLoader.java,v 1.22 2005/06/14 11:46:28 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.ServerProcess_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAObjectLoader;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.22 $, $Date: 2005/06/14 11:46:28 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public final class CORBAAdministrationObjectLoader extends CORBAObjectLoader implements AdministrationObjectLoader {

	public CORBAAdministrationObjectLoader(final ServerConnectionManager serverConnectionManager) {
		super(serverConnectionManager);
	}



	/* Load multiple objects*/

	public Set loadUsers(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.USER_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitUsers(idsT, sessionKey);
			}
		});
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.DOMAIN_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitDomains(idsT, sessionKey);
			}
		});
	}

	public Set loadServers(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SERVER_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServers(idsT, sessionKey);
			}
		});
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MCM_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMCMs(idsT, sessionKey);
			}
		});
	}

	public Set loadServerProcesses(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SERVERPROCESS_ENTITY_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServerProcesses(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadUsersButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.USER_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitUsersButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.DOMAIN_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitDomainsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SERVER_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServersButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.MCM_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitMCMsButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsByCondition(ObjectEntities.SERVERPROCESS_ENTITY_CODE, ids, condition, new TransmitButIdsByConditionProcedure() {
			public IDLEntity[] transmitStorableObjectsButIdsCondition(
					final CommonServer server,
					final Identifier_Transferable[] idsT,
					final SessionKey_Transferable sessionKey,
					final StorableObjectCondition_Transferable conditionT)
					throws AMFICOMRemoteException {
				return ((CMServer) server).transmitServerProcessesButIdsByCondition(idsT, conditionT, sessionKey);
			}
		});
	}



	/*	Save multiple objects*/

	public void saveUsers(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.USER_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveUsers((User_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveDomains(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.DOMAIN_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveDomains((Domain_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveServers(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SERVER_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveServers((Server_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveMCMs(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.MCM_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveMCMs((MCM_Transferable[]) transferables, force, sessionKey);
			}
		});
	}

	public void saveServerProcesses(final Set storableObjects, final boolean force) throws ApplicationException {
		super.saveStorableObjects(ObjectEntities.SERVERPROCESS_ENTITY_CODE, storableObjects, new ReceiveProcedure() {
			public StorableObject_Transferable[] receiveStorableObjects(
					final CommonServer server,
					final IDLEntity transferables[],
					final SessionKey_Transferable sessionKey)
					throws AMFICOMRemoteException {
				return ((CMServer) server).receiveServerProcesses((ServerProcess_Transferable[]) transferables, force, sessionKey);
			}
		});
	}
}
