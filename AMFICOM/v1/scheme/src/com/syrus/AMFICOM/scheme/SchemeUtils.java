/*-
 * $Id: SchemeUtils.java,v 1.20 2005/04/01 13:59:07 bass Exp $
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
 * @version $Revision: 1.20 $, $Date: 2005/04/01 13:59:07 $
 * @todo Move to corba subpackage.
 * @module scheme_v1
 */
public class SchemeUtils {
	private static final char SEPARATOR = ':';

	private SchemeUtils() {
		assert false;
	}

	public static PathElement getPathElement(SchemePath path, Identifier pathElementId)
	{
		PathElement[] pes = path.getPathElementsAsArray();
		for (int i = 0; i < pes.length; i++)
			if (pes[i].getId().equals(pathElementId))
				return pes[i];
		return null;
	}

	static double getKu(PathElement pe)
	{
		switch (pe.getPathElementKind().value())
		{
			case PathElementKind._SCHEME_CABLE_LINK:
			case PathElementKind._SCHEME_LINK:
				AbstractSchemeLink link = (AbstractSchemeLink)pe.getAbstractSchemeElement();
				return link.getOpticalLength() / link.getPhysicalLength();
			default:
				return 1;
		}
	}

	public static SchemeElement getSchemeElementByDevice(Scheme scheme, SchemeDevice device)
	{
		SchemeElement[] elements = scheme.getSchemeElementsAsArray();
		for (int i = 0; i < elements.length; i++)
			if (Arrays.asList(elements[i].getSchemeDevicesAsArray()).contains(device))
				return elements[i];
		return null;
	}

	public static SchemeElement getSchemeElementByDevice(SchemeElement schemeElement, SchemeDevice device)
	{
		if (Arrays.asList(schemeElement.getSchemeDevicesAsArray()).contains(device))
			return schemeElement;

		SchemeElement[] elements = schemeElement.getSchemeElementsAsArray();
		for (int i = 0; i < elements.length; i++)
			if (Arrays.asList(elements[i].getSchemeDevicesAsArray()).contains(device))
				return elements[i];
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
		ht.addAll(Arrays.asList(scheme.getSchemeCableLinksAsArray()));

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
		ht.addAll(Arrays.asList(scheme.getSchemeCableLinksAsArray()));

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
		ht.addAll(Arrays.asList(scheme.getCurrentSchemeMonitoringSolution().getSchemePathsAsArray()));

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
		if (Arrays.asList(scheme.getSchemeElementsAsArray()).contains(element))
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

	static Set getAllChildElements(SchemeElement element)
	{
		if (element.getScheme() == null) {
			HashSet v = new HashSet();
			for (int i = 0; i < element.getSchemeElementsAsArray().length; i++) {
				final SchemeElement innerSchemeElement = element.getSchemeElementsAsArray()[i];
				for (Iterator it = getAllChildElements(innerSchemeElement).iterator(); it.hasNext(); )
					v.add(it.next());
				v.add(innerSchemeElement);
			}
			return v;
		}
		Scheme scheme = element.getScheme();
		return getAllTopLevelElements(scheme);
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
		if (Arrays.asList(scheme.getSchemeElementsAsArray()).contains(se))
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

	public static boolean isSchemeElementContainsLink(SchemeElement se, Identifier link_id)
	{
		SchemeLink[] links = se.getSchemeLinksAsArray();
		for (int i = 0; i < links.length; i++) {
			if (links[i].getId().equals(link_id))
				return true;
		}
		SchemeElement[] elements = se.getSchemeElementsAsArray();
		for (int i = 0; i < elements.length; i++) {
			if (se.getScheme() == null) {
				if (isSchemeElementContainsLink(elements[i], link_id))
					return true;
			}
		}
		return false;
	}

	public static boolean isSchemeElementContainsPort(SchemeElement se, AbstractSchemePort port)
	{
		SchemeDevice[] devices = se.getSchemeDevicesAsArray();
		for (int i = 0; i < devices.length; i++) {
			if (Arrays.asList(devices[i].getSchemePortsAsArray()).contains(port) ||
					Arrays.asList(devices[i].getSchemeCablePortsAsArray()).contains(port))
				return true;
		}
		SchemeElement[] elements = se.getSchemeElementsAsArray();
		for (int i = 0; i < elements.length; i++) {
			if (se.getScheme() == null) {
				if (isSchemeElementContainsPort(elements[i], port))
					return true;
			}
		}
		return false;
	}

	public static boolean isSchemeElementContainsElement(SchemeElement se1, SchemeElement se2)
	{
		if (Arrays.asList(se1.getSchemeElementsAsArray()).contains(se2))
			return true;

		SchemeElement[] elements = se1.getSchemeElementsAsArray();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getScheme() == null) {
				if (isSchemeElementContainsElement(elements[i], se2))
					return true;
			}
		}
		if (se1.getScheme() != null) {
			Scheme inner = se1.getScheme();
			if (isSchemeContainsElement(inner, se2))
					return true;
		}
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].getScheme() != null) {
				Scheme inner = elements[i].getScheme();
				if (isSchemeContainsElement(inner, se2))
					return true;
			}
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
