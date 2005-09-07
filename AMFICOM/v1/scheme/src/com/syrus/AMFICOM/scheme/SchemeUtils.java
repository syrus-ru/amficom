/*-
 * $Id: SchemeUtils.java,v 1.39 2005/09/07 18:31:40 bass Exp $
 *
 * Copyright ø 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_CABLE_LINK;
import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_LINK;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Shitlet;

/**
 * Functionality will be partially moved to appropriate model classes; partially
 * removed Œ¡»’ .
 *
 * @author $Author: bass $
 * @version $Revision: 1.39 $, $Date: 2005/09/07 18:31:40 $
 * @module scheme
 * @deprecated
 */
@Shitlet
@Deprecated
public class SchemeUtils {
	private static final char SEPARATOR = ':';

	private SchemeUtils() {
		assert false;
	}

	/**
	 * @todo Search occurences and change arg2 type to PathElement.
	 * @param schemePath
	 * @param pathElementId
	 */
	public static PathElement getPathElement(final SchemePath schemePath, final Identifier pathElementId) {
		final Set<PathElement> pathElements = schemePath.getPathMembers();
		for (final PathElement pathElement : pathElements) {
			if (pathElement.equals(pathElementId)) {
				return pathElement;
			}
		}
		return null;
	}

	static double getKu(final PathElement pathElement) {
		switch (pathElement.getKind().value()) {
		case _SCHEME_CABLE_LINK:
			/*
			 * Fall through.
			 */
		case _SCHEME_LINK:
			final AbstractSchemeLink link = (AbstractSchemeLink)pathElement.getAbstractSchemeElement();
			return link.getOpticalLength() / link.getPhysicalLength();
		default:
			return 1;
		}
	}

	public static SchemeElement getSchemeElementByDevice(final Scheme scheme, final SchemeDevice schemeDevice) throws ApplicationException {
		for (final SchemeElement schemeElement : scheme.getSchemeElements0()) {
			if (schemeDevice.getParentSchemeElementId().equals(schemeElement)) {
				return schemeElement;
			}
		}
		return null;
	}

	public static SchemeElement getSchemeElementByDevice(final SchemeElement schemeElement, final SchemeDevice schemeDevice) throws ApplicationException {
		if (schemeDevice.getParentSchemeElementId().equals(schemeElement)) {
			return schemeElement;
		}
		for (final SchemeElement schemeElement1 : schemeElement.getSchemeElements0()) {
			if (schemeDevice.getParentSchemeElementId().equals(schemeElement1)) {
				return schemeElement1;
			}
		}
		return null;
	}

	public static SchemeElement getTopLevelSchemeElement(final SchemeElement schemeElement) {
		SchemeElement top = schemeElement;
		while (top.getParentSchemeElement() != null) {
			top = top.getParentSchemeElement();
		}
		return top;
	}

	public static boolean isElementInPath(final SchemePath schemePath, final Identifier abstractSchemeElementId) {
		for (final PathElement pathElement : schemePath.getPathMembers()) {
			if (pathElement.getAbstractSchemeElement().equals(abstractSchemeElementId)) {
				return true;
			}
		}
		return false;
	}

	public static String parseThreadName(final String name) {
		final int pos = name.lastIndexOf(SEPARATOR);
		return pos == name.length() || pos == -1 ? name : name.substring(pos + 1);
	}

	public static double getOpticalLength(final SchemePath schemePath) {
		double length = 0;
		for (final PathElement pathElement : schemePath.getPathMembers()) {
			length += getOpticalLength(pathElement);
		}
		return length;
	}

	static double getOpticalLength(final PathElement pathElement) {
		switch (pathElement.getKind().value()) {
		case _SCHEME_CABLE_LINK:
			/*
			 * Fall through.
			 */
		case _SCHEME_LINK:
			return ((AbstractSchemeLink)pathElement.getAbstractSchemeElement()).getOpticalLength();
		default:
			return 0;
		}
	}

	static void setOpticalLength(final PathElement pathElement, final double d) {
		switch (pathElement.getKind().value()) {
		case _SCHEME_CABLE_LINK:
		case _SCHEME_LINK:
			((AbstractSchemeLink)pathElement.getAbstractSchemeElement()).setOpticalLength(d);
		default:
			break;
		}
	}

	public static double getPhysicalLength(final SchemePath schemePath) {
		double length = 0;
		for (final PathElement pathElement : schemePath.getPathMembers()) {
			length += getPhysicalLength(pathElement);
		}
		return length;
	}

	public static double getPhysicalLength(final PathElement pathElement) {
		switch (pathElement.getKind().value()) {
		case _SCHEME_CABLE_LINK:
			/*
			 * Fall through.
			 */
		case _SCHEME_LINK:
			return ((AbstractSchemeLink)pathElement.getAbstractSchemeElement()).
					getPhysicalLength();
		default:
			return 0;
		}
	}

	public static void setPhysicalLength(final PathElement pathElement, final double d) {
		switch (pathElement.getKind().value()) {
		case _SCHEME_CABLE_LINK:
		case _SCHEME_LINK:
			((AbstractSchemeLink)pathElement.getAbstractSchemeElement()).setPhysicalLength(d);
		default:
			break;
		}
	}
}
