/*
 * $Id: DatabaseAdministrationObjectLoader.java,v 1.8 2005/05/26 19:13:24 bass Exp $
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
 * @version $Revision: 1.8 $, $Date: 2005/05/26 19:13:24 $
 * @author $Author: bass $
 * @module csbridge_v1
 */

public class DatabaseAdministrationObjectLoader extends DatabaseObjectLoader implements AdministrationObjectLoader {

	/* Load multiple objects*/

	public Set loadUsers(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadServers(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadServerProcesses(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	/* Load multiple objects but ids*/

	public Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	/* Save multiple objects*/

	public void saveUsers(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveDomains(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveServers(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveMCMs(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}

	public void saveServerProcesses(Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}
}
