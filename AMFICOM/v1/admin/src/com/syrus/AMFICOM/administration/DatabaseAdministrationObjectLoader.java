/*
 * $Id: DatabaseAdministrationObjectLoader.java,v 1.14 2005/02/24 14:59:46 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.14 $, $Date: 2005/02/24 14:59:46 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public class DatabaseAdministrationObjectLoader implements AdministrationObjectLoader {

	public User loadUser(Identifier id) throws DatabaseException {
		return new User(id);
	}

	public Domain loadDomain(Identifier id) throws DatabaseException {
		return new Domain(id);
	}

	public Server loadServer(Identifier id) throws DatabaseException {
		return new Server(id);
	}

	public MCM loadMCM(Identifier id) throws DatabaseException {
		return new MCM(id);
	}

	//	public PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException {
	//		return new PermissionAttributes(id);
	//	}




	// for multiple objects

	public Collection loadUsers(Collection ids) throws DatabaseException {
		UserDatabase database = (UserDatabase) AdministrationDatabaseContext.userDatabase;
		Collection collection = null;
		try {
			collection = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadUsers | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Collection loadDomains(Collection ids) throws DatabaseException {
		DomainDatabase database = (DomainDatabase) AdministrationDatabaseContext.domainDatabase;
		Collection collection = null;
		try {
			collection = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Collection loadServers(Collection ids) throws DatabaseException {
		ServerDatabase database = (ServerDatabase) AdministrationDatabaseContext.serverDatabase;
		Collection collection = null;
		try {
			collection = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Collection loadMCMs(Collection ids) throws DatabaseException {
		MCMDatabase database = (MCMDatabase) AdministrationDatabaseContext.mcmDatabase;
		Collection collection = null;
		try {
			collection = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadMCMs | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	//  public PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException {
	//      return new PermissionAttributes(id);
	//  }

	/* Load Administration StorableObject but argument ids */





	public Collection loadUsersButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException {
		UserDatabase database = (UserDatabase) AdministrationDatabaseContext.userDatabase;
		Collection collection = null;
		try {
			collection = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadUsersButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Collection loadDomainsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException {
		DomainDatabase database = (DomainDatabase) AdministrationDatabaseContext.domainDatabase;
		Collection collection = null;
		try {
			collection = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Collection loadServersButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException {
		ServerDatabase database = (ServerDatabase) AdministrationDatabaseContext.serverDatabase;
		Collection collection = null;
		try {
			collection = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	public Collection loadMCMsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException {
		MCMDatabase database = (MCMDatabase) AdministrationDatabaseContext.mcmDatabase;
		Collection collection = null;
		try {
			collection = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadMCMsButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return collection;
	}

	//  public PermissionAttributes loadPermissionAttributesButIds(Identifier id) throws DatabaseException {
	//      return new PermissionAttributes(id);
	//  }




	public void saveUser(User user, boolean force) throws DatabaseException, VersionCollisionException {
		UserDatabase database = (UserDatabase) AdministrationDatabaseContext.userDatabase;
		database.update(user, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveDomain(Domain domain, boolean force) throws DatabaseException, VersionCollisionException {
		DomainDatabase database = (DomainDatabase) AdministrationDatabaseContext.domainDatabase;
		database.update(domain, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveServer(Server server, boolean force) throws DatabaseException, VersionCollisionException {
		ServerDatabase database = (ServerDatabase) AdministrationDatabaseContext.serverDatabase;
		database.update(server, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMCM(MCM mcm, boolean force) throws DatabaseException, VersionCollisionException {
		MCMDatabase database = (MCMDatabase) AdministrationDatabaseContext.mcmDatabase;
		database.update(mcm, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException ;





	public void saveUsers(Collection collection, boolean force) throws DatabaseException, VersionCollisionException {
		UserDatabase database = (UserDatabase) AdministrationDatabaseContext.userDatabase;
		database.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveDomains(Collection collection, boolean force) throws DatabaseException, VersionCollisionException {
		DomainDatabase database = (DomainDatabase) AdministrationDatabaseContext.domainDatabase;
		database.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveServers(Collection collection, boolean force) throws DatabaseException, VersionCollisionException {
		ServerDatabase database = (ServerDatabase) AdministrationDatabaseContext.serverDatabase;
		database.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMCMs(Collection collection, boolean force) throws DatabaseException, VersionCollisionException {
		MCMDatabase database = (MCMDatabase) AdministrationDatabaseContext.mcmDatabase;
		database.update(collection, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException ;




	public Set refresh(Set storableObjects) throws DatabaseException {
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

	public void delete(Collection objects) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		Collection entityObjects;
		Short entityCode;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identified)
					identifier = ((Identified) object).getId();
				else
					throw new IllegalDataException("DatabaseAdministrationObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identified");

			entityCode = new Short(identifier.getMajor());
			entityObjects = (Collection) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new LinkedList();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(object);
		}

		StorableObjectDatabase storableObjectDatabase;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			entityCode = (Short) it.next();
			entityObjects = (Collection) map.get(entityCode);
			storableObjectDatabase = AdministrationDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
