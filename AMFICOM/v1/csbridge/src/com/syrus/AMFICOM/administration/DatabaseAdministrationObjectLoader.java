/*
 * $Id: DatabaseAdministrationObjectLoader.java,v 1.12 2005/06/22 19:29:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.12 $, $Date: 2005/06/22 19:29:31 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseAdministrationObjectLoader extends DatabaseObjectLoader implements AdministrationObjectLoader {

	/* Load multiple objects*/

	public Set loadSystemUsers(Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadDomains(Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadServers(Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadMCMs(Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadServerProcesses(Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadSystemUsersButIds(StorableObjectCondition condition, Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set<Identifier> ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	/* Save multiple objects*/

	public void saveSystemUsers(Set<SystemUser> objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveDomains(Set<Domain> objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveServers(Set<Server> objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMCMs(Set<MCM> objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveServerProcesses(Set<ServerProcess> objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
