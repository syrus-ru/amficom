/*
 * $Id: EmptyClientResourceObjectLoader.java,v 1.9 2005/05/23 13:51:16 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.resource;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.9 $, $Date: 2005/05/23 13:51:16 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public class EmptyClientResourceObjectLoader implements ResourceObjectLoader {
	public Set loadImageResources(final Set ids) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public Set loadImageResourcesButIds(final StorableObjectCondition condition,
			final Set ids) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public Set refresh(final Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public void saveImageResources(final Set set,
			final boolean force) throws ApplicationException {
		// empty
	}

	public void delete(final Set ids) {
		// empty
	}
}
