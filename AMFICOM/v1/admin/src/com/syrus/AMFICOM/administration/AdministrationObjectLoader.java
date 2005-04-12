/*
 * $Id: AdministrationObjectLoader.java,v 1.10 2005/04/12 08:12:38 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.10 $, $Date: 2005/04/12 08:12:38 $
 * @author $Author: bass $
 * @module administration_v1
 */

public interface AdministrationObjectLoader {

	User loadUser(Identifier id) throws ApplicationException;

	Domain loadDomain(Identifier id) throws ApplicationException;

	Server loadServer(Identifier id) throws ApplicationException;

	MCM loadMCM(Identifier id) throws ApplicationException;

//	PermissionAttributes loadPermissionAttributes(Identifier id) throws ApplicationException;


    // this block for multiple objects

	Set loadUsers(Set ids) throws ApplicationException;

	Set loadDomains(Set ids) throws ApplicationException;

	Set loadServers(Set ids) throws ApplicationException;

	Set loadMCMs(Set ids) throws ApplicationException;

//  PermissionAttributes loadPermissionAttributes(Set ids) throws ApplicationException;


    /* Load StorableObject but argument ids */

	Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

//  PermissionAttributes loadPermissionAttributesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;


	void saveUser(User user, boolean force) throws ApplicationException;

	void saveDomain(Domain domain, boolean force) throws ApplicationException;

	void saveServer(Server server, boolean force) throws ApplicationException;

	void saveMCM(MCM mcm, boolean force) throws ApplicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws ApplicationException;


	void saveUsers(Set collection, boolean force) throws ApplicationException;

	void saveDomains(Set collection, boolean force) throws ApplicationException;

	void saveServers(Set collection, boolean force) throws ApplicationException;

	void saveMCMs(Set collection, boolean force) throws ApplicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws ApplicationException;


	Set refresh(Set storableObjects) throws ApplicationException;


	void delete(Identifier id) throws IllegalDataException;

	void delete(final Set identifiables) throws IllegalDataException;

}
