/*-
 * $Id: SchemeUtils.java,v 1.37 2005/08/19 16:11:14 arseniy Exp $
 *
 * Copyright ø 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_CABLE_LINK;
import static com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind._SCHEME_LINK;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind.CABLE_SUBNETWORK;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.SchemeElementKind;

/**
 * Functionality will be partially moved to appropriate model classes; partially
 * removed Œ¡»’ .
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.37 $, $Date: 2005/08/19 16:11:14 $
 * @module scheme
 */
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
			if (pathElement.getId().equals(pathElementId)) {
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

	public static SchemeElement getSchemeElementByDevice(final Scheme scheme, final SchemeDevice schemeDevice) {
		for (final SchemeElement schemeElement : scheme.getSchemeElements()) {
			if (schemeDevice.getParentSchemeElementId().equals(schemeElement.getId())) {
				return schemeElement;
			}
		}
		return null;
	}

	public static SchemeElement getSchemeElementByDevice(final SchemeElement schemeElement, final SchemeDevice schemeDevice) {
		if (schemeDevice.getParentSchemeElementId().equals(schemeElement.getId())) {
			return schemeElement;
		}
		for (final SchemeElement schemeElement1 : schemeElement.getSchemeElements()) {
			if (schemeDevice.getParentSchemeElementId().equals(schemeElement1.getId())) {
				return schemeElement1;
			}
		}
		return null;
	}

	// return all top level elements at scheme and at inner schemes
	public static Set<SchemeElement> getTopologicalElements(final Scheme scheme) {
		final Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
		for (final SchemeElement schemeElement : scheme.getSchemeElements()) {
			if (schemeElement.getKind().value() == SchemeElementKind._EQUIPMENTED) {
				schemeElements.add(schemeElement);
			} else {
				final Scheme scheme1 = schemeElement.getScheme();
				if (scheme1.getKind() == CABLE_SUBNETWORK) {
					for (final SchemeElement schemeElement1 : getTopologicalElements(scheme1)) {
						schemeElements.add(schemeElement1);
					}
				} else {
					schemeElements.add(schemeElement);
				}
			}
		}
		return schemeElements;
	}

	// return all top level elements at scheme and at inner cable links
	public static Set<SchemeCableLink> getTopologicalCableLinks(final Scheme scheme) {
		final Set<SchemeCableLink> schemeCableLinks = new HashSet<SchemeCableLink>(scheme.getSchemeCableLinks());
		for (final SchemeElement schemeElement : scheme.getSchemeElements()) {
			if (schemeElement.getKind().value() == SchemeElementKind._SCHEMED) {
				final Scheme scheme1 = schemeElement.getScheme();
				if (scheme1.getKind() == CABLE_SUBNETWORK) {
					for (final SchemeCableLink schemeCableLink : getTopologicalCableLinks(scheme1)) {
						schemeCableLinks.add(schemeCableLink);
					}
				}
			}
		}
		return schemeCableLinks;
	}

	// return all cablelinks at scheme including inner schemes
	public static Set<SchemeCableLink> getAllCableLinks(final Scheme scheme) {
		final Set<SchemeCableLink> schemeCableLinks = new HashSet<SchemeCableLink>(scheme.getSchemeCableLinks());
		for (final SchemeElement schemeElement : scheme.getSchemeElements()) {
			if (schemeElement.getKind().value() == SchemeElementKind._SCHEMED) {
				final Scheme scheme1 = schemeElement.getScheme();
				for (final SchemeCableLink schemeCableLink : getAllCableLinks(scheme1)) {
					schemeCableLinks.add(schemeCableLink);
				}
			}
		}
		return schemeCableLinks;
	}
	
	public static SchemeElement getTopLevelSchemeElement(final SchemeElement schemeElement) {
		SchemeElement top = schemeElement;
		while (top.getParentSchemeElement() != null) {
			top = top.getParentSchemeElement();
		}
		return top;
	}

	public static SchemeElement getTopologicalElement(final Scheme scheme, final SchemeElement schemeElement) {
		if (schemeElement.getParentSchemeId().equals(scheme.getId())) {
			return schemeElement;
		}
		for (final SchemeElement schemeElement1 : scheme.getSchemeElements()) {
			if (schemeElement.getKind().value() == SchemeElementKind._SCHEMED) {
				final Scheme scheme1 = schemeElement1.getScheme();
				if (getAllChildElements(schemeElement1).contains(schemeElement)) {
					return schemeElement1;
				}
				final SchemeElement schemeElement2 = getTopologicalElement(scheme1, schemeElement);
				if (schemeElement2 != null) {
					return scheme1.getKind() == CABLE_SUBNETWORK ? schemeElement2 : schemeElement;
				}
			}
		}
		return null;
	}

	static Set<SchemeElement> getAllChildElements(final SchemeElement schemeElement) {

		final Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
		if (schemeElement.getKind().value() == SchemeElementKind._EQUIPMENTED) {
			for (final SchemeElement schemeElement1 : schemeElement.getSchemeElements()) {
				for (final SchemeElement schemeElement2 : getAllChildElements(schemeElement1)) {
					schemeElements.add(schemeElement2);
				}
				schemeElements.add(schemeElement1);
			}
		} else {
			for (final Scheme scheme : schemeElement.getSchemes()) {
				schemeElements.addAll(getAllTopLevelElements(scheme));
			}
		}
		return schemeElements;
	}

	public static Set<SchemeElement> getAllTopLevelElements(final Scheme scheme) {
		final Set<SchemeElement> schemeElements = new HashSet<SchemeElement>();
		for (final SchemeElement schemeElement : scheme.getSchemeElements()) {
			if (schemeElement.getKind().value() == SchemeElementKind._EQUIPMENTED) {
				schemeElements.add(schemeElement);
			} else {
				for (final SchemeElement schemeElement2 : getAllTopLevelElements(schemeElement.getScheme())) {
					schemeElements.add(schemeElement2);
				}
			}
		}
		return schemeElements;
	}

	public static boolean isSchemeContainsLink(final Scheme scheme, final Identifier schemeLinkId) {
		for (final SchemeLink schemeLink : scheme.getSchemeLinks()) {
			if (schemeLink.getId().equals(schemeLinkId)) {
				return true;
			}
		}
		for (final SchemeElement schemeElement : scheme.getSchemeElements()) {
			if (schemeElement.getKind().value() == SchemeElementKind._SCHEMED) {
				final Scheme scheme1 = schemeElement.getScheme();
				if ((isSchemeElementContainsLink(schemeElement, schemeLinkId)) || isSchemeContainsLink(scheme1, schemeLinkId)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isSchemeContainsElement(final Scheme scheme, final SchemeElement schemeElement) {
		if (schemeElement.getParentSchemeId().equals(scheme.getId())) {
			return true;
		}
		for (final SchemeElement schemeElement1 : scheme.getSchemeElements()) {
			if (schemeElement.getKind().value() == SchemeElementKind._SCHEMED) {
				final Scheme scheme1 = schemeElement1.getScheme();
				if ((isSchemeElementContainsElement(schemeElement1, schemeElement)) || isSchemeContainsElement(scheme1, schemeElement)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isSchemeElementContainsLink(final SchemeElement schemeElement, final Identifier schemeLinkId) {
		for (final SchemeLink schemeLink : schemeElement.getSchemeLinks()) {
			if (schemeLink.getId().equals(schemeLinkId)) {
				return true;
			}
		}
		for (final SchemeElement schemeElement1 : schemeElement.getSchemeElements()) {
			if (schemeElement.getKind().value() == SchemeElementKind._EQUIPMENTED
					&& isSchemeElementContainsLink(schemeElement1, schemeLinkId)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSchemeElementContainsPort(final SchemeElement schemeElement, final AbstractSchemePort abstractSchemePort) {
		for (final SchemeDevice schemeDevice : schemeElement.getSchemeDevices()) {
			if (abstractSchemePort.getParentSchemeDeviceId().equals(schemeDevice.getId())) {
				return true;
			}
		}
		final Set<SchemeElement> schemeElements = schemeElement.getSchemeElements();
		for (final SchemeElement schemeElement1 : schemeElements) {
			if (schemeElement.getKind().value() == SchemeElementKind._EQUIPMENTED
					&& isSchemeElementContainsPort(schemeElement1, abstractSchemePort)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSchemeElementContainsElement(final SchemeElement schemeElement1, final SchemeElement schemeElement2) {
		if (schemeElement2.getParentSchemeElementId().equals(schemeElement1.getId())) {
			return true;
		}
		if (schemeElement1.getKind().value() == SchemeElementKind._SCHEMED) {
			for (final Scheme scheme : schemeElement1.getSchemes()) {
				if (isSchemeContainsElement(scheme, schemeElement2)) {
					return true;
				}
			}
		} else {
			for (final SchemeElement schemeElement : schemeElement1.getSchemeElements()) {
				if (schemeElement.getKind().value() == SchemeElementKind._EQUIPMENTED) {
					if (isSchemeElementContainsElement(schemeElement, schemeElement2)) {
						return true;
					}
				} else {
					for (final Scheme scheme : schemeElement.getSchemes()) {
						if (isSchemeContainsElement(scheme, schemeElement2)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean isSchemeContainsCableLink(final Scheme scheme, final Identifier schemeCableLinkId) {
		for (final SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks()) {
			if (schemeCableLink.getId().equals(schemeCableLinkId)) {
				return true;
			}
		}
		for (final SchemeElement schemeElement : scheme.getSchemeElements()) {
			if (schemeElement.getKind().value() == SchemeElementKind._SCHEMED) {
				final Scheme scheme1 = schemeElement.getScheme();
				if (isSchemeContainsCableLink(scheme1, schemeCableLinkId)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isElementInPath(final SchemePath schemePath, final Identifier abstractSchemeElementId) {
		for (final PathElement pathElement : schemePath.getPathMembers()) {
			if (pathElement.getAbstractSchemeElement().getId().equals(abstractSchemeElementId)) {
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
