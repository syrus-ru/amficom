/*
 * $Id: SchemeStorableObjectPool.java,v 1.3 2004/12/09 15:08:01 bass Exp $
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
 * @version $Revision: 1.3 $, $Date: 2004/12/09 15:08:01 $
 * @module schemecommon_v1
 */
public final class SchemeStorableObjectPool {
	private SchemeStorableObjectPool() {
	}

	public static void cleanChangedStorableObject(final Short entityCode) {
		throw new UnsupportedOperationException();
	}

	public static void cleanChangedStorableObjects() {
		throw new UnsupportedOperationException();
	}

	public static void delete(final Identifier id) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static void delete(final List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static void flush(final boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		throw new UnsupportedOperationException();
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

//	public static void init(final SchemeObjectLoader schemeObjectLoader) {
//		throw new UnsupportedOperationException();
//	}

//	public static void init(final SchemeObjectLoader schemeObjectLoader, final Class cacheClass, final int size) {
//		throw new UnsupportedOperationException();
//	}

//	public static void init(final SchemeObjectLoader schemeObjectLoader, final int size) {
//		throw new UnsupportedOperationException();
//	}

	public static StorableObject putStorableObject(final StorableObject storableObject)
			throws IllegalObjectEntityException {
		throw new UnsupportedOperationException();
	}

	public static void refresh() throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public static void serializePool() {
		throw new UnsupportedOperationException();
	}
}
