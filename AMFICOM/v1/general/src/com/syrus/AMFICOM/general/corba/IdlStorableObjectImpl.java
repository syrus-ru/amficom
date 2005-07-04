/*-
 * $Id: IdlStorableObjectImpl.java,v 1.1 2005/07/04 15:23:09 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import com.syrus.AMFICOM.general.StorableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/04 15:23:09 $
 * @module general_v1
 */
final class IdlStorableObjectImpl extends IdlStorableObject {
	private static final long serialVersionUID = 8884720657154236801L;

	IdlStorableObjectImpl() {
		// empty
	}

	IdlStorableObjectImpl(IdlIdentifier id,
			long created,
			long modified,
			IdlIdentifier creatorId,
			IdlIdentifier modifierId,
			long version) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
	}

	/**
	 * @see com.syrus.AMFICOM.general.corba.IdlStorableObject#getNative()
	 */
	@Override
	public StorableObject getNative() {
		throw new UnsupportedOperationException();
	}
}
