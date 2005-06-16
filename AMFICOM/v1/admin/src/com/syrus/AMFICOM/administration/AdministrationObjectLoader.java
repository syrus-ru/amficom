/*
 * $Id: AdministrationObjectLoader.java,v 1.18 2005/05/23 13:07:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.18 $, $Date: 2005/05/23 13:07:57 $
 * @author $Author: bass $
 * @module administration_v1
 */

public interface AdministrationObjectLoader {

	/* Load multiple objects*/

	Set loadUsers(Set ids) throws ApplicationException;

	Set loadDomains(Set ids) throws ApplicationException;

	Set loadServers(Set ids) throws ApplicationException;

	Set loadMCMs(Set ids) throws ApplicationException;

	Set loadServerProcesses(Set ids) throws ApplicationException;

//  PermissionAttributes loadPermissionAttributes(Set ids) throws ApplicationException;



	/* Load multiple objects but ids*/

	Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

//  PermissionAttributes loadPermissionAttributesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;



	/* Save multiple objects*/

	void saveUsers(Set objects, boolean force) throws ApplicationException;

	void saveDomains(Set objects, boolean force) throws ApplicationException;

	void saveServers(Set objects, boolean force) throws ApplicationException;

	void saveMCMs(Set objects, boolean force) throws ApplicationException;

	void saveServerProcesses(Set objects, boolean force) throws ApplicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, boolean force) throws ApplicationException;



	/*	Refresh*/

	Set refresh(final Set storableObjects) throws ApplicationException;



	/*	Delete*/

	void delete(final Set identifiables);

}
