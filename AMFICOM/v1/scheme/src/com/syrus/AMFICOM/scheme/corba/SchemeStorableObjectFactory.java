package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.StorableObjectFactory;

public final class SchemeStorableObjectFactory implements StorableObjectFactory {
	private static final StorableObjectFactory INSTANCE = new SchemeStorableObjectFactory();

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
	public static CableChannelingItem createCableChannelingItem() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemePath createPath() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static PathElement createPathElement() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static Scheme createScheme() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemeCableLink createSchemeCableLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemeCablePort createSchemeCablePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemeCableThread createSchemeCableThread() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemeDevice createSchemeDevice() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemeElement createSchemeElement() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemeElement createSchemeElement(SchemeProtoElement proto) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemeLink createSchemeLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemePort createSchemePort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemeProtoElement createSchemeProtoElement() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static SchemeProtoGroup createSchemeProtoGroup() {
		throw new UnsupportedOperationException();
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
		try {
			Identifier id = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE);
			final long currentTimeMillis = System.currentTimeMillis();
			SchemeProtoGroup schemeProtoGroup = null;
			schemeProtoGroup.setChanged(INSTANCE, true);
			return schemeProtoGroup;
		} catch (final IllegalObjectEntityException ioee) {
			ioee.printStackTrace();
			return null;
		}
	}
}
