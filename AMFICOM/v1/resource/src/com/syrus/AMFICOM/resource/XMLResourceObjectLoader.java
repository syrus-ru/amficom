/*
 * $Id: XMLResourceObjectLoader.java,v 1.1 2005/02/15 08:59:10 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

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
 * @version $Revision: 1.1 $, $Date: 2005/02/15 08:59:10 $
 * @author $Author: bob $
 * @module resource_v1
 */
public class XMLResourceObjectLoader implements ResourceObjectLoader {

	private StorableObjectXML	resourceXML;

	public XMLResourceObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "resource");
		this.resourceXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		try {
			this.resourceXML.delete(id);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLResourceObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.resourceXML.flush();
	}

	public void delete(Collection ids) throws CommunicationException, DatabaseException {
		try {
			for (Iterator it = ids.iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				this.resourceXML.delete(id);
			}
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLResourceObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.resourceXML.flush();

	}

	private StorableObject loadStorableObject(Identifier id) throws DatabaseException {
		try {
			return this.resourceXML.retrieve(id);
		} catch (ObjectNotFoundException e) {
			throw new DatabaseException("XMLResourceObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (RetrieveObjectException e) {
			throw new DatabaseException("XMLResourceObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new DatabaseException("XMLResourceObjectLoader.load"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}
	}
	
	private Collection loadStorableObjects(Collection ids) throws DatabaseException {
		Collection objects = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	private List loadStorableObjectButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException {
		try {
			return this.resourceXML.retrieveByCondition(ids, condition);
		} catch (RetrieveObjectException e) {
			throw new DatabaseException("XMLResourceObjectLoader.loadStorableObjectButIds | caught "
					+ e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new DatabaseException("XMLResourceObjectLoader.loadStorableObjectButIds | caught "
					+ e.getMessage(), e);
		}
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws DatabaseException {
		Identifier id = storableObject.getId();
		Identifier modifierId = SessionContext.getAccessIdentity().getUserId();
		try {
			this.resourceXML.updateObject(storableObject, force, modifierId);
		} catch (UpdateObjectException e) {
			throw new DatabaseException("XMLResourceObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new DatabaseException("XMLResourceObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		} catch (VersionCollisionException e) {
			throw new DatabaseException("XMLResourceObjectLoader.save"
					+ ObjectEntities.codeToString(id.getMajor()) + " | caught " + e.getMessage(), e);
		}
	}
	
	private void saveStorableObjects(Collection storableObjects, boolean force) throws DatabaseException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.resourceXML.flush();
	}
	
	public StorableObject loadImageResource(Identifier id) throws DatabaseException {
		return this.loadStorableObject(id);
	}

	public Collection loadImageResources(Collection ids) throws DatabaseException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadImageResourcesButIds(	StorableObjectCondition condition,
												Collection ids) throws DatabaseException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set refresh(Set storableObjects) throws DatabaseException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void saveImageResource(	AbstractImageResource abstractImageResource,
									boolean force) throws DatabaseException {
		this.saveStorableObject(abstractImageResource, force);
	}
	
	public void saveImageResources(	Collection collection,
									boolean force) throws DatabaseException {
		this.saveStorableObjects(collection, force);
	}

}
