/*
 * $Id: DatabaseAdministrationObjectLoader.java,v 1.11 2005/06/16 10:41:43 bass Exp $
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
 * @version $Revision: 1.11 $, $Date: 2005/06/16 10:41:43 $
 * @author $Author: bass $
 * @module csbridge_v1
 */

public class DatabaseAdministrationObjectLoader extends DatabaseObjectLoader implements AdministrationObjectLoader {

	/* Load multiple objects*/

	public Set loadSystemUsers(Set ids) throws ApplicationException {
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

	public Set loadSystemUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	/* Save multiple objects*/

	public void saveSystemUsers(Set objects, boolean force) throws ApplicationException {
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
