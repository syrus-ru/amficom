/*
 * $Id: AdministrationObjectLoader.java,v 1.8 2005/02/24 14:59:46 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collection;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.8 $, $Date: 2005/02/24 14:59:46 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public interface AdministrationObjectLoader {

	User loadUser(Identifier id) throws ApplicationException;

	Domain loadDomain(Identifier id) throws ApplicationException;

	Server loadServer(Identifier id) throws ApplicationException;

	MCM loadMCM(Identifier id) throws ApplicationException;

//	PermissionAttributes loadPermissionAttributes(Identifier id) throws ApplicationException;


    // this block for multiple objects

	Collection loadUsers(Collection ids) throws ApplicationException;

	Collection loadDomains(Collection ids) throws ApplicationException;

	Collection loadServers(Collection ids) throws ApplicationException;

	Collection loadMCMs(Collection ids) throws ApplicationException;

//  PermissionAttributes loadPermissionAttributes(Collection ids) throws ApplicationException;


    /* Load StorableObject but argument ids */

	Collection loadUsersButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Collection loadDomainsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Collection loadServersButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Collection loadMCMsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

//  PermissionAttributes loadPermissionAttributesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;


	void saveUser(User user, boolean force) throws ApplicationException;

	void saveDomain(Domain domain, boolean force) throws ApplicationException;

	void saveServer(Server server, boolean force) throws ApplicationException;

	void saveMCM(MCM mcm, boolean force) throws ApplicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws ApplicationException;


	void saveUsers(Collection collection, boolean force) throws ApplicationException;

	void saveDomains(Collection collection, boolean force) throws ApplicationException;

	void saveServers(Collection collection, boolean force) throws ApplicationException;

	void saveMCMs(Collection collection, boolean force) throws ApplicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws ApplicationException;


	Set refresh(Set storableObjects) throws ApplicationException;


	void delete(Identifier id) throws IllegalDataException;

	void delete(Collection objects) throws IllegalDataException;

}
