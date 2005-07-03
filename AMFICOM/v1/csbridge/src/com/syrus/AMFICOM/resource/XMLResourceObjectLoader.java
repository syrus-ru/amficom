/*
 * $Id: XMLResourceObjectLoader.java,v 1.5 2005/05/23 13:51:17 bass Exp $
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
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectXML;
import com.syrus.AMFICOM.general.StorableObjectXMLDriver;
import com.syrus.AMFICOM.general.XMLObjectLoader;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/23 13:51:17 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class XMLResourceObjectLoader extends XMLObjectLoader implements ResourceObjectLoader {
	private StorableObjectXML	resourceXML;

	public XMLResourceObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "resource");
		this.resourceXML = new StorableObjectXML(driver);
	}

	/**
	 * @param identifiables
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#delete(java.util.Set)
	 */
	public void delete(final Set identifiables) {
		for (Iterator it = identifiables.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.resourceXML.delete(id);
		}
		this.resourceXML.flush();
	}

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#loadImageResources(java.util.Set)
	 */
	public Set loadImageResources(final Set ids) throws ApplicationException {
		Set objects = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			objects.add(this.resourceXML.retrieve(id));
		}
		return objects;
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#loadImageResourcesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadImageResourcesButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException {
		return this.resourceXML.retrieveByCondition(ids, condition);
	}

	/**
	 * @param storableObjects
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#refresh(java.util.Set)
	 */
	public Set refresh(final Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	/**
	 * @param storableObjects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#saveImageResources(java.util.Set, boolean)
	 */
	public void saveImageResources(final Set storableObjects,
			final boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.resourceXML.updateObject(storableObject, force);
		}
		this.resourceXML.flush();
	}
}
