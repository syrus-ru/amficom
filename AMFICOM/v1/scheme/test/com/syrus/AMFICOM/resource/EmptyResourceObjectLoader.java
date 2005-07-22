/*-
 * $Id: EmptyResourceObjectLoader.java,v 1.1 2005/07/22 15:09:40 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/22 15:09:40 $
 * @module scheme
 */
public final class EmptyResourceObjectLoader implements ResourceObjectLoader {

	/**
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#loadImageResources(java.util.Set)
	 */
	public Set loadImageResources(Set<Identifier> ids)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param condition
	 * @param ids
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#loadImageResourcesButIds(com.syrus.AMFICOM.general.StorableObjectCondition, java.util.Set)
	 */
	public Set loadImageResourcesButIds(StorableObjectCondition condition,
			Set<Identifier> ids) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#refresh(java.util.Set)
	 */
	public Set refresh(Set< ? extends StorableObject> storableObjects)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @param force
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#saveImageResources(java.util.Set, boolean)
	 */
	public void saveImageResources(
			Set< ? extends AbstractImageResource> objects,
			boolean force) throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param objects
	 * @see com.syrus.AMFICOM.resource.ResourceObjectLoader#delete(java.util.Set)
	 */
	public void delete(Set< ? extends Identifiable> objects) {
		throw new UnsupportedOperationException();
	}

}
