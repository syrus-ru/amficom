/*
* $Id: MCMAdministrationObjectLoader.java,v 1.30 2005/06/21 12:44:30 bass Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.administration.AdministrationObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;


/**
 * @version $Revision: 1.30 $, $Date: 2005/06/21 12:44:30 $
 * @author $Author: bass $
 * @module mcm_v1
 */
final class MCMAdministrationObjectLoader extends MCMObjectLoader implements AdministrationObjectLoader {

	public MCMAdministrationObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager);
	}

	/* Load multiple objects*/

	public Set loadServers(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.SERVER_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitServers(idsT, sessionKey);
			}
		});
	}

	public Set loadMCMs(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ObjectEntities.MCM_CODE, ids, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					IdlIdentifier[] idsT,
					IdlSessionKey sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitMCMs(idsT, sessionKey);
			}
		});
	}




	/*
	 * MCM do not need in all below methods
	 * */

	public Set loadSystemUsers(final Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadDomains(final Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}

	public Set loadServerProcesses(final Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids);
	}




	public Set loadSystemUsersButIds(final StorableObjectCondition condition, final Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadDomainsButIds(final StorableObjectCondition condition, final Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadServersButIds(final StorableObjectCondition condition, final Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadMCMsButIds(final StorableObjectCondition condition, final Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadServerProcessesButIds(final StorableObjectCondition condition, final Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}





	public void saveSystemUsers(final Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveDomains(final Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveServers(final Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveMCMs(final Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveServerProcesses(final Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
}
