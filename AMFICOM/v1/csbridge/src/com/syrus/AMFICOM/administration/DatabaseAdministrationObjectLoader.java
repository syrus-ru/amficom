/*
 * $Id: DatabaseAdministrationObjectLoader.java,v 1.7 2005/05/24 13:24:58 bass Exp $
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
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.7 $, $Date: 2005/05/24 13:24:58 $
 * @author $Author: bass $
 * @module csbridge_v1
 */

public class DatabaseAdministrationObjectLoader extends DatabaseObjectLoader implements AdministrationObjectLoader {

	/* Load multiple objects*/

	public Set loadUsers(Set ids) throws ApplicationException {
		UserDatabase database = (UserDatabase) DatabaseContext.getDatabase(ObjectEntities.USER_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		DomainDatabase database = (DomainDatabase) DatabaseContext.getDatabase(ObjectEntities.DOMAIN_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadServers(Set ids) throws ApplicationException {
		ServerDatabase database = (ServerDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVER_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		MCMDatabase database = (MCMDatabase) DatabaseContext.getDatabase(ObjectEntities.MCM_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadServerProcesses(Set ids) throws ApplicationException {
		ServerProcessDatabase database = (ServerProcessDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	//  public PermissionAttributes loadPermissionAttributes(Identifier id) throws RetrieveObjectException {
	//      return new PermissionAttributes(id);
	//  }



	/* Load multiple objects but ids*/

	public Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		UserDatabase database = (UserDatabase) DatabaseContext.getDatabase(ObjectEntities.USER_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		DomainDatabase database = (DomainDatabase) DatabaseContext.getDatabase(ObjectEntities.DOMAIN_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		ServerDatabase database = (ServerDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVER_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		MCMDatabase database = (MCMDatabase) DatabaseContext.getDatabase(ObjectEntities.MCM_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		ServerProcessDatabase database = (ServerProcessDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	//  public PermissionAttributes loadPermissionAttributesButIds(Identifier id) throws ApplicationException {
	//      return new PermissionAttributes(id);
	//  }



	/* Save multiple objects*/

	public void saveUsers(Set collection, boolean force) throws ApplicationException {
		UserDatabase database = (UserDatabase) DatabaseContext.getDatabase(ObjectEntities.USER_ENTITY_CODE);
		database.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveDomains(Set collection, boolean force) throws ApplicationException {
		DomainDatabase database = (DomainDatabase) DatabaseContext.getDatabase(ObjectEntities.DOMAIN_ENTITY_CODE);
		database.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveServers(Set collection, boolean force) throws ApplicationException {
		ServerDatabase database = (ServerDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVER_ENTITY_CODE);
		database.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMCMs(Set collection, boolean force) throws ApplicationException {
		MCMDatabase database = (MCMDatabase) DatabaseContext.getDatabase(ObjectEntities.MCM_ENTITY_CODE);
		database.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveServerProcesses(Set collection, boolean force) throws ApplicationException {
		ServerProcessDatabase database = (ServerProcessDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_ENTITY_CODE);
		database.update(collection, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException ;



	/*	Refresh*/

	public Set refresh(Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);

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
			final StorableObjectDatabase storableObjectDatabase = DatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}
}
