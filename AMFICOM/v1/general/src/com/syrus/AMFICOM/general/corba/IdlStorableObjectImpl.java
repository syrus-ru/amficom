/*-
 * $Id: IdlStorableObjectImpl.java,v 1.2 2005/08/08 11:27:26 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general.corba;

import com.syrus.AMFICOM.general.StorableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/08 11:27:26 $
 * @module general
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
