/*
 * $Id: DatabaseResourceObjectLoader.java,v 1.7 2005/05/26 19:13:24 bass Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/05/26 19:13:24 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public class DatabaseResourceObjectLoader extends DatabaseObjectLoader implements ResourceObjectLoader {
	public Set loadImageResources(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids);
	}

	public Set loadImageResourcesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIds(condition, ids);
	}

	public void saveImageResources(final Set objects, boolean force) throws ApplicationException {
		super.saveStorableObjects(objects, force);
	}
}
