/*
 * $Id: ResourceStorableObjectPool.java,v 1.1 2004/12/06 09:00:10 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import java.util.List;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/12/06 09:00:10 $
 * @module resource_v1
 */
public final class ResourceStorableObjectPool {
	private ResourceStorableObjectPool() {
	}

	public static StorableObject getStorableObject(final Identifier objectId, final boolean useLoader)
			throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static List getStorableObjects(final List objectIds, final boolean useLoader)
			throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static List getStorableObjectsByCondition(final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public static List getStorableObjectsByConditionButIds(final List ids, final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public static StorableObject putStorableObject(final StorableObject storableObject)
			throws IllegalObjectEntityException {
		throw new UnsupportedOperationException();
	}
}
