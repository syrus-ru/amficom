/*-
 * $Id: SchemeContainerDelegate.java,v 1.1 2005/09/12 02:52:17 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/09/12 02:52:17 $
 * @module scheme
 */
final class SchemeContainerDelegate {
	private StorableObjectCondition condition;

	private Set<Scheme> schemes;

	SchemeContainerDelegate(final SchemeContainer schemeContainer) {
		this.condition = new LinkedIdsCondition(schemeContainer.getId(), SCHEME_CODE);
	}

	Set<Scheme> getSchemes(final boolean usePool) throws ApplicationException {
		final boolean schemesIsNull = this.schemes == null;
		if (schemesIsNull || usePool) {
			if (schemesIsNull) {
				this.schemes = new HashSet<Scheme>();
			} else {
				this.schemes.clear();
			}
			this.schemes.addAll(StorableObjectPool.<Scheme>getStorableObjectsByCondition(this.condition, true));
		}
		return this.schemes;
	}
}
