/*
 * $Id: DatabaseAdministrationObjectLoader.java,v 1.22 2005/04/05 10:32:31 arseniy Exp $
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

import com.syrus.AMFICOM.general.AbstractObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.22 $, $Date: 2005/04/05 10:32:31 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public class DatabaseAdministrationObjectLoader extends AbstractObjectLoader implements AdministrationObjectLoader {

	public User loadUser(Identifier id) throws ApplicationException {
		return new User(id);
	}

	public Domain loadDomain(Identifier id) throws ApplicationException {
		return new Domain(id);
	}

	public Server loadServer(Identifier id) throws ApplicationException {
		return new Server(id);
	}

	public MCM loadMCM(Identifier id) throws ApplicationException {
		return new MCM(id);
	}

	//	public PermissionAttributes loadPermissionAttributes(Identifier id) throws ApplicationException {
	//		return new PermissionAttributes(id);
	//	}




	// for multiple objects

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

	//  public PermissionAttributes loadPermissionAttributes(Identifier id) throws RetrieveObjectException {
	//      return new PermissionAttributes(id);
	//  }

	/* Load Administration StorableObject but argument ids */





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

	//  public PermissionAttributes loadPermissionAttributesButIds(Identifier id) throws ApplicationException {
	//      return new PermissionAttributes(id);
	//  }




	public void saveUser(User user, boolean force) throws ApplicationException {
		UserDatabase database = AdministrationDatabaseContext.getUserDatabase();
		database.update(user, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveDomain(Domain domain, boolean force) throws ApplicationException {
		DomainDatabase database = AdministrationDatabaseContext.getDomainDatabase();
		database.update(domain, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveServer(Server server, boolean force) throws ApplicationException {
		ServerDatabase database = AdministrationDatabaseContext.getServerDatabase();
		database.update(server, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMCM(MCM mcm, boolean force) throws ApplicationException {
		MCMDatabase database = AdministrationDatabaseContext.getMCMDatabase();
		database.update(mcm, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException ;





	public void saveUsers(Set collection, boolean force) throws ApplicationException {
		UserDatabase database = AdministrationDatabaseContext.getUserDatabase();
		database.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveDomains(Set collection, boolean force) throws ApplicationException {
		DomainDatabase database = AdministrationDatabaseContext.getDomainDatabase();
		database.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveServers(Set collection, boolean force) throws ApplicationException {
		ServerDatabase database = AdministrationDatabaseContext.getServerDatabase();
		database.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMCMs(Set collection, boolean force) throws ApplicationException {
		MCMDatabase database = AdministrationDatabaseContext.getMCMDatabase();
		database.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException ;




	public Set refresh(Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = AdministrationDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}




	public void delete(Identifier id) throws IllegalDataException {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = AdministrationDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(Set objects) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		Set entityObjects;
		Short entityCode;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identifiable)
					identifier = ((Identifiable) object).getId();
				else
					throw new IllegalDataException("DatabaseAdministrationObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identifiable");

			entityCode = new Short(identifier.getMajor());
			entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(object);
		}

		StorableObjectDatabase storableObjectDatabase;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			entityCode = (Short) it.next();
			entityObjects = (Set) map.get(entityCode);
			storableObjectDatabase = AdministrationDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
