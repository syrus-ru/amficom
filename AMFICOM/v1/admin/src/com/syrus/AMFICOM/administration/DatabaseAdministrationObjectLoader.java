/*
 * $Id: DatabaseAdministrationObjectLoader.java,v 1.9 2005/02/11 11:56:08 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/02/11 11:56:08 $
 * @author $Author: bob $
 * @module administration_v1
 */

public class DatabaseAdministrationObjectLoader implements AdministrationObjectLoader {

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
		UserDatabase database = (UserDatabase) AdministrationDatabaseContext.userDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadUsers | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}

	public List loadDomains(List ids) throws DatabaseException, CommunicationException {
		DomainDatabase database = (DomainDatabase) AdministrationDatabaseContext.domainDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}

	public List loadServers(List ids) throws DatabaseException, CommunicationException {
		ServerDatabase database = (ServerDatabase) AdministrationDatabaseContext.serverDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadDomains | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}

	public List loadMCMs(List ids) throws DatabaseException, CommunicationException {
		MCMDatabase database = (MCMDatabase) AdministrationDatabaseContext.mcmDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadMCMs | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}

	//  public PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException, CommunicationException {
	//      return new PermissionAttributes(id);
	//  }

	/* Load Administration StorableObject but argument ids */





	public List loadUsersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		UserDatabase database = (UserDatabase) AdministrationDatabaseContext.userDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadUsersButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}

	public List loadDomainsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		DomainDatabase database = (DomainDatabase) AdministrationDatabaseContext.domainDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}

	public List loadServersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		ServerDatabase database = (ServerDatabase) AdministrationDatabaseContext.serverDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadDomainsButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}

	public List loadMCMsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		MCMDatabase database = (MCMDatabase) AdministrationDatabaseContext.mcmDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.loadMCMsButIds | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		return list;
	}

	//  public PermissionAttributes loadPermissionAttributesButIds(Identifier id) throws DatabaseException, CommunicationException {
	//      return new PermissionAttributes(id);
	//  }




	public void saveUser(User user, AccessIdentity accessIdentity, boolean force) throws DatabaseException, CommunicationException {
		UserDatabase database = (UserDatabase) AdministrationDatabaseContext.userDatabase;
		try {
			database.update(user, accessIdentity.getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveUser | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveUser | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveUser | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	public void saveDomain(Domain domain, AccessIdentity accessIdentity, boolean force) throws DatabaseException, CommunicationException {
		DomainDatabase database = (DomainDatabase) AdministrationDatabaseContext.domainDatabase;
		try {
			database.update(domain, accessIdentity.getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveDomain | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveDomain | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveDomain | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	public void saveServer(Server server, AccessIdentity accessIdentity, boolean force) throws DatabaseException, CommunicationException {
		ServerDatabase database = (ServerDatabase) AdministrationDatabaseContext.serverDatabase;
		try {
			database.update(server, accessIdentity.getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveServer | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveServer | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveServer | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	public void saveMCM(MCM mcm, AccessIdentity accessIdentity, boolean force) throws DatabaseException, CommunicationException {
		MCMDatabase database = (MCMDatabase) AdministrationDatabaseContext.mcmDatabase;
		try {
			database.update(mcm, accessIdentity.getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveMCM | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveMCM | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveMCM | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException ;





	public void saveUsers(List list, AccessIdentity accessIdentity, boolean force) throws DatabaseException, CommunicationException {
		UserDatabase database = (UserDatabase) AdministrationDatabaseContext.userDatabase;
		try {
			database.update(list, accessIdentity.getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveUsers | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveUsers | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveUsers | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	public void saveDomains(List list, AccessIdentity accessIdentity, boolean force) throws DatabaseException, CommunicationException {
		DomainDatabase database = (DomainDatabase) AdministrationDatabaseContext.domainDatabase;
		try {
			database.update(list, accessIdentity.getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveDomains | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveDomains | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveDomains | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	public void saveServers(List list, AccessIdentity accessIdentity, boolean force) throws DatabaseException, CommunicationException {
		ServerDatabase database = (ServerDatabase) AdministrationDatabaseContext.serverDatabase;
		try {
			database.update(list, accessIdentity.getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveServers | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveServers | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveServers | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	public void saveMCMs(List list, AccessIdentity accessIdentity, boolean force) throws DatabaseException, CommunicationException {
		MCMDatabase database = (MCMDatabase) AdministrationDatabaseContext.mcmDatabase;
		try {
			database.update(list, accessIdentity.getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveMCMs | UpdateObjectException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveMCMs | Illegal Storable Object: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
		catch (VersionCollisionException e) {
			String mesg = "DatabaseAdministrationObjectLoader.saveMCMs | VersionCollisionException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

	//	public void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws DatabaseException ;




	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		try {
			StorableObjectDatabase database = AdministrationDatabaseContext.getDatabase(entityCode);

			if (database != null)
				return database.refresh(storableObjects);

			return Collections.EMPTY_SET;
		}
		catch (DatabaseException e) {
			Log.errorMessage("DatabaseAdministrationObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseAdministrationObjectLoader.refresh | DatabaseException: " + e.getMessage());
		}
	}




	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		delete(id, null);
	}

	public void delete(List objects) throws CommunicationException, DatabaseException, IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
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
							+ object.getClass().getName()
							+ " isn't Identifier or Identified");
			Short entityCode = new Short(identifier.getMajor());
			List list = (List) map.get(entityCode);
			if (list == null) {
				list = new LinkedList();
				map.put(entityCode, list);
			}
			list.add(object);
		}

		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			List list = (List) map.get(entityCode);
			this.delete(null, list);
		}
	}

	private void delete(Identifier id, List objects) throws DatabaseException {
		short entityCode = (id != null) ? id.getMajor() : 0;
		if (id == null) {
			if (objects.isEmpty())
				return;
			Object obj = objects.iterator().next();
			if (obj instanceof Identifier)
				entityCode = ((Identifier) obj).getMajor();
			else
				if (obj instanceof Identified)
					entityCode = ((Identified) obj).getId().getMajor();
		}
		try {
			StorableObjectDatabase database = AdministrationDatabaseContext.getDatabase(entityCode);
			if (database != null) {
				if (id != null)
					database.delete(id);
				else
					if (objects != null && !objects.isEmpty()) {
						database.delete(objects);
					}
			}
		}
		catch (IllegalDataException e) {
			String mesg = "DatabaseAdministrationObjectLoader.delete | IllegalDataException: " + e.getMessage();
			Log.errorMessage(mesg);
			throw new DatabaseException(mesg, e);
		}
	}

}
