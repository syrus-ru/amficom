/*
 * $Id: AdministrationObjectLoader.java,v 1.6 2005/02/11 18:40:09 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collection;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/11 18:40:09 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public interface AdministrationObjectLoader {

	User loadUser(Identifier id) throws DatabaseException, CommunicationException;

	Domain loadDomain(Identifier id) throws DatabaseException, CommunicationException;

	Server loadServer(Identifier id) throws DatabaseException, CommunicationException;

	MCM loadMCM(Identifier id) throws DatabaseException, CommunicationException;

//	PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException, CommunicationException;


    // this block for multiple objects

	Collection loadUsers(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadDomains(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadServers(Collection ids) throws DatabaseException, CommunicationException;

	Collection loadMCMs(Collection ids) throws DatabaseException, CommunicationException;

//  PermissionAttributes loadPermissionAttributes(Collection ids) throws DatabaseException, CommunicationException;


    /* Load StorableObject but argument ids */

	Collection loadUsersButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	Collection loadDomainsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	Collection loadServersButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	Collection loadMCMsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

//  PermissionAttributes loadPermissionAttributesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;


	void saveUser(User user, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveDomain(Domain domain, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveServer(Server server, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMCM(MCM mcm, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	void saveUsers(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveDomains(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveServers(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMCMs(Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	Set refresh(Set storableObjects) throws CommunicationException, DatabaseException;

	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(Collection objects) throws CommunicationException, DatabaseException, IllegalDataException;

}
