/*
 * $Id: DatabaseAdministrationObjectLoader.java,v 1.1 2005/01/14 18:05:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/14 18:05:13 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public class DatabaseAdministrationObjectLoader implements AdministrationObjectLoader {

	private StorableObjectDatabase getDatabase(short entityCode){
		StorableObjectDatabase database = null;
		switch (entityCode) {
			case ObjectEntities.USER_ENTITY_CODE:
				database = AdministrationDatabaseContext.getUserDatabase();
				break;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				database = AdministrationDatabaseContext.getDomainDatabase();
				break;
			case ObjectEntities.SERVER_ENTITY_CODE:
				database = AdministrationDatabaseContext.getServerDatabase();
				break;
			case ObjectEntities.MCM_ENTITY_CODE:
				database = AdministrationDatabaseContext.getDomainDatabase();
				break;
//			case ObjectEntities.PERMATTR_ENTITY_CODE:
//				database = AdministrationDatabaseContext.getPermissionAttributesDatabase();
//				break;
			default:
				Log.errorMessage("DatabaseAdministrationObjectLoader.getDatabase | Unknown entity: " + ObjectEntities.codeToString(entityCode));                
		}
		return database;
	}

	private void delete(Identifier id, List ids) throws DatabaseException {
		short entityCode = (id != null) ? id.getMajor() : 0;
		if (id == null) {
			if (ids.isEmpty())
				return;
			Object obj = ids.iterator().next();
			if (obj instanceof Identifier)
				entityCode = ((Identifier)obj).getMajor();
			else
				if (obj instanceof Identified)
					entityCode = ((Identified)obj).getId().getMajor();
		}
		try {
			StorableObjectDatabase database = this.getDatabase(entityCode); 
			if (database != null) {
				if (id != null)
					database.delete(id);
				else
					if (ids != null && !ids.isEmpty()) {
						database.delete(ids);
					}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.delete | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.delete | DatabaseException: " + e.getMessage());
		}
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		delete(id, null);       
	}

	public void delete(List ids) throws CommunicationException, DatabaseException {
			delete(null, ids);
	}

	public User loadUser(Identifier id) throws DatabaseException, CommunicationException {
		return new User(id);
	}

	public Domain loadDomain(Identifier id) throws DatabaseException, CommunicationException {
		return new Domain(id);
	}

	public Server loadServer(Identifier id) throws DatabaseException, CommunicationException {
		return new Server(id);
	}

	public MCM loadMCM(Identifier id) throws DatabaseException, CommunicationException {
		return new MCM(id);
	}

//	public PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException, CommunicationException {
//		return new PermissionAttributes(id);
//	}


    // for multiple objects

	public List loadUsers(List ids) throws DatabaseException, CommunicationException {
		UserDatabase database = (UserDatabase)AdministrationDatabaseContext.getUserDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.loadUsers | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.loadUsers | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadDomains(List ids) throws DatabaseException, CommunicationException {
		DomainDatabase database = (DomainDatabase)AdministrationDatabaseContext.getDomainDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadServers(List ids) throws DatabaseException, CommunicationException {
		ServerDatabase database = (ServerDatabase)AdministrationDatabaseContext.getServerDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMCMs(List ids) throws DatabaseException, CommunicationException {
		MCMDatabase database = (MCMDatabase)AdministrationDatabaseContext.getMCMDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.loadMCMs | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.loadMCMs | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}


//  public PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException, CommunicationException {
//      return new PermissionAttributes(id);
//  }


    /* Load Administration StorableObject but argument ids */

	public List loadUsersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		UserDatabase database = (UserDatabase)AdministrationDatabaseContext.getUserDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.loadUsersButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.loadUsersButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadDomainsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		DomainDatabase database = (DomainDatabase)AdministrationDatabaseContext.getDomainDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadServersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		ServerDatabase database = (ServerDatabase)AdministrationDatabaseContext.getServerDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMCMsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		MCMDatabase database = (MCMDatabase)AdministrationDatabaseContext.getMCMDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.loadMCMsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.loadMCMsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

//  public PermissionAttributes loadPermissionAttributesButIds(Identifier id) throws DatabaseException, CommunicationException {
//      return new PermissionAttributes(id);
//  }


	public void saveUser(User user, boolean force) throws DatabaseException, CommunicationException {
		UserDatabase database = (UserDatabase)AdministrationDatabaseContext.getUserDatabase();
		try {
			database.update(user, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveUser | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveUser | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveUser | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveUser | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveUser | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveUser | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveDomain(Domain domain, boolean force) throws DatabaseException, CommunicationException {
		DomainDatabase database = (DomainDatabase)AdministrationDatabaseContext.getDomainDatabase();
		try {
			database.update(domain, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveDomain | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveDomain | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveDomain | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveDomain | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveDomain | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveDomain | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveServer(Server server, boolean force) throws DatabaseException, CommunicationException {
		ServerDatabase database = (ServerDatabase)AdministrationDatabaseContext.getServerDatabase();
		try {
			database.update(server, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveServer | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveServer | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveServer | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveServer | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveServer | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveServer | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMCM(MCM mcm, boolean force) throws DatabaseException, CommunicationException {
		MCMDatabase database = (MCMDatabase)AdministrationDatabaseContext.getMCMDatabase();
		try {
			database.update(mcm, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveMCM | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveMCM | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveMCM | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveMCM | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveMCM | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveMCM | VersionCollisionException: " + e.getMessage());
		}
	}

//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException ;


	public void saveUsers(List list, boolean force) throws DatabaseException, CommunicationException {
		UserDatabase database = (UserDatabase)AdministrationDatabaseContext.getUserDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveUsers | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveUsers | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveUsers | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveUsers | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveUsers | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveUsers | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveDomains(List list, boolean force) throws DatabaseException, CommunicationException {
		DomainDatabase database = (DomainDatabase)AdministrationDatabaseContext.getDomainDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveDomains | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveDomains | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveDomains | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveDomains | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveDomains | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveDomains | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveServers(List list, boolean force) throws DatabaseException, CommunicationException {
		ServerDatabase database = (ServerDatabase)AdministrationDatabaseContext.getServerDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveServers | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveServers | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveServers | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveServers | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveServers | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveServers | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMCMs(List list, boolean force) throws DatabaseException, CommunicationException {
		MCMDatabase database = (MCMDatabase)AdministrationDatabaseContext.getMCMDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveMCMs | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveMCMs | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveMCMs | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveMCMs | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.saveMCMs | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.saveMCMs | VersionCollisionException: " + e.getMessage());
		}
	}

//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException ;


	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		try {
		    StorableObjectDatabase database = this.getDatabase(entityCode);
			
			if (database != null)
				return database.refresh(storableObjects);

			return Collections.EMPTY_SET;
		}
		catch (DatabaseException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.refresh | DatabaseException: " + e.getMessage());
		}
	}

}
