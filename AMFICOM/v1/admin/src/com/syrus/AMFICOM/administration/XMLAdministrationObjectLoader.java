/*
 * $Id: XMLAdministrationObjectLoader.java,v 1.6 2005/02/15 07:11:36 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.administration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/15 07:11:36 $
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

	public void delete(Collection ids) throws CommunicationException, DatabaseException {
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

	public Collection loadDomains(Collection ids) throws DatabaseException, CommunicationException {
		Collection objects = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	public Collection loadDomainsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public MCM loadMCM(Identifier id) throws DatabaseException, CommunicationException {
		return (MCM) this.loadStorableObject(id);
	}

	public Collection loadMCMs(Collection ids) throws DatabaseException, CommunicationException {
		Collection objects = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	public Collection loadMCMsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Server loadServer(Identifier id) throws DatabaseException, CommunicationException {
		return (Server) this.loadStorableObject(id);
	}

	public Collection loadServers(Collection ids) throws DatabaseException, CommunicationException {
		Collection objects = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	public Collection loadServersButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public User loadUser(Identifier id) throws DatabaseException, CommunicationException {
		return (User) this.loadStorableObject(id);
	}

	public Collection loadUsers(Collection ids) throws DatabaseException, CommunicationException {
		Collection objects = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	public Collection loadUsersButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void saveDomain(Domain domain, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(domain, force);
		this.administrationXML.flush();
	}

	public void saveDomains(Collection collection, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(collection, force);

	}

	public void saveMCM(MCM mcm, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(mcm, force);
		this.administrationXML.flush();

	}

	public void saveMCMs(Collection collection, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(collection, force);

	}

	public void saveServer(Server server, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(server, force);
		this.administrationXML.flush();
	}

	public void saveServers(Collection collection, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(collection, force);

	}

	public void saveUser(User user, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObject(user, force);
		this.administrationXML.flush();
	}

	public void saveUsers(Collection collection, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(collection, force);

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

	private List loadStorableObjectButIds(StorableObjectCondition condition, Collection ids) throws CommunicationException {
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

	private void saveStorableObject(StorableObject storableObject, boolean force) throws CommunicationException {
		Identifier id = storableObject.getId();
		Identifier modifierId = SessionContext.getAccessIdentity().getUserId();
		try {
			this.administrationXML.updateObject(storableObject, force, modifierId);
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

	private void saveStorableObjects(Collection storableObjects, boolean force) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.administrationXML.flush();
	}

}
