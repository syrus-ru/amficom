/*
 * $Id: XMLAdministrationObjectLoader.java,v 1.2 2005/02/11 10:34:58 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/11 10:34:58 $
 * @author $Author: bob $
 * @module admin_v1
 */
public class XMLAdministrationObjectLoader implements AdministrationObjectLoader {

	private StorableObjectXML	administrationXML;

	public XMLAdministrationObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "administration");
		this.administrationXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		try {
			this.administrationXML.delete(id);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLAdministrationObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.administrationXML.flush();
	}

	public void delete(List ids) throws CommunicationException, DatabaseException {
		try {
			for (Iterator it = ids.iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				this.administrationXML.delete(id);
			}
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLAdministrationObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.administrationXML.flush();

	}

	public Domain loadDomain(Identifier id) throws DatabaseException, CommunicationException {
		return (Domain) this.loadStorableObject(id);
	}

	public List loadDomains(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadDomainsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MCM loadMCM(Identifier id) throws DatabaseException, CommunicationException {
		return (MCM) this.loadStorableObject(id);
	}

	public List loadMCMs(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadMCMsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Server loadServer(Identifier id) throws DatabaseException, CommunicationException {
		return (Server) this.loadStorableObject(id);
	}

	public List loadServers(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadServersButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public User loadUser(Identifier id) throws DatabaseException, CommunicationException {
		return (User) this.loadStorableObject(id);
	}

	public List loadUsers(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadUsersButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void saveDomain(Domain domain, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(domain, accessIdentity);
		this.administrationXML.flush();
	}

	public void saveDomains(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveMCM(MCM mcm, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(mcm, accessIdentity);
		this.administrationXML.flush();

	}

	public void saveMCMs(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveServer(Server server, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(server, accessIdentity);
		this.administrationXML.flush();
	}

	public void saveServers(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	public void saveUser(User user, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(user, accessIdentity);
		this.administrationXML.flush();
	}

	public void saveUsers(List list, AccessIdentity accessIdentity, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list, accessIdentity);

	}

	private StorableObject loadStorableObject(Identifier id) throws CommunicationException {
		try {
			return this.administrationXML.retrieve(id);
		} catch (ObjectNotFoundException e) {
			throw new CommunicationException("XMLAdministrationObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (RetrieveObjectException e) {
			throw new CommunicationException("XMLAdministrationObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLAdministrationObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}
	}

	private List loadStorableObjectButIds(StorableObjectCondition condition, List ids) throws CommunicationException {
		try {
			return this.administrationXML.retrieveByCondition(ids, condition);
		} catch (RetrieveObjectException e) {
			throw new CommunicationException("XMLAdministrationObjectLoader.loadParameterTypesButIds | caught "
					+ e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLAdministrationObjectLoader.loadParameterTypesButIds | caught "
					+ e.getMessage(), e);
		}

	}

	private void saveStorableObject(StorableObject storableObject, AccessIdentity accessIdentity) throws CommunicationException {
		Identifier id = storableObject.getId();
		try {
			this.administrationXML.updateObject(storableObject, accessIdentity.getUserId());
		} catch (UpdateObjectException e) {
			throw new CommunicationException("XMLAdministrationObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLAdministrationObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (VersionCollisionException e) {
			throw new CommunicationException("XMLAdministrationObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}

	}

	private void saveStorableObjects(List storableObjects, AccessIdentity accessIdentity) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, accessIdentity);
		}
		this.administrationXML.flush();
	}

}
