/*
 * $Id: DatabaseAdministrationObjectLoader.java,v 1.5 2005/05/01 17:29:09 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/01 17:29:09 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseAdministrationObjectLoader extends DatabaseObjectLoader implements AdministrationObjectLoader {

	/* Load multiple objects*/

	public Set loadUsers(Set ids) throws ApplicationException {
		UserDatabase database = AdministrationDatabaseContext.getUserDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		DomainDatabase database = AdministrationDatabaseContext.getDomainDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadServers(Set ids) throws ApplicationException {
		ServerDatabase database = AdministrationDatabaseContext.getServerDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		MCMDatabase database = AdministrationDatabaseContext.getMCMDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadServerProcesses(Set ids) throws ApplicationException {
		ServerProcessDatabase database = AdministrationDatabaseContext.getServerProcessDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	//  public PermissionAttributes loadPermissionAttributes(Identifier id) throws RetrieveObjectException {
	//      return new PermissionAttributes(id);
	//  }



	/* Load multiple objects but ids*/

	public Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		UserDatabase database = AdministrationDatabaseContext.getUserDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		DomainDatabase database = AdministrationDatabaseContext.getDomainDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		ServerDatabase database = AdministrationDatabaseContext.getServerDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MCMDatabase database = AdministrationDatabaseContext.getMCMDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		ServerProcessDatabase database = AdministrationDatabaseContext.getServerProcessDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	//  public PermissionAttributes loadPermissionAttributesButIds(Identifier id) throws ApplicationException {
	//      return new PermissionAttributes(id);
	//  }



	/* Save multiple objects*/

	public void saveUsers(Set collection, boolean force) throws ApplicationException {
		UserDatabase database = AdministrationDatabaseContext.getUserDatabase();
		database.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveDomains(Set collection, boolean force) throws ApplicationException {
		DomainDatabase database = AdministrationDatabaseContext.getDomainDatabase();
		database.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveServers(Set collection, boolean force) throws ApplicationException {
		ServerDatabase database = AdministrationDatabaseContext.getServerDatabase();
		database.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMCMs(Set collection, boolean force) throws ApplicationException {
		MCMDatabase database = AdministrationDatabaseContext.getMCMDatabase();
		database.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveServerProcesses(Set collection, boolean force) throws ApplicationException {
		ServerProcessDatabase database = AdministrationDatabaseContext.getServerProcessDatabase();
		database.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException ;



	/*	Refresh*/

	public Set refresh(Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = AdministrationDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}



	/*	Delete*/

	public void delete(final Set identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;
		/**
		 * @todo: use Trove collection instead java.util.Map
		 */
		final Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifiable identifiable = (Identifiable) identifiableIterator.next();
			final Short entityCode = new Short(identifiable.getId().getMajor());
			Set entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(identifiable);
		}

		for (final Iterator entityCodeIterator = map.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			final Set entityObjects = (Set) map.get(entityCode);
			final StorableObjectDatabase storableObjectDatabase = AdministrationDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
