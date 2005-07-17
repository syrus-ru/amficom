/*-
 * $Id: IdlAbstractSchemeElementImpl.java,v 1.2 2005/07/17 05:20:26 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/07/17 05:20:26 $
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
			final IdlIdentifier parentSchemeId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.parentSchemeId = parentSchemeId;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractSchemeElement getNative() {
		throw new UnsupportedOperationException();
	}
}
