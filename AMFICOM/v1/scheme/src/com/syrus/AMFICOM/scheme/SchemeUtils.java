/*
 * $Id: SchemeUtils.java,v 1.4 2004/12/21 16:50:42 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;

/**
 * Functionality will be partially moved to {@link PathElement}. 
 *
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/12/21 16:50:42 $
 * @module scheme_v1
 */
public class SchemeUtils
{
	private static final char separator = ':';

	private SchemeUtils() {
	}

	public static List getCablePorts(SchemeElement se)
	{
		if (se.schemeDevices().length == 1) {
			return Collections.unmodifiableList(Arrays.asList(se.schemeDevices()[0].schemeCablePorts()));
		}

		List ports = new LinkedList();
		SchemeDevice[] devices = se.schemeDevices();
		for (int i = 0; i < devices.length; i++) {
			ports.addAll(Arrays.asList(devices[i].schemeCablePorts()));
		}
		return Collections.unmodifiableList(ports);
	}

	public static List getCablePorts(SchemeProtoElement proto)
	{
		if (proto.devices().length == 1) {
			return Collections.unmodifiableList(Arrays.asList(proto.devices()[0].schemeCablePorts()));
		}

		List ports = new LinkedList();
		SchemeDevice[] devices = proto.devices();
		for (int i = 0; i < devices.length; i++) {
			ports.addAll(Arrays.asList(devices[i].schemeCablePorts()));
		}
		return Collections.unmodifiableList(ports);
	}

	public static List getPorts(SchemeElement se)
	{
		if (se.schemeDevices().length == 1) {
			return Collections.unmodifiableList(Arrays.asList(se.schemeDevices()[0].schemePorts()));
		}

		List ports = new LinkedList();
		SchemeDevice[] devices = se.schemeDevices();
		for (int i = 0; i < devices.length; i++) {
			ports.addAll(Arrays.asList(devices[i].schemePorts()));
		}
		return Collections.unmodifiableList(ports);
	}

	public static List getPorts(SchemeProtoElement proto)
	{
		if (proto.devices().length == 1) {
			return Collections.unmodifiableList(Arrays.asList(proto.devices()[0].schemePorts()));
		}

		List ports = new LinkedList();
		SchemeDevice[] devices = proto.devices();
		for (int i = 0; i < devices.length; i++) {
			ports.addAll(Arrays.asList(devices[i].schemePorts()));
		}
		return Collections.unmodifiableList(ports);
	}

	public static PathElement getPathElement(SchemePath path, Identifier pathElementId)
	{
		PathElement[] pes = path.links();
		for (int i = 0; i < pes.length; i++)
			if (pes[i].id().equals(pathElementId))
				return pes[i];
		return null;
	}

	public static double getKu(PathElement pe)
	{
		switch (pe.type().value())
		{
			case Type._SCHEME_CABLE_LINK:
			case Type._SCHEME_LINK:
				AbstractSchemeLink link = (AbstractSchemeLink)pe.abstractSchemeElement();
				return link.opticalLength() / link.physicalLength();
			default:
				return 1;
		}
	}

	public static SchemeElement getSchemeElementByDevice(Scheme scheme, SchemeDevice device)
	{
		SchemeElement[] elements = scheme.schemeElements();
		for (int i = 0; i < elements.length; i++)
			if (Arrays.asList(elements[i].schemeDevices()).contains(device))
				return elements[i];
		return null;
	}

	public static SchemeElement getSchemeElementByDevice(SchemeElement schemeElement, SchemeDevice device)
	{
		if (Arrays.asList(schemeElement.schemeDevices()).contains(device))
			return schemeElement;

		SchemeElement[] elements = schemeElement.schemeElements();
		for (int i = 0; i < elements.length; i++)
			if (Arrays.asList(elements[i].schemeDevices()).contains(device))
				return elements[i];
		return null;
	}

	// return all top level elements at scheme and at inner schemes
	public static Collection getTopologicalElements(Scheme scheme)
	{
		HashSet ht = new HashSet();
		for (int i = 0; i < scheme.schemeElements().length; i++) {
			SchemeElement el = scheme.schemeElements()[i];
			if (el.internalScheme() == null)
				ht.add(el);
			else {
				Scheme sc = el.internalScheme();
				if (sc.type().value() ==
						com.syrus.AMFICOM.scheme.corba.SchemePackage.Type._CABLE_SUBNETWORK) {
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
	public static Collection getTopologicalCableLinks(Scheme scheme)
	{
		HashSet ht = new HashSet();
		ht.addAll(Arrays.asList(scheme.schemeCableLinks()));

		for (int i = 0; i < scheme.schemeElements().length; i++) {
			SchemeElement el = scheme.schemeElements()[i];
			if (el.internalScheme() != null) {
				Scheme sc = el.internalScheme();
				if (sc.type().value() ==
						com.syrus.AMFICOM.scheme.corba.SchemePackage.Type._CABLE_SUBNETWORK) {
					for (Iterator inner = getTopologicalCableLinks(sc).iterator(); inner.hasNext(); )
						ht.add(inner.next());
				}
			}
		}
		return ht;
	}

	// return all cablelinks at scheme including inner schemes
	public static Collection getAllCableLinks(Scheme scheme)
	{
		HashSet ht = new HashSet();
		ht.addAll(Arrays.asList(scheme.schemeCableLinks()));

		for (int i = 0; i < scheme.schemeElements().length; i++) {
			SchemeElement el = scheme.schemeElements()[i];
			if (el.internalScheme() != null) {
				Scheme sc = el.internalScheme();
				for (Iterator inner = getAllCableLinks(sc).iterator(); inner.hasNext(); )
					ht.add(inner.next());

			}
		}
		return ht;
	}

	public static Collection getTopologicalPaths(Scheme scheme)
	{
		HashSet ht = new HashSet();
		ht.addAll(Arrays.asList(scheme.schemeMonitoringSolution().schemePaths()));

		for (int i = 0; i < scheme.schemeElements().length; i++) {
			SchemeElement el = scheme.schemeElements()[i];
			if (el.internalScheme() != null) {
				Scheme sc = el.internalScheme();
				if (sc.type().value() ==
						com.syrus.AMFICOM.scheme.corba.SchemePackage.Type._CABLE_SUBNETWORK) {
					for (Iterator inner = getTopologicalPaths(sc).iterator(); inner.hasNext(); )
						ht.add(inner.next());
				}
			}
		}
		return ht;
	}

	public static SchemeElement getTopologicalElement(Scheme scheme, SchemeElement element)
	{
		if (Arrays.asList(scheme.schemeElements()).contains(element))
			return element;

		for(int i = 0; i < scheme.schemeElements().length; i++) // Search inner elements
		{
			SchemeElement el = scheme.schemeElements()[i];
			if (el.internalScheme() == null)
			{
				if (getAllChildElements(el).contains(element))
						return el;
			}
		}

		for(int i = 0; i < scheme.schemeElements().length; i++)  // Search inner schemes
		{
			SchemeElement el = scheme.schemeElements()[i];
			if (el.internalScheme() != null)
			{
				Scheme inner = el.internalScheme();
				SchemeElement inner_se = getTopologicalElement(inner, element);
				if (inner_se != null)
				{
					if (inner.type().value() == com.syrus.AMFICOM.scheme.corba.SchemePackage.Type._CABLE_SUBNETWORK)
						return inner_se;
					else
						return element;
				}
			}
		}
		return null;
	}

	public static Collection getAllChildElements(SchemeElement element)
	{
		if (element.internalScheme() == null) {
			HashSet v = new HashSet();
			for (int i = 0; i < element.schemeElements().length; i++) {
				SchemeElement inner_se = element.schemeElements()[i];
				for (Iterator it = getAllChildElements(inner_se).iterator(); it.hasNext(); )
					v.add(it.next());
				v.add(inner_se);
			}
			return v;
		}
		else {
			Scheme scheme = element.internalScheme();
			return getAllTopLevelElements(scheme);
		}
	}

	public static Collection getTopLevelElements(Scheme scheme)
	{
		return Arrays.asList(scheme.schemeElements());
	}

	public static Collection getAllTopLevelElements(Scheme scheme)
	{
		HashSet ht = new HashSet();
		for (int i = 0; i < scheme.schemeElements().length; i++) {
			SchemeElement el = scheme.schemeElements()[i];
			if (el.internalScheme() == null)
				ht.add(el);
			else {
				Scheme sc = el.internalScheme();
				for (Iterator inner = getAllTopLevelElements(sc).iterator(); inner.hasNext(); )
					ht.add(inner.next());
			}
		}
		return ht;
	}

	public static boolean isSchemeContainsLink(Scheme scheme, Identifier link_id)
	{
		SchemeLink[] links = scheme.schemeLinks();
		for (int i = 0; i < links.length; i++) {
			if (links[i].id().equals(link_id))
				return true;
		}
		SchemeElement[] elements = scheme.schemeElements();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].internalScheme() == null) {
				if (isSchemeElementContainsLink(elements[i], link_id))
					return true;
			}
		}
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].internalScheme() != null) {
				Scheme inner = elements[i].internalScheme();
				if (isSchemeContainsLink(inner, link_id))
					return true;
			}
		}
		return false;
	}

	public static boolean isSchemeContainsElement(Scheme scheme, SchemeElement se)
	{
		if (Arrays.asList(scheme.schemeElements()).contains(se))
			return true;

		SchemeElement[] elements = scheme.schemeElements();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].internalScheme() == null) {
				if (isSchemeElementContainsElement(elements[i], se))
					return true;
			}
		}
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].internalScheme() != null) {
				Scheme inner = elements[i].internalScheme();
				if (isSchemeContainsElement(inner, se))
					return true;
			}
		}
		return false;
	}

	public static boolean isSchemeElementContainsLink(SchemeElement se, Identifier link_id)
	{
		SchemeLink[] links = se.schemeLinks();
		for (int i = 0; i < links.length; i++) {
			if (links[i].id().equals(link_id))
				return true;
		}
		SchemeElement[] elements = se.schemeElements();
		for (int i = 0; i < elements.length; i++) {
			if (se.internalScheme() == null) {
				if (isSchemeElementContainsLink(elements[i], link_id))
					return true;
			}
		}
		return false;
	}

	public static boolean isSchemeElementContainsPort(SchemeElement se, AbstractSchemePort port)
	{
		SchemeDevice[] devices = se.schemeDevices();
		for (int i = 0; i < devices.length; i++) {
			if (Arrays.asList(devices[i].schemePorts()).contains(port) ||
					Arrays.asList(devices[i].schemeCablePorts()).contains(port))
				return true;
		}
		SchemeElement[] elements = se.schemeElements();
		for (int i = 0; i < elements.length; i++) {
			if (se.internalScheme() == null) {
				if (isSchemeElementContainsPort(elements[i], port))
					return true;
			}
		}
		return false;
	}

	public static boolean isSchemeElementContainsElement(SchemeElement se1, SchemeElement se2)
	{
		if (Arrays.asList(se1.schemeElements()).contains(se2))
			return true;

		SchemeElement[] elements = se1.schemeElements();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].internalScheme() == null) {
				if (isSchemeElementContainsElement(elements[i], se2))
					return true;
			}
		}
		if (se1.internalScheme() != null) {
			Scheme inner = se1.internalScheme();
			if (isSchemeContainsElement(inner, se2))
					return true;
		}
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].internalScheme() != null) {
				Scheme inner = elements[i].internalScheme();
				if (isSchemeContainsElement(inner, se2))
					return true;
			}
		}
		return false;
	}

	public static boolean isSchemeContainsCableLink(Scheme scheme, Identifier cable_link_id)
	{
		SchemeCableLink[] links = scheme.schemeCableLinks();
		for (int i = 0; i < links.length; i++) {
			if (links[i].id().equals(cable_link_id))
				return true;
		}

		SchemeElement[] elements = scheme.schemeElements();
		for (int i = 0; i < elements.length; i++) {
			if (elements[i].internalScheme() != null) {
				Scheme inner = elements[i].internalScheme();
				if (isSchemeContainsCableLink(inner, cable_link_id))
					return true;
			}
		}
		return false;
	}

	public static boolean isElementInPath(SchemePath path, Identifier element_id)
	{
		PathElement[] pes = path.links();
		for (int i = 0; i < pes.length; i++) {
			if (pes[i].abstractSchemeElement().id().equals(element_id))
				return true;
		}
		return false;
	}

	public static String parseThreadName(String name)
	{
		int pos = name.lastIndexOf(separator);
		if (pos == -1)
			return name;
		return pos == name.length() ? name : name.substring(pos + 1);
	}

	public static double getOpticalLength(SchemePath path)
	{
		double length = 0;
		PathElement[] links = path.links();
		for (int i = 0; i < links.length; i++)
			length += getOpticalLength(links[i]);
		return length;
	}

	public static double getOpticalLength(PathElement pe)
	{
		switch (pe.type().value()) {
			case Type._SCHEME_CABLE_LINK:
			case Type._SCHEME_LINK:
				return ((AbstractSchemeLink)pe.abstractSchemeElement()).opticalLength();
			default:
				return 0;
		}
	}

	public static void setOpticalLength(PathElement pe, double d)
	{
		switch (pe.type().value()) {
			case Type._SCHEME_CABLE_LINK:
			case Type._SCHEME_LINK:
				((AbstractSchemeLink)pe.abstractSchemeElement()).opticalLength(d);
		}
	}

	public static double getPhysicalLength(SchemePath path)
	{
		double length = 0;
		PathElement[] links = path.links();
		for (int i = 0; i < links.length; i++)
			length += getPhysicalLength(links[i]);
		return length;
	}

	public static double getPhysicalLength(PathElement pe)
	{
		switch (pe.type().value()) {
			case Type._SCHEME_CABLE_LINK:
			case Type._SCHEME_LINK:
				return ((AbstractSchemeLink)pe.abstractSchemeElement()).
						physicalLength();
			default:
				return 0;
		}
	}

	public static void setPhysicalLength(PathElement pe, double d)
	{
		switch (pe.type().value()) {
			case Type._SCHEME_CABLE_LINK:
			case Type._SCHEME_LINK:
				((AbstractSchemeLink)pe.abstractSchemeElement()).physicalLength(d);
		}
	}
}
