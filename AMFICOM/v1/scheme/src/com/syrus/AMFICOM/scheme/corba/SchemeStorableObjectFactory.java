package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.StorableObjectFactory;
//import com.syrus.AMFICOM.Client.Resource.Pool;

public final class SchemeStorableObjectFactory implements StorableObjectFactory {
	private static final StorableObjectFactory INSTANCE = new SchemeStorableObjectFactory();

	private static SchemeDefaultFactory schemeFactory;
	private static SchemePathDefaultFactory schemePathFactory;
	private static PathElementDefaultFactory pathElementFactory;
	private static SchemeElementDefaultFactory schemeElementFactory;
	private static SchemeProtoElementDefaultFactory schemeProtoElementFactory;
	private static final SchemeProtoGroupDefaultFactory SCHEME_PROTO_GROUP_FACTORY = new SchemeProtoGroupDefaultFactory();
	private static final CableChannelingItemDefaultFactory CABLE_CHANNELING_ITEM_DEFAULT_FACTORY = new CableChannelingItemDefaultFactory();
	private static SchemeDeviceDefaultFactory schemeDeviceFactory;
	private static SchemeLinkDefaultFactory schemeLinkFactory;
	private static SchemeCableLinkDefaultFactory schemeCableLinkFactory;
	private static SchemeCableThreadDefaultFactory schemeCableThreadFactory;
	private static SchemePortDefaultFactory schemePortFactory;
	private static SchemeCablePortDefaultFactory schemeCablePortFactory;

	/**
	 * No <code>assert false</code> code in this constructor as it gets
	 * invoked, though once.
	 */
	private SchemeStorableObjectFactory() {
		// empty
	}

	/**
	 * @deprecated
	 */
	public static SchemeProtoGroup createSchemeProtoGroup() {
		return SCHEME_PROTO_GROUP_FACTORY.newDefaultInstance();
	}

	/**
	 * @param creatorId cannot be null
	 * @param name cannot be null
	 * @param parentSchemeProtoGroup may be null (for a top-level group).
	 */
	public static SchemeProtoGroup newSchemeProtoGroup(final Identifier creatorId,
			final String name,
			final SchemeProtoGroup parentSchemeProtoGroup) {
		assert creatorId != null && name != null;
		Identifier id = IdentifierPool.getGeneratedIdentifierImpl(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE);
		final long currentTimeMillis = System.currentTimeMillis();
		SchemeProtoGroup schemeProtoGroup = SCHEME_PROTO_GROUP_FACTORY.newInstance(id,
				currentTimeMillis,
				currentTimeMillis,
				creatorId,
				creatorId,
				0L,
				name,
				parentSchemeProtoGroup);
		schemeProtoGroup.setChanged(INSTANCE, true);
		return schemeProtoGroup;
	}

	/**
	 * @deprecated
	 */
	public static Scheme createScheme() {
		if (schemeFactory == null)
			schemeFactory = new SchemeDefaultFactory();
		Scheme scheme = schemeFactory.newDefaultInstance();
		return scheme;
	}

	/**
	 * @deprecated
	 */
	public static SchemePath createPath() {
		if (schemePathFactory == null)
			schemePathFactory = new SchemePathDefaultFactory();
		SchemePath path = schemePathFactory.newDefaultInstance();
		return path;
	}

	/**
	 * @deprecated
	 */
	public static PathElement createPathElement() {
		if (pathElementFactory == null)
			pathElementFactory = new PathElementDefaultFactory();
		PathElement pe = pathElementFactory.newDefaultInstance();
		return pe;
	}

	/**
	 * @deprecated
	 */
	public static SchemeProtoElement createSchemeProtoElement() {
		if (schemeProtoElementFactory == null)
			schemeProtoElementFactory = new SchemeProtoElementDefaultFactory();
		SchemeProtoElement proto = schemeProtoElementFactory.newDefaultInstance();
		return proto;
	}

	/**
	 * @deprecated
	 */
	public static SchemeElement createSchemeElement() {
		if (schemeElementFactory == null)
			schemeElementFactory = new SchemeElementDefaultFactory();
		SchemeElement se = schemeElementFactory.newDefaultInstance();
		return se;
	}

	/**
	 * @deprecated
	 */
	public static SchemeElement createSchemeElement(SchemeProtoElement proto) {
		if (schemeElementFactory == null)
			schemeElementFactory = new SchemeElementDefaultFactory();
		SchemeElement se = schemeElementFactory.newDefaultInstance();
		se.name(proto.name() + " (" + se.id().identifierString() + ")");
		se.schemeProtoElement(proto);
		se.equipmentType(proto.equipmentType());

		SchemeDevice[] devices = new SchemeDevice[proto.devices().length];
		for (int i = 0; i < devices.length; i++)
			devices[i] = proto.devices()[i].cloneInstance();
		se.schemeDevices(devices);

		SchemeElement[] elements = new SchemeElement[proto.protoElements().length];
		for (int i = 0; i < elements.length; i++) {
			elements[i] = createSchemeElement(proto.protoElements()[i]);
//			Pool.put("proto2schemeids", proto.protoElements()[i].id().identifierString(), elements[i].id());
		}

		SchemeLink[] links = new SchemeLink[proto.links().length];
		for (int i = 0; i < links.length; i++)
			links[i] = proto.links()[i].cloneInstance();
		se.schemeLinks(links);

//		Characteristic[] chars = (Characteristic[]) proto.characteristics().clone();
		se.characteristicsImpl(proto.characteristicsImpl());

//		se.schemeCell() = new byte[proto.schemeCell().length];
//		System.arraycopy(proto.schemecell, 0, schemecell, 0, schemecell.length);
//		ugo = new byte[proto.ugo.length];
//		System.arraycopy(proto.ugo, 0, ugo, 0, ugo.length);

		return se;
	}

	/**
	 * @deprecated
	 */
	public static SchemeDevice createSchemeDevice() {
		if (schemeDeviceFactory == null)
			schemeDeviceFactory = new SchemeDeviceDefaultFactory();
		SchemeDevice device = schemeDeviceFactory.newDefaultInstance();
		return device;
	}

	/**
	 * @deprecated
	 */
	public static SchemeLink createSchemeLink() {
		if (schemeLinkFactory == null)
			schemeLinkFactory = new SchemeLinkDefaultFactory();
		SchemeLink link = schemeLinkFactory.newDefaultInstance();
		return link;
	}

	/**
	 * @deprecated
	 */
	public static SchemeCableLink createSchemeCableLink() {
		if (schemeCableLinkFactory == null)
			schemeCableLinkFactory = new SchemeCableLinkDefaultFactory();
		SchemeCableLink link = schemeCableLinkFactory.newDefaultInstance();
		return link;
	}

	/**
	 * @deprecated
	 */
	public static SchemeCableThread createSchemeCableThread() {
		if (schemeCableThreadFactory == null)
			schemeCableThreadFactory = new SchemeCableThreadDefaultFactory();
		SchemeCableThread thread = schemeCableThreadFactory.newDefaultInstance();
		return thread;
	}

	/**
	 * @deprecated
	 */
	public static SchemePort createSchemePort() {
		if (schemePortFactory == null)
			schemePortFactory = new SchemePortDefaultFactory();
		SchemePort port = schemePortFactory.newDefaultInstance();
		return port;
	}

	/**
	 * @deprecated
	 */
	public static SchemeCablePort createSchemeCablePort() {
		if (schemeCablePortFactory == null)
			schemeCablePortFactory = new SchemeCablePortDefaultFactory();
		SchemeCablePort port = schemeCablePortFactory.newDefaultInstance();
		return port;
	}

	/**
	 * @deprecated
	 */
	public static CableChannelingItem createCableChannelingItem() {
		return CABLE_CHANNELING_ITEM_DEFAULT_FACTORY.newDefaultInstance();
	}
}
