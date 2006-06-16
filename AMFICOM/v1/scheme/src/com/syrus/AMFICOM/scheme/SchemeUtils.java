/*-
 * $Id: SchemeUtils.java,v 1.49 2006/06/16 10:09:29 bass Exp $
 *
 * Copyright ø 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_CABLE_LINK;
import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_LINK;

import com.syrus.util.Shitlet;

/**
 * Functionality will be partially moved to appropriate model classes; partially
 * removed Œ¡»’ .
 *
 * @author $Author: bass $
 * @version $Revision: 1.49 $, $Date: 2006/06/16 10:09:29 $
 * @module scheme
 * @deprecated
 */
@Shitlet
@Deprecated
public final class SchemeUtils {
	private SchemeUtils() {
		assert false;
	}

	static double getKu(final PathElement pathElement) {
		switch (pathElement.getKind().value()) {
		case _SCHEME_CABLE_LINK:
		case _SCHEME_LINK:
			final AbstractSchemeLink link = (AbstractSchemeLink) pathElement.getAbstractSchemeElement();
			return link.getOpticalLength() / link.getPhysicalLength();
		default:
			return 1;
		}
	}

	public static SchemeElement getTopLevelSchemeElement(final SchemeElement schemeElement) {
		SchemeElement top = schemeElement;
		while (top.getParentSchemeElement() != null) {
			top = top.getParentSchemeElement();
		}
		return top;
	}
}
