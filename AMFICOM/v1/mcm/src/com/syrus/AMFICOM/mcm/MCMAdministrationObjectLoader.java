/*
* $Id: MCMAdministrationObjectLoader.java,v 1.24 2005/06/01 20:54:59 arseniy Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.AdministrationObjectLoader;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;


/**
 * @version $Revision: 1.24 $, $Date: 2005/06/01 20:54:59 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMAdministrationObjectLoader extends MCMObjectLoader implements AdministrationObjectLoader {

	public MCMAdministrationObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager, new DatabaseAdministrationObjectLoader());
	}

	/* Load multiple objects*/

	public Set loadServers(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.SERVER_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitServers(idsT, sessionKey);
			}
		});
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.MCM_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMCMs(idsT, sessionKey);
			}
		});
	}




	/*
	 * MCM do not need in all below methods
	 * */

	public Set loadUsers(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadDomains(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadServerProcesses(Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}




	public Set loadUsersButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}





	public void saveUser(User user, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, user: " + user.getId() + ", force: " + force);
	}

	public void saveDomain(Domain domain, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, domain: " + domain.getId() + ", force: " + force);
	}

	public void saveServer(Server server, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, server: " + server.getId() + ", force: " + force);
	}

	public void saveMCM(MCM mcm, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, mcm: " + mcm.getId() + ", force: " + force);
	}

	public void saveServerProcess(ServerProcess serverProcess, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, server: " + serverProcess.getId() + ", force: " + force);
	}





	public void saveUsers(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveDomains(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveServers(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveMCMs(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveServerProcesses(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
}
