/*-
 * $Id: SchemeCablePort.java,v 1.4 2005/03/25 10:15:12 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/03/25 10:15:12 $
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
	 * @see AbstractSchemePort#getAbstractSchemeLink()
	 */
	public AbstractSchemeLink getAbstractSchemeLink() {
		return getSchemeCableLink();
	}

	/**
	 * @param abstractSchemeLink
	 * @see AbstractSchemePort#setAbstractSchemeLink(AbstractSchemeLink)
	 */
	public void setAbstractSchemeLink(final AbstractSchemeLink abstractSchemeLink) {
		setSchemeCableLink((SchemeCableLink) abstractSchemeLink);
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
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	public SchemeCableLink getSchemeCableLink() {
		throw new UnsupportedOperationException();
	}

	public void setSchemeCableLink(final SchemeCableLink schemeCableLink) {
		throw new UnsupportedOperationException();
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
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
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

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemeCablePort createInstance() {
		throw new UnsupportedOperationException();
	}
}
