/*-
 * $Id: SchemeUtils.java,v 1.22 2005/04/13 18:43:58 bass Exp $
 *
 * Copyright ø 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.corba.PathElementKind;
import com.syrus.AMFICOM.scheme.corba.SchemeKind;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Functionality will be partially moved to appropriate model classes; partially
 * removed Œ¡»’ .
 *
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2005/04/13 18:43:58 $
 * @module scheme_v1
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
		final Set pathElements = schemePath.getPathElements();
		for (final Iterator pathElementIterator = pathElements.iterator(); pathElementIterator.hasNext();) {
			final PathElement pathElement = (PathElement) pathElementIterator.next();
			if (pathElement.getId().equals(pathElementId))
				return pathElement;
		}
		return null;
	}

	static double getKu(final PathElement pathElement) {
		switch (pathElement.getPathElementKind().value()) {
			case PathElementKind._SCHEME_CABLE_LINK:
			case PathElementKind._SCHEME_LINK:
				AbstractSchemeLink link = (AbstractSchemeLink)pathElement.getAbstractSchemeElement();
				return link.getOpticalLength() / link.getPhysicalLength();
			default:
				return 1;
		}
	}

	public static SchemeElement getSchemeElementByDevice(final Scheme scheme, final SchemeDevice schemeDevice) {
		for (final Iterator schemeElementIterator = scheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final SchemeElement schemeElement = (SchemeElement) schemeElementIterator.next();
			if (schemeElement.getSchemeDevices().contains(schemeDevice))
				return schemeElement;
		}
		return null;
	}

	public static SchemeElement getSchemeElementByDevice(final SchemeElement schemeElement, final SchemeDevice schemeDevice) {
		if (schemeElement.getSchemeDevices().contains(schemeDevice))
			return schemeElement;
		for (final Iterator schemeElementIterator = schemeElement.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final SchemeElement schemeElement1 = (SchemeElement) schemeElementIterator.next();
			if (schemeElement1.getSchemeDevices().contains(schemeDevice))
				return schemeElement1;
		}
		return null;
	}

	// return all top level elements at scheme and at inner schemes
	public static Set getTopologicalElements(final Scheme scheme) {
		Set schemeElements = new HashSet();
		for (final Iterator schemeElementIterator = scheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final SchemeElement schemeElement = (SchemeElement) schemeElementIterator.next();
			if (schemeElement.getSchemes().isEmpty())
				schemeElements.add(schemeElement);
			else {
				final Scheme scheme1 = schemeElement.getScheme();
				if (scheme1.getSchemeKind().value() == SchemeKind._CABLE_SUBNETWORK)
					for (final Iterator schemeElementIterator1 = getTopologicalElements(scheme1).iterator(); schemeElementIterator1.hasNext(); )
						schemeElements.add(schemeElementIterator1.next());
				else
					schemeElements.add(schemeElement);
			}
		}
		return schemeElements;
	}

	// return all top level elements at scheme and at inner cable links
	public static Set getTopologicalCableLinks(final Scheme scheme) {
		final Set schemeCableLinks = new HashSet(scheme.getSchemeCableLinks());
		for (final Iterator schemeElementIterator = scheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final SchemeElement schemeElement = (SchemeElement) schemeElementIterator.next();
			if (schemeElement.getScheme() != null) {
				final Scheme scheme1 = schemeElement.getScheme();
				if (scheme1.getSchemeKind().value() == SchemeKind._CABLE_SUBNETWORK)
					for (Iterator inner = getTopologicalCableLinks(scheme1).iterator(); inner.hasNext(); )
						schemeCableLinks.add(inner.next());
			}
		}
		return schemeCableLinks;
	}

	// return all cablelinks at scheme including inner schemes
	public static Set getAllCableLinks(final Scheme scheme) {
		final Set schemeCableLinks = new HashSet(scheme.getSchemeCableLinks());

		for (final Iterator schemeElementIterator = scheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final SchemeElement schemeElement = (SchemeElement) schemeElementIterator.next();
			if (schemeElement.getScheme() != null) {
				Scheme sc = schemeElement.getScheme();
				for (Iterator inner = getAllCableLinks(sc).iterator(); inner.hasNext(); )
					schemeCableLinks.add(inner.next());

			}
		}
		return schemeCableLinks;
	}

	public static Set getTopologicalPaths(final Scheme scheme) {
		final Set schemePaths = new HashSet(scheme.getCurrentSchemeMonitoringSolution().getSchemePaths());
		for (final Iterator schemeElementIterator = scheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final Scheme scheme1 = ((SchemeElement) schemeElementIterator.next()).getScheme();
			if (scheme1 != null && scheme1.getSchemeKind().value() == SchemeKind._CABLE_SUBNETWORK)
				for (final Iterator schemePathIterator = getTopologicalPaths(scheme1).iterator(); schemePathIterator.hasNext(); )
					schemePaths.add(schemePathIterator.next());
		}
		return schemePaths;
	}

	public static SchemeElement getTopologicalElement(final Scheme scheme, final SchemeElement schemeElement) {
		if (scheme.getSchemeElements().contains(schemeElement))
			return schemeElement;
		for (final Iterator schemeElementIterator = scheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) { // Search inner elements
			final SchemeElement schemeElement1 = (SchemeElement) schemeElementIterator.next();
			final Scheme scheme1 = schemeElement1.getScheme();
			if (scheme1 == null && getAllChildElements(schemeElement1).contains(schemeElement))
				return schemeElement1;
			final SchemeElement schemeElement2 = getTopologicalElement(scheme1, schemeElement);
			if (schemeElement2 != null) {
				if (scheme1.getSchemeKind().value() == SchemeKind._CABLE_SUBNETWORK)
					return schemeElement2;
				return schemeElement;
			}
		}
		return null;
	}

	static Set getAllChildElements(final SchemeElement schemeElement) {
		if (schemeElement.getSchemes().isEmpty()) {
			final Set schemeElements = new HashSet();
			for (final Iterator schemeElementIterator = schemeElement.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
				final SchemeElement schemeElement1 = (SchemeElement) schemeElementIterator.next();
				for (final Iterator schemeElementIterator1 = getAllChildElements(schemeElement1).iterator(); schemeElementIterator1.hasNext(); )
					schemeElements.add(schemeElementIterator1.next());
				schemeElements.add(schemeElement1);
			}
			return schemeElements;
		}
		final Set schemeElements = new HashSet();
		for (final Iterator schemeIterator = schemeElement.getSchemes().iterator(); schemeIterator.hasNext();)
			schemeElements.addAll(getAllTopLevelElements((Scheme) schemeIterator.next()));
		return schemeElements;
	}

	public static Set getAllTopLevelElements(final Scheme scheme) {
		final Set schemeElements = new HashSet();
		for (final Iterator schemeElementIterator = scheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final SchemeElement schemeElement = (SchemeElement) schemeElementIterator.next();
			if (schemeElement.getScheme() == null)
				schemeElements.add(schemeElement);
			else
				for (final Iterator schemeElementIterator1 = getAllTopLevelElements(schemeElement.getScheme()).iterator(); schemeElementIterator1.hasNext(); )
					schemeElements.add(schemeElementIterator1.next());
		}
		return schemeElements;
	}

	public static boolean isSchemeContainsLink(final Scheme scheme, final Identifier schemeLinkId) {
		for (final Iterator schemeLinkIterator = scheme.getSchemeLinks().iterator(); schemeLinkIterator.hasNext();)
			if (((SchemeLink) schemeLinkIterator.next()).getId().equals(schemeLinkId))
				return true;
		for (final Iterator schemeElementIterator = scheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final SchemeElement schemeElement = (SchemeElement) schemeElementIterator.next();
			final Scheme scheme1 = schemeElement.getScheme();
			if ((scheme1 == null && isSchemeElementContainsLink(schemeElement, schemeLinkId)) || isSchemeContainsLink(scheme1, schemeLinkId))
				return true;
		}
		return false;
	}

	public static boolean isSchemeContainsElement(final Scheme scheme, final SchemeElement schemeElement) {
		if (scheme.getSchemeElements().contains(schemeElement))
			return true;
		for (final Iterator schemeElementIterator = scheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final SchemeElement schemeElement1 = (SchemeElement) schemeElementIterator.next();
			final Scheme scheme1 = schemeElement1.getScheme();
			if ((scheme1 == null && isSchemeElementContainsElement(schemeElement1, schemeElement)) || isSchemeContainsElement(scheme1, schemeElement))
				return true;
		}
		return false;
	}

	public static boolean isSchemeElementContainsLink(final SchemeElement schemeElement, final Identifier schemeLinkId) {
		for (final Iterator schemeLinkIterator = schemeElement.getSchemeLinks().iterator(); schemeLinkIterator.hasNext();)
			if (((SchemeLink) schemeLinkIterator.next()).getId().equals(schemeLinkId))
				return true;
		for (final Iterator schemeElementIterator = schemeElement.getSchemeElements().iterator(); schemeElementIterator.hasNext();)
			if (schemeElement.getSchemes().isEmpty()
					&& isSchemeElementContainsLink((SchemeElement) schemeElementIterator.next(), schemeLinkId))
				return true;
		return false;
	}

	public static boolean isSchemeElementContainsPort(final SchemeElement schemeElement, final AbstractSchemePort abstractSchemePort) {
		for (final Iterator schemeDeviceIterator = schemeElement.getSchemeDevices().iterator(); schemeDeviceIterator.hasNext();) {
			final SchemeDevice schemeDevice = (SchemeDevice) schemeDeviceIterator.next();
			if (schemeDevice.getSchemePorts().contains(abstractSchemePort)
					|| schemeDevice.getSchemeCablePorts().contains(abstractSchemePort))
				return true;
		}
		final Set schemeElements = schemeElement.getSchemeElements();
		for (final Iterator schemeElementIterator = schemeElements.iterator(); schemeElementIterator.hasNext();)
			if (schemeElement.getSchemes().isEmpty()
					&& isSchemeElementContainsPort((SchemeElement) schemeElementIterator.next(), abstractSchemePort))
				return true;
		return false;
	}

	public static boolean isSchemeElementContainsElement(final SchemeElement schemeElement1, final SchemeElement schemeElement2) {
		if (schemeElement1.getSchemeElements().contains(schemeElement2))
			return true;
		for (final Iterator schemeIterator = schemeElement1.getSchemes().iterator(); schemeIterator.hasNext();)
			if (isSchemeContainsElement((Scheme) schemeIterator.next(), schemeElement2))
				return true;
		for (final Iterator schemeElementIterator = schemeElement1.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final SchemeElement schemeElement = (SchemeElement) schemeElementIterator.next();
			final Set schemes = schemeElement.getSchemes();
			if (schemes.isEmpty()
					&& isSchemeElementContainsElement(schemeElement, schemeElement2))
				return true;
			for (final Iterator schemeIterator = schemes.iterator(); schemeIterator.hasNext();)
				if (isSchemeContainsElement((Scheme) schemeIterator.next(), schemeElement2))
					return true;
		}
		return false;
	}

	public static boolean isSchemeContainsCableLink(final Scheme scheme, final Identifier schemeCableLinkId) {
		for (final Iterator schemeCableLinkIterator = scheme.getSchemeCableLinks().iterator(); schemeCableLinkIterator.hasNext();)
			if (((SchemeCableLink) schemeCableLinkIterator.next()).getId().equals(schemeCableLinkId))
				return true;
		for (final Iterator schemeElementIterator = scheme.getSchemeElements().iterator(); schemeElementIterator.hasNext();) {
			final Scheme scheme1 = ((SchemeElement) schemeElementIterator.next()).getScheme();
			if (scheme1 != null && isSchemeContainsCableLink(scheme1, schemeCableLinkId))
				return true;
		}
		return false;
	}

	public static boolean isElementInPath(final SchemePath schemePath, final Identifier abstractSchemeElementId) {
		for (final Iterator pathElementIterator = schemePath.getPathElements().iterator(); pathElementIterator.hasNext();)
			if (((PathElement) pathElementIterator.next()).getAbstractSchemeElement().getId().equals(abstractSchemeElementId))
				return true;
		return false;
	}

	public static String parseThreadName(String name) {
		int pos = name.lastIndexOf(SEPARATOR);
		if (pos == -1)
			return name;
		return pos == name.length() ? name : name.substring(pos + 1);
	}

	public static double getOpticalLength(final SchemePath schemePath) {
		double length = 0;
		for (final Iterator pathElementIterator = schemePath.getPathElements().iterator(); pathElementIterator.hasNext();)
			length += getOpticalLength((PathElement) pathElementIterator.next());
		return length;
	}

	static double getOpticalLength(PathElement pe) {
		switch (pe.getPathElementKind().value()) {
			case PathElementKind._SCHEME_CABLE_LINK:
			case PathElementKind._SCHEME_LINK:
				return ((AbstractSchemeLink)pe.getAbstractSchemeElement()).getOpticalLength();
			default:
				return 0;
		}
	}

	static void setOpticalLength(PathElement pe, double d) {
		switch (pe.getPathElementKind().value()) {
			case PathElementKind._SCHEME_CABLE_LINK:
			case PathElementKind._SCHEME_LINK:
				((AbstractSchemeLink)pe.getAbstractSchemeElement()).setOpticalLength(d);
		}
	}

	public static double getPhysicalLength(final SchemePath schemePath) {
		double length = 0;
		for (final Iterator pathElementIterator = schemePath.getPathElements().iterator(); pathElementIterator.hasNext();) {
			length += getPhysicalLength((PathElement) pathElementIterator.next());
		}
		return length;
	}

	public static double getPhysicalLength(PathElement pe) {
		switch (pe.getPathElementKind().value()) {
			case PathElementKind._SCHEME_CABLE_LINK:
			case PathElementKind._SCHEME_LINK:
				return ((AbstractSchemeLink)pe.getAbstractSchemeElement()).
						getPhysicalLength();
			default:
				return 0;
		}
	}

	/**
	 * @deprecated
	 */
	public static void setPhysicalLength(PathElement pe, double d) {
		switch (pe.getPathElementKind().value()) {
			case PathElementKind._SCHEME_CABLE_LINK:
			case PathElementKind._SCHEME_LINK:
				((AbstractSchemeLink)pe.getAbstractSchemeElement()).setPhysicalLength(d);
		}
	}
}
