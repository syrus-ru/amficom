package com.syrus.AMFICOM.Client.General.Scheme;

import java.util.*;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.Client.Resource.Pool;

public class SchemeFactory
{
	private static SchemeDefaultFactory schemeFactory;
	private static SchemePathDefaultFactory schemePathFactory;
	private static PathElementDefaultFactory pathElementFactory;
	private static SchemeElementDefaultFactory schemeElementFactory;
	private static SchemeProtoElementDefaultFactory schemeProtoElementFactory;
	private static SchemeProtoGroupDefaultFactory schemeProtoGroupFactory;
	private static SchemeDeviceDefaultFactory schemeDeviceFactory;
	private static SchemeLinkDefaultFactory schemeLinkFactory;
	private static SchemeCableLinkDefaultFactory schemeCableLinkFactory;
	private static SchemeCableThreadDefaultFactory schemeCableThreadFactory;
	private static SchemePortDefaultFactory schemePortFactory;
	private static SchemeCablePortDefaultFactory schemeCablePortFactory;

	public static Scheme createScheme()
	{
		if (schemeFactory == null)
			schemeFactory = new SchemeDefaultFactory();
		Scheme scheme = schemeFactory.newInstance();
		return scheme;
	}

	public static SchemePath createPath()
	{
		if (schemePathFactory == null)
			schemePathFactory = new SchemePathDefaultFactory();
		SchemePath path = schemePathFactory.newInstance();
		return path;
	}

	public static PathElement createPathElement()
	{
		if (pathElementFactory == null)
			pathElementFactory = new PathElementDefaultFactory();
		PathElement pe = pathElementFactory.newInstance();
		return pe;
	}

	public static SchemeProtoGroup createSchemeProtoGroup()
	{
		if (schemeProtoGroupFactory == null)
			schemeProtoGroupFactory = new SchemeProtoGroupDefaultFactory();
		SchemeProtoGroup group = schemeProtoGroupFactory.newInstance();
		return group;
	}

	public static SchemeProtoElement createSchemeProtoElement()
	{
		if (schemeProtoElementFactory == null)
			schemeProtoElementFactory = new SchemeProtoElementDefaultFactory();
		SchemeProtoElement proto = schemeProtoElementFactory.newInstance();
		return proto;
	}

	public static SchemeElement createSchemeElement()
	{
		if (schemeElementFactory == null)
			schemeElementFactory = new SchemeElementDefaultFactory();
		SchemeElement se = schemeElementFactory.newInstance();
		return se;
	}

	public static SchemeElement createSchemeElement(SchemeProtoElement proto)
	{
		if (schemeElementFactory == null)
			schemeElementFactory = new SchemeElementDefaultFactory();
		SchemeElement se = schemeElementFactory.newInstance();
		se.name(proto.name() + " (" + se.id().identifierString() + ")");
		se.schemeProtoElement(proto);
		se.equipmentType(proto.equipmentType());

		SchemeDevice[] devices = new SchemeDevice[proto.devices().length];
		for(int i = 0; i < devices.length; i++)
			devices[i] = proto.devices()[i].cloneInstance();
		se.schemeDevices(devices);

		SchemeElement[] elements = new SchemeElement[proto.protoElements().length];
		for(int i = 0; i < elements.length; i++)
		{
			elements[i] = createSchemeElement(proto.protoElements()[i]);
			Pool.put("proto2schemeids", proto.protoElements()[i].id().identifierString(), elements[i].id());
		}

		SchemeLink[] links = new SchemeLink[proto.links().length];
		for(int i = 0; i < links.length; i++)
			links[i] = proto.links()[i].cloneInstance();
		se.schemeLinks(links);

		Characteristic[] chars = (Characteristic[]) proto.characteristics().clone();
		se.characteristics(proto.characteristics());

//		se.schemeCell() = new byte[proto.schemeCell().length];
//		System.arraycopy(proto.schemecell, 0, schemecell, 0, schemecell.length);
//		ugo = new byte[proto.ugo.length];
//		System.arraycopy(proto.ugo, 0, ugo, 0, ugo.length);

		return se;
	}

	public static SchemeDevice createSchemeDevice()
	{
		if (schemeDeviceFactory == null)
			schemeDeviceFactory = new SchemeDeviceDefaultFactory();
		SchemeDevice device = schemeDeviceFactory.newInstance();
		return device;
	}

	public static SchemeLink createSchemeLink()
	{
		if (schemeLinkFactory == null)
			schemeLinkFactory = new SchemeLinkDefaultFactory();
		SchemeLink link = schemeLinkFactory.newInstance();
		return link;
	}

	public static SchemeCableLink createSchemeCableLink()
	{
		if (schemeCableLinkFactory == null)
			schemeCableLinkFactory = new SchemeCableLinkDefaultFactory();
		SchemeCableLink link = schemeCableLinkFactory.newInstance();
		return link;
	}

	public static SchemeCableThread createSchemeCableThread()
	{
		if (schemeCableThreadFactory == null)
			schemeCableThreadFactory = new SchemeCableThreadDefaultFactory();
		SchemeCableThread thread = schemeCableThreadFactory.newInstance();
		return thread;
	}

	public static SchemePort createSchemePort()
	{
		if (schemePortFactory == null)
			schemePortFactory = new SchemePortDefaultFactory();
		SchemePort port = schemePortFactory.newInstance();
		return port;
	}

	public static SchemeCablePort createSchemeCablePort()
	{
		if (schemeCablePortFactory == null)
			schemeCablePortFactory = new SchemeCablePortDefaultFactory();
		SchemeCablePort port = schemeCablePortFactory.newInstance();
		return port;
	}
}
