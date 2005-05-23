/*
 * $Id: XMLAdministrationObjectLoader.java,v 1.5 2005/05/23 12:56:33 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.XMLObjectLoader;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/23 12:56:33 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class XMLAdministrationObjectLoader extends XMLObjectLoader implements AdministrationObjectLoader {

	private StorableObjectXML	administrationXML;

	public XMLAdministrationObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "administration");
		this.administrationXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) {
		this.administrationXML.delete(id);
		this.administrationXML.flush();
	}

	public void delete(Set ids) {
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.administrationXML.delete(id);
		}
		this.administrationXML.flush();
	}

	public Domain loadDomain(Identifier id) throws ApplicationException {
		return (Domain) this.loadStorableObject(id);
	}

	public Set loadDomains(Set ids) throws ApplicationException {
		Set objects = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	public Set loadDomainsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MCM loadMCM(Identifier id) throws ApplicationException {
		return (MCM) this.loadStorableObject(id);
	}

	public Set loadMCMs(Set ids) throws ApplicationException {
		Set objects = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	public Set loadMCMsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Server loadServer(Identifier id) throws ApplicationException {
		return (Server) this.loadStorableObject(id);
	}

	public Set loadServers(Set ids) throws ApplicationException {
		Set objects = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	public Set loadServersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public User loadUser(Identifier id) throws ApplicationException {
		return (User) this.loadStorableObject(id);
	}

	public Set loadUsers(Set ids) throws ApplicationException {
		Set objects = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	public Set loadUsersButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public ServerProcess loadServerProcess(Identifier id) throws ApplicationException {
		return (ServerProcess) this.loadStorableObject(id);
	}

	public Set loadServerProcesses(Set ids) throws ApplicationException {
		Set objects = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	public Set loadServerProcessesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	/**
	 * @param storableObjects
	 * @see com.syrus.AMFICOM.administration.AdministrationObjectLoader#refresh(java.util.Set)
	 */
	public Set refresh(final Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public void saveDomain(Domain domain, boolean force) throws ApplicationException {
		this.saveStorableObject(domain, force);
		this.administrationXML.flush();
	}

	public void saveDomains(Set collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);

	}

	public void saveMCM(MCM mcm, boolean force) throws ApplicationException {
		this.saveStorableObject(mcm, force);
		this.administrationXML.flush();

	}

	public void saveMCMs(Set collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);

	}

	public void saveServer(Server server, boolean force) throws ApplicationException {
		this.saveStorableObject(server, force);
		this.administrationXML.flush();
	}

	public void saveServers(Set collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);

	}

	public void saveUser(User user, boolean force) throws ApplicationException {
		this.saveStorableObject(user, force);
		this.administrationXML.flush();
	}

	public void saveUsers(Set collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);

	}

	public void saveServerProcess(ServerProcess serverProcess, boolean force) throws ApplicationException {
		this.saveStorableObject(serverProcess, force);
		this.administrationXML.flush();
	}

	public void saveServerProcesses(Set collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);

	}

	private StorableObject loadStorableObject(Identifier id) throws ApplicationException {
		try {
			return this.administrationXML.retrieve(id);
		} catch (ObjectNotFoundException e) {
			throw new ApplicationException("XMLAdministrationObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}
	}

	private Set loadStorableObjectButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		try {
			return this.administrationXML.retrieveByCondition(ids, condition);
		} catch (RetrieveObjectException e) {
			throw new ApplicationException("XMLAdministrationObjectLoader.loadStorableObjectButIds | caught "
					+ e.getMessage(), e);
		}
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		Identifier id = storableObject.getId();
		try {
			this.administrationXML.updateObject(storableObject, force);
		}
		catch (UpdateObjectException e) {
			throw new ApplicationException("XMLAdministrationObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}
	}

	private void saveStorableObjects(Set storableObjects, boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.administrationXML.flush();
	}

}
