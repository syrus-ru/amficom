package com.syrus.AMFICOM.scheme;

import java.util.*;

import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;

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

	public double getOpticalLength(PathElement pe)
	{
		switch (pe.type().value())
		{
			case Type._SCHEME_CABLE_LINK:
			case Type._SCHEME_LINK:
				return ( (AbstractSchemeLink) pe.abstractSchemeElement()).opticalLength();
			default:
				return 0;
		}
	}

	public void setOpticalLength(PathElement pe, double d)
	{
		switch (pe.type().value())
		{
			case Type._SCHEME_CABLE_LINK:
			case Type._SCHEME_LINK:
				( (AbstractSchemeLink) pe.abstractSchemeElement()).opticalLength(d);
		}
	}

	public double getPhysicalLength(PathElement pe)
	{
		switch (pe.type().value())
		{
			case Type._SCHEME_CABLE_LINK:
			case Type._SCHEME_LINK:
				return ( (AbstractSchemeLink) pe.abstractSchemeElement()).
						physicalLength();
			default:
				return 0;
		}
	}

	public void setPhysicalLength(PathElement pe, double d)
	{
		switch (pe.type().value())
		{
			case Type._SCHEME_CABLE_LINK:
			case Type._SCHEME_LINK:
				( (AbstractSchemeLink) pe.abstractSchemeElement()).physicalLength(d);
		}
	}
}
