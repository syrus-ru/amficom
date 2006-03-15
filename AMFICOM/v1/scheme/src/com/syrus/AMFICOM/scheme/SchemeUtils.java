/*-
 * $Id: SchemeUtils.java,v 1.46 2006/03/13 13:54:01 bass Exp $
 *
 * Copyright ø 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_CABLE_LINK;
import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_LINK;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.util.Shitlet;

/**
 * Functionality will be partially moved to appropriate model classes; partially
 * removed Œ¡»’ .
 *
 * @author $Author: bass $
 * @version $Revision: 1.46 $, $Date: 2006/03/13 13:54:01 $
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

	public static double getOpticalLength(final SchemePath schemePath)
	throws ApplicationException {
		return schemePath.getOpticalLength();
	}

	static void setOpticalLength(final PathElement pathElement, final double d) {
		pathElement.setOpticalLength(d);
	}

	public static double getPhysicalLength(final SchemePath schemePath)
	throws ApplicationException {
		return schemePath.getPhysicalLength();
	}

	public static double getPhysicalLength(final PathElement pathElement) {
		return pathElement.getPhysicalLength();
	}

	public static void setPhysicalLength(final PathElement pathElement, final double d) {
		pathElement.setPhysicalLength(d);
	}
}
