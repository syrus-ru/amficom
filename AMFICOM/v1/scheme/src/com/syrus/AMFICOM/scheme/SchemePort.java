/*-
 * $Id: SchemePort.java,v 1.8 2005/04/04 13:17:21 bass Exp $
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
import org.omg.CORBA.portable.IDLEntity;

/**
 * #08 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/04/04 13:17:21 $
 * @module scheme_v1
 */
public final class SchemePort extends AbstractSchemePort {
	private static final long serialVersionUID = 3256436993469658930L;

	/**
	 * @param id
	 */
	protected SchemePort(Identifier id) {
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
	protected SchemePort(Identifier id, Date created, Date modified,
			Identifier creatorId, Identifier modifierId,
			long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @deprecated Use {@link #createInstance(Identifier)}instead.
	 */
	public static SchemePort createInstance() {
		throw new UnsupportedOperationException();
	}

	public static SchemePort createInstance(final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemePort schemePort = new SchemePort(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_PORT_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemePort.changed = true;
			return schemePort;
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemePort.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	public Object clone() {
		final SchemePort schemePort = (SchemePort) super.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemePort;
	}

	/**
	 * @see AbstractSchemePort#getAbstractSchemeLink()
	 */
	public AbstractSchemeLink getAbstractSchemeLink() {
		return getSchemeLink();
	}

	/**
	 * @see Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		return CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPORT;
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
		assert port == null || port.getSort() == PortSort._PORT_SORT_PORT: ErrorMessages.OBJECT_BADLY_INITIALIZED;
		return port;
	}

	public SchemeCableThread getSchemeCableThread() {
		throw new UnsupportedOperationException();
	}

	public SchemeLink getSchemeLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param abstractSchemeLink
	 * @see AbstractSchemePort#setAbstractSchemeLink(AbstractSchemeLink)
	 * @deprecated Use one of:
	 *             <ul><li>{@link AbstractSchemeLink#setSourceAbstractSchemePort(AbstractSchemePort)};</li>
	 *             <li>{@link AbstractSchemeLink#setTargetAbstractSchemePort(AbstractSchemePort)};</li>
	 *             <li>{@link SchemeLink#setSourceSchemePort(SchemePort)};</li>
	 *             <li>{@link SchemeLink#setTargetSchemePort(SchemePort)}</li></ul>
	 *             -- instead.
	 */
	public void setAbstractSchemeLink(final AbstractSchemeLink abstractSchemeLink) {
		setSchemeLink((SchemeLink) abstractSchemeLink);
	}

	/**
	 * @param port
	 * @see AbstractSchemePort#setPort(Port)
	 */
	public void setPort(final Port port) {
		assert port == null || port.getSort() == PortSort._PORT_SORT_PORT: ErrorMessages.NATURE_INVALID;
		super.setPort(port);
	}

	/**
	 * @param schemeCableThread
	 * @deprecated Use one of:
	 *             <ul><li>{@link SchemeCableThread#setSourceSchemePort(SchemePort)};</li>
	 *             <li>{@link SchemeCableThread#setTargetSchemePort(SchemePort)}</li></ul>
	 *             -- instead.
	 */
	public void setSchemeCableThread(final SchemeCableThread schemeCableThread) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeLink
	 * @deprecated Use one of:
	 *             <ul><li>{@link SchemeLink#setSourceSchemePort(SchemePort)};</li>
	 *             <li>{@link SchemeLink#setTargetSchemePort(SchemePort)}</li></ul>
	 *             -- instead.
	 */
	public void setSchemeLink(final SchemeLink schemeLink) {
		throw new UnsupportedOperationException();
	}
}
