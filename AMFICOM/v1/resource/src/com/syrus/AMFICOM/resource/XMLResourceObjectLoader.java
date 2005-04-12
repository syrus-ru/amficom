/*
 * $Id: XMLResourceObjectLoader.java,v 1.7 2005/04/12 16:58:45 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;

/**
 * @version $Revision: 1.7 $, $Date: 2005/04/12 16:58:45 $
 * @author $Author: arseniy $
 * @module resource_v1
 */
public class XMLResourceObjectLoader implements ResourceObjectLoader {

	private StorableObjectXML	resourceXML;

	public XMLResourceObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "resource"); //$NON-NLS-1$
		this.resourceXML = new StorableObjectXML(driver);
	}

	public void delete(Identifier id) {
		this.resourceXML.delete(id);
		this.resourceXML.flush();
	}

	public void delete(final Set identifiables) {
		for (Iterator it = identifiables.iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				this.resourceXML.delete(id);
		}
		this.resourceXML.flush();

	}

	private StorableObject loadStorableObject(Identifier id) throws ApplicationException {
		return this.resourceXML.retrieve(id);
	}
	
	private Set loadStorableObjects(Set ids) throws ApplicationException {
		Set objects = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.loadStorableObject(id));
		}
		return objects;
	}

	private Set loadStorableObjectButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.resourceXML.retrieveByCondition(ids, condition);
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		Identifier modifierId = SessionContext.getAccessIdentity().getUserId();
		this.resourceXML.updateObject(storableObject, force, modifierId);
	}
	
	private void saveStorableObjects(Set storableObjects, boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.resourceXML.flush();
	}
	
	public StorableObject loadImageResource(Identifier id) throws ApplicationException {
		return this.loadStorableObject(id);
	}

	public Set loadImageResources(Set ids) throws ApplicationException {
		return this.loadStorableObjects(ids);
	}

	public Set loadImageResourcesButIds(	StorableObjectCondition condition,
												Set ids) throws ApplicationException {
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
	
	public void saveImageResources(	Set collection,
									boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);
	}

}
