/*-
 * $Id: SchemeCablePort.java,v 1.7 2005/04/01 13:59:07 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.corba.PortSort;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import java.util.*;

/**
 * #09 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/04/01 13:59:07 $
 * @module scheme_v1
 */
public final class SchemeCablePort extends AbstractSchemePort {
	private static final long serialVersionUID = 4050767078690534455L;

	/**
	 * @param id
	 */
	protected SchemeCablePort(Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected SchemeCablePort(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeCablePort createInstance() {
		throw new UnsupportedOperationException();
	}

	public static SchemeCablePort createInstance(
			final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeCablePort schemeCablePort = new SchemeCablePort(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeCablePort.changed = true;
			return schemeCablePort;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeCablePort.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	public Object clone() {
		final SchemeCablePort schemeCablePort = (SchemeCablePort) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeCablePort;
	}

	/**
	 * @see AbstractSchemePort#getAbstractSchemeLink()
	 */
	public AbstractSchemeLink getAbstractSchemeLink() {
		return getSchemeCableLink();
	}

	/**
	 * @see Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLEPORT;
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public Set getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see AbstractSchemePort#getPort()
	 */
	public Port getPort() {
		final Port port = super.getPort();
		assert port == null || port.getSort() == PortSort._PORT_SORT_CABLE_PORT: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return port;
	}

	public SchemeCableLink getSchemeCableLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param abstractSchemeLink
	 * @see AbstractSchemePort#setAbstractSchemeLink(AbstractSchemeLink)
	 * @deprecated Use one of:
	 *             <ul><li>{@link AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)};</li>
	 *             <li>{@link AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)};</li>
	 *             <li>{@link SchemeCableLink#setSourceSchemeCablePort(SchemeCablePort)};</li>
	 *             <li>{@link SchemeCableLink#setTargetSchemeCablePort(SchemeCablePort)}</li></ul>
	 *             -- instead.
	 */
	public void setAbstractSchemeLink(final AbstractSchemeLink abstractSchemeLink) {
		setSchemeCableLink((SchemeCableLink) abstractSchemeLink);
	}

	/**
	 * @param port
	 * @see AbstractSchemePort#setPort(Port)
	 */
	public void setPort(final Port port) {
		assert port == null || port.getSort() == PortSort._PORT_SORT_CABLE_PORT: ErrorMessages.NATURE_INVALID;
		super.setPort(port);
	}

	/**
	 * @param schemeCableLink
	 * @deprecated Use one of:
	 *             <ul><li>{@link SchemeCableLink#setSourceSchemeCablePort(SchemeCablePort)};</li>
	 *             <li>{@link SchemeCableLink#setTargetSchemeCablePort(SchemeCablePort)}</li></ul>
	 *             -- instead.
	 */
	public void setSchemeCableLink(final SchemeCableLink schemeCableLink) {
		throw new UnsupportedOperationException();
	}
}
