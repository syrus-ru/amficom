/*-
 * $Id: IdlAbstractSchemeElementImpl.java,v 1.1 2005/07/07 15:52:10 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.AbstractSchemeElement;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/07 15:52:10 $
 * @module scheme_v1
 */
final class IdlAbstractSchemeElementImpl extends IdlAbstractSchemeElement {
	private static final long serialVersionUID = 8649218456187947593L;

	IdlAbstractSchemeElementImpl() {
		// empty
	}

	IdlAbstractSchemeElementImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final IdlIdentifier parentSchemeId,
			final IdlIdentifier characteristicIds[]) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.parentSchemeId = parentSchemeId;
		this.characteristicIds = characteristicIds;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractSchemeElement getNative() {
		throw new UnsupportedOperationException();
	}
}
