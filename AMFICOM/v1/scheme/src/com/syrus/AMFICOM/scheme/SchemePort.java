/*-
 * $Id: SchemePort.java,v 1.4 2005/03/25 10:15:12 bass Exp $
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
public final class SchemePort extends AbstractSchemePort {
	private static final long serialVersionUID = 3256436993469658930L;

	protected Identifier schemeCableThreadId = null;

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
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	public SchemeCableThread getSchemeCableThread() {
		throw new UnsupportedOperationException();
	}

	public SchemeLink getSchemeLink() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param abstractSchemeLink
	 * @see AbstractSchemePort#setAbstractSchemeLink(AbstractSchemeLink)
	 * @deprecated
	 */
	public void setAbstractSchemeLink(final AbstractSchemeLink abstractSchemeLink) {
		setSchemeLink((SchemeLink) abstractSchemeLink);
	}

	/**
	 * @param schemeCableThread
	 * @deprecated
	 */
	public void setSchemeCableThread(final SchemeCableThread schemeCableThread) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param schemeLink
	 * @deprecated
	 */
	public void setSchemeLink(final SchemeLink schemeLink) {
		throw new UnsupportedOperationException();
	}
}
