/*
 * $Id: SchemeStorableObjectPool.java,v 1.2 2004/11/24 15:27:09 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.List;
import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.general.corba.StorableObject;
import com.syrus.AMFICOM.general.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/11/24 15:27:09 $
 * @module schemecommon_v1
 */
public class SchemeStorableObjectPool {
	private SchemeStorableObjectPool() {
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
