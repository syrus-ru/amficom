/*-
 * $Id: IdlSchemeProtoGroupImpl.java,v 1.2 2005/07/24 17:08:16 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/07/24 17:08:16 $
 * @module scheme
 */
final class IdlSchemeProtoGroupImpl extends IdlSchemeProtoGroup {
	private static final long serialVersionUID = 5920689360142390043L;

	IdlSchemeProtoGroupImpl() {
		// empty
	}

	IdlSchemeProtoGroupImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version,
			final String name,
			final String description,
			final IdlIdentifier symbolId,
			final IdlIdentifier parentSchemeProtoGroupId) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
		this.name = name;
		this.description = description;
		this.symbolId = symbolId;
		this.parentSchemeProtoGroupId = parentSchemeProtoGroupId;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public SchemeProtoGroup getNative() {
		return new SchemeProtoGroup(this);
	}
}
