/*-
 * $Id: IdlAbstractNodeImpl.java,v 1.1 2005/07/07 13:12:30 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map.corba;

import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.map.AbstractNode;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/07 13:12:30 $
 * @module map_v1
 */
final class IdlAbstractNodeImpl extends IdlAbstractNode {
	private static final long serialVersionUID = -7881152030081598655L;

	IdlAbstractNodeImpl() {
		// empty
	}

	IdlAbstractNodeImpl(final IdlIdentifier id,
			final long created,
			final long modified,
			final IdlIdentifier creatorId,
			final IdlIdentifier modifierId,
			final long version) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
	}

	/**
	 * @throws IdlCreateObjectException
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public AbstractNode getNative() throws IdlCreateObjectException {
		throw new UnsupportedOperationException();
	}
}
