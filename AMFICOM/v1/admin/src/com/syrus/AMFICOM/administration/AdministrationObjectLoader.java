/*
 * $Id: AdministrationObjectLoader.java,v 1.1 2005/01/14 18:05:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/14 18:05:13 $
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

	List loadUsers(List ids) throws DatabaseException, CommunicationException;

	List loadDomains(List ids) throws DatabaseException, CommunicationException;

	List loadServers(List ids) throws DatabaseException, CommunicationException;

	List loadMCMs(List ids) throws DatabaseException, CommunicationException;

//  PermissionAttributes loadPermissionAttributes(List ids) throws DatabaseException, CommunicationException;


    /* Load StorableObject but argument ids */

	List loadUsersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadDomainsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadServersButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadMCMsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

//  PermissionAttributes loadPermissionAttributesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;


	void saveUser(User user, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveDomain(Domain domain, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveServer(Server server, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMCM(MCM mcm, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	void saveUsers(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveDomains(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveServers(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveMCMs(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	Set refresh(Set storableObjects) throws CommunicationException, DatabaseException;

	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(List ids) throws CommunicationException, DatabaseException;

}
