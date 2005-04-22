/*
 * $Id: AdministrationObjectLoader.java,v 1.13 2005/04/22 17:07:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.13 $, $Date: 2005/04/22 17:07:56 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public interface AdministrationObjectLoader {

	/* Load single object*/

	User loadUser(Identifier id) throws ApplicationException;

	Domain loadDomain(Identifier id) throws ApplicationException;

	Server loadServer(Identifier id) throws ApplicationException;

	MCM loadMCM(Identifier id) throws ApplicationException;

//	PermissionAttributes loadPermissionAttributes(Identifier id) throws ApplicationException;



	/* Load multiple objects*/

	Set loadUsers(Set ids) throws ApplicationException;

	Set loadDomains(Set ids) throws ApplicationException;

	Set loadServers(Set ids) throws ApplicationException;

	Set loadMCMs(Set ids) throws ApplicationException;

//  PermissionAttributes loadPermissionAttributes(Set ids) throws ApplicationException;



	/* Load multiple objects but ids*/

	Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

//  PermissionAttributes loadPermissionAttributesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;



	/* Save single object*/

	void saveUser(User user, boolean force) throws ApplicationException;

	void saveDomain(Domain domain, boolean force) throws ApplicationException;

	void saveServer(Server server, boolean force) throws ApplicationException;

	void saveMCM(MCM mcm, boolean force) throws ApplicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws ApplicationException;



	/* Save multiple object*/

	void saveUsers(Set objects, boolean force) throws ApplicationException;

	void saveDomains(Set objects, boolean force) throws ApplicationException;

	void saveServers(Set objects, boolean force) throws ApplicationException;

	void saveMCMs(Set objects, boolean force) throws ApplicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws ApplicationException;



	Set refresh(Set storableObjects) throws ApplicationException;



	void delete(Identifier id);

	void delete(final Set identifiables);

}
