/*
 * $Id: AdministrationObjectLoader.java,v 1.20 2005/06/22 19:22:12 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.20 $, $Date: 2005/06/22 19:22:12 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public interface AdministrationObjectLoader {

	/* Load multiple objects*/

	Set loadSystemUsers(final Set<Identifier> ids) throws ApplicationException;

	Set loadDomains(final Set<Identifier> ids) throws ApplicationException;

	Set loadServers(final Set<Identifier> ids) throws ApplicationException;

	Set loadMCMs(final Set<Identifier> ids) throws ApplicationException;

	Set loadServerProcesses(final Set<Identifier> ids) throws ApplicationException;

//  PermissionAttributes loadPermissionAttributes(final Set<Identifier> ids) throws ApplicationException;



	/* Load multiple objects but ids*/

	Set loadSystemUsersButIds(StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadDomainsButIds(StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadServersButIds(StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadMCMsButIds(StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadServerProcessesButIds(StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

//  PermissionAttributes loadPermissionAttributesButIds(StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;



	/* Save multiple objects*/

	void saveSystemUsers(final Set<SystemUser> objects, final boolean force) throws ApplicationException;

	void saveDomains(final Set<Domain> objects, final boolean force) throws ApplicationException;

	void saveServers(final Set<Server> objects, final boolean force) throws ApplicationException;

	void saveMCMs(final Set<MCM> objects, final boolean force) throws ApplicationException;

	void saveServerProcesses(final Set<ServerProcess> objects, final boolean force) throws ApplicationException;

//	void savePermissionAttributes(PermissionAttributes permissionAttributes, final boolean force) throws ApplicationException;



	/*	Refresh*/

	Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException;



	/*	Delete*/

	void delete(final Set<? extends Identifiable> identifiables);

}
