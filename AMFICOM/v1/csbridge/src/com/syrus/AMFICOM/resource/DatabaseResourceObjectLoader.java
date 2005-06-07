/*
 * $Id: DatabaseResourceObjectLoader.java,v 1.9 2005/06/07 13:18:51 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.resource;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.9 $, $Date: 2005/06/07 13:18:51 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public class DatabaseResourceObjectLoader extends DatabaseObjectLoader implements ResourceObjectLoader {
	public Set loadImageResources(final Set ids) throws ApplicationException {
		return loadStorableObjects(ids);
	}

	public Set loadImageResourcesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return loadStorableObjectsButIdsByCondition(condition, ids);
	}

	public void saveImageResources(final Set objects, boolean force) throws ApplicationException {
		saveStorableObjects(objects, force);
	}
}
