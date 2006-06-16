/*-
 * $Id: SchemeUtilities.java,v 1.1 2006/06/16 10:56:24 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPROTOGROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/06/16 10:56:24 $
 * @module scheme
 */
public final class SchemeUtilities {
	private static final StorableObjectCondition ROOT_SCHEME_PROTO_GROUPS_CONDITION
			= new LinkedIdsCondition(VOID_IDENTIFIER, SCHEMEPROTOGROUP_CODE);

	private static final StorableObjectCondition ROOT_SCHEMES_CONDITION
			= new LinkedIdsCondition(VOID_IDENTIFIER, SCHEME_CODE);

	private SchemeUtilities() {
		assert false;
	}

	public static Set<SchemeProtoGroup> getRootSchemeProtoGroups()
	throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(
				ROOT_SCHEME_PROTO_GROUPS_CONDITION,
				true);
	}

	public static Set<SchemeProtoGroup> getRootSchemeProtoGroupsButIds(
			final Set<Identifier> ids)
	throws ApplicationException {
		return StorableObjectPool.getStorableObjectsButIdsByCondition(
				ids,
				ROOT_SCHEME_PROTO_GROUPS_CONDITION,
				true);
	}

	public static Set<Scheme> getRootSchemes()
	throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(
				ROOT_SCHEMES_CONDITION,
				true);
	}

	public static Set<Scheme> getRootSchemesButIds(
			final Set<Identifier> ids)
	throws ApplicationException {
		return StorableObjectPool.getStorableObjectsButIdsByCondition(
				ids,
				ROOT_SCHEMES_CONDITION,
				true);
	}
}
