/*
 * $Id: XMLResourceObjectLoader.java,v 1.4 2005/02/25 12:06:35 bass Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/25 12:06:35 $
 * @author $Author: bass $
 * @module resource_v1
 */
public class XMLResourceObjectLoader implements ResourceObjectLoader {

	private StorableObjectXML	resourceXML;

	public XMLResourceObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "resource"); //$NON-NLS-1$
		this.resourceXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) throws IllegalDataException {
		this.resourceXML.delete(id);
		this.resourceXML.flush();
	}

	public void delete(Collection ids) throws IllegalDataException {
		for (Iterator it = ids.iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				this.resourceXML.delete(id);
		}
		this.resourceXML.flush();

	}

	private StorableObject loadStorableObject(Identifier id) throws ApplicationException {
		return this.resourceXML.retrieve(id);
	}
	
	private Collection loadStorableObjects(Collection ids) throws ApplicationException {
		Collection objects = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	private List loadStorableObjectButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.resourceXML.retrieveByCondition(ids, condition);
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		Identifier modifierId = SessionContext.getAccessIdentity().getUserId();
		this.resourceXML.updateObject(storableObject, force, modifierId);
	}
	
	private void saveStorableObjects(Collection storableObjects, boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.resourceXML.flush();
	}
	
	public StorableObject loadImageResource(Identifier id) throws ApplicationException {
		return this.loadStorableObject(id);
	}

	public Collection loadImageResources(Collection ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Collection loadImageResourcesButIds(	StorableObjectCondition condition,
												Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set refresh(Set storableObjects) throws DatabaseException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void saveImageResource(	AbstractImageResource abstractImageResource,
									boolean force) throws ApplicationException {
		this.saveStorableObject(abstractImageResource, force);
	}
	
	public void saveImageResources(	Collection collection,
									boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);
	}

}
