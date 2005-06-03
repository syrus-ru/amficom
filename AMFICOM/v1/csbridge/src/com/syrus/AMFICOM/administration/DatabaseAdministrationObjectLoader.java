/*
 * $Id: DatabaseAdministrationObjectLoader.java,v 1.9 2005/06/03 15:23:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.9 $, $Date: 2005/06/03 15:23:58 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseAdministrationObjectLoader extends DatabaseObjectLoader implements AdministrationObjectLoader {

	/* Load multiple objects*/

	public Set loadUsers(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadServers(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadServerProcesses(Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIds(condition, ids);
	}

	/* Save multiple objects*/

	public void saveUsers(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveDomains(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveServers(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveMCMs(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}

	public void saveServerProcesses(Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
