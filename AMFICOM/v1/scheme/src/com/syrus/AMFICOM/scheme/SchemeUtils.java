/*-
 * $Id: SchemeUtils.java,v 1.21 2005/04/12 18:12:19 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.corba.PathElementKind;
import java.util.*;

/**
 * Functionality will be partially moved to {@link PathElement}.
 *
 * @author $Author: bass $
 * @version $Revision: 1.21 $, $Date: 2005/04/12 18:12:19 $
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
		SchemeElement[] elements = scheme.getSchemeElementsAsArray();
		for (int i = 0; i < elements.length; i++)
			if (elements[i].getSchemeDevices().contains(schemeDevice))
				return elements[i];
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
	public static Set getTopologicalElements(Scheme scheme)
	{
		HashSet ht = new HashSet();
		for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
			SchemeElement el = scheme.getSchemeElementsAsArray()[i];
			if (el.getScheme() == null)
				ht.add(el);
			else {
				Scheme sc = el.getScheme();
				if (sc.getSchemeKind().value() ==
						com.syrus.AMFICOM.scheme.corba.SchemeKind._CABLE_SUBNETWORK) {
					for (Iterator it = getTopologicalElements(sc).iterator(); it.hasNext(); )
						ht.add(it.next());
				}
				else
					ht.add(el);
			}
		}
		return ht;
	}

	// return all top level elements at scheme and at inner cable links
	public static Set getTopologicalCableLinks(Scheme scheme)
	{
		HashSet ht = new HashSet();
		ht.addAll(scheme.getSchemeCableLinks());

		for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
			SchemeElement el = scheme.getSchemeElementsAsArray()[i];
			if (el.getScheme() != null) {
				Scheme sc = el.getScheme();
				if (sc.getSchemeKind().value() ==
						com.syrus.AMFICOM.scheme.corba.SchemeKind._CABLE_SUBNETWORK) {
					for (Iterator inner = getTopologicalCableLinks(sc).iterator(); inner.hasNext(); )
						ht.add(inner.next());
				}
			}
		}
		return ht;
	}

	// return all cablelinks at scheme including inner schemes
	public static Set getAllCableLinks(Scheme scheme)
	{
		HashSet ht = new HashSet();
		ht.addAll(scheme.getSchemeCableLinks());

		for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
			SchemeElement el = scheme.getSchemeElementsAsArray()[i];
			if (el.getScheme() != null) {
				Scheme sc = el.getScheme();
				for (Iterator inner = getAllCableLinks(sc).iterator(); inner.hasNext(); )
					ht.add(inner.next());

			}
		}
		return ht;
	}

	public static Set getTopologicalPaths(Scheme scheme)
	{
		HashSet ht = new HashSet();
		ht.addAll(scheme.getCurrentSchemeMonitoringSolution().getSchemePaths());

		for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
			SchemeElement el = scheme.getSchemeElementsAsArray()[i];
			if (el.getScheme() != null) {
				Scheme sc = el.getScheme();
				if (sc.getSchemeKind().value() ==
						com.syrus.AMFICOM.scheme.corba.SchemeKind._CABLE_SUBNETWORK) {
					for (Iterator inner = getTopologicalPaths(sc).iterator(); inner.hasNext(); )
						ht.add(inner.next());
				}
			}
		}
		return ht;
	}

	public static SchemeElement getTopologicalElement(Scheme scheme, SchemeElement element)
	{
		if (scheme.getSchemeElements().contains(element))
			return element;

		for(int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) // Search inner elements
		{
			SchemeElement el = scheme.getSchemeElementsAsArray()[i];
			if (el.getScheme() == null)
			{
				if (getAllChildElements(el).contains(element))
						return el;
			}
		}

		for(int i = 0; i < scheme.getSchemeElementsAsArray().length; i++)  // Search inner schemes
		{
			SchemeElement el = scheme.getSchemeElementsAsArray()[i];
			if (el.getScheme() != null)
			{
				final Scheme innerScheme = el.getScheme();
				final SchemeElement innerSchemeElement = getTopologicalElement(innerScheme, element);
				if (innerSchemeElement != null)
				{
					if (innerScheme.getSchemeKind().value() == com.syrus.AMFICOM.scheme.corba.SchemeKind._CABLE_SUBNETWORK)
						return innerSchemeElement;
					return element;
				}
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

	public static Set getAllTopLevelElements(Scheme scheme)
	{
		HashSet ht = new HashSet();
		for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
			SchemeElement el = scheme.getSchemeElementsAsArray()[i];
			if (el.getScheme() == null)
				ht.add(el);
			else {
				Scheme sc = el.getScheme();
				for (Iterator inner = getAllTopLevelElements(sc).iterator(); inner.hasNext(); )
					ht.add(inner.next());
			}
		}
		return ht;
	}

	public static boolean isSchemeContainsLink(Scheme scheme, Identifier link_id)
	{
		SchemeLink[] links = scheme.getSchemeLinksAsArray();
		for (int i = 0; i < links.length; i++) {
			if (links[i].getId().equals(link_id))
				return true;
		}
		SchemeElement[] elements = scheme.getSchemeElementsAsArray();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getScheme() == null) {
				if (isSchemeElementContainsLink(elements[i], link_id))
					return true;
			}
		}
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getScheme() != null) {
				Scheme inner = elements[i].getScheme();
				if (isSchemeContainsLink(inner, link_id))
					return true;
			}
		}
		return false;
	}

	public static boolean isSchemeContainsElement(Scheme scheme, SchemeElement se)
	{
		if (scheme.getSchemeElements().contains(se))
			return true;

		SchemeElement[] elements = scheme.getSchemeElementsAsArray();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getScheme() == null) {
				if (isSchemeElementContainsElement(elements[i], se))
					return true;
			}
		}
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getScheme() != null) {
				Scheme inner = elements[i].getScheme();
				if (isSchemeContainsElement(inner, se))
					return true;
			}
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

	public static boolean isSchemeContainsCableLink(Scheme scheme, Identifier cable_link_id)
	{
		SchemeCableLink[] links = scheme.getSchemeCableLinksAsArray();
		for (int i = 0; i < links.length; i++) {
			if (links[i].getId().equals(cable_link_id))
				return true;
		}

		SchemeElement[] elements = scheme.getSchemeElementsAsArray();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getScheme() != null) {
				Scheme inner = elements[i].getScheme();
				if (isSchemeContainsCableLink(inner, cable_link_id))
					return true;
			}
		}
		return false;
	}

	public static boolean isElementInPath(SchemePath path, Identifier element_id)
	{
		PathElement[] pes = path.getPathElementsAsArray();
		for (int i = 0; i < pes.length; i++) {
			if (pes[i].getAbstractSchemeElement().getId().equals(element_id))
				return true;
		}
		return false;
	}

	public static String parseThreadName(String name)
	{
		int pos = name.lastIndexOf(SEPARATOR);
		if (pos == -1)
			return name;
		return pos == name.length() ? name : name.substring(pos + 1);
	}

	public static double getOpticalLength(SchemePath path)
	{
		double length = 0;
		PathElement[] links = path.getPathElementsAsArray();
		for (int i = 0; i < links.length; i++)
			length += getOpticalLength(links[i]);
		return length;
	}

	static double getOpticalLength(PathElement pe)
	{
		switch (pe.getPathElementKind().value()) {
			case PathElementKind._SCHEME_CABLE_LINK:
			case PathElementKind._SCHEME_LINK:
				return ((AbstractSchemeLink)pe.getAbstractSchemeElement()).getOpticalLength();
			default:
				return 0;
		}
	}

	static void setOpticalLength(PathElement pe, double d)
	{
		switch (pe.getPathElementKind().value()) {
			case PathElementKind._SCHEME_CABLE_LINK:
			case PathElementKind._SCHEME_LINK:
				((AbstractSchemeLink)pe.getAbstractSchemeElement()).setOpticalLength(d);
		}
	}

	public static double getPhysicalLength(SchemePath path)
	{
		double length = 0;
		PathElement[] links = path.getPathElementsAsArray();
		for (int i = 0; i < links.length; i++)
			length += getPhysicalLength(links[i]);
		return length;
	}

	public static double getPhysicalLength(PathElement pe)
	{
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
	public static void setPhysicalLength(PathElement pe, double d)
	{
		switch (pe.getPathElementKind().value()) {
			case PathElementKind._SCHEME_CABLE_LINK:
			case PathElementKind._SCHEME_LINK:
				((AbstractSchemeLink)pe.getAbstractSchemeElement()).setPhysicalLength(d);
		}
	}
}
